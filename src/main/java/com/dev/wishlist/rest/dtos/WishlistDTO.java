package com.dev.wishlist.rest.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public class WishlistDTO {

    @Schema(
            description = "Wishlist's name",
            example = "Birthday wishlist"
    )
    private String name;

    @Schema(
            description = "Describes whether the wishlist is available publicly",
            example = "true"
    )
    private Boolean isPublic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public String toString() {
        return "WishlistUpdateDTO{" +
                "name='" + name + '\'' +
                ", isPublic=" + isPublic +
                '}';
    }
}
