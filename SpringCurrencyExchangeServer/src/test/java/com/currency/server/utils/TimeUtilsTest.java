package com.currency.server.utils;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.junit.jupiter.api.Test;

public class TimeUtilsTest {

    @Test
    public void testGetDateMethodShouldCreateCorrectObject() {
        final Date date = TimeUtils.getDate(2000, Calendar.JANUARY, 28);
        assertEquals("28 Jan 2000 00:00:00 GMT", date.toGMTString());
    }

    @Test
    public void testGetDateMethodShouldCreateCorrectObject2() {
        final Date date = TimeUtils.getDate(2027, Calendar.JUNE, 26);
        assertEquals("26 Jun 2027 00:00:00 GMT", date.toGMTString());
    }

    @Test
    public void testGetDateMethodShouldCreateCorrectObject3() {
        Date date = TimeUtils.getDate(2021, Calendar.OCTOBER, 22);
        assertEquals("22 Oct 2021 00:00:00 GMT", date.toGMTString());
    }

    @Test
    public void testGetDateMethodShouldCreateCorrectObject4() {
        Date date = TimeUtils.getDate(2021, Calendar.OCTOBER, 24);
        assertEquals("24 Oct 2021 00:00:00 GMT", date.toGMTString());
    }

    @Test
    public void testAddHoursMethodAddHoursMethodShouldAddCorrectValue() {
        final Date date = TimeUtils.getDate(2000, Calendar.JANUARY, 30);
        assertEquals("30 Jan 2000 00:00:00 GMT", date.toGMTString());
        TimeUtils.addHours(date, 10);
        assertEquals("30 Jan 2000 10:00:00 GMT", date.toGMTString());
        TimeUtils.addHours(date, 10);
        assertEquals("30 Jan 2000 20:00:00 GMT", date.toGMTString());
        TimeUtils.addHours(date, -5);
        assertEquals("30 Jan 2000 15:00:00 GMT", date.toGMTString());
    }

    @Test
    public void testThisYearMethodShouldReturnThisYear() {
        assertTrue(new Date().toGMTString().contains(String.valueOf(TimeUtils.getThisYear())), "TimeUtils.thisYear returns invalid number");
    }

    @Test
    public void testAddHoursMethodShouldWorkCorrectWithNegativeValues() {
        final Date date = TimeUtils.getDate(2000, Calendar.JANUARY, 10);
        assertEquals("10 Jan 2000 00:00:00 GMT", date.toGMTString());
        TimeUtils.addHours(date, -12);
        assertEquals("9 Jan 2000 12:00:00 GMT", date.toGMTString());
        TimeUtils.addHours(date, -24 * 5);
        assertEquals("4 Jan 2000 12:00:00 GMT", date.toGMTString());
    }

    @Test
    public void testAddMillisecondsMethodShouldAddMilliseconds() {
        final Date date = new Date(0);
        TimeUtils.addMilliseconds(date, 100);
        assertEquals(100, date.getTime());
        TimeUtils.addMilliseconds(date, 100);
        assertEquals(200, date.getTime());
    }

    @Test
    public void testAddHoursMethodShouldWorkCorrectWithBigValues() {
        final Date date = TimeUtils.getDate(2000, Calendar.JANUARY, 10);
        assertEquals("10 Jan 2000 00:00:00 GMT", date.toGMTString());
        TimeUtils.addHours(date, 24 * 10);
        assertEquals("20 Jan 2000 00:00:00 GMT", date.toGMTString());
        TimeUtils.addHours(date, -24 * 15);
        assertEquals("5 Jan 2000 00:00:00 GMT", date.toGMTString());
    }

    @Test
    public void testgetTodayDayStartDateMethodShouldAlwaysReturnTime00_00_00() {
        final Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            final Date date = TimeUtils.getTodayDayStartDate(random.nextLong());
            assertTrue(date.toGMTString().contains("00:00:00"), "Date is not expected " + date.toGMTString());
        }
    }

    @Test
    public void testGetDateMethodReturnsCorrectDateForGivenDate() {
        final Random random = new Random();
        for (int i = 0; i < 2000; i++) {
            long time = TimeUtils.getTodayDayStartTime(random.nextLong());
            validateTimeIsZeroForHoursMinitsSecondsMilliseconds(time);
        }
    }

    @Test
    public void testGetTodayDayStartTimeMethodShouldAlwaysSetDateToHaveFormatForHoursMinutesSecondsAndMillisecondsAsZeroValue() {
        final Random random = new Random();
        for (int i = 0; i < 2000; i++) {
            final Date date = TimeUtils.getTodayDayStartDate(random.nextLong());
            final String gmtString = date.toGMTString();
            assertTrue(gmtString.contains("00:00:00"), "Date is not expected  " + gmtString);
            validateTimeIsZeroForHoursMinitsSecondsMilliseconds(date.getTime());
        }
    }

    @Test
    public void testSetTodayDayStartDateMethodShouldAlwaysSetDateToHaveFormatForHoursMinutesSecondsAndMillisecondsAsZeroValue() {
        final Random random = new Random();
        for (int i = 0; i < 2000; i++) {
            final Date date = new Date(random.nextLong());
            TimeUtils.setTodayDayStartDate(date);
            final String gmtString = date.toGMTString();
            assertTrue(gmtString.contains("00:00:00"), "Date is not expected  " + gmtString);
            final long time = date.getTime();
            validateTimeIsZeroForHoursMinitsSecondsMilliseconds(time);
        }
    }

    public void validateTimeIsZeroForHoursMinitsSecondsMilliseconds(
            final long time) {
        assertEquals(0, time % 1000, "Milliseconds must be 0");
        assertEquals(0, time % 1000 * 60, "Seconds must be 0");
        assertEquals(0, time % 1000 * 60 * 60, "Minutes must be 0");
        assertEquals(0, time % 1000 * 60 * 60 * 24, "Hours must be 0");
    }


    @Test
    public void testGetDateMethodShouldReturnDateWithAddedHours() {
        final Date date = TimeUtils.getDate(2000, 1, 1);
        final Date date2 = TimeUtils.getDate(2000, 1, 1, 16);
        assertEquals(TimeUtils.MS_IN_HOUR * 16, date2.getTime() - date.getTime());
    }

}