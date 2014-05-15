package com.mongodb.mongoapp.repository;

import java.util.ArrayList;
import java.util.List;

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

}
