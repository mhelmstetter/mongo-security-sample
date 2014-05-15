package com.mongodb.mongoapp.domain;

import org.springframework.data.mongodb.core.mapping.Field;

public class SecurityLabeledValue {

    @Field("sl")
    private SecurityLabel securityLabel;
    private Object value;

    public SecurityLabel getSecurityLabel() {
        return securityLabel;
    }

    public void setSecurityLabel(SecurityLabel securityLabel) {
        this.securityLabel = securityLabel;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        sb.append(" ");
        sb.append(securityLabel);
        return sb.toString();
    }
    
    

}
