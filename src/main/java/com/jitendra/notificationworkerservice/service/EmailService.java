package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
@Service
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void send(NotificationEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getEmail());
            message.setSubject("Smart E-Commerce Notification");
            message.setText(buildMessage(event));

            mailSender.send(message);

            log.info("Email sent to {}", event.getEmail());

        } catch (Exception e) {
            log.error("Email sending failed", e);
        }
    }

    private String buildMessage(NotificationEvent event) {
        switch (event.getType()) {
            case "PAYMENT_SUCCESS":
                return "Payment successful for Order #" + event.getOrder_id();
            case "PAYMENT_FAILED":
                return "Payment failed for Order #" + event.getOrder_id();
            case "ORDER_PLACED":
                return "Your order #" + event.getOrder_id() + " has been placed successfully!";
            case "ORDER_CANCELLED":
                return "Your order #" + event.getOrder_id() + " has been cancelled.";
            default:
                return "Update for Order #" + event.getOrder_id();
        }
    }
}