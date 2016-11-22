package com.audreytroutt.androidbeginners.firstapp;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FirstTest {

    @Test
    public void trueIsTrue() {
        assertThat(true, is(true));
        assertEquals(true, true);
        assertTrue(true);
    }

    @Test
    public void falseIsFalse() {
        assertThat(false, is(false));
        assertEquals(false, false);
        assertFalse(false);
    }
}
