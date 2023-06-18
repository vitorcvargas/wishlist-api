package com.dev.wishlist.testutils.integration;

import com.dev.wishlist.testutils.integration.containers.KafkaTestContainer;
import com.dev.wishlist.testutils.integration.containers.MongoContainer;
import com.dev.wishlist.testutils.integration.containers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
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
    public static GenericContainer redisContainer = RedisContainer.getInstance();
    public static KafkaContainer kafkaContainer = KafkaTestContainer.getInstance();

    @BeforeEach
    void setPort() {
        port = randomPort;
    }

    static {
        mongoContainer.start();
        redisContainer.start();
        kafkaContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoContainer::getReplicaSetUrl);
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379).toString());
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.kafka.topics", () -> "wishlist.product.added,wishlist.product.deleted");
    }
}
