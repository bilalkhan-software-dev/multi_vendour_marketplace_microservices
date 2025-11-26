package com.vendor_marketplace.product_command_service.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendor_marketplace.common.dto.event.ProductCreateEvent;
import com.vendor_marketplace.common.dto.event.ProductDeleteEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.common.dto.response.SellerResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.product_command_service.dao.interfaces.ProductDao;
import com.vendor_marketplace.product_command_service.feignClient.ThirdPartySellerService;
import com.vendor_marketplace.product_command_service.mapper.ProductMapper;
import com.vendor_marketplace.product_command_service.models.dto.request.ProductCreateRequest;
import com.vendor_marketplace.product_command_service.models.dto.request.ProductUpdateRequest;
import com.vendor_marketplace.product_command_service.models.entity.Category;
import com.vendor_marketplace.product_command_service.models.entity.Product;
import com.vendor_marketplace.product_command_service.kafka.publisher.KafkaPublisherService;
import com.vendor_marketplace.product_command_service.services.ProductService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.vendor_marketplace.common.utils.ProductUtil.calculateDiscountPercentage;

@Service
@RequiredArgsConstructor
@Slf4j
class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ThirdPartySellerService sellerService;
    private final ObjectMapper objectMapper;
    private final KafkaPublisherService kafkaPublisherService;

    @Override
    @Transactional
    public ProductResponse addProduct(final String authUserSellerId, ProductCreateRequest request) {

        log.info("Product creation request received | sellerId={} | title={}",
                authUserSellerId, request.getTitle());

        // Validate seller
        String businessName = getAndVerifySeller(authUserSellerId);
        log.info("Seller verified successfully | sellerId={} | businessName={}",
                authUserSellerId, businessName);

        // Build categories
        Category category1 = createOrGetCategory(request.getCategory1(), 1, null);
        Category category2 = createOrGetCategory(request.getCategory2(), 2, category1);
        Category category3 = createOrGetCategory(request.getCategory3(), 3, category2);

        Category finalCategory = category3 != null ? category3 : (category2 != null ? category2 : category1);

        // Validate price
        validatePrice(request.getMrpPrice(), request.getSellingPrice());

        int discount = calculateDiscountPercentage(request.getMrpPrice(), request.getSellingPrice());
        String productId = UUID.randomUUID().toString().replace("-", "");

        Product product = Product.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .mrpPrice(request.getMrpPrice())
                .sellingPrice(request.getSellingPrice())
                .colors(request.getColors())
                .brand(businessName)
                .productId(productId)
                .sellerId(authUserSellerId)
                .stocks(Optional.ofNullable(request.getStocks()).orElse(10))
                .images(request.getImages())
                .sizes(request.getSizes())
                .discountInPercentage((double) discount)
                .category(finalCategory)
                .build();

        Product savedProduct = productDao.save(product);

        log.info("Product saved successfully | productId={} | sellerId={} | dbId={}",
                productId, authUserSellerId, savedProduct.getId());

        // Publish event
        ProductCreateEvent event = ProductCreateEvent.builder()
                .productId(productId)
                .title(request.getTitle())
                .description(request.getDescription())
                .mrpPrice(request.getMrpPrice())
                .sellingPrice(request.getSellingPrice())
                .stocks(request.getStocks())
                .colors(request.getColors())
                .sizes(request.getSizes())
                .images(request.getImages())
                .brand(businessName)
                .category1(request.getCategory1())
                .category2(request.getCategory2())
                .category3(request.getCategory3())
                .sellerId(authUserSellerId)
                .build();

        kafkaPublisherService.kafkaProductCreateEventPublisher(event);

        return ProductMapper.toProductResponse(savedProduct);
    }

    @Override
    public void deleteProductById(String authUserSellerId, String productId) {

        log.info("Delete request received for product | productId={} | sellerId={}",
                productId, authUserSellerId);

        Product product = productDao.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        validateSeller(authUserSellerId, product);

        productDao.deleteByProductId(productId);

        log.info("Product deleted successfully | productId={} | sellerId={}",
                productId, authUserSellerId);

        ProductDeleteEvent event = ProductDeleteEvent.builder()
                .productId(productId)
                .sellerId(authUserSellerId)
                .build();

        kafkaPublisherService.kafkaProductDeleteEventPublisher(event);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(String productId, String authUserSellerId, ProductUpdateRequest request) {

        log.info("Product update request received | productId={} | sellerId={}",
                productId, authUserSellerId);

        Product product = productDao.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        validateSeller(authUserSellerId, product);

        // Apply updates
        Optional.ofNullable(request.getTitle()).ifPresent(product::setTitle);
        Optional.ofNullable(request.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(request.getMrpPrice()).ifPresent(product::setMrpPrice);
        Optional.ofNullable(request.getSellingPrice()).ifPresent(product::setSellingPrice);
        Optional.ofNullable(request.getColors()).ifPresent(product::setColors);
        Optional.ofNullable(request.getImages()).ifPresent(product::setImages);
        Optional.ofNullable(request.getSizes()).ifPresent(product::setSizes);
        Optional.ofNullable(request.getStocks()).ifPresent(product::setStocks);

        if (product.getMrpPrice() != null && product.getSellingPrice() != null) {
            product.setDiscountInPercentage(
                    (double) calculateDiscountPercentage(product.getMrpPrice(), product.getSellingPrice())
            );
        }

        Product savedProduct = productDao.save(product);

        log.info("Product updated successfully | productId={} | sellerId={}",
                productId, authUserSellerId);

        ProductUpdateEvent event = ProductUpdateEvent.builder()
                .productId(productId)
                .title(request.getTitle())
                .description(request.getDescription())
                .mrpPrice(request.getMrpPrice())
                .sellingPrice(request.getSellingPrice())
                .stocks(request.getStocks())
                .sizes(request.getSizes())
                .images(request.getImages())
                .colors(request.getColors())
                .build();

        kafkaPublisherService.kafkaProductUpdateEventPublisher(event);

        return ProductMapper.toProductResponse(savedProduct);
    }

    @Override
    @Transactional
    public void updateStocks(String productId, int quantity) {

        log.info("Stock update request | productId={} | quantity={}", productId, quantity);

        if (quantity <= 0) {
            throw new ValidationException("Quantity must be greater than zero");
        }

        Product product = productDao.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        int currentStock = product.getStocks();

        if (currentStock < quantity) {
            log.warn("Stock update failed - insufficient stock | productId={} | currentStock={} | requested={}",
                    productId, currentStock, quantity);
            throw new ValidationException("Not enough stock for product: " + productId);
        }

        int newStock = currentStock - quantity;
        product.setStocks(newStock);
        productDao.save(product);

        log.info("Stock updated successfully | productId={} | oldStock={} | newStock={}",
                productId, currentStock, newStock);

        ProductUpdateStockEvent event = ProductUpdateStockEvent.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        kafkaPublisherService.kafkaProductUpdateStockEventPublisher(event);
    }

    private void validateSeller(String authUserSellerId, Product product) {
        if (!product.getSellerId().equals(authUserSellerId)) {
            log.warn("Unauthorized attempt to modify product | productId={} | requestSellerId={} | ownerSellerId={}",
                    product.getProductId(), authUserSellerId, product.getSellerId());
            throw new IllegalArgumentException("Unauthorized: You cannot modify this product.");
        }
    }

    private void validatePrice(int mrp, int selling) {
        if (mrp <= 0) {
            throw new IllegalArgumentException("MRP must be > 0");
        }
        if (selling < 0) {
            throw new IllegalArgumentException("Selling price cannot be negative");
        }
        if (selling > mrp) {
            throw new IllegalArgumentException("Selling price cannot exceed MRP");
        }
    }

    private Category createOrGetCategory(String categoryId, int level, Category parent) {
        if (categoryId == null || categoryId.isBlank()) {
            return null;
        }

        return productDao.findByCategoryId(categoryId)
                .orElseGet(() -> {
                    log.info("Creating new category | categoryId={} | level={}", categoryId, level);
                    return productDao.saveCategory(
                            Category.builder()
                                    .categoryId(categoryId)
                                    .name(categoryId.replace("_", " "))
                                    .level(level)
                                    .parentCategory(parent)
                                    .build()
                    );
                });
    }

    private String getAndVerifySeller(String sellerId) {

        log.info("Verifying seller | sellerId={}", sellerId);

        ResponseEntity<Map<String, Object>> response = sellerService.getSellerById(sellerId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Seller validation failed | sellerId={} | status={}",
                    sellerId, response.getStatusCode());
            throw new ResourceNotFoundException("Seller not found");
        }

        Map<String, Object> body = response.getBody();
        if (body == null || body.get("data") == null) {
            log.error("Seller lookup returned empty body | sellerId={}", sellerId);
            throw new ResourceNotFoundException("Seller not found");
        }

        SellerResponse seller = objectMapper.convertValue(body.get("data"), SellerResponse.class);

        log.info("Seller validated | sellerId={} | businessName={}",
                sellerId, seller.getBusinessDetails().getBusinessName());

        return seller.getBusinessDetails().getBusinessName();
    }
}
