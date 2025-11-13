package com.vendor_marketplace.auth_service.utils;

import java.util.Random;

import static com.vendor_marketplace.auth_service.utils.Constants.OTP_LENGTH;

public class RandomUtil {

    public static String toGenerateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}