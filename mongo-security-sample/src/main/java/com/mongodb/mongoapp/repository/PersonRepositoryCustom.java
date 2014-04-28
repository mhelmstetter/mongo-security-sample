package com.mongodb.mongoapp.repository;

import org.springframework.data.domain.Pageable;

import com.mongodb.mongoapp.domain.Person;

public interface PersonRepositoryCustom {
    public Person findPerson();
    
    public Iterable<Person> findPersons(final Pageable pageable);
}
