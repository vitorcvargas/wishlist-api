package com.dev.wishlist.repositories;


import com.dev.wishlist.models.ProductProjection;
import com.dev.wishlist.models.Wishlist;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class WishlistAggregation {

    private final MongoTemplate mongoTemplate;

    public WishlistAggregation(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public AggregationResults<ProductProjection> findAllProductsByUserIdAndSearchInput(final Long userId, final String search) {

        TypedAggregation<Wishlist> aggregation = Aggregation.newAggregation(
                Wishlist.class,
                Aggregation.match(new Criteria("userId").is(userId)),
                Aggregation.unwind("products"),
                Aggregation.match(new Criteria("products.name").regex(format("/%s/", search), "i")),
                Aggregation.group("products").addToSet("products").as("products"),
                Aggregation.project("products")
        );


        AggregationResults<ProductProjection> resultObject = mongoTemplate.aggregate(aggregation, "wishlist",
                ProductProjection.class);

        return resultObject;
    }
}
