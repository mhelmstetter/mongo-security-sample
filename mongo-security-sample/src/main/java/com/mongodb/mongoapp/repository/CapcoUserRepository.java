package com.mongodb.mongoapp.repository;

import com.mongodb.mongoapp.domain.CapcoUser;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * CRUD operations for supported for CapcoUserRepository.
 *
 * <p> Note: <br/>
 *           The interface is designed to work with mongodb and the primary key type is ObjectId.
 * </p>
 */
public interface CapcoUserRepository {

    /**
     * Gets a {@link com.mongodb.mongoapp.domain.CapcoUser} for a specific {@link com.mongodb.mongoapp.domain.CapcoUser#getId()}.
     *
     * @param id
     *            the {@link com.mongodb.mongoapp.domain.CapcoUser#getId()} of the {@link com.mongodb.mongoapp.domain.CapcoUser} to find.
     * @return a {@link com.mongodb.mongoapp.domain.CapcoUser} for the given id. Cannot be null.
     * @throws org.springframework.dao.EmptyResultDataAccessException
     *             if the {@link com.mongodb.mongoapp.domain.CapcoUser} cannot be found
     */
    CapcoUser getUser(ObjectId id);

    /**
     * Finds a given {@link com.mongodb.mongoapp.domain.CapcoUser} by email address.
     *
     * @param email
     *            the email address to use to find a {@link com.mongodb.mongoapp.domain.CapcoUser}. Cannot be null.
     * @return a {@link com.mongodb.mongoapp.domain.CapcoUser} for the given email or null if one could not be found.
     * @throws IllegalArgumentException
     *             if email is null.
     */
    CapcoUser findUserByEmail(String email);


    /**
     * Finds any {@link com.mongodb.mongoapp.domain.CapcoUser} that has an email that starts with {@code partialEmail}.
     *
     * @param partialEmail
     *            the email address to use to find {@link com.mongodb.mongoapp.domain.CapcoUser}s. Cannot be null or empty String.
     * @return a List of {@link com.mongodb.mongoapp.domain.CapcoUser}s that have an email that starts with given partialEmail. The returned value
     *         will never be null. If no results are found an empty List will be returned.
     * @throws IllegalArgumentException
     *             if email is null or empty String.
     */
    List<CapcoUser> findUsersByPartialEmail(String partialEmail);

    /**
     * Creates a new {@link com.mongodb.mongoapp.domain.CapcoUser}.
     *
     * @param user
     *            the new {@link com.mongodb.mongoapp.domain.CapcoUser} to create. The {@link com.mongodb.mongoapp.domain.CapcoUser#getId()} must be null.
     * @return the new {@link com.mongodb.mongoapp.domain.CapcoUser#getId()}.
     * @throws IllegalArgumentException
     *             if {@link com.mongodb.mongoapp.domain.CapcoUser#getId()} is non-null.
     */
    ObjectId createUser(CapcoUser user);
}
