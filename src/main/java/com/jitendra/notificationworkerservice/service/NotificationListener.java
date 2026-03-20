package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;
import com.jitendra.event.PaymentFailedEvent;
import com.jitendra.event.PaymentSuccessEvent;
import com.jitendra.notificationworkerservice.model.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationProcessor processor;

    @KafkaListener(topics = "payment-success", groupId = "order-group")
    public void consumePaymentSuccess(PaymentSuccessEvent event) {

        System.out.println("Payment successful for order: " + event.getOrderId());
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setEmail(event.getEmail());
        notificationEvent.setOrder_id(event.getOrderId());
        notificationEvent.setPhone(event.getPhone());
        notificationEvent.setType(NotificationType.PAYMENT_SUCCESS.toString());
        processor.process(notificationEvent);
    }

    @KafkaListener(topics = "payment-failed", groupId = "order-group")
    public void consumePaymentFailed(PaymentFailedEvent event) {

        System.out.println("Payment failed for order: " + event.getOrderId());
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setEmail(event.getEmail());
        notificationEvent.setPhone(event.getPhone());
        notificationEvent.setOrder_id(event.getOrderId());
        notificationEvent.setType(NotificationType.PAYMENT_FAILED.toString());
          processor.process(notificationEvent);

    }
}