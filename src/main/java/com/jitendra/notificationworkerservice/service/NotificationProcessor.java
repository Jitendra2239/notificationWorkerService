package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.jitendra.notificationworkerservice.model.NotificationType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessor {

    private final EmailService emailService;
    private final PushService pushService;
    private final SmsService smsService;

    public void process(NotificationEvent event) {

        log.info("Processing notification: {}", event.getType());

        switch (event.getType()) {
            case "ORDER_CREATED":
            case "PAYMENT_SUCCESS":
                sendSafe(() -> emailService.send(event));
                sendSafe(() -> pushService.send(event));
                break;

            case "ORDER_CANCELLED":
            case "PAYMENT_FAILED":
                sendSafe(() -> emailService.send(event));
                sendSafe(() -> smsService.send(event));
                sendSafe(() -> pushService.send(event));
                break;

            default:
                log.warn("Unsupported notification type: {}", event.getType());
        }
    }

    private void sendSafe(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            log.error("Notification failed", e);
        }
    }
}