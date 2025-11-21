package com.vendor_marketplace.common.dto.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateEvent {


    private String productId;
    private String title;
    private String description;

    private Integer stocks;
    private Integer sellingPrice;
    private Integer mrpPrice;

    private List<String> images;
    private List<String> colors;
    private List<String> sizes;

}
