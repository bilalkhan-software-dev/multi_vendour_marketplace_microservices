package com.vendor_marketplace.home_service.dao.interfaces;

import com.vendor_marketplace.home_service.models.entity.HomeCategory;
import com.vendor_marketplace.home_service.models.entity.enums.HomeCategorySection;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface HomeCategoryDao {
    List<HomeCategory> saveAll(List<HomeCategory> homeCategories);

    HomeCategory save(HomeCategory homeCategory);

    Optional<HomeCategory> findById(String id);

    boolean existsById(String id);

    boolean existsByCategoryId(String categoryId);

    void deleteById(String id);

    Page<HomeCategory> findByCategorySection(HomeCategorySection homeCategorySection, int page, int size, boolean isNewest);

    Page<HomeCategory> findAll(int page, int size, boolean isNewest);

    List<HomeCategory> findAll();
}
