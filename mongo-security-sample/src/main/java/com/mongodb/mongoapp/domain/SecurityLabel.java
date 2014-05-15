package com.mongodb.mongoapp.domain;

import java.util.ArrayList;
import java.util.Iterator;
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

    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append(classification);
        if (sci != null) {
            sb.append("//");
            for (Iterator<String> i = sci.iterator(); i.hasNext();) {
                sb.append(i.next());
                if (i.hasNext()) {
                    sb.append("/");
                }
            }    
        }
        
        return sb.toString();
    }

    public void addSci(String sci2) {
        if (sci == null) {
            sci = new ArrayList<String>();
        }
        sci.add(sci2);
    }

}
