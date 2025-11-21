package com.vendor_marketplace.common.utils;


public class ProductUtil {

    public static int calculateDiscountPercentage(int totalMrpPrice, int totalSellingPrice) {

        double discount = totalMrpPrice - totalSellingPrice;
        double discountPercentage = (discount / totalMrpPrice) * 100;
        return (int) discountPercentage;
    }

    public static long convertPKRToDollar(long totalAmount) {
        double usdAmount = totalAmount / 280.0; // PKR to USD
        // USD to cents
        return (long) (usdAmount * 100);
    }

}
