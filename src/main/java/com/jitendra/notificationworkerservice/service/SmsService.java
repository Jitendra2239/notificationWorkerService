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
            String messageBody = buildMessage(event);

            com.twilio.rest.api.v2010.account.Message message =
                    com.twilio.rest.api.v2010.account.Message.creator(
                            new com.twilio.type.PhoneNumber(event.getPhone()),
                            new com.twilio.type.PhoneNumber(fromNumber),
                            messageBody
                    ).create();

            log.info("SMS sent: {}", message.getSid());

        } catch (Exception e) {
            log.error("SMS sending failed", e);
        }
    }

    private String buildMessage(NotificationEvent event) {

        switch (event.getType()) {

            case "PAYMENT_SUCCESS":
                return "✅ Payment successful for Order #"+event.getOrder_id();

            case "PAYMENT_FAILED":
                return "❌ Payment failed for Order #" + event.getOrder_id();

            case "ORDER_CANCELLED":
                return "⚠️ Order #" + event.getOrder_id() + " has been cancelled";

            default:
                return "Notification for Order #" + event.getOrder_id() + " has been sent";
        }
    }
}