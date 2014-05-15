 package com.mongodb.mongoapp.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.mongoapp.domain.SecurityLabel;

@Component
public class SecurityLabelReadConverter implements Converter<BasicDBList, SecurityLabel> {

    public SecurityLabel convert(BasicDBList dbo) {
        SecurityLabel sl = new SecurityLabel();
        for (Object o : dbo) {
            BasicDBList nestedList = (BasicDBList)o;
            for (Object x : nestedList) {
                DBObject d = (DBObject)x;
                String sci = (String)d.get("sci");
                if (sci != null) {
                    sl.addSci(sci);
                }
                String classification = (String)d.get("c");
                if (classification != null) {
                    sl.setClassification(classification);
                }
                
            }
//            DBObject label = (DBObject)nested.get(0);
//            String classification = (String)label.get("c");
//            sl.setClassification(classification);
            
            
        }
       
        return sl;
    }

}
