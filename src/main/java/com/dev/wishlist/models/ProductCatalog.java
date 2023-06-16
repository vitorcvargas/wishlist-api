package com.dev.wishlist.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@RedisHash
public class ProductCatalog {

    @Id
    private Long id;
    private String name;
    private String description;
    private BigDecimal amount;
    private String s3ImageLink;

    public ProductCatalog() {
    }

    public ProductCatalog(Long id, String name, String description, BigDecimal amount, String s3ImageLink) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.s3ImageLink = s3ImageLink;
    }

    public ProductCatalog(String name, String description, BigDecimal amount, String s3ImageLink) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.s3ImageLink = s3ImageLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getS3ImageLink() {
        return s3ImageLink;
    }

    public void setS3ImageLink(String s3ImageLink) {
        this.s3ImageLink = s3ImageLink;
    }

    @Override
    public String toString() {
        return "ProductCatalog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", s3ImageLink='" + s3ImageLink + '\'' +
                '}';
    }
}
