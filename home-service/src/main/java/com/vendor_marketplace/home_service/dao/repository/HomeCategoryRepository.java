package com.vendor_marketplace.home_service.dao.repository;

import com.vendor_marketplace.home_service.models.entity.HomeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HomeCategoryRepository extends MongoRepository<HomeCategory, String> {


    Page<HomeCategory> findByHomeCategorySection(String homeCategorySection, Pageable pageable);

    boolean existsByCategoryId(String categoryId);
}
