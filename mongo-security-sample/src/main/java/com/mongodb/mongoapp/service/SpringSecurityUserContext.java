package com.mongodb.mongoapp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.mongodb.mongoapp.domain.CapcoUser;

/**
 * An implementation of {@link UserContext}; we looks up the {@link CapcoUser} using the Spring Security's
 * {@link Authentication} by email id.
 *
 * <p>Note:     we are using SPRING to
 *              manage the user signin aspect.  But we store the CAPCO safely in our userDao repository.
 *              So store the user in 2 different places. Remember CAPCO is highly secure and most actual
 *              implementation will use a separate web service call to safely read the users permissions.
 * </p>
 * @see com.mongodb.mongoapp.domain.CapcoUser
 */
@Component
public class SpringSecurityUserContext implements UserContext {
    private final CapcoSecurityService capcoSecurityService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SpringSecurityUserContext(CapcoSecurityService capcoSecurityService, UserDetailsService userDetailsService) {
        if (capcoSecurityService == null) {
            throw new IllegalArgumentException("capcoSecurityService cannot be null");
        }
        if (userDetailsService == null) {
            throw new IllegalArgumentException("userDetailsService cannot be null");
        }
        this.capcoSecurityService = capcoSecurityService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Get the {@link CapcoUser} by obtaining the currently logged in Spring Security user's
     * {@link Authentication#getName()} and using that to find the {@link CapcoUser} by email address (since for our
     * application Spring Security usernames are email addresses).
     *
     * <p> Notes:
     *        From Spring we make a call of :  authentication.getName() <br/>  <br/>
     *
     *         This is effectively authentication.getPrincipal().  And in this reference implementation,
     *         we are signing in by email.
     *
     *          <br/>
     *          <br/>
     *          And then we take that email and use it to look up the CapcoUser and then their specific CAPCO setting.
     * </p>
     *
     * @throws  IllegalStateException  if the spring user is not found in the CAPCO mongoDB.
     */
    @Override
    public CapcoUser getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }
        String email = authentication.getName();  // authentication.getPrincipal() returns the same, we are signing in by email
        if (email == null) {
            return null;
        }
        CapcoUser result = capcoSecurityService.findUserByEmail(email);
        if (result == null) {
            throw new IllegalStateException(
                    "Spring Security is not in synch with CapcoUsers. Could not find user with email " + email);
        }
        return result;
    }

    @Override
    public void setCurrentUser(CapcoUser user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                user.getPassword(),userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
