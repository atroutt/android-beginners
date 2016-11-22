package com.audreytroutt.androidbeginners.firstapp;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by audrey on 11/13/16.
 */

public class DeveloperDaysCalculatorTests {

    @Test
    public void testDaysAsDeveloper() {
        DeveloperDaysCalculator calc = new DeveloperDaysCalculator();
        Calendar start = Calendar.getInstance();
        start.set(2016, Calendar.NOVEMBER, 11);
        Date startDate = start.getTime();
        final Date today = new Date();
        long daysAsDeveloper = calc.daysAsAndroidDeveloper(startDate, today);
        assertEquals(2, daysAsDeveloper);
    }

    @Test
    public void testDaysAsDeveloperBackwardsInput() {
        DeveloperDaysCalculator calc = new DeveloperDaysCalculator();
        Calendar start = Calendar.getInstance();
        start.set(2016, Calendar.NOVEMBER, 11);
        Date startDate = start.getTime();
        final Date today = new Date();
        long daysAsDeveloper = calc.daysAsAndroidDeveloper(today, startDate);
        assertEquals(2, daysAsDeveloper);
    }
}
