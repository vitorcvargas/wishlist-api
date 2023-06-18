package com.dev.wishlist.configuration;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.List;

@Configuration
public class KafkaAdminConfig {

    @Value("${spring.kafka.topics}")
    private List<String> topics;
    private final KafkaProperties properties;

    public KafkaAdminConfig(final KafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        final var configs = new HashMap<String, Object>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaAdmin.NewTopics newTopics(final KafkaAdmin kafkaAdmin) {
        final var newTopics = new KafkaAdmin.NewTopics(
                topics.stream().map(topic -> TopicBuilder.name(topic).partitions(2).build())
                        .toArray(NewTopic[]::new));

        return newTopics;
    }
}
