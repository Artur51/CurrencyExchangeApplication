package com.currency.server.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class UtilsTest {


    @Test
    public void testMaxMethodShouldReturnMaxValue() {
        assertEquals(1, Utils.max(1, null));
        assertEquals(1, Utils.max(null, 1));
        assertEquals(2, Utils.max(1, 2));
        assertEquals(2, Utils.max(2, 1));
    }
    @Test
    public void testMinMethodShouldReturnMinValue() {
        assertEquals(1, Utils.min(1, null));
        assertEquals(1, Utils.min(null, 1));
        assertEquals(1, Utils.min(1, 2));
        assertEquals(1, Utils.min(2, 1));
    }

    @Test
    public void testMinMethodShouldReturnMinDate() {
        assertEquals(new Date(1), Utils.min(null, new Date(1)));
        assertEquals(new Date(1), Utils.min(new Date(1), null));
        assertEquals(new Date(1), Utils.min(new Date(1), new Date(2)));
        assertEquals(new Date(1), Utils.min(new Date(2), new Date(1)));
    }

    @Test
    public void testMaxMethodShouldReturnMaxDate() {
        assertEquals(new Date(1), Utils.max(null, new Date(1)));
        assertEquals(new Date(1), Utils.max(new Date(1), null));
        assertEquals(new Date(2), Utils.max(new Date(1), new Date(2)));
        assertEquals(new Date(2), Utils.max(new Date(2), new Date(1)));
    }


}