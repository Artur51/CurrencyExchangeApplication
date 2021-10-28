package com.currency.server.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;

public class CurrencyExchangeTimeAndRatesRecordValidatorTest {

    public static final Date weekend1 = TimeUtils.getDate(2021, Calendar.OCTOBER, 23);
    public static final Date weekend2 = TimeUtils.getDate(2021, Calendar.OCTOBER, 24);
    public static final Date weekend3 = TimeUtils.getDate(2021, Calendar.OCTOBER, 30);
    public static final Date weekend4 = TimeUtils.getDate(2021, Calendar.OCTOBER, 31);

    public static final Date workingday1 = TimeUtils.getDate(2021, Calendar.OCTOBER, 11);
    public static final Date workingday2 = TimeUtils.getDate(2021, Calendar.OCTOBER, 12);
    public static final Date workingday3 = TimeUtils.getDate(2021, Calendar.OCTOBER, 13);
    public static final Date workingday4 = TimeUtils.getDate(2021, Calendar.OCTOBER, 14);
    public static final Date workingday5 = TimeUtils.getDate(2021, Calendar.OCTOBER, 15);

    @Test
    public void testIsWorkingDayMethodShouldReturnTrueForWorkingDay() {
        CurrencyExchangeTimeAndRatesRecordValidator validator = new CurrencyExchangeTimeAndRatesRecordValidator();
        assertTrue(validator.isWorkingDay(workingday1), "failed to validate working day " + workingday1.toGMTString());
        assertTrue(validator.isWorkingDay(workingday2), "failed to validate working day " + workingday2.toGMTString());
        assertTrue(validator.isWorkingDay(workingday3), "failed to validate working day " + workingday3.toGMTString());
        assertTrue(validator.isWorkingDay(workingday4), "failed to validate working day " + workingday4.toGMTString());
        assertTrue(validator.isWorkingDay(workingday5), "failed to validate working day " + workingday5.toGMTString());
    }

    @Test
    public void testIsWorkingDayMethodShouldReturnFalseForTargetClosingDayOrWeekend() {
        CurrencyExchangeTimeAndRatesRecordValidator validator = new CurrencyExchangeTimeAndRatesRecordValidator();
        assertFalse(validator.isWorkingDay(weekend1), "failed to validate isWorkingDay method with date " + weekend1.toGMTString());
        assertFalse(validator.isWorkingDay(weekend2), "failed to validate isWorkingDay method with date " + weekend2.toGMTString());
        assertFalse(validator.isWorkingDay(weekend3), "failed to validate isWorkingDay method with date " + weekend3.toGMTString());
        assertFalse(validator.isWorkingDay(weekend4), "failed to validate isWorkingDay method with date " + weekend4.toGMTString());
    }

    @Test
    public void testIsWeekendMethodMethodShouldReturnTrueIfDayIsWeekend() {
        CurrencyExchangeTimeAndRatesRecordValidator validator = new CurrencyExchangeTimeAndRatesRecordValidator();

        assertTrue(validator.isWeekend(weekend1), "failed to validate isWeekend method with date " + weekend1.toGMTString());
        assertTrue(validator.isWeekend(weekend2), "failed to validate isWeekend method with date " + weekend2.toGMTString());
        assertTrue(validator.isWeekend(weekend3), "failed to validate isWeekend method with date " + weekend3.toGMTString());
        assertTrue(validator.isWeekend(weekend4), "failed to validate isWeekend method with date " + weekend4.toGMTString());
    }

    public void validateContainsDate(
            int year,
            final Date data) {
        CurrencyExchangeTimeAndRatesRecordValidator validator = getTestValidator(new Date());
        final String date = data.toGMTString();
        final Date[] targetClosedDays = validator.getTargetClosedDays(year);
        final String dates = Stream.of(targetClosedDays).map(Date::toGMTString).collect(Collectors.joining("\n"));

        assertTrue(dates.contains(date), "Method must contain test date \n" + dates + "\n\n" + date);
    }

