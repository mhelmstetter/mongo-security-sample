package com.mongodb.mongoapp.domain;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Favorites {
    
    @Field("sl")
    private SecurityLabel securityLabel;
    
    private List<String> cartoonCharacters;



    public List<String> getCartoonCharacters() {
        return cartoonCharacters;
    }

    public void setCartoonCharacters(List<String> cartoonCharacters) {
        this.cartoonCharacters = cartoonCharacters;
    }

    public SecurityLabel getSecurityLabel() {
        return securityLabel;
    }

    public void setSecurityLabel(SecurityLabel sl) {
        this.securityLabel = sl;
    }

}
