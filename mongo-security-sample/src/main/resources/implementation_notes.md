FLAC - field-level access control for security.

This document describes the implementation notes.

We expect that you will have users and their associated security properties in a system.  We offer an easy programmatic way
to associate user access controls.

Key concepts:
  Given a user and their respective access control , e.g. c:TS , it needs to be input to mongodb's FLAC tool.
  This is done easily.  We have an opague internal structure that is such strings will convert into by calling
  a utility function, e.g.

      CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS"}) =>
        generates what we call a CapcoVisibility string.  That string is passed into a system
        capability.

      Currently in detail that string will expand into this format (if viewed in javascript):
      	[ { c:"TS" }, { c:"S" }, { c:"U" }, { c:"C" } ]   (( which is needed by the
                                                             reference FLAC implementation))

 
   See file src/main/java/com/mongodb/mongoapp/domain/CapcoUser.java
   for some sample calls to convertJavaToEncodeCapcoVisibility  for a few sample Demo CapcoUsers being setup with
   CAPCO visibility settings.
   

   The subsystem used ObjectID as the type of the primary keys.  MongoDB java driver is smart and can handle a conversion of
   BigInteger to ObjectID and visa versa.  However to keep it simple we will use ObjectID.  The downside of this is that our
   domain layer now depends on the mongo java as a componenent. 
   