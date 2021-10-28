package com.currency.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;

@Component
public class CurrencyExchangeTimeAndRatesRecordValidator {

    @Autowired
    HttpServletRequest httpServletRequest;

    Locale getLocale() {
        return httpServletRequest.getLocale();
    }

    @Value("${settings.exchangeRateProviderUpdateTimeZone}")
    String exchangeRateProviderUpdateTimeZone;
    @Value("${settings.exchangeRateProviderUpdateTime}")
    String exchangeRateProviderUpdateTime;

    /**
     * Checks if the current date is not weekend nor target closed day.
     */
    public boolean isWorkingDay(
            Date date) {
        if (isWeekend(date)) {
            return false;
        }
        final Date[] targetClosedDays = getTargetClosedDays(TimeUtils.getThisYear());
        for (Date d : targetClosedDays) {
            if (d.equals(date)) {
                return false;
            }
        }
        return true;
    }

    boolean isWeekend(
            Date date) {
        return (date.getDay() == 0 || date.getDay() == 6);
    }

    public static Date[] getTargetClosedDays(
            int year) {
        return new Date[] { TimeUtils.getDate(year, Calendar.JANUARY, 1), //
                EasterDayCalculator.getGoodFriday(year), //
                EasterDayCalculator.getEasterMonday(year), //
                TimeUtils.getDate(year, Calendar.MAY, 1), //
                TimeUtils.getDate(year, Calendar.DECEMBER, 25), //
                TimeUtils.getDate(year, Calendar.DECEMBER, 26) //
        };
    }

    /**
     * Method returns date with hours and minutes match to the time when exchange rates will occur transformed to
     * UTC from provided by properties time zone (originally is expected CET time zone).
     * Returned value can be compared with new Date() to determinate if it is outdated or not.
     */
    Date getValidTillDate(
            CurrencyExchangeTimeAndRates record) {

        TimeZone timeZone = getTimeZone();
        final Date date = shiftDayToOpen(record);
        final Locale locale = getLocale();
        convertTimeZoneToUTC(date, timeZone, locale);

        Date updateExchangeRateTime = getExchangeRateHoursShift();
        final int time = (int) updateExchangeRateTime.getTime();
        TimeUtils.addMilliseconds(date, time);
        TimeUtils.addHours(date, 24);

        return date;
    }

    public Date getExchangeRateHoursShift() {
        try {
            Date updateExhangeRateTime = new SimpleDateFormat("HH:mm").parse(exchangeRateProviderUpdateTime);
            return updateExhangeRateTime;
        } catch (ParseException e) {
            return null;
        }
    }

    public TimeZone getTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone(exchangeRateProviderUpdateTimeZone);
        return timeZone;
    }

    /**
     * Checks if the record is actual based on record time working/closed days calculation for the next time to
     * download exchange rates.
     */
    public boolean isRecordDateValid(
            CurrencyExchangeTimeAndRates currentRecord) {
        final Date validDateAndTimeUtc = getValidTillDate(currentRecord);
        final Date nowDate = getNowDate();
        final boolean after = validDateAndTimeUtc.after(nowDate);
        return after;
    }

    public static void convertTimeZoneToUTC(
            final Date date,
            TimeZone timeZone,
            final Locale locale) {
        Calendar cal = Calendar.getInstance(timeZone, locale);
        int shift = cal.get(Calendar.DST_OFFSET);
        shift += cal.get(Calendar.ZONE_OFFSET);

        TimeUtils.addMilliseconds(date, -shift);
    }

    public Date shiftDayToOpen(
            CurrencyExchangeTimeAndRates record) {
        final Date date = new Date(record.getTime().getTime());
        while (!isWorkingDay(date)) {
            TimeUtils.addHours(date, 24);
        }
        return date;
    }

    Date getNowDate() {
        return new Date();
    }

}