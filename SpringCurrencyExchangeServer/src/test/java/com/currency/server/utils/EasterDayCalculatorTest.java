package com.currency.server.utils;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EasterDayCalculatorTest {

    @Test
    public void testGetEasterMethodShouldProvideCorrectEasterDateFor2020Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("12 Apr 2020 00:00:00 GMT", easterDayCalculator.getEaster(2020).toGMTString());
    }

    @Test
    public void testGetEasterMethodShouldProvideCorrectEasterDateFor2021Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("4 Apr 2021 00:00:00 GMT", easterDayCalculator.getEaster(2021).toGMTString());
    }

    @Test
    public void testGetEasterMethodShouldProvideCorrectEasterMondayDateFor2021Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("5 Apr 2021 00:00:00 GMT", easterDayCalculator.getEasterMonday(2021).toGMTString());
    }

    @Test
    public void testGetEasterMethodShouldProvideCorrectEasterDateFor2022Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("17 Apr 2022 00:00:00 GMT", easterDayCalculator.getEaster(2022).toGMTString());
    }

    @Test
    public void testGetEasterMethodShouldProvideCorrectEasterMondayDateFor2022Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("18 Apr 2022 00:00:00 GMT", easterDayCalculator.getEasterMonday(2022).toGMTString());
    }


    @Test
    public void testGetEasterMethodShouldProvideCorrectEasterDateFor2030Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("21 Apr 2030 00:00:00 GMT", easterDayCalculator.getEaster(2030).toGMTString());
    }

    @Test
    public void testGetGoodFridayMethodShouldProvideCorrectDateFor2030Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("19 Apr 2030 00:00:00 GMT", easterDayCalculator.getGoodFriday(2030).toGMTString());
    }

    @Test
    public void testGetGoodFridayMethodShouldProvideCorrectDateFor2020Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("10 Apr 2020 00:00:00 GMT", easterDayCalculator.getGoodFriday(2020).toGMTString());
    }

    @Test
    public void testGetGoodFridayMethodShouldProvideCorrectDateFor2021Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("2 Apr 2021 00:00:00 GMT", easterDayCalculator.getGoodFriday(2021).toGMTString());
    }

    @Test
    public void testGetGoodFridayMethodShouldProvideCorrectDateFor2022Year() {
        final EasterDayCalculator easterDayCalculator = new EasterDayCalculator();
        assertEquals("15 Apr 2022 00:00:00 GMT", easterDayCalculator.getGoodFriday(2022).toGMTString());
    }

}