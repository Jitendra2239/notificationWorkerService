package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.*;
import com.jitendra.notificationworkerservice.model.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationProcessor processor;

    @KafkaListener(topics = "payment-success", groupId = "notification-group")
    public void consumePaymentSuccess(PaymentSuccessEvent event) {

        System.out.println("Payment successful for order: " + event.getOrderId()+"tophone:"+event.getPhone());
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setEmail(event.getEmail());
        notificationEvent.setOrder_id(event.getOrderId());
        notificationEvent.setPhone(event.getPhone());
        notificationEvent.setType(NotificationType.PAYMENT_SUCCESS.toString());
        processor.process(notificationEvent);
    }

    @KafkaListener(topics = "payment-failed", groupId = "notification-group")
    public void consumePaymentFailed(PaymentFailedEvent event) {

        System.out.println("Payment failed for order: " + event.getOrderId()+"phone "+event.getPhone());
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setEmail(event.getEmail());
        notificationEvent.setPhone(event.getPhone());
        notificationEvent.setOrder_id(event.getOrderId());
        notificationEvent.setType(NotificationType.PAYMENT_FAILED.toString());
          processor.process(notificationEvent);

    }
    @KafkaListener(topics = "shipment-created", groupId = "notification-group")
    public void notifyShipment(ShipmentCreatedEvent event) {

        String message = "Your order has been shipped 🚚. Tracking ID: "
                + event.getTrackingId();
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setPhone(event.getPhone());
        notificationEvent.setOrder_id(event.getOrderId());
        notificationEvent.setType(NotificationType.SHIPMENT_CREATED.toString());
        notificationEvent.setEmail(event.getEmail());
        notificationEvent.setMessage(message);

        System.out.println("Shipment successful for order: " + event.getOrderId()+"with trakingId  :"+event.getTrackingId());
        processor.process(notificationEvent);
    }

    @KafkaListener(topics = "shipment-failed", groupId = "notification-group")
    public void notifyFailure(ShipmentFailedEvent event) {

        String message = "Your shipment failed. We will retry shortly.";
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setPhone(event.getPhone());
        notificationEvent.setOrder_id(event.getOrderId());
        notificationEvent.setType(NotificationType.PAYMENT_FAILED.toString());
        notificationEvent.setEmail(event.getEmail());
        notificationEvent.setMessage(message);
        processor.process(notificationEvent);
    }
}