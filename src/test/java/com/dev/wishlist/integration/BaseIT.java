package com.dev.wishlist.integration;

import com.dev.wishlist.integration.containers.MongoContainer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.port;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@DirtiesContext
public class BaseIT {

    @LocalServerPort
    private Integer randomPort;

    public static MongoDBContainer mongoContainer = MongoContainer.getInstance();

    @BeforeEach
    void setPort() {
        port = randomPort;
    }

    static {
        mongoContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
    }
}
