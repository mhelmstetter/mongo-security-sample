package com.mongodb.mongoapp.repository;

import com.mongodb.BasicDBObject;
import org.springframework.data.domain.Pageable;

import com.mongodb.mongoapp.domain.Person;

public interface PersonRepositoryCustom {

    // set the CAPCO visibility for the following findPerson(s) commands
    public void setCapcoVisibilityString(String capcoVisibilityString);

    public String getCapcoVisibilityString();     // retrieve current visibility

    public Person findPerson();
    
    public Iterable<Person> findPersons(final Pageable pageable);               // find all Persons
    public Iterable<Person> findPersons(BasicDBObject criteria, final Pageable pageable);     // filter by $match criteria
}
