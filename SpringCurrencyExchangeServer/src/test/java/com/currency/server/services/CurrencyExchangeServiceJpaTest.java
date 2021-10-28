package com.currency.server.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.currency.server.pojo.exchange.CurrencyExchangeLogsPageRequest;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvents;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.repositories.CurrencyExchangeTimeAndRatesRepository;
import com.currency.server.utils.TimeUtils;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyExchangeServiceJpaTest {
    @Autowired
    CurrencyExchangeService service;
    @Autowired
    private CurrencyExchangeTimeAndRatesRepository repository;

    @Test
    public void testDatabaseShouldReturnMostLatestRecordIfHasSeveral() throws IOException {
        ArrayList<CurrencyExchangeTimeAndRates> list = new ArrayList<>();
            Date date = TimeUtils.getDate(2021, Calendar.JANUARY, 1);
        for (int i = 0; i < 10; i++) {
            date = new Date(date.getTime());
            TimeUtils.addHours(date,24);
            CurrencyExchangeTimeAndRates item = new CurrencyExchangeTimeAndRates();
            item.setTime(date);
            list.add(item);
        }

        repository.saveAll(list);
        repository.flush();

        CurrencyExchangeTimeAndRates item = repository.findTopByOrderByTimeDesc();
        assertEquals(date, item.getTime());
    }
    
    @Test
    @Sql(scripts = "/filter-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetLoggedEventDataMethodShouldIncludePaginationObject() {
        final CurrencyExchangeLogsPageRequest request = new CurrencyExchangeLogsPageRequest();
        final ExchangeCurrencyLogEventQueryData filter = new ExchangeCurrencyLogEventQueryData();
        request.setQueryData(filter);

        ExchangeCurrencyLogEvents result = service.getLoggedEventData(request);
        assertNotNull(result);
        assertNotNull(result.getData());
        assertNotNull(result.getData().getPageable());
        assertEquals(10, result.getData().getSize());
        assertEquals(2, result.getData().getTotalPages());
        assertEquals(12, result.getData().getTotalElements());
    }
}
