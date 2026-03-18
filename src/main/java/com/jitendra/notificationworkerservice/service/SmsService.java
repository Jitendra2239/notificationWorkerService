package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void send(NotificationEvent event) {
        // Use Twilio SDK or REST API
        System.out.println("Sending SMS to " + event.getPhone());
    }
}