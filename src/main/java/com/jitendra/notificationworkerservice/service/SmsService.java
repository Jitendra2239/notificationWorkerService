package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;
import com.twilio.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.jitendra.notificationworkerservice.model.NotificationType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    @Value("${twilio.phone.number}")
    private String fromNumber;

    public void send(NotificationEvent event) {

        try {
            // Validate phone number
            String toPhone = "+91"+event.getPhone();

            if (toPhone == null || toPhone.isBlank()) {
                log.warn("Cannot send SMS: recipient phone number is missing for Order ID {}", event.getOrder_id());
                return;
            }

            // Validate Twilio sender number
            if (fromNumber == null || fromNumber.isBlank()) {
                log.error("Twilio sender number is not configured!");
                return;
            }

            // Build message body safely
            String messageBody = buildMessage(event);
            if (messageBody == null || messageBody.isBlank()) {
                messageBody = "Notification for Order #" + event.getOrder_id();
            }

            // Send SMS
            com.twilio.rest.api.v2010.account.Message message =
                    com.twilio.rest.api.v2010.account.Message.creator(
                            new com.twilio.type.PhoneNumber(toPhone),
                            new com.twilio.type.PhoneNumber(fromNumber),
                            messageBody
                    ).create();

            log.info("SMS sent successfully: {}", message.getSid());

        } catch (Exception e) {
            log.error("SMS sending failed for Order ID {}", event.getOrder_id(), e);
        }
    }

    private String buildMessage(NotificationEvent event) {
        if (event == null) return null;

        return switch (event.getType()) {
            case "PAYMENT_SUCCESS" -> "Payment successful for Order #" + event.getOrder_id();
            case "PAYMENT_FAILED" -> "Payment failed for Order #" + event.getOrder_id();
            case "ORDER_CANCELLED" -> "Order #" + event.getOrder_id() + " has been cancelled";
            case  "SHIPMENT_CREATED"-> "Shipment successful for Order #" + event.getOrder_id();
            case "SHIPMENT_FAIELD"-> "Shipment failed for Order #" + event.getOrder_id();
            default -> "Notification for Order #" + event.getOrder_id() + " has been sent";
        };
    }
}