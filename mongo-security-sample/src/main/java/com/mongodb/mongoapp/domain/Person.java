package com.mongodb.mongoapp.domain;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Person extends PersistedDomainObject {

    private String firstName;
    private String lastName;
    
    private SecurityLabeledValue country;
    
    private SecurityLabeledValue ssn;
    
    
    private Favorites favorites;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SecurityLabeledValue getCountry() {
        return country;
    }

    public void setCountry(SecurityLabeledValue country) {
        this.country = country;
    }

    public Favorites getFavorites() {
        return favorites;
    }

    public void setFavorites(Favorites favorites) {
        this.favorites = favorites;
    }

    public SecurityLabeledValue getSsn() {
        return ssn;
    }

    public void setSsn(SecurityLabeledValue ssn) {
        this.ssn = ssn;
    }

}
