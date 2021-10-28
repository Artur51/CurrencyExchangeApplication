package com.currency.server.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyExchangeTimeAndRatesRecordValidatorSpringBootTest {
    @Autowired
    CurrencyExchangeTimeAndRatesRecordValidator validator;

    @Test
    public void testShiftDayToOpenMethodShouldAlwaysReturnWorkingDay() {
        for (int i = 0; i < 2000; i++) {
            final CurrencyExchangeTimeAndRates record = getRecordWithRandomDateForYear2021();
            final Date result = validator.shiftDayToOpen(record);
            assertTrue(validator.isWorkingDay(result));
        }
    }

    @Test
    public void testgetExhangeRateHoursShiftMethodShouldAlwaysReturnCorrecctConstantValueExpectedFromSettings() {
        assertEquals(16 * 1000 * 60 * 60, validator.getExchangeRateHoursShift().getTime());
    }

    @Test
    public void testGetValidTillDateMethodShouldReturnCorrectShiftValueMathTo1600ExpectedFromSettingsFile() {
        for (int i = 0; i < 2000; i++) {
            final CurrencyExchangeTimeAndRates record = getRecordWithRandomDateForYear2021();
            final Date result = validator.getValidTillDate(record);
            // 4 pm CET translated to UTC is 15:00
            assertTrue(result.toGMTString().contains("14:00"), "expected time missmatch; expected 14:00 " + result.toGMTString());
            assertTrue(result.toGMTString().contains("2021"), "expected year missmatch; expected 2021 " + result.toGMTString());
        }
    }

    @Test
    public void testGetValidTillDateMethodReturnDateInUTCFormatEqualsTo4PmCETTimeZone() {
        for (int i = 0; i < 2000; i++) {
            final CurrencyExchangeTimeAndRates record = getRecordWithRandomDateForYear2021();
            final Date result = validator.getValidTillDate(record);
            // 4 pm CET translated to UTC is 15:00 - 2 hours
            assertTrue(result.toGMTString().contains("14:00"), "expected time missmatch; expected 14:00 " + result.toGMTString());
            assertTrue(result.toGMTString().contains("2021"), "expected year missmatch; expected 2021 " + result.toGMTString());
        }
    }

    @Test
    public void testconvertTimeZoneCETToUTCCustomDateResultDateShouldReturnMinusTwoHoursTime() {
        final Date date = TimeUtils.getDate(2021, 10, 23);
        final Date dateExpected = TimeUtils.getDate(2021, 10, 23);
        TimeUtils.addHours(dateExpected, -2);
        validator.convertTimeZoneToUTC(date, TimeZone.getTimeZone("CET"), Locale.forLanguageTag("en"));
        assertEquals(dateExpected, date);
    }

    public CurrencyExchangeTimeAndRates getRecordWithRandomDateForYear2021() {
        final CurrencyExchangeTimeAndRates record = new CurrencyExchangeTimeAndRates();
        record.setTime(getRandomDateForYear2021());
        return record;
    }


    public Date getRandomDateForYear2021() {
        final Random random = new Random();
        final int month = random.nextInt(12);
        final int day = random.nextInt(27) + 1;
        Date date = TimeUtils.getDate(2021, month, day);
        assertTrue(date.toGMTString().contains("2021"), "Expected year 2021 " + date.toGMTString() + " when month " + month + " day " + day);
        return date;
    }

    @Test
    public void testGetTimeZoneMethodShouldReturnCETTimeZoneWeExpecteFromSettingsFile() {
        assertEquals(TimeZone.getTimeZone("CET"), validator.getTimeZone());
    }

}
