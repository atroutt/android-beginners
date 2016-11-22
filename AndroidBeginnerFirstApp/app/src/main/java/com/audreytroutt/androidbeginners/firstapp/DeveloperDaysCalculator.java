package com.audreytroutt.androidbeginners.firstapp;

import java.util.concurrent.TimeUnit;

import java.util.Date;

/**
 * Created by audrey on 11/13/16.
 */

public class DeveloperDaysCalculator {

    public long daysAsAndroidDeveloper(Date startDate, Date today) {
        long startMs = startDate.getTime();
        long todayMS = today.getTime();
        return Math.abs(TimeUnit.MILLISECONDS.toDays(todayMS - startMs));
    }
 }
