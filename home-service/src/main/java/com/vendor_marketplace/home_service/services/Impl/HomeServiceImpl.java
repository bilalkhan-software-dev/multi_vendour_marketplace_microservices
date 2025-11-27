package com.vendor_marketplace.home_service.services.Impl;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.home_service.dao.interfaces.HomeCategoryDao;
import com.vendor_marketplace.home_service.mapper.HomeCategoryMapper;
import com.vendor_marketplace.home_service.models.dto.request.HomeCategoryRequest;
import com.vendor_marketplace.home_service.models.dto.request.UpdateHomeCategoryRequest;
import com.vendor_marketplace.home_service.models.dto.response.Home;
import com.vendor_marketplace.home_service.models.dto.response.HomeCategoryResponse;
import com.vendor_marketplace.home_service.models.entity.HomeCategory;
import com.vendor_marketplace.home_service.models.entity.enums.HomeCategorySection;
import com.vendor_marketplace.home_service.services.HomeService;
import com.vendor_marketplace.home_service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
class HomeServiceImpl implements HomeService {

    private final HomeCategoryDao homeCategoryDao;

    @Override
    @Transactional
    public HomeCategoryResponse createHomeCategory(HomeCategoryRequest homeCategoryRequest) {
        HomeCategory homeCategory = HomeCategory.builder()
                .name(homeCategoryRequest.getName())
                .categoryId(homeCategoryRequest.getCategoryId())
                .homeCategorySection(homeCategoryRequest.getHomeCategorySection().name())
                .image(homeCategoryRequest.getImage())
                .build();

        HomeCategory saved = homeCategoryDao.save(homeCategory);


        return HomeCategoryMapper.toHomeCategoryResponse(saved);
    }

    @Override
    @Transactional
    public HomeCategoryResponse updateHomeCategory(String homeCategoryId, UpdateHomeCategoryRequest req) {

        HomeCategory homeCategory = homeCategoryDao.findById(homeCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Home Category Not Found"));


        Optional.ofNullable(req.getName())
                .filter(name -> !Objects.equals(name, homeCategory.getName()))
                .ifPresent(homeCategory::setName);

        Optional.ofNullable(req.getImage())
                .filter(image -> !Objects.equals(image, homeCategory.getImage()))
                .ifPresent(homeCategory::setImage);

        Optional.ofNullable(req.getCategoryId())
                .filter(id -> !Objects.equals(id, homeCategory.getCategoryId()))
                .ifPresent(homeCategory::setCategoryId);

        Optional.ofNullable(req.getHomeCategorySection())
                .ifPresent(h -> homeCategory.setHomeCategorySection(h.name()));

        HomeCategory updated = homeCategoryDao.save(homeCategory);

        return HomeCategoryMapper.toHomeCategoryResponse(updated);
    }

    @Override
    @Transactional
    public void deleteHomeCategory(String homeCategoryId) {

        boolean exists = homeCategoryDao.existsById(homeCategoryId);

        if (exists) {
            homeCategoryDao.deleteById(homeCategoryId);
        } else {
            throw new ResourceNotFoundException("Home Category Not Found");
        }
    }

    @Transactional
    @Override
    public Home createHomeCategories(List<HomeCategoryRequest> homeCategoryRequests) {

        List<HomeCategory> newCategories = homeCategoryRequests.stream()
                .filter(req -> !homeCategoryDao.existsByCategoryId(req.getCategoryId()))
                .map(req -> HomeCategory.builder()
                        .name(req.getName())
                        .categoryId(req.getCategoryId())
                        .homeCategorySection(req.getHomeCategorySection().name())
                        .image(req.getImage())
                        .build())
                .collect(Collectors.toList());

        if (!newCategories.isEmpty()) {
            homeCategoryDao.saveAll(newCategories);
        }

        List<HomeCategory> allCategories = homeCategoryDao.findAll();
        return PageUtils.createHomePageData(allCategories);
    }

    /**
     * For normal user
     */
    @Override
    public Home getHomeCategories() {
        List<HomeCategory> categories = homeCategoryDao.findAll();
        return PageUtils.createHomePageData(categories);
    }

    /**
     * For admin
     */
    @Override
    public PagedResponse<HomeCategoryResponse> getHomeCategoriesBySection(HomeCategorySection section, int page, int size, boolean isNewest) {
        Page<HomeCategory> categories = homeCategoryDao.findByCategorySection(section, page, size, isNewest);

        return PageUtils.buildPagedResponse(categories, HomeCategoryMapper::toHomeCategoryResponse);
    }


    /**
     * For admin
     */
    @Override
    public PagedResponse<HomeCategoryResponse> getAllHomeCategories(int page, int size, boolean isNewest) {
        Page<HomeCategory> categories = homeCategoryDao.findAll(page, size, isNewest);

        return PageUtils.buildPagedResponse(categories, HomeCategoryMapper::toHomeCategoryResponse);
    }


}
