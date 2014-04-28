package com.mongodb.mongoapp.domain;

import java.util.List;

public class SecurityLabel {
    
    private String classification;
    
    private List<String> sci;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public List<String> getSci() {
        return sci;
    }

    public void setSci(List<String> sci) {
        this.sci = sci;
    }

}
