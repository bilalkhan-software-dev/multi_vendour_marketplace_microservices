package com.vendor_marketplace.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResponse <T> {

    private List<T> content;
    private boolean isFirstPage;
    private boolean isLastPage;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;


}
