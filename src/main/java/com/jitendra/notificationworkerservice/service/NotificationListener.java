package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationProcessor processor;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void listen(NotificationEvent event) {
        processor.process(event);
    }
}