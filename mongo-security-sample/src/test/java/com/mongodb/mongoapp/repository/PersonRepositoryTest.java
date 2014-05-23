package com.mongodb.mongoapp.repository;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.mongodb.*;
import junit.framework.Assert;
import org.bson.types.BasicBSONList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.mongoapp.domain.Favorites;
import com.mongodb.mongoapp.domain.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    @Test
    public void testSave() {
        Person p = new Person();
        Favorites favorites = new Favorites();
        List<String> cartoonCharacters = new ArrayList<String>();
        cartoonCharacters.add("Moe");
        cartoonCharacters.add("Larry");
        favorites.setCartoonCharacters(cartoonCharacters);
        p.setFavorites(favorites);
        personRepository.save(p);
        //System.out.println(p);

        Person p2 = personRepository.findAll().iterator().next();
        System.out.println(p2);
    }

    @Test
    public void testFind() {


        Person p2 = personRepository.findAll().iterator().next();
        //System.out.println(p2.getFavorites().getSecurityLabel().get(0));
    }

    @Test
    public void testStdJavaDriverFind() throws UnknownHostException {

        Mongo mongo = new Mongo();

        DB db = mongo.getDB("test");
        DBCollection customersCollection = db.getCollection("ttt$customers");
        customersCollection.drop();

        customersCollection.find();

        DBObject address = new BasicDBObject("city", "NYC");
        address.put("street", "Broadway");

        DBObject addresses = new BasicDBObject();

        if (false) {
            BasicBSONList bsonList = new BasicBSONList();
            bsonList.putAll(addresses);
            addresses.putAll(bsonList);
        } else {
            addresses.putAll(address);
        }

        DBObject customer = new BasicDBObject("firstname", "Tom");
        customer.put("lastname", "Smith");
        customer.put("addresses", addresses);

        customersCollection.insert(customer);
        //        customersCollection.find(null, null);
        Assert.assertEquals(1, customersCollection.count());

        // customersCollection.find()
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

}