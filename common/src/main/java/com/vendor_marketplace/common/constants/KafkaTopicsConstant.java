package com.vendor_marketplace.common.constants;

public class KafkaTopicsConstant {


    public static final String USER_CREATED_TOPIC = "user-created-topic";

    public static final String SELLER_CREATED_TOPIC = "seller-created-topic";

    public static final String SEND_OTP_TOPIC = "send-otp-topic";

    public static final String PRODUCT_CREATE_TOPIC = "product-event-topic";

    public static final String PRODUCT_UPDATE_TOPIC = "product-update-topic";

    public static final String PRODUCT_DELETE_TOPIC = "product-delete-topic";

    public static final String PRODUCT_UPDATE_STOCK_TOPIC = "product-update-stock-topic";

    public static final String TRANSACTION_CREATED_TOPIC = "transaction-created-topic";
    public static final String SELLER_REPORT_TOPIC = "seller-report-topic";

    /**
     * We don't need this SELLER_REPORT_ORDER_CANCEL_TOPIC also handle for order cancel then update refund and order cancel value
     * public static final String SELLER_REPORT_ORDER_CANCEL_TOPIC = "seller-report-order-cancel-topic";
     */

    public static final String ORDER_CREATED_TOPIC = "order-created-topic";
    public static final String ORDER_NOTIFICATION_TOPIC = "order-notification-topic";

    public static final String PAYMENT_SUCCESS_TOPIC = "payment-success-topic";

    /**
     * Either payment failed or payment cancel then publish this topic
     */
    public static final String PAYMENT_FAILED_TOPIC = "order-failed-topic";



}
