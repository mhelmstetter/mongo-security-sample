package com.mongodb.mongoapp.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.AggregationOptions;
import com.mongodb.BasicDBObject;
import com.mongodb.Cursor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.mongoapp.domain.Person;

public class PersonRepositoryImpl implements PersonRepositoryCustom {
    
    @Autowired
    MongoTemplate mongoTemplate;

    public Person findPerson() {
        DBCollection person = mongoTemplate.getCollection("person");
        
        AggregationOptions options = AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).build();
        List<DBObject> pipeline = new ArrayList<DBObject>();
        
        DBObject match = new BasicDBObject("$match", new BasicDBObject() );
        pipeline.add(match);
        
        Cursor cursor = person.aggregate(pipeline, options);
        DBObject personDbo = cursor.next();
        
        Person p = mongoTemplate.getConverter().read(Person.class, personDbo);
        
        return p;
    }
    
    public Iterable<Person> findPersons(final Pageable pageable) {
        DBCollection person = mongoTemplate.getCollection("person");
        
        AggregationOptions options = AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).build();
        List<DBObject> pipeline = new ArrayList<DBObject>();
        
        DBObject match = new BasicDBObject("$match", new BasicDBObject() );
        DBObject limit = new BasicDBObject("$limit", 10);
        
        pipeline.add(match);
        pipeline.add(limit);
        
        Cursor cursor = person.aggregate(pipeline, options);
        List<Person> persons = new ArrayList<Person>();
        while (cursor.hasNext()) {
            DBObject personDbo = cursor.next();
            
            Person p = mongoTemplate.getConverter().read(Person.class, personDbo);
            persons.add(p);
            
        }
        
        
        return persons;
    }

}
