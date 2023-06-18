package com.dev.wishlist.migrations;

import com.mongodb.client.model.IndexOptions;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.schema.JsonSchemaProperty;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.data.mongodb.core.validation.Validator;

@ChangeUnit(id = "init-collections", order = "001", author = "vitor")
public class InitCollections {

    @Execution
    public void beforeExecution(final MongoTemplate mongoTemplate) {
        mongoTemplate.createCollection("wishlist", CollectionOptions.empty()
                        .validator(Validator.schema(MongoJsonSchema.builder()
                                .required("userId", "products")
                                .properties(
                                        JsonSchemaProperty.int64("userId"),
                                        JsonSchemaProperty.array("products")).build())))
                .createIndex(new Document("userId", 1), new IndexOptions().name("userId").unique(true));
    }

    @RollbackExecution
    public void rollbackExecution() {
    }
}
