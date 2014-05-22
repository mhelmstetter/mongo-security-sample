package com.mongodb.mongoapp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.mongodb.*;
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

    private String capcoVisibilityString = "[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]";

    @Autowired
    MongoTemplate mongoTemplate;
    
    private String securityExpression;

    public Person findPerson() {
        DBCollection person = mongoTemplate.getCollection("person");
        
        AggregationOptions options = AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).build();
        List<DBObject> pipeline = new ArrayList<DBObject>();

        String visibility = getCapcoVisibilityString();
        //String visibility = "[\"S\"]";
        DBObject redact = getRedactCommand(visibility);

        DBObject criteria = null;  // null means find all documents ( criteria of {} )

        addRedactionMatchToPipeline(pipeline, criteria, redact);
        
        logger.debug("findPerson() " + pipeline.toString());
        
        Cursor cursor = person.aggregate(pipeline, options);
        DBObject personDbo = cursor.next();
        
        Person p = mongoTemplate.getConverter().read(Person.class, personDbo);
        
        return p;
    }

    /**
     * Queries for an object in this collection.
     * <p>
     * An empty DBObject will match every document in the collection.
     * Regardless of fields specified, the _id fields are always returned.
     * </p>
     * <p>
     * An example that returns the "x" and "_id" fields for every document
     * in the collection that has an "x" field:
     * </p>
     * <pre>
     * {@code
     * BasicDBObject keys = new BasicDBObject();
     * keys.put("x", 1);
     *
     * DBCursor cursor = collection.find(new BasicDBObject(), keys);}
     * </pre>
     *
     * @param ref object for which to search
     * @param keys fields to return
     * @return a cursor to iterate over results
     * @mongodb.driver.manual tutorial/query-documents/ Query
     */
    //public DBCursor find( DBObject ref , DBObject keys ){
    //    return new DBCursor( this, ref, keys, getReadPreference());
    //}


    // interface is DBObject ref , DBObject keys
    public Iterable<Person> findPersons(final Pageable pageable/*, match , projection, .... */) {
        BasicDBObject criteria = null;  // null means find all documents ( criteria of {} )
        return findPersons(criteria,   pageable);
    }

    /**
     * For instance:
     *
     * <tt><pre>
     *                  BasicDBObject criteria = new BasicDBObject("lastName", "Best");
     *                  Iterable<Person>  it = findPersons( criteria, pageable)
     * </pre></tt>
     * @param criteria  the selection criteria
     * @param pageable
     * @return
     */
    public Iterable<Person> findPersons(BasicDBObject criteria, final Pageable pageable) {
        DBCollection person = mongoTemplate.getCollection("person");

        AggregationOptions options = AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).build();
        List<DBObject> pipeline = new ArrayList<DBObject>();

        String visibility = getCapcoVisibilityString();
        DBObject redact = getRedactCommand(visibility);

        addRedactionMatchToPipeline(pipeline, criteria, redact);
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

    // FUTURE : this is for future use, we pass in a DBCollection and the criteria, etc ...

    public Iterable<Person> findBasedOnRedactedQuery(DBCollection collection, DBObject criteria, DBObject fields, final Pageable pageable/*, match , projection, .... */) {

        AggregationOptions options = AggregationOptions.builder().outputMode(AggregationOptions.OutputMode.CURSOR).build();
        List<DBObject> pipeline = new ArrayList<DBObject>();

        String visibility = getCapcoVisibilityString();
        DBObject redact = getRedactCommand(visibility);

        addRedactionMatchToPipeline(pipeline, criteria, redact);
        addLimitToPipeline(pageable, pipeline);

        logger.debug("findBasedOnRedactedQuery on collection: " + collection.getFullName() +  " " + pipeline.toString());

        //////////////
        // BUGBUG : for now we need to use a Collection of Person !
        //////////////
        Cursor cursor = collection.aggregate(pipeline, options);
        List<Person> persons = new ArrayList<Person>();
        while (cursor.hasNext()) {
            DBObject personDbo = cursor.next();

            Person p = mongoTemplate.getConverter().read(Person.class, personDbo);
            persons.add(p);

        }


        return persons;
    }

    /**
     * Get a single document from collection.
     *
     * @param criteria    the selection criteria using query operators.
     * @param fields      specifies which projection MongoDB will return from the documents in the result set.
     * @return A document that satisfies the query specified as the argument to this method.
     * @mongodb.driver.manual tutorial/query-documents/ Query
     * @since 2.12.0
     */
    DBObject findOne(DBCollection collection, DBObject criteria, DBObject fields, DBObject orderBy, ReadPreference readPref,
                     long maxTime, TimeUnit maxTimeUnit) {

       /*       * @param orderBy     A document whose fields specify the attributes on which to sort the result set.
                * @param readPref    {@code ReadPreference} to be used for this operation
                * @param maxTime     the maximum time that the server will allow this operation to execute before killing it
                * @param maxTimeUnit the unit that maxTime is specified in
        *
        * Are additional flags that can be specified by the findOne() method.
        */

        return null;
    }


    /**
     * Add "$redact" mongodb command incantation to pipeline , after inserting the MATCH element.
     *
     * @param pipeline   the pipeline that will form the basis of the aggregate operation
     * @param criteria   the match criteria desired by user, if none pass in NULL
     * @param redact     the redact clause
     */
    private void addRedactionMatchToPipeline(List<DBObject> pipeline, DBObject criteria, DBObject redact) {
        DBObject match = new BasicDBObject("$match", ( (criteria == null) ? new BasicDBObject() : criteria ) );
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

    /** This may be used to override any set CAPCO visibility settings as needed by the organization */
    private void maybeOverrideCAPCOToUnclassOnly() {

        // e.g. you might want to keep it limited to unclassified
        boolean showUnclassifiedOnly = false;

        if (showUnclassifiedOnly) capcoVisibilityString = "[ { c:\"U\" } ]";
    }

    /**
     * @return  CAPCO visibility       string, e.g.
     *
     * <tt>"[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]"</tt>
     *
     */
    public String getCapcoVisibilityString() {

        return capcoVisibilityString;
    }

    /** set the CAPCO visibility string, e.g.
     *
     * <tt>"[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]"</tt>
     *
     * @param capcoVisibilityString
     */
    public void setCapcoVisibilityString(String capcoVisibilityString) {
        this.capcoVisibilityString = capcoVisibilityString;
        maybeOverrideCAPCOToUnclassOnly();
    }



    /** sets the SecurityExpression string that is specific to the $redact operator */
    public void setSecurityExpression(String securityExpression) {
        this.securityExpression = securityExpression;
    }

}
