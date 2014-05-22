package com.mongodb.mongoapp.domain;

import com.mongodb.mongoapp.util.CapcoVisibilityUtil;
import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * User that has a Capco User String giving CAPCO rights in the system
 */
public class CapcoUser  extends PersistedDomainObject implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String capcoUserString;


    /** Test (or demo) CapcoUsers gives some sample CapcoUser settings for an UNCLASSIFIED user and one that has TS with a number
     *  of SCI.
     */
    public static class TestCapcoUsers {
        public final static CapcoUser UNCLASS_USER = new CapcoUser("frank", "UNCLASS_TestAccount", "frank_unclassified@.example.com", CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:U"}));
        public final static CapcoUser TS_USER = new CapcoUser("frank", "TS_TestAccount", "frank_ts@.example.com", CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS",  "sci:TK",  "sci:SI",  "sci:G",  "sci:HCS"}));

    }

    public CapcoUser(ObjectId id, String firstName, String lastName, String email, String password, String capcoUserString) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.capcoUserString = capcoUserString;
    }


    public CapcoUser(String firstName, String lastName, String email, String password, String capcoUserString) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.capcoUserString = capcoUserString;
    }

    /** no password set version */
    public CapcoUser(String firstName, String lastName, String email, String capcoUserString) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.capcoUserString = capcoUserString;
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

    public String getCapcoUserString() {
        return capcoUserString;
    }

    public void setCapcoUserString(String capcoUserString) {
        this.capcoUserString = capcoUserString;
    }



    @Override
    public String toString() {
        return String.format("CapcoUser: %s %s : email: %s", this.firstName, this.lastName, this.email);
    }
}
