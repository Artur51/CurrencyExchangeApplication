package com.currency.server.controllers;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import com.currency.server.pojo.exchange.CurrencyExchangeRequest;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeRate;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.pojo.login.UserRegistrationData;
import com.currency.server.services.CurrencyExchangeService;
import com.currency.server.services.UserRegistrationService;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyExchangeControllerMakeRecordTest {
    @Autowired
    private CurrencyExchangeService currencyExchange;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Test
    public void testShouldMakeOneRandomExchange() throws Exception {
        UserRegistrationData user = new UserRegistrationData();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        user = userRegistrationService.addUser(user);

        final CurrencyExchangeTimeAndRates actualData = currencyExchange.getCurrencyExchangeInfo();

        CurrencyExchangeRate any = getRandomValue(actualData);
        CurrencyExchangeRate any2 = null;
        while (any2 == null || any.equals(any2)) {
            any2 = getRandomValue(actualData);
            if (any2.getCurrency().equals(any.getCurrency())) {
                any2 = null;
            }
        }

        int value = new Random().nextInt(50) * 10;
        CurrencyExchangeRequest request = new CurrencyExchangeRequest();

        request.setCurrencySold(any.getCurrency());
        request.setCurrencyPurchase(any2.getCurrency());
        BigDecimal currencyAmountSold = new BigDecimal(String.valueOf(value));

        request.setCurrencyAmountSold(currencyAmountSold);
        ExchangeCurrencyLogEvent exchangeCurrencyLogEvent = currencyExchange.makeExchange(user, request);

        assertNotNull(exchangeCurrencyLogEvent);
        assertNotNull(exchangeCurrencyLogEvent.getCreatedAt());
        assertNotNull(exchangeCurrencyLogEvent.getId());
    }

    private CurrencyExchangeRate getRandomValue(
            CurrencyExchangeTimeAndRates actualData) {
        Random r = new Random();
        int index = r.nextInt(actualData.getExchangeRates().size());
        CurrencyExchangeRate item = actualData.getExchangeRates().get(index);

        while (item == null || !StringUtils.hasText(item.getCurrency())) {
            index = r.nextInt(actualData.getExchangeRates().size());
            item = actualData.getExchangeRates().get(index);
        }
        return item;
    }
}
