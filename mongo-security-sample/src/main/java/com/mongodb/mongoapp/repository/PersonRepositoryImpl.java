package com.mongodb.mongoapp.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.mongoapp.domain.Person;
import com.mongodb.util.JSON;

@Component
public class PersonRepositoryImpl implements PersonRepositoryCustom {
    
    protected static final Logger logger = LoggerFactory.getLogger(PersonRepositoryImpl.class);
    
    @Autowired
    MongoTemplate mongoTemplate;
    
    private String securityExpression;

    public Person findPerson() {
        DBCollection person = mongoTemplate.getCollection("person");
        
        AggregationOptions options = AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).build();
        List<DBObject> pipeline = new ArrayList<DBObject>();
        
        String visibility = "[\"S\"]";
        String userSecurityExpression = String.format(securityExpression, visibility);
        DBObject redactCommand = (DBObject) JSON.parse(userSecurityExpression);
        DBObject redact = new BasicDBObject("$redact", redactCommand);
        
        DBObject match = new BasicDBObject("$match", new BasicDBObject() );
        pipeline.add(match);
        pipeline.add(redact);
        
        logger.debug("findPerson() " + pipeline.toString());
        
        Cursor cursor = person.aggregate(pipeline, options);
        DBObject personDbo = cursor.next();
        
        Person p = mongoTemplate.getConverter().read(Person.class, personDbo);
        
        return p;
    }
    
    public Iterable<Person> findPersons(final Pageable pageable) {
        DBCollection person = mongoTemplate.getCollection("person");
        
        AggregationOptions options = AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).build();
        List<DBObject> pipeline = new ArrayList<DBObject>();
        
        //String visibility = "[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]";
        String visibility = "[ { c:\"U\" } ]";
        String userSecurityExpression = String.format(securityExpression, visibility);
        
        logger.debug("**************** findPersons() " + userSecurityExpression);
        
        DBObject redactCommand = (DBObject) JSON.parse(userSecurityExpression);
        DBObject redact = new BasicDBObject("$redact", redactCommand);
        
        DBObject match = new BasicDBObject("$match", new BasicDBObject() );
        pipeline.add(match);
        pipeline.add(redact);
        
        DBObject limit = new BasicDBObject("$limit", pageable.getPageSize());
        
        pipeline.add(limit);
        logger.debug(pipeline.toString());
        Cursor cursor = person.aggregate(pipeline, options);
        List<Person> persons = new ArrayList<Person>();
        while (cursor.hasNext()) {
            DBObject personDbo = cursor.next();
            
            Person p = mongoTemplate.getConverter().read(Person.class, personDbo);
            persons.add(p);
            
        }
        
        
        return persons;
    }

    public void setSecurityExpression(String securityExpression) {
        this.securityExpression = securityExpression;
    }

}
