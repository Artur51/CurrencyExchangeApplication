package com.currency.server.pojo.exchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;

import com.currency.server.utils.TimeUtils;

public class ExchangeCurrencyLogEventQueryDataTest {

    @Test
    public void testIsEmptyMethodShouldReturnTrueIsNoFieldsSet() {
        ExchangeCurrencyLogEventQueryData data = new ExchangeCurrencyLogEventQueryData();
        assertTrue(data.isEmpty(), "Expected object is empty");
    }

    @Test
    public void testIsEmptyMethodShouldReturnFalseIsNoFieldsSet() {
        ExchangeCurrencyLogEventQueryData data = new ExchangeCurrencyLogEventQueryData();
        data.setCreatedAtEnd(new Date());
        assertFalse(data.isEmpty(), "Expected object is not empty");
    }

    public void testCreatedAtMethodShouldReturnMinMaxValueIfVariablesAreNotReversed() {
        ExchangeCurrencyLogEventQueryData r = new ExchangeCurrencyLogEventQueryData();
        r.setCreatedAtStart(TimeUtils.getDate(2000, 1, 1));
        r.setCreatedAtEnd(TimeUtils.getDate(2000, 1, 2));

        assertEquals(TimeUtils.getDate(2000, 1, 1), r.getCreatedAtStart());
        assertEquals(TimeUtils.getDate(2000, 1, 2), r.getCreatedAtEnd());
    }

    @Test
    public void testCreatedAtMethodShouldReturnMinMaxValueIfVariablesAreReversed() {
        ExchangeCurrencyLogEventQueryData r = new ExchangeCurrencyLogEventQueryData();
        r.setCreatedAtStart(TimeUtils.getDate(2000, 1, 2));
        r.setCreatedAtEnd(TimeUtils.getDate(2000, 1, 1));

        assertEquals(TimeUtils.getDate(2000, 1, 1), r.getCreatedAtStart());
        assertEquals(TimeUtils.getDate(2000, 1, 2), r.getCreatedAtEnd());
    }

    @Test
    public void testGetCurrencyAmountSoldMethodShouldReturnMinMaxValueIfVariablesAreNotReversed() {
        ExchangeCurrencyLogEventQueryData r = new ExchangeCurrencyLogEventQueryData();
        r.setCurrencyAmountSoldMin(1);
        r.setCurrencyAmountSoldMax(2);

        assertEquals(1, r.getCurrencyAmountSoldMin());
        assertEquals(2, r.getCurrencyAmountSoldMax());
    }

    @Test
    public void testGetCurrencyAmountSoldMethodShouldReturnMinMaxValueIfVariablesAreReversed() {
        ExchangeCurrencyLogEventQueryData r = new ExchangeCurrencyLogEventQueryData();
        r.setCurrencyAmountSoldMin(2);
        r.setCurrencyAmountSoldMax(1);

        assertEquals(1, r.getCurrencyAmountSoldMin());
        assertEquals(2, r.getCurrencyAmountSoldMax());
    }

    @Test
    public void testGetCurrencyAmountReceivedMethodShouldReturnMinMaxValueIfVariablesAreNotReversed() {
        ExchangeCurrencyLogEventQueryData r = new ExchangeCurrencyLogEventQueryData();
        r.setCurrencyAmountReceivedMin(1);
        r.setCurrencyAmountReceivedMax(2);

        assertEquals(1, r.getCurrencyAmountReceivedMin());
        assertEquals(2, r.getCurrencyAmountReceivedMax());
    }

    @Test
    public void testGetCurrencyAmountReceivedMethodShouldReturnMinMaxValueIfVariablesAreReversed() {
        ExchangeCurrencyLogEventQueryData r = new ExchangeCurrencyLogEventQueryData();
        r.setCurrencyAmountReceivedMin(2);
        r.setCurrencyAmountReceivedMax(1);

        assertEquals(1, r.getCurrencyAmountReceivedMin());
        assertEquals(2, r.getCurrencyAmountReceivedMax());
    }

}