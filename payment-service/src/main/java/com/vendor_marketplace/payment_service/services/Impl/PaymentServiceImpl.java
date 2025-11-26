package com.vendor_marketplace.payment_service.services.Impl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.vendor_marketplace.common.dto.enums.PaymentMethod;
import com.vendor_marketplace.common.dto.enums.PaymentStatus;
import com.vendor_marketplace.common.dto.event.OrderCreatedEvent;
import com.vendor_marketplace.common.dto.event.PaymentCancelOrFailEvent;
import com.vendor_marketplace.common.dto.event.PaymentSuccessEvent;
import com.vendor_marketplace.common.dto.event.TransactionCreateEvent;
import com.vendor_marketplace.common.dto.response.PagedResponse;
import com.vendor_marketplace.common.exception.ResourceNotFoundException;
import com.vendor_marketplace.payment_service.dao.interfaces.PaymentDao;
import com.vendor_marketplace.payment_service.kafka.publisher.KafkaEventPublisher;
import com.vendor_marketplace.payment_service.mapper.PaymentMapper;
import com.vendor_marketplace.payment_service.models.dto.response.PaymentResponse;
import com.vendor_marketplace.payment_service.models.entity.Payment;
import com.vendor_marketplace.payment_service.services.PaymentService;
import com.vendor_marketplace.payment_service.services.StripeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
class PaymentServiceImpl implements PaymentService {

    private final StripeService stripeService;
    private final KafkaEventPublisher kafkaEventPublisher;
    private final PaymentDao paymentDao;

    @Override
    public void processPaymentCreation(OrderCreatedEvent event) {

        PaymentMethod paymentMethod = event.getPaymentMethod();
        String orderId = event.getOrderId();
        Integer totalAmount = event.getTotalAmount();
        String customerEmail = event.getCustomerEmail();
        String customerId = event.getCustomerId();
        List<String> sellerIds = event.getSellerIds();

        Session session = stripeService.createPaymentLinkSession(orderId, sellerIds, customerId, customerEmail, Long.valueOf(totalAmount));

        Payment payment = Payment.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .paymentSessionId(session.getId())
                .paymentLinkUrl(session.getUrl())
                .totalAmount(Long.valueOf(totalAmount))
                .orderId(orderId)
                .userId(customerId)
                .userEmail(customerEmail)
                .paymentMethod(paymentMethod.name())
                .sellerIds(sellerIds)
                .build();

        paymentDao.save(payment);
    }

    @Override
    public String getPaymentLinkOfTheOrderId(String orderId) {

        boolean exist = paymentDao.existByOrderId(orderId);
        if (exist) {
            return paymentDao.findPaymentLinkOfTheOrder(orderId);
        }
        return null;
    }

    @Override
    public PaymentResponse getPaymentDetails(Long id) {
        return PaymentMapper.toPaymentResponse(paymentDao.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found with id: " + id)
        ));
    }

    @Override
    public PaymentResponse getPaymentDetails(String orderId) {
        return PaymentMapper.toPaymentResponse(paymentDao.findByOrderId(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found with Order ID: " + orderId)
        ));
    }

    @Override
    public PaymentResponse getPaymentDetailByPaymentSessionId(String paymentSessionId) {
        return PaymentMapper.toPaymentResponse(paymentDao.findByPaymentSessionId(paymentSessionId).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found with Session ID: " + paymentSessionId)
        ));
    }

    @Override
    public void deletePaymentById(Long id) {

        boolean exist = paymentDao.existById(id);
        if (!exist) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
        paymentDao.deleteById(id);

    }

    @Override
    public void deletePaymentByOrderId(String orderId) {

        boolean exist = paymentDao.existByOrderId(orderId);
        if (!exist) {
            throw new ResourceNotFoundException("Payment not found with Order ID: " + orderId);
        }
        paymentDao.deleteByOrderId(orderId);

    }

    @Override
    public void verifyPaymentAndPublish(String paymentSessionId, String orderId, PaymentStatus paymentStatus) throws StripeException {
        log.info("Verify payment for session_id: {}, orderId: {}. payment_status: {}", paymentSessionId, orderId, paymentStatus.name());


        if (paymentSessionId == null || paymentSessionId.trim().isEmpty()) {
            log.debug("Stopping verifying payment session id is null");
            throw new IllegalArgumentException("Payment session ID cannot be null or empty");
        }

        if (orderId == null || orderId.trim().isEmpty()) {
            log.debug("Stopping verifying payment order id is null");
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }

        boolean verified = stripeService.verifyStripe(paymentSessionId);

        Payment payment = paymentDao.findByPaymentSessionId(paymentSessionId).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found with paymentSessionId: " + paymentSessionId)
        );

        if (!orderId.equals(payment.getOrderId())) {
            log.info("Order ID: {} mismatch when payment verify", orderId);
            throw new IllegalArgumentException(
                    "Order ID mismatch. Provided: " + orderId + ", Expected: " + payment.getOrderId());
        }

        if (payment.getPaymentStatus() == paymentStatus) {
            log.info("Payment status already set to {}. PaymentSessionId: {}, OrderId: {}",
                    paymentStatus, paymentSessionId, orderId);
            return;
        }

        if (verified) {
            log.info("Payment is verified success. Now updating status and publish event");
            payment.setPaymentStatus(paymentStatus);
            paymentDao.save(payment);

            PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                    .orderId(orderId)
                    .email(payment.getUserEmail())
                    .build();
            kafkaEventPublisher.publishPaymentSuccessEvent(event);

            payment.getSellerIds().forEach(seller -> {
                TransactionCreateEvent transactionCreateEvent = TransactionCreateEvent.builder()
                        .customerId(payment.getUserId())
                        .orderId(orderId)
                        .sellerId(seller)
                        .build();
                kafkaEventPublisher.publishTransactionEvent(transactionCreateEvent);
            });


        } else {
            log.info("Payment is not verified. Now updating status and publish event");
            PaymentCancelOrFailEvent event = PaymentCancelOrFailEvent.builder()
                    .orderId(orderId)
                    .email(payment.getUserEmail())
                    .paymentStatus(paymentStatus)
                    .build();
            paymentDao.save(payment);
            kafkaEventPublisher.publishPaymentCancelFailEvent(event);
        }
    }

    @Override
    public boolean checkPaymentSessionIdStatus(String paymentSessionId) throws StripeException {
        return stripeService.verifyStripe(paymentSessionId);
    }

    @Override
    public PagedResponse<PaymentResponse> getAllPayments(int page, int size, boolean isNewest) {

        Page<Payment> payments = paymentDao.findAll(page, size, isNewest);

        return PagedResponse.<PaymentResponse>builder()
                .content(payments.getContent().stream().map(PaymentMapper::toPaymentResponse).toList())
                .totalPages(payments.getTotalPages())
                .pageSize(payments.getSize())
                .totalElements(payments.getTotalElements())
                .isLastPage(payments.isLast())
                .isFirstPage(payments.isFirst())
                .pageNumber(payments.getNumber())
                .build();
    }
}
