package com.mongodb.mongoapp.repository;

import org.springframework.data.domain.Pageable;

import com.mongodb.mongoapp.domain.Person;

public interface PersonRepositoryCustom {

    // set the CAPCO visibility for the following findPerson(s) commands
    public void setCapcoVisibilityString(String capcoVisibilityString);

    public String getCapcoVisibilityString();     // retrieve current visibility

    public Person findPerson();
    
    public Iterable<Person> findPersons(final Pageable pageable);
}
