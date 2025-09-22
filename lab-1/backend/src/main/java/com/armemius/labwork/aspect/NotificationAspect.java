package com.armemius.labwork.aspect;

import com.armemius.labwork.annotations.NotifyClients;
import com.armemius.labwork.websocket.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {

    private final WebSocketNotificationService notificationService;

    @AfterReturning("@annotation(notifyClients)")
    public void sendNotification(NotifyClients notifyClients) {
        String message = notifyClients.value();
        notificationService.broadcast(message);
    }
}
