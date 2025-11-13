package com.vendor_marketplace.auth_service.utils;

import java.util.concurrent.TimeUnit;

public final class Constants {

    public static final Long JWT_TOKEN_EXPIRATION = TimeUnit.DAYS.toMillis(3);

    public static final int OTP_LENGTH = 6;


}
