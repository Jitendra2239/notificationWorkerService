package com.jitendra.notificationworkerservice.service;

import com.jitendra.event.NotificationEvent;

public interface NotificationService {
    public void send(NotificationEvent event);
}
