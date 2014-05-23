package com.mongodb.mongoapp.domain;

import com.mongodb.mongoapp.util.CapcoVisibilityUtil;
import org.junit.Test;
import org.springframework.security.crypto.util.EncodingUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class UserSecurityAttributesTest {

    @Test
    public void testGetCapcoUserStringHappyPath() throws Exception {

        UserSecurityAttributes userSecurityAttributes = new UserSecurityAttributes("TS", Arrays.asList("TK"), Arrays.asList("US"));

        assertEquals("TS", userSecurityAttributes.getClearance());
        final List<String> tk = Arrays.asList("TK");
        compareListsOrderNotImportant(tk, userSecurityAttributes.getSci());
        compareListsOrderNotImportant("US", userSecurityAttributes.getCountry());
    }


    @Test
    public void testSetClearanceOnlyDefaultsOfNull() throws Exception {

        UserSecurityAttributes userSecurityAttributes = new UserSecurityAttributes("TS", null, null);

        assertEquals("TS", userSecurityAttributes.getClearance());
        final List<String> tk = new ArrayList<String>();
        compareListsOrderNotImportant(tk, userSecurityAttributes.getSci());
        final List<String> co = new ArrayList<String>();
        compareListsOrderNotImportant(co, userSecurityAttributes.getCountry());

    }

    @Test
    public void testSetClearanceOnlyDefaultsOfEmptyList() throws Exception {

        UserSecurityAttributes userSecurityAttributes = new UserSecurityAttributes("TS", new ArrayList<String>(), new ArrayList<String>());

        assertEquals("TS", userSecurityAttributes.getClearance());
        final List<String> tk = new ArrayList<String>();
        compareListsOrderNotImportant(tk, userSecurityAttributes.getSci());
        final List<String> co = new ArrayList<String>();
        compareListsOrderNotImportant(co, userSecurityAttributes.getCountry());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSciAndNoClearanceAsEmptyString() throws Exception {
        UserSecurityAttributes userSecurityAttributes = new UserSecurityAttributes("", new ArrayList<String>(), new ArrayList<String>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSciAndNoClearance() throws Exception {
        UserSecurityAttributes userSecurityAttributes = new UserSecurityAttributes(null, new ArrayList<String>(), new ArrayList<String>());
    }

    @Test
    public void testGetCountry() throws Exception {
        UserSecurityAttributes userSecurityAttributes = new UserSecurityAttributes("TS", new ArrayList<String>(), Arrays.asList("US", "DE"));

        final List<String> co = Arrays.asList("DE", "US");
        compareListsOrderNotImportant(co, userSecurityAttributes.getCountry());

    }

    @Test
    public void testSetCountryClearanceAndNoSci() throws Exception {
        UserSecurityAttributes userSecurityAttributes = new UserSecurityAttributes("S", null, Arrays.asList("US"));

        assertEquals("S", userSecurityAttributes.getClearance());
        final List<String> tk = new ArrayList<String>();
        compareListsOrderNotImportant(tk, userSecurityAttributes.getSci());
        compareListsOrderNotImportant("US", userSecurityAttributes.getCountry());

    }

    @Test
    public void testGetCapcoUserString() throws Exception {
        UserSecurityAttributes userSecurityAttributes = new UserSecurityAttributes("TS", Arrays.asList("TK"), Arrays.asList("US"));

        UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{"c:TS", "sci:TK", "sci:SI", "sci:G", "sci:HCS"});

        final String s = "[ { c:\"TS\" }, { c:\"S\" }, { c:\"C\" }, { c:\"U\" }, { sci:\"TK\" }, { relto:\"US\" } ]";

        assertEquals(s, userSecurityAttributes.getCapcoUserString());

    }

    @Test
    public void testGetCapcoUserStringManySci() throws Exception {
        UserSecurityAttributes userSecurityAttributes = new UserSecurityAttributes("TS", Arrays.asList("TK", "SI", "G", "HCS"), Arrays.asList("US"));

        UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{"c:TS", "sci:TK", "sci:SI", "sci:G", "sci:HCS"});

        final String s = "[ { c:\"TS\" }, { c:\"S\" }, { c:\"C\" }, { c:\"U\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" }, { relto:\"US\" } ]";

        assertEquals(s, userSecurityAttributes.getCapcoUserString());

    }

    // low level tests
    @Test
    public void testConvertJavaToEncodeCapcoVisibility() throws Exception {

        assertEquals("[  ]", UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{}));
        assertEquals("[  ]", UserSecurityAttributes.EncodingUtils.expandCapcoVisibility((List<String>) null));
        if (CapcoVisibilityUtil.EXPAND_CAPCO_AS_TREE_OF_VISIBILITY) {
            assertEquals("[ { c:\"TS\" }, { c:\"S\" }, { c:\"C\" }, { c:\"U\" } ]", UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{"c:TS", "c:S"}));

        } else {
            assertEquals("[ { c:\"TS\" } ]", UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{"c:TS:fo"}));
            assertEquals("[ { c:\"TS\" }, { c:\"S\" } ]", UserSecurityAttributes.EncodingUtils.expandCapcoVisibility(new String[]{"c:TS:fo", "c:S"}));
        }
    }


    private void compareListsOrderNotImportant(List<String> e, List<String> actual) {
        assertEquals(new HashSet(e), new HashSet(actual));
    }

    private void compareListsOrderNotImportant(String oneElement, List<String> actual) {
        assertEquals(new HashSet(Arrays.asList(oneElement)), new HashSet(actual));
    }
}