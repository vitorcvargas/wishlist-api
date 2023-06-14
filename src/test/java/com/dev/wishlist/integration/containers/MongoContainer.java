package com.dev.wishlist.integration.containers;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

public class MongoContainer {

    private static final String IMAGE_VERSION = "mongo:latest";

    @Container
    private static final MongoDBContainer container = new MongoDBContainer(IMAGE_VERSION);

    public static MongoDBContainer getInstance() {
        return container;
    }
}
