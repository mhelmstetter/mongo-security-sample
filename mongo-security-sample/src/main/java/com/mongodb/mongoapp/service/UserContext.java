package com.mongodb.mongoapp.service;

import com.mongodb.mongoapp.domain.CapcoUser;

/**
 * Manages the current {@link CapcoUser}. This demonstrates how you can incorporate FLAC into larger applications.
 *
 * <p>
 *
 *  Suggested best practice is to abstract out accessing the current user to return the application
 *  specific user rather than interacting with Spring Security classes directly.
 * </p>
 *
 * @see com.mongodb.mongoapp.service.SpringSecurityUserContext
 */

public interface UserContext {

    /**
     * Gets the currently logged in {@link CapcoUser} or null if there is no authenticated user.
     *
     * @throws  IllegalStateException  if the spring user is not found in the CAPCO mongoDB.
     * @return  {@link CapcoUser} or null (if no Spring info is known about the current user).
     */
    CapcoUser getCurrentUser();

    /**
     * Sets the currently logged in {@link CapcoUser}.
     * @param user the logged in {@link CapcoUser}. Cannot be null.
     * @throws IllegalArgumentException if the {@link CapcoUser} is null.
     */
    void setCurrentUser(CapcoUser user);
}

