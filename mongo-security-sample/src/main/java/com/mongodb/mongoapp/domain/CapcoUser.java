package com.mongodb.mongoapp.domain;

import com.mongodb.mongoapp.util.CapcoVisibilityUtil;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * User that has a Capco User String giving CAPCO rights in the system
 */
public class CapcoUser  extends PersistedDomainObject implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    // the following will encode the capcoUserString:
    private UserSecurityAttributes userSecurityAttributes;

    /** Test (or demo) CapcoUsers gives some sample CapcoUser settings for an UNCLASSIFIED user and one that has TS with a number
     *  of SCI.
     */
    public static class TestCapcoUsers {
        public final static CapcoUser UNCLASS_USER =
                new CapcoUser("frank", "UNCLASS_TestAccount", "frank_unclassified@.example.com",
                        "U", null, null);
        public final static CapcoUser TS_USER = new CapcoUser("frank", "TS_TestAccount", "frank_ts@.example.com",
                "TS", Arrays.asList("TK", "SI", "G", "HCS"), null);



    }

    public CapcoUser(ObjectId id, String firstName, String lastName, String email, String password, String clearance, List<String> sci, List<String> country) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userSecurityAttributes = new UserSecurityAttributes(clearance, sci, country);
    }


    public CapcoUser(String firstName, String lastName, String email, String password, String clearance, List<String> sci, List<String> country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userSecurityAttributes = new UserSecurityAttributes(clearance, sci, country);
    }

    /** no password set version */
    public CapcoUser(String firstName, String lastName, String email, String clearance, List<String> sci, List<String> country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userSecurityAttributes = new UserSecurityAttributes(clearance, sci, country);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {  return password;    }

    public void setPassword(String password) { this.password = password; }

    /** get the read only  UserSecurityAttributes */
    public UserSecurityAttributes getUserSecurityAttributes() { return userSecurityAttributes;  }

    @Override
    public String toString() {
        return String.format("CapcoUser: %s %s : email: %s", this.firstName, this.lastName, this.email);
    }
}
