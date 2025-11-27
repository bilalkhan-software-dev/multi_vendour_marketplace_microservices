package com.vendor_marketplace.home_service.dao.implementation;

import com.vendor_marketplace.home_service.dao.interfaces.HomeCategoryDao;
import com.vendor_marketplace.home_service.dao.repository.HomeCategoryRepository;
import com.vendor_marketplace.home_service.models.entity.HomeCategory;
import com.vendor_marketplace.home_service.models.entity.enums.HomeCategorySection;
import com.vendor_marketplace.home_service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class HomeCategoryDaoImpl implements HomeCategoryDao {

    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<HomeCategory> saveAll(List<HomeCategory> homeCategories) {
        return homeCategoryRepository.saveAll(homeCategories);
    }

    @Override
    public HomeCategory save(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public Optional<HomeCategory> findById(String id) {
        return homeCategoryRepository.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return homeCategoryRepository.existsById(id);
    }

    @Override
    public boolean existsByCategoryId(String categoryId) {
        return homeCategoryRepository.existsByCategoryId(categoryId);
    }

    @Override
    public void deleteById(String id) {
        homeCategoryRepository.deleteById(id);
    }

    @Override
    public Page<HomeCategory> findByCategorySection(HomeCategorySection homeCategorySection, int page, int size, boolean isNewest) {
        Pageable pageable = PageUtils.toBuildSortAndPage(page, size, isNewest);
        return homeCategoryRepository.findByHomeCategorySection(homeCategorySection.name(), pageable);
    }

    @Override
    public Page<HomeCategory> findAll(int page, int size, boolean isNewest) {
        Pageable pageable = PageUtils.toBuildSortAndPage(page, size, isNewest);
        return homeCategoryRepository.findAll(pageable);
    }

    @Override
    public List<HomeCategory> findAll() {
        return homeCategoryRepository.findAll();
    }
}
