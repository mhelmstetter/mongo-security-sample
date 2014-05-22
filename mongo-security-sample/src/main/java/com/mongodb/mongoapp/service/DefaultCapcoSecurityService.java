package com.mongodb.mongoapp.service;
import java.util.List;

import com.mongodb.mongoapp.domain.CapcoUser;
import com.mongodb.mongoapp.repository.CapcoUserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Repository;


/**
 * A default implementation of {@link CapcoSecurityService} ; we delegates to appropriate Data Access components.
 *
 */
@Repository
public class DefaultCapcoSecurityService implements CapcoSecurityService {
    private final CapcoUserRepository userDao;
    private final UserDetailsManager userDetailsManager;

    @Autowired
    public DefaultCapcoSecurityService(CapcoUserRepository userDao, UserDetailsManager userDetailsManager) {
        if (userDao == null) {
            throw new IllegalArgumentException("userDao cannot be null");
        }
        if (userDetailsManager == null) {
            throw new IllegalArgumentException("userDetailsManager cannot be null");
        }
        this.userDao = userDao;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    public CapcoUser getUser(ObjectId id) {
        return userDao.getUser(id);
    }

    @Override
    public CapcoUser findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    @Override
    public List<CapcoUser> findUsersByPartialEmail(String partialEmail) {
        return userDao.findUsersByPartialEmail(partialEmail);
    }

    @Override
    public ObjectId createUser(CapcoUser user) throws UnsupportedOperationException {
        // the following is very Spring specific but may be of use to porting to new applications:

        final boolean allowDynamicUserCreation = false;
        if (allowDynamicUserCreation) {
            createUserInSpringFramework(user);
            return createUserInCAPCORepository(user);
        } else {
            throw new UnsupportedOperationException("Dynamic user creation is not allow.");
        }
    }


    // The following create the user in 2 different places. Remember we are using SPRING to
    // manage the user signin aspect.  But we store the CAPCO safely in our userDao repository.

    private void createUserInSpringFramework(CapcoUser user) {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        UserDetails userDetails = new User(user.getEmail(), user.getPassword(), authorities);
        userDetailsManager.createUser(userDetails);
    }

    private ObjectId createUserInCAPCORepository(CapcoUser user) {
        return userDao.createUser(user);
    }
}
