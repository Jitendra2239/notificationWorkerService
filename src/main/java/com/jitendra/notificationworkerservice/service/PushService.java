package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class PushService {

    private final WebClient webClient = WebClient.create();

    public void send(NotificationEvent event) {

        webClient.post()
                .uri("https://fcm.googleapis.com/fcm/send")
                .header("Authorization", "key=YOUR_SERVER_KEY")
                .bodyValue(Map.of(
                        "to", "DEVICE_TOKEN",
                        "notification", Map.of(
                                "title", event.getType(),
                                "body", event.getMessage()
                        )
                ))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }
}