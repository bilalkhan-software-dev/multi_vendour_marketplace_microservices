package com.vendor_marketplace.home_service.utils;

import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.home_service.mapper.HomeCategoryMapper;
import com.vendor_marketplace.home_service.models.dto.response.Home;
import com.vendor_marketplace.home_service.models.dto.response.HomeCategoryResponse;
import com.vendor_marketplace.home_service.models.entity.HomeCategory;
import com.vendor_marketplace.home_service.models.entity.enums.HomeCategorySection;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PageUtils {

    private PageUtils() {
    }

    public static Pageable toBuildSortAndPage(final int page, final int size, final boolean isNewest) {
        Sort sort = Sort.by(isNewest ? Sort.Direction.DESC : Sort.Direction.ASC, "createdAt");

        return PageRequest.of(page, size, sort);
    }

    /**
     * @param <I>    the input type (source)
     * @param <O>    the output type (target)
     * @param page   the source page containing input elements
     * @param mapper function to convert input elements to output type
     * @return transformed paged response with output elements
     */
    public static <I, O> PagedResponse<O> buildPagedResponse(Page<@NonNull I> page, Function<I, O> mapper) {
        return PagedResponse.<O>builder()
                .content(page.getContent().stream()
                        .map(mapper)
                        .toList())
                .isFirstPage(page.isFirst())
                .isLastPage(page.isLast())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    public static Home createHomePageData(List<HomeCategory> categories) {

        List<HomeCategoryResponse> list = categories.stream().map(HomeCategoryMapper::toHomeCategoryResponse).toList();

        List<HomeCategoryResponse> shopByCategories = list.stream().filter(homeCategory -> Objects.equals(homeCategory.getHomeCategorySection(), HomeCategorySection.SHOP_BY_CATEGORIES.name())).toList();

        List<HomeCategoryResponse> gridCategories = list.stream().filter(homeCategory -> Objects.equals(homeCategory.getHomeCategorySection(), HomeCategorySection.GRID.name())).toList();

        List<HomeCategoryResponse> electronicsCategories = list.stream().filter(homeCategory -> Objects.equals(homeCategory.getHomeCategorySection(), HomeCategorySection.ELECTRONICS_CATEGORIES.name())).toList();


        return Home.builder()
                .electronicCategories(electronicsCategories)
                .shopByCategory(shopByCategories)
                .grid(gridCategories)
                .build();
    }
}
