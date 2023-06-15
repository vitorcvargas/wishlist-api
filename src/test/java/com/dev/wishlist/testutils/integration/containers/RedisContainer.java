package com.dev.wishlist.testutils.integration.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class RedisContainer {

    private static final String IMAGE_VERSION = "redis:5.0.3-alpine";

    @Container
    static GenericContainer redis =
            new GenericContainer(DockerImageName.parse(IMAGE_VERSION))
                    .withExposedPorts(6379);

    public static GenericContainer getInstance() {
        return redis;
    }

    private RedisContainer() {
    }
}
