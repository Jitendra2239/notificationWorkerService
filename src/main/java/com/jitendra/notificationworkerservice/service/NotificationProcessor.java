package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProcessor {

    private final EmailService emailService;
    private final PushService pushService;
    private final SmsService smsService;

    public void process(NotificationEvent event) {

        switch (event.getType()) {

            case "ORDER_PLACED":
            case "PAYMENT_SUCCESS":
                emailService.send(event);
                pushService.send(event);
                break;

            case "ORDER_CANCELLED":
                emailService.send(event);
                smsService.send(event);
                break;
        }
    }
}