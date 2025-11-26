package com.vendor_marketplace.payment_service.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "stripe")
@Data
@Validated
public class StripeConfig {

    @NotBlank(message = "Stripe secret key is required")
    private String secretKey;

    @NotBlank(message = "Success URL is required")
    private String successUrl;

    @NotBlank(message = "Cancel URL template is required")
    private String cancelUrl;
}
