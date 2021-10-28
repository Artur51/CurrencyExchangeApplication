package com.currency.server.repositories;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData.OrderBy;

public class ExchangeCurrencyLogEventDataFetcherTest {

    @Test
    public void testFromObjectMethodWithParameterCurrencySoldShouldBeValueInMapAndKeyMappingInQueryString() {
        final ExchangeCurrencyLogEventQueryData object = new ExchangeCurrencyLogEventQueryData();
        object.setCurrencySold("USD");
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.setQueryObject(object);
        fetcher.generateObjectsQuery();
        final String expected = " SELECT ex FROM ExchangeCurrencyLogEvent ex  JOIN FETCH ex.currencySold currency_sold  JOIN FETCH ex.currencyPurchase currency_purchase  JOIN FETCH ex.user user  WHERE  currency_sold.currency = :currencySold  ORDER BY ex.createdAt  DESC ";
        assertEquals(expected, fetcher
                .getQuery());
        assertEquals("currencySold", fetcher.getParameters().keySet().stream().collect(Collectors.joining()));
        assertEquals("USD", fetcher.getParameters().values().stream().map(Object::toString).collect(Collectors.joining()));
    }

    @Test
    public void testFromObjectMethodWithParameterCurrencyPurchaseShouldBeValueInMapAndKeyMappingInQueryString() {
        final ExchangeCurrencyLogEventQueryData object = new ExchangeCurrencyLogEventQueryData();
        object.setCurrencyPurchase("USD");
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.setQueryObject(object);
        fetcher.generateObjectsQuery();
        final String expected = " SELECT ex FROM ExchangeCurrencyLogEvent ex  JOIN FETCH ex.currencySold currency_sold  JOIN FETCH ex.currencyPurchase currency_purchase  JOIN FETCH ex.user user  WHERE  currency_purchase.currency = :currencyPurchase  ORDER BY ex.createdAt  DESC ";
        assertEquals(expected, fetcher
                .getQuery());
        assertEquals("currencyPurchase", fetcher.getParameters().keySet().stream().collect(Collectors.joining()));
        assertEquals("USD", fetcher.getParameters().values().stream().map(Object::toString).collect(Collectors.joining()));
    }

    @Test
    public void testAddEqualsQueryMethodCreatesQueryWithCorrectPattern() {
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.addEqualsQuery("CompareWithField", "fileName", "fieldValue", false);
        assertEquals(" CompareWithField = :fileName ", fetcher.getQuery());
    }

    @Test
    public void testAddLikeQueryMethodCreatesQueryWithCorrectPattern() {
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.addLikeQuery("CompareWithField", "fileName", "fieldValue");
        assertEquals(" CompareWithField like %:fileName% ", fetcher.getQuery());
    }

    @Test
    public void testAddRangeQueryMethodCreatesCorrectPatternBetweenQueryWhenTwoValuesAreProvided() {
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.addRangeQuery("CompareWithField", "date1", "date2", "2020/01/01", "2021/02/02");
        assertEquals(" CompareWithField BETWEEN :date1 AND :date2 ", fetcher.getQuery());
    }

    @Test
    public void testAddRangeQueryMethodCreatesCorrectPatternWhenOnlyMinValueProvided() {
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.addRangeQuery("CompareWithField", "date1", "date2", "2020/01/01", null);
        assertEquals(" CompareWithField > :date1 ", fetcher.getQuery());
    }

    @Test
    public void testAddRangeQueryMethodCreatesCorrectPatternWhenOnlyMaxValueProvided() {
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.addRangeQuery("CompareWithField", "date1", "date2", null, "2020/01/01");
        assertEquals(" CompareWithField < :2020/01/01 ", fetcher.getQuery());
    }

    @Test
    public void testAddParameterMethodAddObjectIntoParametersMap() {
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.addParameter("KEY", "VALUE");
        assertTrue(fetcher.getParameters().keySet().contains("KEY"));
        assertTrue(fetcher.getParameters().values().contains("VALUE"));
    }



    @Test
    public void testAddOrderByMethodShouldReturnOrderByQueryCurrencyAmountReceived() {

        final ExchangeCurrencyLogEventQueryData object = new ExchangeCurrencyLogEventQueryData();
        object.setCurrencySold("USD");
        object.setOrderBy(OrderBy.currencyAmountReceived);
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.setQueryObject(object);
        fetcher.generateObjectsQuery();
        fetcher.addOrderBy(object);

        final String expected = " SELECT ex FROM ExchangeCurrencyLogEvent ex  JOIN FETCH ex.currencySold currency_sold  JOIN FETCH ex.currencyPurchase currency_purchase  JOIN FETCH ex.user user  WHERE  currency_sold.currency = :currencySold  ORDER BY ex.currencyAmountReceived  ASC  ORDER BY ex.currencyAmountReceived  ASC ";
        assertEquals(expected, fetcher.getQuery());
        assertEquals("currencySold", fetcher.getParameters().keySet().stream().collect(Collectors.joining()));
        assertEquals("USD", fetcher.getParameters().values().stream().map(Object::toString).collect(Collectors.joining()));
    }

    @Test
    public void testAddOrderByMethodShouldReturnOrderByQueryCreatedAt() {
        final ExchangeCurrencyLogEventQueryData object = new ExchangeCurrencyLogEventQueryData();
        object.setCurrencySold("AUD");
        object.setOrderBy(OrderBy.createdAt);
        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.setQueryObject(object);
        fetcher.generateObjectsQuery();
        fetcher.addOrderBy(object);

        final String expected = " SELECT ex FROM ExchangeCurrencyLogEvent ex  JOIN FETCH ex.currencySold currency_sold  JOIN FETCH ex.currencyPurchase currency_purchase  JOIN FETCH ex.user user  WHERE  currency_sold.currency = :currencySold  ORDER BY ex.createdAt  DESC  ORDER BY ex.createdAt  DESC ";
        assertEquals(expected, fetcher.getQuery());
        assertEquals("currencySold", fetcher.getParameters().keySet().stream().collect(Collectors.joining()));
        assertEquals("AUD", fetcher.getParameters().values().stream().map(Object::toString).collect(Collectors.joining()));
    }

}