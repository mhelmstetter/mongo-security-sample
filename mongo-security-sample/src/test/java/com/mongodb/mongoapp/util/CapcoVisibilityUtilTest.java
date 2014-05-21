package com.mongodb.mongoapp.util;

import org.junit.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.util.List;

import static org.junit.Assert.*;

import static junit.framework.Assert.assertEquals;

public class CapcoVisibilityUtilTest {

    @Test
    public void testConvertJavaToEncodeCapcoVisibility() throws Exception {

        assertEquals("[  ]", CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility( new String[] {} ));
        assertEquals("[  ]", CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility( (List<String>)null ));
        if (CapcoVisibilityUtil.EXPAND_CAPCO_AS_TREE_OF_VISIBILITY) {
            assertEquals("[ { c:\"TS\" }, { c:\"S\" }, { c:\"C\" }, { c:\"U\" } ]", CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS", "c:S"}));

        } else {
            assertEquals("[ { c:\"TS\" } ]", CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS:fo"}));
            assertEquals("[ { c:\"TS\" }, { c:\"S\" } ]", CapcoVisibilityUtil.convertJavaToEncodeCapcoVisibility(new String[]{"c:TS:fo", "c:S"}));
        }
    }
}