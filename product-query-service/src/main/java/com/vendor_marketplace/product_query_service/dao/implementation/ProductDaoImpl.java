package com.vendor_marketplace.product_query_service.dao.implementation;

import com.vendor_marketplace.common.dto.response.ProductResponse;
import com.vendor_marketplace.product_query_service.dao.interfaces.ProductDao;
import com.vendor_marketplace.product_query_service.dao.repository.CategoryRepository;
import com.vendor_marketplace.product_query_service.dao.repository.ProductRepository;
import com.vendor_marketplace.product_query_service.mapper.ProductMapper;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.product_query_service.models.entity.Category;
import com.vendor_marketplace.product_query_service.models.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
class ProductDaoImpl implements ProductDao {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MongoTemplate mongoTemplate;

    private static final int DEFAULT_PAGE_SIZE = 12;

    /**
     * Find products based on multiple filters such as:
     * title, category, brand, price range, colors, size, discount, stock, sorting & pagination.
     */
    @Override
    public PagedResponse<ProductResponse> findProducts(
            String title,
            String category,
            String brand,
            String colors,
            String sizes,
            Integer minPrice,
            Integer maxPrice,
            Integer minDiscount,
            String sort,
            String stock,
            Integer pageNumber
    ) {

        Query query = new Query();
        List<Criteria> conditions = new ArrayList<>();

        // Search by title starts with
        if (hasValue(title)) {
            conditions.add(Criteria.where("title")
                    .regex("^" + title, "i"));
        }

        // Filter by category (ignore "All")
        if (hasValue(category) && !category.equalsIgnoreCase("All")) {

            categoryRepository.findByCategoryId(category.trim())
                    .ifPresent(
                            cat -> conditions.add(Criteria.where("category.$id").is(cat.getId()))
                    );

        }

        // Filter by brand
        if (hasValue(brand)) {
            conditions.add(Criteria.where("brand").is(brand));
        }

        // Filter by colors
        if (hasValue(colors)) {
            conditions.add(Criteria.where("colors").is(colors));
        }

        // Filter by sizes
        if (hasValue(sizes)) {
            conditions.add(Criteria.where("sizes").is(sizes));
        }

        // Price range filter
        if (minPrice != null && maxPrice != null) {
            conditions.add(Criteria.where("sellingPrice").gte(minPrice).lte(maxPrice));
        } else if (minPrice != null) {
            conditions.add(Criteria.where("sellingPrice").gte(minPrice));
        } else if (maxPrice != null) {
            conditions.add(Criteria.where("sellingPrice").lte(maxPrice));
        }

        // Minimum discount filter
        if (minDiscount != null) {
            conditions.add(Criteria.where("discountInPercentage").gte(minDiscount));
        }

        if (hasValue(stock)) {
            if (stock.equalsIgnoreCase("in_stock")) {
                conditions.add(Criteria.where("stocks").gt(0));
            } else if (stock.equalsIgnoreCase("out_of_stock")) {
                conditions.add(Criteria.where("stocks").is(0));
            }
        }

        // APPLY ALL CONDITIONS
        if (!conditions.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(conditions.toArray(new Criteria[0])));
        }

        // SORTING & PAGINATION
        Sort sortOption = Sort.by(Sort.Direction.DESC, "createdAt"); // default

        if (hasValue(sort)) {
            switch (sort) {
                case "low_to_high" -> sortOption = Sort.by(Sort.Direction.ASC, "sellingPrice");
                case "high_to_low" -> sortOption = Sort.by(Sort.Direction.DESC, "sellingPrice");
                case "newest" -> sortOption = Sort.by(Sort.Direction.DESC, "createdAt");
                case "oldest" -> sortOption = Sort.by(Sort.Direction.ASC, "createdAt");
            }
        }

        query.with(sortOption);

        int page = (pageNumber != null) ? pageNumber : 0;
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sortOption);
        return executePagedQuery(query, pageable);
    }

    /**
     * Simple keyword search across title, description, category name and id.
     */
    @Override
    public PagedResponse<ProductResponse> searchProducts(String text, Integer pageNumber) {

        Query query = new Query();

        if (hasValue(text)) {
            String regex = ".*" + text.toLowerCase() + ".*";

            List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(text);

          List<String> categoryIds  =  categories.stream().map(Category::getId).toList();

            query.addCriteria(
                    new Criteria().orOperator(
                            Criteria.where("title").regex(regex, "i"),
                            Criteria.where("description").regex(regex, "i"),
                            Criteria.where("category.$id").in(categoryIds)
                    )
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        int page = (pageNumber == null) ? 0 : pageNumber;
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sort);

        query.with(sort);
        query.fields().include("title")
                .include("productId")
                .include("category");
        return executePagedQuery(query, pageable);
    }

    // Checks if string has content
    private boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }

    @Override
    public Optional<Product> findByProductId(String productId) {
        return productRepository.findByProductId(productId);
    }

    @Override
    public Optional<Category> findByCategoryId(String categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteByProductId(String id) {
        productRepository.deleteByProductId(id);
    }

    @Override
    public PagedResponse<ProductResponse> findSimilarProducts(String productId, int pageNumber, boolean isNewest) {


        Optional<Product> productOpt = productRepository.findByProductId(productId);
        if (productOpt.isEmpty()) {
            return buildEmptyPagedResponse(pageNumber);
        }

        Product product = productOpt.get();
        String categoryId = product.getCategory().getId();

        Sort sort = Sort.by(isNewest ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(pageNumber, DEFAULT_PAGE_SIZE, sort);

        Page<Product> similarProducts = productRepository.findByCategory_IdAndProductIdNot(
                categoryId, productId, pageable
        );

        return buildPagedResponse(similarProducts);
    }

    @Override
    public PagedResponse<ProductResponse> findSellerProducts(String sellerId, int pageNo, boolean isNewest) {
        Sort sort = Sort.by(isNewest ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, DEFAULT_PAGE_SIZE, sort);

        Page<Product> response = productRepository.findBySellerId(sellerId, pageable);
        return buildPagedResponse(response);
    }

    private PagedResponse<ProductResponse> executePagedQuery(Query query, Pageable pageable) {
        // Apply pagination to the query
        query.with(pageable);

        // Execute query with pagination
        List<Product> products = mongoTemplate.find(query, Product.class);

        // Count total elements (without pagination)
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Product.class);

        List<ProductResponse> responses = products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        long totalPages = (total + pageSize - 1) / pageSize;

        return PagedResponse.<ProductResponse>builder()
                .content(responses)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .totalElements(total)
                .totalPages((int) totalPages)
                .isLastPage((pageNumber + 1) >= totalPages)
                .isFirstPage(pageNumber == 0)
                .build();
    }

    private PagedResponse<ProductResponse> buildPagedResponse(Page<Product> productPage) {
        List<ProductResponse> content = productPage.getContent()
                .stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());

        return PagedResponse.<ProductResponse>builder()
                .content(content)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .isLastPage(productPage.isLast())
                .isFirstPage(productPage.isFirst())
                .build();
    }

    private PagedResponse<ProductResponse> buildEmptyPagedResponse(int pageNumber) {
        return PagedResponse.<ProductResponse>builder()
                .content(List.of())
                .pageNumber(pageNumber)
                .pageSize(DEFAULT_PAGE_SIZE)
                .totalElements(0)
                .totalPages(0)
                .isLastPage(true)
                .isFirstPage(true)
                .build();
    }
}