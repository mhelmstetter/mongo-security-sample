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

    private String visibility = "[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]";

    @Autowired
    MongoTemplate mongoTemplate;
    
    private String securityExpression;

    public Person findPerson() {
        DBCollection person = mongoTemplate.getCollection("person");
        
        AggregationOptions options = AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).build();
        List<DBObject> pipeline = new ArrayList<DBObject>();

        String visibility = getCAPCOVisibilityString();
        //String visibility = "[\"S\"]";
        DBObject redact = getRedactCommand(visibility);

        addRedactionMatchToPipeline(pipeline, redact);
        
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

        String visibility = getCAPCOVisibilityString();
        DBObject redact = getRedactCommand(visibility);

        addRedactionMatchToPipeline(pipeline, redact);
        addLimitToPipeline(pageable, pipeline);

        logger.debug("findPersons() " + pipeline.toString());

        Cursor cursor = person.aggregate(pipeline, options);
        List<Person> persons = new ArrayList<Person>();
        while (cursor.hasNext()) {
            DBObject personDbo = cursor.next();
            
            Person p = mongoTemplate.getConverter().read(Person.class, personDbo);
            persons.add(p);
            
        }
        
        
        return persons;
    }

    private String getCAPCOVisibilityString() {

        maybeOverrideCAPCOToUnclassOnly();
        return visibility;
    }

    private void maybeOverrideCAPCOToUnclassOnly() {
        boolean showUnclassifiedOnly = false;

        if (showUnclassifiedOnly) visibility = "[ { c:\"U\" } ]";
    }

    /** Add "$redact" mongodb command incantation to pipeline */
    private void addRedactionMatchToPipeline(List<DBObject> pipeline, DBObject redact) {
        DBObject match = new BasicDBObject("$match", new BasicDBObject() );
        pipeline.add(match);
        pipeline.add(redact);
    }

    /** Add "limit" for multi-object results to mongodb command incantation to pipeline */
    private void addLimitToPipeline(Pageable pageable, List<DBObject> pipeline) {
        int pageSize = pageable.getPageSize();
        if (pageSize <= 0)  return;        // TODO: should we also define a default page size for 0?

        DBObject limit = new BasicDBObject("$limit", pageSize);

        pipeline.add(limit);
    }

    /** build the "$redact" mongodb command based on CAPCO visibility setting */
    private DBObject getRedactCommand(String visibility) {
        if (visibility == null) visibility = "[\"U\"]";
        String userSecurityExpression = String.format(securityExpression, visibility);
        logger.debug("**************** findPerson/s() userSecurityExpression: " + userSecurityExpression);
        DBObject redactCommand = (DBObject) JSON.parse(userSecurityExpression);
        return new BasicDBObject("$redact", redactCommand);
    }

    /**
     * @return  CAPCO visibility       string, e.g.
     *
     * <tt>"[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]"</tt>
     *
     */
    public String getVisibility() {
        return visibility;
    }

    /** set the CAPCO visibility string, e.g.
     *
     * <tt>"[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]"</tt>
     *
     * @param visibility
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    /** sets the SecurityExpression string that is specific to the $redact operator */
    public void setSecurityExpression(String securityExpression) {
        this.securityExpression = securityExpression;
    }

}
