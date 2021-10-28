package com.currency.server.utils;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyExchangeTimeAndRatesRecordValidatorIsRecordDateValidMethodTest {
    @SpyBean
    CurrencyExchangeTimeAndRatesRecordValidator validator;

    final Date weekend = TimeUtils.getDate(2021, Calendar.NOVEMBER, 1);
    final Date nextDayAfterWeekend = TimeUtils.getDate(2021, Calendar.OCTOBER, 31);

    final Date workingDay12Oct = TimeUtils.getDate(2021, Calendar.OCTOBER, 12);
    final Date workingDay13Oct5PM = TimeUtils.getDate(2021, Calendar.OCTOBER, 13, 17);

    Date goodFriday2022 = TimeUtils.getDate(2022, Calendar.APRIL, 15);
    Date nextDayAfterGoodFriday2022 = TimeUtils.getDate(2022, Calendar.APRIL, 16);
    Date easterMonday2022 = TimeUtils.getDate(2022, Calendar.APRIL, 18);
    Date nextDayAfterEasterMonday2022 = TimeUtils.getDate(2022, Calendar.APRIL, 19);

    @Test
    public void testMethodShouldReturnFalseIfRecordIsOutDatedWhenItIsNextDayEventingAfter4PM() {
        final CurrencyExchangeTimeAndRates record = new CurrencyExchangeTimeAndRates();
        record.setTime(workingDay12Oct);

        when(validator.getNowDate()).thenReturn(workingDay13Oct5PM);

        boolean result = validator.isRecordDateValid(record);
        assertFalse(result);
    }

    @Test
    public void testMethodShouldReturnTrueIfRecordIsActualDate() {
        final CurrencyExchangeTimeAndRates record = new CurrencyExchangeTimeAndRates();
        record.setTime(workingDay12Oct);
        when(validator.getNowDate()).thenReturn(workingDay12Oct);

        boolean result = validator.isRecordDateValid(record);
        assertTrue(result);
    }

    @Test
    public void testMethodShouldReturnTrueWhenWeValidateRecordWithGoodFridayDateAndCurrentDayIsFollowingByGoodFridaySaturday() {
        final CurrencyExchangeTimeAndRates record = new CurrencyExchangeTimeAndRates();
        record.setTime(goodFriday2022);
        when(validator.getNowDate()).thenReturn(nextDayAfterGoodFriday2022);
        boolean result = validator.isRecordDateValid(record);
        assertTrue(result);
    }

}