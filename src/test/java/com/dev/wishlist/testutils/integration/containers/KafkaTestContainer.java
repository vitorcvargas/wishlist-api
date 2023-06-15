package com.dev.wishlist.testutils.integration.containers;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class KafkaTestContainer {
    private static final String IMAGE_VERSION = "confluentinc/cp-kafka:latest";

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse(IMAGE_VERSION));

    public static KafkaContainer getInstance() {
        return kafkaContainer;
    }

    private KafkaTestContainer() {
    }
}
