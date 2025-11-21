package com.vendor_marketplace.product_query_service.services;

import com.vendor_marketplace.common.dto.event.ProductCreateEvent;
import com.vendor_marketplace.common.dto.event.ProductDeleteEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateEvent;
import com.vendor_marketplace.common.dto.event.ProductUpdateStockEvent;

public interface ProductConsumerService {

    void addProduct(ProductCreateEvent event);

    void deleteProductById(ProductDeleteEvent event);

    void updateProduct(ProductUpdateEvent event);

    void updateStocks(ProductUpdateStockEvent event);

}
