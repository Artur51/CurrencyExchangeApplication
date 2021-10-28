package com.currency.server.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    static final TimeZone defaultTimeZone = TimeZone.getTimeZone("GMT");
    static Calendar calendar = Calendar.getInstance(defaultTimeZone);
    public static final int MS_IN_HOUR = 3600000;

    public static synchronized Date getDate(
            int year,
            int month,
            int day,
            int hours) {
        final Date date = getDate(year, month, day);
        addHours(date, hours);
        return date;
    }

    /**
     * Get GMT date for provided data
     */
    public static synchronized Date getDate(
            int year,
            int month,
            int day) {
        calendar.setTimeInMillis(0);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /***
     * Returns UTC (GMT) Date representing day start with excluded hours, minutes, seconds and milliseconds
     * (00:00:00).
     */
    public static Date getTodayDayStartDate() {
        long time = System.currentTimeMillis();
        return getTodayDayStartDate(time);
    }

    /**
     * Increment given date time with provided hours value. You can use negative value to decrement time amount.
     */
    public static synchronized void addHours(
            Date date,
            int hours) {
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        date.setTime(calendar.getTimeInMillis());
    }

    public static synchronized void addMilliseconds(
            Date date,
            int milliseconds) {
        long time = date.getTime();
        date.setTime(time + milliseconds);
    }

    /**
     * Returns this year in format YYYY ( start counting 0 year not as Date class from 1900).
     */
    public static int getThisYear() {
        return new Date().getYear() + 1900;
    }

    public static Date getTodayDayStartDate(
            long time) {
        time = getTodayDayStartTime(time);
        return new Date(time);
    }

    public static long getTodayDayStartTime(
            long time) {
        time -= time % (24 * MS_IN_HOUR);
        return time;
    }

    public static void setTodayDayStartDate(
            Date date) {
        final long todayDayStartDate = getTodayDayStartTime(date.getTime());
        date.setTime(todayDayStartDate);
    }
}