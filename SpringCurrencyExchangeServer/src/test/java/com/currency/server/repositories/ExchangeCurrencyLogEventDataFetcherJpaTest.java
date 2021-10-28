package com.currency.server.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData;
import com.currency.server.utils.TimeUtils;

@DataJpaTest
@ActiveProfiles("test")
public class ExchangeCurrencyLogEventDataFetcherJpaTest {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    CurrencyExchangeTimeAndRatesRepository repository;
    @Autowired
    ExchangeCurrencyLogEventRepository eventsRepository;

    @Test
    @Sql(scripts = "/filter-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetResultTotalCountMethodShouldReturnRecordsCount() {
        final List<ExchangeCurrencyLogEvent> findAll = eventsRepository.findAll();
        assertEquals(12, findAll.size());

        final ExchangeCurrencyLogEventQueryData queryData = new ExchangeCurrencyLogEventQueryData();
        queryData.setCurrencyPurchase("JPY");

        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.setQueryObject(queryData);

        Long count = fetcher.getResultTotalCount(entityManager.getEntityManager());
        assertEquals(2, count);
    }

    @Test
    @Sql(scripts = "/filter-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetResultFromDateTillDateShouldReturnRecordsFrom5Till7Count() {
        final List<ExchangeCurrencyLogEvent> findAll = eventsRepository.findAll();
        assertEquals(12, findAll.size());

        final ExchangeCurrencyLogEventQueryData queryData = new ExchangeCurrencyLogEventQueryData();
        final Date date1 = TimeUtils.getDate(2020, Calendar.JANUARY, 5);
        final Date date2 = TimeUtils.getDate(2020, Calendar.JANUARY, 7);

        queryData.setCreatedAtStart(date1);
        queryData.setCreatedAtEnd(date2);

        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.setQueryObject(queryData);
        
        Long count = fetcher.getResultTotalCount(entityManager.getEntityManager());
        assertEquals(2, count);
    }

    @Test
    @Sql(scripts = "/filter-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testSelectedSoldAndPurchaseCurrenciesAtTheSameTimeShuldReturnRecords() {
        final List<ExchangeCurrencyLogEvent> findAll = eventsRepository.findAll();
        assertEquals(12, findAll.size());

        final ExchangeCurrencyLogEventQueryData queryData = new ExchangeCurrencyLogEventQueryData();
        queryData.setCurrencySold("RUB");
        queryData.setCurrencyPurchase("AUD");

        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.setQueryObject(queryData);
        Long count = fetcher.getResultTotalCount(entityManager.getEntityManager());
        assertEquals(1, count);
    }

    @Test
    @Sql(scripts = "/filter-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetResultFromDateTillDateShouldReturnRecordsFrom2Till6Count() {
        final List<ExchangeCurrencyLogEvent> findAll = eventsRepository.findAll();
        assertEquals(12, findAll.size());
        final ExchangeCurrencyLogEventQueryData queryData = new ExchangeCurrencyLogEventQueryData();
        final Date date1 = TimeUtils.getDate(2020, Calendar.JANUARY, 2);
        final Date date2 = TimeUtils.getDate(2020, Calendar.JANUARY, 6);

        queryData.setCreatedAtStart(date1);
        queryData.setCreatedAtEnd(date2);

        final ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        fetcher.setQueryObject(queryData);

        Long count = fetcher.getResultTotalCount(entityManager.getEntityManager());
        assertEquals(4, count);
    }
}