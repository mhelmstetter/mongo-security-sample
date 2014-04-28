package com.mongodb.mongoapp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mongodb.mongoapp.domain.Person;



@Repository
public interface PersonRepository  extends PagingAndSortingRepository<Person, ObjectId>, PersonRepositoryCustom {
    
   

}
