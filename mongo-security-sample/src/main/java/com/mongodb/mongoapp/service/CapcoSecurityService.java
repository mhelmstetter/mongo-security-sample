package com.mongodb.mongoapp.service;

import com.mongodb.mongoapp.domain.CapcoUser;
import org.bson.types.ObjectId;
import java.util.List;

/**
 * Provides a Capco based SecurityService.
 */
public interface CapcoSecurityService {

    /**
     * Gets a {@link CapcoUser} for a specific {@link CapcoUser#getId()}.
     *
     * @param id
     *            the {@link CapcoUser#getId()} of the {@link CapcoUser} to find.
     * @return a {@link CapcoUser} for the given id. Cannot be null.
     * @throws org.springframework.dao.EmptyResultDataAccessException
     *             if the {@link CapcoUser} cannot be found
     */
    CapcoUser getUser(ObjectId id);


    /**
     * Finds a given {@link CapcoUser} by email address.
     *
     * @param email
     *            the email address to use to find a {@link CapcoUser}. Cannot be null.
     * @return a {@link CapcoUser} for the given email or null if one could not be found.
     * @throws IllegalArgumentException
     *             if email is null.
     */
    CapcoUser findUserByEmail(String email);

    /**
     * Finds a list of {@link CapcoUser} by partial email address.
     *
     * @param partialEmail
     *            the partial email address to use to find a {@link CapcoUser}. Cannot be null.
     * @return a list of {@link CapcoUser} for the given partial email or empty list if one could not be found.
     * @throws IllegalArgumentException
     *             if partialEmail is null.
     */
    List<CapcoUser> findUsersByPartialEmail(String partialEmail);

    /**
     * Creates new {@link CapcoUser}.
     *
     * @param user
     *            a {@link CapcoUser}. Cannot be null.
     * @return a the {@link CapcoUser#getId()} of the {@link CapcoUser} for the user or null if one could not be found.
     *
     * @throws IllegalArgumentException
     *             if user is null.
     * @throws UnsupportedOperationException
     *             if you can not create users dynamically.
     * @return a the {@link CapcoUser#getId()} of the {@link CapcoUser} for the user or null if one could not be found.
     *
     */
    ObjectId createUser(CapcoUser user) throws UnsupportedOperationException;

    }
