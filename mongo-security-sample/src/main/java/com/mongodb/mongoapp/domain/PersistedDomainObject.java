package com.mongodb.mongoapp.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Base object that represents a Persisted DomainObject.
 *
 * <p> Notes: <br/>
 *       id below is an ObjectId since we will be using this DomainObject class in objects that may
 *       be persisted in mongodb.  As such we select ObjectId since it gives greater design choices and
 *       in web scale DBs you often design a rich safe ID rather than an Integer as you might with a Relational DB.
 * </p>
 *
 */
public class PersistedDomainObject implements Serializable {
    @Id
    protected ObjectId id;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
