package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private final WebClient webClient = WebClient.create();

    public void send(NotificationEvent event) {

        webClient.post()
                .uri("https://api.sendgrid.com/v3/mail/send")
                .header("Authorization", "Bearer YOUR_API_KEY")
                .bodyValue(buildRequest(event))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
    }

    private Map<String, Object> buildRequest(NotificationEvent event) {
        return Map.of(
            "personalizations", List.of(
                Map.of("to", List.of(Map.of("email", event.getEmail())))
            ),
            "from", Map.of("email", "your@email.com"),
            "subject", event.getType(),
            "content", List.of(
                Map.of("type", "text/plain", "value", event.getMessage())
            )
        );
    }
}