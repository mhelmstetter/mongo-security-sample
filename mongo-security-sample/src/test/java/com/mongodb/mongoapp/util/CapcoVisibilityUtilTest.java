package com.mongodb.mongoapp.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class CapcoVisibilityUtilTest {

    @Test
    public void test1() {

        Assert.assertEquals(null, CapcoVisibilityUtil.recusivelyExpandCapcoVisibility(null));
        compareListsOrderNotImportant(Arrays.asList("c:C", "c:U"), CapcoVisibilityUtil.recusivelyExpandCapcoVisibility(Arrays.asList("c:C")));
        compareListsOrderNotImportant(Arrays.asList("c:S", "c:C", "c:U"), CapcoVisibilityUtil.recusivelyExpandCapcoVisibility(Arrays.asList("c:S")));
        compareListsOrderNotImportant(Arrays.asList("c:S", "c:C", "c:U", "c:TS"), CapcoVisibilityUtil.recusivelyExpandCapcoVisibility(Arrays.asList("c:TS")));
    }


    private void compareListsOrderNotImportant(List<String> e, List<String> actual) {
        Assert.assertEquals(new HashSet(e), new HashSet(actual));
    }

    private void compareListsOrderNotImportant(String oneElement, List<String> actual) {
        Assert.assertEquals(new HashSet(Arrays.asList(oneElement)), new HashSet(actual));
    }

}