    @Test
    public void testGetTargetClosedDaysShouldContainNewYearsDay1January2021() {
        validateContainsDate(2021, TimeUtils.getDate(2021, Calendar.JANUARY, 1));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainGoodFriday2April2021() {
        validateContainsDate(2021, TimeUtils.getDate(2021, Calendar.APRIL, 2));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainEasterMonday5April2021() {
        validateContainsDate(2021, TimeUtils.getDate(2021, Calendar.APRIL, 5));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainLabourDay1May2021() {
        validateContainsDate(2021, TimeUtils.getDate(2021, Calendar.MAY, 1));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainChristmasDay25December2021() {
        validateContainsDate(2021, TimeUtils.getDate(2021, Calendar.DECEMBER, 25));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainChristmasHoliday26December2021() {
        validateContainsDate(2021, TimeUtils.getDate(2021, Calendar.DECEMBER, 25));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainNewYearsDay1January2022() {
        validateContainsDate(2022, TimeUtils.getDate(2022, Calendar.JANUARY, 1));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainGoodFriday15April2022() {
        validateContainsDate(2022, TimeUtils.getDate(2022, Calendar.APRIL, 15));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainEasterMonday18April2022() {
        validateContainsDate(2022, TimeUtils.getDate(2022, Calendar.APRIL, 18));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainLabourDay1May2022() {
        validateContainsDate(2022, TimeUtils.getDate(2022, Calendar.MAY, 1));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainChristmasDay25December2022() {
        validateContainsDate(2022, TimeUtils.getDate(2022, Calendar.DECEMBER, 25));
    }

    @Test
    public void testGetTargetClosedDaysShouldContainChristmasHoliday26December2022() {
        validateContainsDate(2022, TimeUtils.getDate(2022, Calendar.DECEMBER, 26));
    }

    @Test
    public void testIsRecordDateValidMethodDoesNotChangeCurrentRecordTimeWhenRecordTime() {
        final Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            CurrencyExchangeTimeAndRatesRecordValidator validator = getTestValidator(new Date());
            validator.exchangeRateProviderUpdateTimeZone = "CET";

            final CurrencyExchangeTimeAndRates record = new CurrencyExchangeTimeAndRates();
            final long time = random.nextLong();
            Date date = new Date(time);
            record.setTime(date);
            validator.isRecordDateValid(record);

            assertEquals(time, date.getTime(), "Date time should not be changed during isRecordDateValid check");
        }
    }

    @Test
    public void testIsRecordDateValidMethodShouldNotFindWorkingDaysBetweenSameWorkingDayUsedTwise() {
        Date today = TimeUtils.getDate(2021, Calendar.OCTOBER, 26);
        CurrencyExchangeTimeAndRatesRecordValidator validator = getTestValidator(today);
        Date recordTime = TimeUtils.getDate(2021, Calendar.OCTOBER, 26);
        TimeUtils.setTodayDayStartDate(recordTime);
        TimeUtils.setTodayDayStartDate(today);

        final CurrencyExchangeTimeAndRates record = new CurrencyExchangeTimeAndRates();
        record.setTime(recordTime);
        boolean result = validator.isRecordDateValid(record);
        assertTrue(result, "Between friday and sunday must not be working days.");
    }

    @Test
    public void testGetValidTillDateMethodShouldReturnNextDayIfRecordIsCreatedToday() {
        Date today = TimeUtils.getDate(2021, Calendar.OCTOBER, 26);
        CurrencyExchangeTimeAndRatesRecordValidator validator = getTestValidator(today);
        Date recordTime = new Date(today.getTime());
        final CurrencyExchangeTimeAndRates record = new CurrencyExchangeTimeAndRates();
        record.setTime(recordTime);
        boolean result = validator.isRecordDateValid(record);
        assertTrue(result, "Today record must be valid.");
    }

    public CurrencyExchangeTimeAndRatesRecordValidator getTestValidator(
            Date today) {
        CurrencyExchangeTimeAndRatesRecordValidator validator = new CurrencyExchangeTimeAndRatesRecordValidator() {
            @Override
            public Date getNowDate() {
                return today;
            }

            @Override
            Locale getLocale() {
                return Locale.getDefault();
            }
        };
        validator.exchangeRateProviderUpdateTimeZone = "UTC";
        validator.exchangeRateProviderUpdateTime = "16:00";
        return validator;
    }

}