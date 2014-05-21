package com.mongodb.mongoapp.repository;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
    }

}
