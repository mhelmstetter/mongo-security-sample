package com.mongodb.mongoapp.domain;

import java.util.List;

import org.springframework.data.annotation.Reference;

public class Favorites {
    
    @Reference
    private SecurityLabel sl;
    
    private List<String> cartoonCharacters;

    public SecurityLabel getSl() {
        return sl;
    }

    public void setSl(SecurityLabel sl) {
        this.sl = sl;
    }

    public List<String> getCartoonCharacters() {
        return cartoonCharacters;
    }

    public void setCartoonCharacters(List<String> cartoonCharacters) {
        this.cartoonCharacters = cartoonCharacters;
    }

}
