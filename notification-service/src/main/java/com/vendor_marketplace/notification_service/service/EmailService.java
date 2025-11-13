package com.vendor_marketplace.notification_service.service;

import com.vendor_marketplace.common.dto.event.SendOTPEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmail(SendOTPEvent emailMessage) throws MessagingException, UnsupportedEncodingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            helper.setFrom(sender, "Vendor Marketplace (Do not reply)");
            helper.setText(emailMessage.getBody(), true);
            javaMailSender.send(mimeMessage);
            log.info("Email sent to {}", emailMessage.getTo());
        } catch (MessagingException e) {
            log.error("Mail send failed: {}", e.getMessage());
            throw new MailSendException("Failed to send mail for OTP. Please try again later.");
        }
    }
}
