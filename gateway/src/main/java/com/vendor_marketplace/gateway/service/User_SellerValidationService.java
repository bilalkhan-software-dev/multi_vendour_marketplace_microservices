package com.vendor_marketplace.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class User_SellerValidationService {

    private final WebClient.Builder webClient;

    public Mono<Boolean> validateUserAuthId(String id) {
        return webClient.build().get()
                .uri("http://USER-SERVICE/api/v2/user/validate/{id}", id)
                .retrieve()
                .bodyToMono(Boolean.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .doAfterRetry(retrySignal -> log.warn("Retrying validation for {} attempt {}", id, retrySignal.totalRetries())))
                .doOnSuccess(valid -> log.debug("User validation result for {}: {}", id, valid))
                .onErrorResume(WebClientResponseException.class, this::handleWebClientError)
                .defaultIfEmpty(false);
    }

    public Mono<Boolean> validateSellerAuthId(String id) {
        return webClient.build().get()
                .uri("http://SELLER-SERVICE/api/v2/seller/validate/{id}", id)
                .retrieve()
                .bodyToMono(Boolean.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .doOnSuccess(valid -> log.debug("Seller validation result for {}: {}", id, valid))
                .onErrorResume(WebClientResponseException.class, this::handleWebClientError)
                .defaultIfEmpty(false);
    }

    private Mono<Boolean> handleWebClientError(WebClientResponseException exception) {
        if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
            log.warn("Resource not found during validation: {}", exception.getMessage());
            return Mono.just(false);
        } else if (exception.getStatusCode() == HttpStatus.BAD_REQUEST) {
            log.error("Bad request during validation: {}", exception.getMessage());
            return Mono.just(false);
        } else if (exception.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
            log.error("Service unavailable during validation: {}", exception.getMessage());
            return Mono.just(false);
        }

        log.error("Unexpected error during validation: {}", exception.getMessage());
        return Mono.just(false);
    }
}