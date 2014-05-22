package com.mongodb.mongoapp.repository;

import com.mongodb.mongoapp.domain.CapcoUser;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * CRUD operations for CapcoUserRepository.
 *
 */

@Component
public class CapcoUserRepositoryImpl implements CapcoUserRepository {
    /**
     * Gets a {@link com.mongodb.mongoapp.domain.CapcoUser} for a specific {@link com.mongodb.mongoapp.domain.CapcoUser#getId()}.
     *
     * @param id
     *            the {@link com.mongodb.mongoapp.domain.CapcoUser#getId()} of the {@link com.mongodb.mongoapp.domain.CapcoUser} to find.
     * @return a {@link com.mongodb.mongoapp.domain.CapcoUser} for the given id. Cannot be null.
     * @throws org.springframework.dao.EmptyResultDataAccessException
     *             if the {@link com.mongodb.mongoapp.domain.CapcoUser} cannot be found
     */
    @Override
    public CapcoUser getUser(ObjectId id) {
        return CapcoUser.TestCapcoUsers.TS_USER;                   // TODO: for demo
    }

    /**
     * Finds a given {@link com.mongodb.mongoapp.domain.CapcoUser} by email address.
     *
     * @param email
     *            the email address to use to find a {@link com.mongodb.mongoapp.domain.CapcoUser}. Cannot be null.
     * @return a {@link com.mongodb.mongoapp.domain.CapcoUser} for the given email or null if one could not be found.
     * @throws IllegalArgumentException
     *             if email is null.
     */
    @Override
    public CapcoUser findUserByEmail(String email) {
        return CapcoUser.TestCapcoUsers.TS_USER;                   // TODO: for demo
    }

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
    @Override
    public List<CapcoUser> findUsersByPartialEmail(String partialEmail) {
        return Arrays.asList(CapcoUser.TestCapcoUsers.TS_USER, CapcoUser.TestCapcoUsers.UNCLASS_USER); // TODO: for demo
    }

    /**
     * Creates new {@link CapcoUser}.
     *
     * @param user
     *            a {@link CapcoUser}. Cannot be null.
     * @return a the {@link CapcoUser#getId()} of the {@link CapcoUser} for the user or null if one could not be found.
     * @throws IllegalArgumentException
     *             if user is null.
     */
    @Override
    public ObjectId createUser(CapcoUser user) {
        return null;   // TODO: for demo - no ability to add users on the fly!
    }
}
