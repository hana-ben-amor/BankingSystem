package com.hana.notification_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "transaction-events", groupId = "notification-group")
    public void handleTransactionNotification(String message) {
        System.out.println("RECEIVED Notification : "+message);
    }

}