package com.vendor_marketplace.product_query_service.services.impl;

import com.vendor_marketplace.common.dto.event.ProductCreateEvent;
import com.vendor_marketplace.common.dto.event.ProductDeleteEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;
import com.vendor_marketplace.product_query_service.dao.interfaces.ProductDao;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.product_query_service.models.entity.Category;
import com.vendor_marketplace.product_query_service.models.entity.Product;
import com.vendor_marketplace.product_query_service.services.ProductConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.vendor_marketplace.common.utils.ProductUtil.calculateDiscountPercentage;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductConsumerServiceImpl implements ProductConsumerService {

    private final ProductDao productDao;


    @Override
    @Transactional
    public void addProduct(ProductCreateEvent event) {

        log.info("addProduct consumer received: product={}", event);
        validateProductCreateEvent(event);

        if (productDao.findByProductId(event.getProductId()).isPresent()) {
            log.info("Product already exists, skipping creation: productId={}", event.getProductId());
            return;
        }
        Category finalCategory = buildCategoryHierarchy(
                event.getCategory1(),
                event.getCategory2(),
                event.getCategory3()
        );

        Product product = Product.builder()
                .productId(event.getProductId())
                .brand(event.getBrand())
                .description(event.getDescription())
                .sizes(event.getSizes())
                .images(event.getImages())
                .sellingPrice(event.getSellingPrice())
                .mrpPrice(event.getMrpPrice())
                .discountInPercentage((double) calculateDiscountPercentage(event.getMrpPrice(), event.getSellingPrice()))
                .title(event.getTitle())
                .colors(event.getColors())
                .stocks(event.getStocks())
                .category(finalCategory)
                .sellerId(event.getSellerId())
                .build();

        productDao.save(product);

        log.info("Product added successfully");
    }

    @Override
    public void deleteProductById(ProductDeleteEvent event) {
        validateEvent(event.getProductId());

        log.info("deleteProduct consumer received: productId={}", event.getProductId());
        Product product = productDao.findByProductId(event.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );
        validateSeller(event.getSellerId(), product);
        productDao.deleteByProductId(event.getProductId());
        log.info("Product deleted successfully");
    }

    @Override
    @Transactional
    public void updateProduct(ProductUpdateEvent event) {
        validateEvent(event.getProductId());

        log.info("updateProduct consumer received: productId= {}", event.getProductId());
        Product product = productDao.findByProductId(event.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found")
        );

        Optional.ofNullable(event.getTitle()).ifPresent(product::setTitle);
        Optional.ofNullable(event.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(event.getMrpPrice()).ifPresent(product::setMrpPrice);
        Optional.ofNullable(event.getSellingPrice()).ifPresent(product::setSellingPrice);
        Optional.ofNullable(event.getColors()).ifPresent(product::setColors);
        Optional.ofNullable(event.getImages()).ifPresent(product::setImages);
        Optional.ofNullable(event.getSizes()).ifPresent(product::setSizes);
        Optional.ofNullable(event.getStocks()).ifPresent(product::setStocks);

        if (product.getMrpPrice() != null && product.getSellingPrice() != null) {
            product.setDiscountInPercentage(
                    (double) calculateDiscountPercentage(product.getMrpPrice(), product.getSellingPrice())
            );
        }

        productDao.save(product);
        log.info("Product updated successfully: id={}", product.getId());

    }

    @Override
    @Transactional
    public void updateStocks(ProductUpdateStockEvent event) {
        validateEvent(event.getProductId());
        log.info("updateStocks consumer received: productId= {}", event.getProductId());

        String productId = event.getProductId();
        int quantity = event.getQuantity();

        log.info("Stock update request | productId={} | quantity={}", productId, quantity);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Product product = productDao.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        int currentStock = product.getStocks();

        if (currentStock < quantity) {
            log.warn("Stock update failed - insufficient stock | productId={} | currentStock={} | requested={}",
                    productId, currentStock, quantity);
            throw new IllegalArgumentException("Not enough stock for product: " + productId);
        }

        int newStock = currentStock - quantity;
        product.setStocks(newStock);
        productDao.save(product);

        log.info("Stock updated successfully | productId={} | oldStock={} | newStock={}",
                productId, currentStock, newStock);
    }

    private Category buildCategoryHierarchy(String cat1, String cat2, String cat3) {
        Category category1 = createOrGetCategory(cat1, 1, null);
        Category category2 = createOrGetCategory(cat2, 2, category1);
        Category category3 = createOrGetCategory(cat3, 3, category2);

        return category3 != null ? category3 :
                (category2 != null ? category2 : category1);
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

    private void validateSeller(String authUserSellerId, Product product) {
        if (!product.getSellerId().equals(authUserSellerId)) {
            log.warn("Unauthorized attempt to modify product | productId={} | requestSellerId={} | ownerSellerId={}",
                    product.getProductId(), authUserSellerId, product.getSellerId());
            throw new IllegalArgumentException("Unauthorized: You cannot modify this product.");
        }
    }

    private void validateProductCreateEvent(ProductCreateEvent event) {
        validateEvent(event.getProductId());

        if (event.getSellerId() == null || event.getSellerId().isBlank()) {
            throw new IllegalArgumentException("Seller ID cannot be null or empty");
        }
    }

    private void validateEvent(String productId) {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
    }

}
