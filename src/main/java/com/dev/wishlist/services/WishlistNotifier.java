package com.dev.wishlist.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.dev.wishlist.utils.APIConstants.PRODUCT_ADDED_TOPIC;

@Service
public class WishlistNotifier {

    private final KafkaTemplate<Long, Long> kafkaTemplate;

    public WishlistNotifier(KafkaTemplate<Long, Long> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void notify(String topic, Long userId, Long productId) {
        kafkaTemplate.send(PRODUCT_ADDED_TOPIC, userId, productId);
    }
}
