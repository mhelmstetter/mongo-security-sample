# README.md = mongodb-security-sample

Detailed project notes are in the source code under resources folder in a file called implementation_notes.md
(which documents the specific guide to enable you to integrate this reference implementation in your system).

Our sample implementation will show off the FLAC features.  To see do the following,

(1) install mongodb
(2) import the sample data into the test database
(3) build and run the example java implementation:
(4) try out the following urls:
http://localhost:8080/mongo-app/browse
http://localhost:8080/mongo-app/flipFlop

The first will manage the currentUser who is logged in and show info appropriate for that user.
The second will flip flop between an UNCLASSIFIED user and one that has many access rights, just REFRESH your browser to see the views that each would see using the system.



Big picture:

We use SPRING and our own CAPCO database.  In file mongoapp/service/SpringSecurityUserContext.java
you will see us get the CapcoUser. We first obtain the currently logged in Spring Security user's
name or principal name to find the CapcoUser} by email address (since for our
application Spring Security usernames are email addresses).


So from the WEB layer and Spring security we make a call of :  authentication.getName()
again to get the user email.  
And then we take that email and use it to look up the CapcoUser and then their specific CAPCO setting.
That setting is then used to drive what the user can see.



Samples:
===========
https://github.com/spring-projects/spring-security/tree/master/samples/contacts-xml

http://www.mkyong.com/spring-security/spring-security-form-login-example/