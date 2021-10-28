package com.currency.server.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Class to calculate easter date based on formula provided by
 * https://www.geeksforgeeks.org/how-to-calculate-the-easter-date-for-a-given-year-using-gauss-algorithm/
 * All calculations done on the basis of Gauss Easter Algorithm
 */
public class EasterDayCalculator {

    public static Date getEasterMonday(
            int year) {
        final Date date = getEaster(year);
        TimeUtils.addHours(date, 24);
        return date;
    }

    public static Date getGoodFriday(
            int year) {
        final Date date = getEaster(year);
        TimeUtils.addHours(date, -48);
        return date;
    }

    /**
     * Function calculates and prints easter date for given year
     */
    public static Date getEaster(
            int year) {
        float A, B, C, P, Q, M, N, D, E;
        A = year % 19;
        B = year % 4;
        C = year % 7;
        P = (float) Math.floor(year / 100);
        Q = (float) Math.floor((13 + 8 * P) / 25);
        M = (15 - Q + P - P / 4) % 30;
        N = (4 + P - P / 4) % 7;
        D = (19 * A + M) % 30;
        E = (2 * B + 4 * C + 6 * D + N) % 7;
        int days = (int) (22 + D + E);

        // A corner case, when D is 29
        if ((D == 29) && (E == 6)) {
            return TimeUtils.getDate(year, Calendar.APRIL, 19);
        }
        // Another corner case, when D is 28
        else if ((D == 28) && (E == 6)) {
            return TimeUtils.getDate(year, Calendar.APRIL, 18);
        } else {
            // If days > 31, move to April; April = 4th Month
            if (days > 31) {
                return TimeUtils.getDate(year, Calendar.APRIL, days - 31);
            }
            // Otherwise, stay on March March = 3rd Month
            else {
                return TimeUtils.getDate(year, Calendar.MARCH, days);
            }
        }
    }

    public static void main(
            String[] args) {
        for (int i = 2019; i < 2030; i++) {
            final Date easter = getEaster(i);
            System.out.println(" year " + i + " easter " + easter.toGMTString());
        }
    }
}