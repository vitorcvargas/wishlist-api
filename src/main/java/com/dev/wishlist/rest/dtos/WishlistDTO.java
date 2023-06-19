package com.dev.wishlist.rest.dtos;

public class WishlistDTO {

    private String name;
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
