package com.jitendra.notificationworkerservice.service;



import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.auth.oauth2.GoogleCredentials;


import com.jitendra.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
@Slf4j
public class PushService {

    public void send(NotificationEvent event) {

        try {
            // Build FCM message
            Message message = Message.builder()
                    .setToken(event.getMessage())  // FCM device token
                    .setNotification(Notification.builder()
                            .setTitle("Smart E-Commerce")
                            .setBody(buildMessage(event))
                            .build()).putData("order_id",String.valueOf(event.getOrder_id())).build();


            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Push sent successfully: {}", response);

        } catch (FirebaseMessagingException e) {
            log.error("Push notification failed", e);
        }
    }

    private String buildMessage(NotificationEvent event) {
        switch (event.getType()) {
            case "PAYMENT_SUCCESS":
                return "Payment successful for Order #" + event.getOrder_id();
            case "PAYMENT_FAILED":
                return "Payment failed for Order #" + event.getOrder_id();
            case "ORDER_PLACED":
                return "🛒 Your order #" + event.getOrder_id() + " has been placed successfully!";
            case "ORDER_CANCELLED":
                return "Your order #" + event.getOrder_id() + " has been cancelled.";
            default:
                return "Update for Order #" + event.getOrder_id();
        }
    }
}