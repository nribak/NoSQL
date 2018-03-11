package org.ribak.nosql;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void regex() throws Exception {
        String s = "abcdab";
        String regex = "^ab.+";
        Assert.assertTrue(s.matches(regex));
    }

    @Test
    public void instances() throws Exception {
        Object o = 12;
        Assert.assertTrue(o instanceof Integer);
    }
}