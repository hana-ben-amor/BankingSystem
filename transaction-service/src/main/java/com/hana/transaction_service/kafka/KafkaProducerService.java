package com.hana.transaction_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "transaction-events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendTransactionNotification(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}