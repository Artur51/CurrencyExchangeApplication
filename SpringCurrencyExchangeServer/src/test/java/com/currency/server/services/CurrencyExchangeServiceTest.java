package com.currency.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import com.currency.server.controllers.errors.Errors;
import com.currency.server.pojo.exchange.CurrencyExchangeLogsPageRequest;
import com.currency.server.pojo.exchange.CurrencyExchangeRequest;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvents;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeRate;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.pojo.login.UserRegistrationData;
import com.currency.server.repositories.CurrencyExchangeTimeAndRatesRepository;
import com.currency.server.repositories.ExchangeCurrencyLogEventRepository;
import com.currency.server.utils.CurrencyExchangeTimeAndRatesRecordValidator;
import com.currency.server.utils.TimeUtils;

@SpringBootTest()
@ActiveProfiles({ "test" })
public class CurrencyExchangeServiceTest {
    @MockBean
    CurrencyExchangeRatesProviderService dataProvider;

    @MockBean
    ExchangeCurrencyLogEventRepository logEventsRepository;

    @Autowired
    CurrencyExchangeService service;
    @MockBean
    private CurrencyExchangeTimeAndRatesRepository currencyExchangeTimeAndRatesRepository;

    @BeforeEach
    private void beforEach() {
        when(currencyExchangeTimeAndRatesRepository.saveAndFlush(Mockito.any(CurrencyExchangeTimeAndRates.class))).thenAnswer(i -> i.getArguments()[0]);
        when(logEventsRepository.saveAndFlush(Mockito.any(ExchangeCurrencyLogEvent.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    String[] defaultRates = { "USD", "1.1602", "JPY", "131.65", "BGN", "1.9558" };

    public CurrencyExchangeTimeAndRates getOutDatedCurrencyExchangeTimeAndRatesRecord() {
        final CurrencyExchangeTimeAndRates record = getActualCurrencyExchangeTimeAndRatesRecord();
        TimeUtils.addHours(record.getTime(), -24 * 5);
        return record;
    }

    public CurrencyExchangeTimeAndRates getActualCurrencyExchangeTimeAndRatesRecord() {
        return getActualCurrencyExchangeTimeAndRatesRecord(defaultRates);
    }

    public CurrencyExchangeTimeAndRates getActualCurrencyExchangeTimeAndRatesRecord(
            String... rates) {
        final CurrencyExchangeTimeAndRates record = new CurrencyExchangeTimeAndRates();
        record.setTime(new Date());
        assertTrue(rates.length % 2 == 0);
        for (int i = 0; i < rates.length; i += 2) {
            record.getExchangeRates().add(new CurrencyExchangeRate(rates[i], rates[i + 1]));
        }
        return record;
    }

    @Test
    public void testShouldReturnCurrentRecordFromServerWhenApplicationStartedAndPutItIntoDatabase() throws IOException {

        final CurrencyExchangeTimeAndRates actualTestRecord = getActualCurrencyExchangeTimeAndRatesRecord();
        when(dataProvider.getExchangeData()).thenReturn(actualTestRecord);
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(null);

        CurrencyExchangeTimeAndRates record = service.getCurrencyExchangeInfo();
        assertEquals(actualTestRecord, record);

        Mockito.verify(dataProvider, Mockito.times(1)).getExchangeData();
        Mockito.verify(currencyExchangeTimeAndRatesRepository, Mockito.times(1)).findTopByOrderByTimeDesc();
        Mockito.verify(currencyExchangeTimeAndRatesRepository, Mockito.times(1)).saveAndFlush(actualTestRecord);
    }

    @Test
    public void testShouldReturnCurrentRecordIfCurrentRecordIsActual() throws IOException {
        final CurrencyExchangeTimeAndRates databaseRecord = getActualCurrencyExchangeTimeAndRatesRecord();
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(databaseRecord);

        final CurrencyExchangeTimeAndRates actualRecord = getActualCurrencyExchangeTimeAndRatesRecord();
        when(dataProvider.getExchangeData()).thenReturn(actualRecord);

        CurrencyExchangeTimeAndRates record = service.getCurrencyExchangeInfo();
        assertEquals(databaseRecord, record);

        Mockito.verify(dataProvider, Mockito.times(0)).getExchangeData();
    }

    @Autowired
    private CurrencyExchangeTimeAndRatesRecordValidator recordValidator;

    @Test
    public void testShouldReturnNewDateRecordIfDatabaseRecordIsOutDated() throws IOException {
        final CurrencyExchangeTimeAndRates outdatedRecord = getOutDatedCurrencyExchangeTimeAndRatesRecord();
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(outdatedRecord);

        assertTrue(!recordValidator.isRecordDateValid(outdatedRecord), "Record must be outdated.");
        
        final CurrencyExchangeTimeAndRates actualRecord = getActualCurrencyExchangeTimeAndRatesRecord();
        when(dataProvider.getExchangeData()).thenReturn(actualRecord);

        CurrencyExchangeTimeAndRates record = service.getCurrencyExchangeInfo();
        assertEquals(actualRecord, record, "Expected actual record to be found but not an outdated one.");

        Mockito.verify(dataProvider, Mockito.times(1)).getExchangeData();
    }

    @Test
    public void testRequestWhenNoCurrencyDataFromProviderShouldFailWithErrorCode() throws IOException {
        when(dataProvider.getExchangeData()).thenThrow(Errors.currencyExchangeRateDataUnavailable);
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(null);

        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            CurrencyExchangeTimeAndRates record = service.getCurrencyExchangeInfo();
            assertEquals(null, record);
        });
        assertEquals(thrown, Errors.currencyExchangeRateDataUnavailable);

        Mockito.verify(dataProvider, Mockito.times(1)).getExchangeData();
        Mockito.verify(currencyExchangeTimeAndRatesRepository, Mockito.times(1)).findTopByOrderByTimeDesc();
    }

    @Test
    public void testGetCurrencyExchangeInfoMethodShouldOnlyOnceRequestDataAndStoreToDatabaseOnlyOnce()
            throws IOException {
        final CurrencyExchangeTimeAndRates actualTestRecord = getActualCurrencyExchangeTimeAndRatesRecord();
        when(dataProvider.getExchangeData()).thenReturn(actualTestRecord);

        // first get from provider, save to database
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(null);
        CurrencyExchangeTimeAndRates record = service.getCurrencyExchangeInfo();
        assertNotNull(record);

        // second and third attempt get from database stored record
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(record);
        CurrencyExchangeTimeAndRates record2 = service.getCurrencyExchangeInfo();
        CurrencyExchangeTimeAndRates record3 = service.getCurrencyExchangeInfo();

        assertNotNull(record2);
        assertNotNull(record3);

        assertEquals(record3.getTime(), record2.getTime());

        Mockito.verify(dataProvider, Mockito.times(1)).getExchangeData();
        Mockito.verify(logEventsRepository, Mockito.times(0)).saveAndFlush(Mockito.any(ExchangeCurrencyLogEvent.class));
        Mockito.verify(currencyExchangeTimeAndRatesRepository, Mockito.times(1)).saveAndFlush(Mockito.any(CurrencyExchangeTimeAndRates.class));
        Mockito.verify(currencyExchangeTimeAndRatesRepository, Mockito.times(3)).findTopByOrderByTimeDesc();
    }

    public void testMakeExchangeShouldMakeRecordIntoDatabase() throws IOException {
        final CurrencyExchangeTimeAndRates actualTestRecord = getActualCurrencyExchangeTimeAndRatesRecord();
        when(dataProvider.getExchangeData()).thenReturn(actualTestRecord);

        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(null);
        CurrencyExchangeTimeAndRates record = service.getCurrencyExchangeInfo();
        assertNotNull(record);

        Mockito.verify(dataProvider, Mockito.times(1)).getExchangeData();
        Mockito.verify(currencyExchangeTimeAndRatesRepository, Mockito.times(1)).saveAndFlush(Mockito.any(CurrencyExchangeTimeAndRates.class));
        Mockito.verify(logEventsRepository, Mockito.times(0)).saveAndFlush(Mockito.any(ExchangeCurrencyLogEvent.class));
        Mockito.verify(currencyExchangeTimeAndRatesRepository, Mockito.times(1)).findTopByOrderByTimeDesc();
    }

    @Test
    public void testMakeExchangeMethodShouldCorrectCalculateExchangeForEuroToUsdSet() throws IOException {

        final CurrencyExchangeTimeAndRates outdatedRecord = //
                getActualCurrencyExchangeTimeAndRatesRecord("USD", "1.25");
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(outdatedRecord);

        final UserRegistrationData user = getUser();
        final CurrencyExchangeRequest request = getRequest("EUR", "USD", "1.0");
        final ExchangeCurrencyLogEvent result = service.makeExchange(user, request);

        assertEquals("EUR", result.getCurrencySold().getCurrency());
        assertEquals("USD", result.getCurrencyPurchase().getCurrency());
        assertEquals("1.25", result.getCurrencyAmountReceived().toString());

        Mockito.verify(logEventsRepository, Mockito.times(1)).saveAndFlush(result);
    }

    @Test
    public void testMakeExchangeMethodShouldCorrectCalculateExchangeForUsdToEuroSet() throws IOException {
        final CurrencyExchangeTimeAndRates outdatedRecord = //
                getActualCurrencyExchangeTimeAndRatesRecord("USD", "1.1672");
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(outdatedRecord);

        final UserRegistrationData user = getUser();
        final CurrencyExchangeRequest request = getRequest("USD", "EUR", "1.0");
        final ExchangeCurrencyLogEvent result = service.makeExchange(user, request);

        assertEquals("USD", result.getCurrencySold().getCurrency());
        assertEquals("EUR", result.getCurrencyPurchase().getCurrency());
        assertEquals("0.857", result.getCurrencyAmountReceived().toString());
    }


    public CurrencyExchangeRequest getRequest(
            final String from,
            final String to,
            final String val) {
        final CurrencyExchangeRequest request = new CurrencyExchangeRequest();
        request.setCurrencyAmountSold(new BigDecimal(val));
        request.setCurrencyPurchase(to);
        request.setCurrencySold(from);
        return request;
    }

    public UserRegistrationData getUser() {
        final UserRegistrationData user = new UserRegistrationData();
        user.setUsername("someuser");
        user.setUsername("somepassword");
        return user;
    }

    @Test
    public void testMakeExchangeMethodShouldCorrectCalculateExchangeForJPYToUSDSet() throws IOException {

        final CurrencyExchangeTimeAndRates record = //
                getActualCurrencyExchangeTimeAndRatesRecord("JPY", "131.65", "USD", "1.1602");
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(record);

        final UserRegistrationData user = getUser();
        final CurrencyExchangeRequest request = getRequest("JPY", "USD", "500");
        final ExchangeCurrencyLogEvent result = service.makeExchange(user, request);

        assertEquals("500", result.getCurrencyAmountSold().toString());
        assertEquals("JPY", result.getCurrencySold().getCurrency());
        assertEquals("USD", result.getCurrencyPurchase().getCurrency());
        assertEquals("4.41", result.getCurrencyAmountReceived().toString());
    }

    @Test
    public void testMakeExchangeMethodShouldCorrectCalculateExchangeForCHFToUSDSet() throws IOException {
        final CurrencyExchangeTimeAndRates record = //
                getActualCurrencyExchangeTimeAndRatesRecord("CHF", "10", "USD", "2");
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(record);

        final UserRegistrationData user = getUser();
        final CurrencyExchangeRequest request = getRequest("CHF", "USD", "100");
        final ExchangeCurrencyLogEvent result = service.makeExchange(user, request);

        assertEquals("100", result.getCurrencyAmountSold().toString());
        assertEquals("CHF", result.getCurrencySold().getCurrency());
        assertEquals("USD", result.getCurrencyPurchase().getCurrency());
        assertEquals("20", result.getCurrencyAmountReceived().toString());
    }

    @Test
    public void testMakeExchangeMethodShouldThrowExceptionInCaseTheSameCurrencyUsed() {
        final CurrencyExchangeTimeAndRates record = //
                getActualCurrencyExchangeTimeAndRatesRecord("USD", "2");
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(record);

        final UserRegistrationData user = getUser();
        final CurrencyExchangeRequest request = getRequest("USD", "USD", "100");
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            final ExchangeCurrencyLogEvent result = service.makeExchange(user, request);
        });
        assertEquals(Errors.sameTypeCurrencies, thrown);

    }

    @Test
    public void testGetFromMethodShouldReturnRecordWithTheSameTypeFromActualData() {
        final CurrencyExchangeTimeAndRates record = //
                getActualCurrencyExchangeTimeAndRatesRecord("USD", "2");

        final CurrencyExchangeRate from = service.getFrom(record, "USD");
        assertEquals(from.getCurrency(), "USD");
        assertEquals(from.getRate().toString(), "2");
    }

    @Test
    public void testForNotExistedCurrencyShouldReturnException() {
        final CurrencyExchangeTimeAndRates record = //
                getActualCurrencyExchangeTimeAndRatesRecord("USD", "2");
        when(currencyExchangeTimeAndRatesRepository.findTopByOrderByTimeDesc()).thenReturn(record);

        final UserRegistrationData user = getUser();
        final CurrencyExchangeRequest request = getRequest("SOME", "OTHER", "100");
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            final ExchangeCurrencyLogEvent result = service.makeExchange(user, request);
        });
        assertTrue(thrown.getMessage().contains("Exchange currency is invalid:"));
    }

    @Test
    public void testGetLoggedEventDataMethodShouldReturnDatabaseStoredLogsAsValidObject() {
        final CurrencyExchangeLogsPageRequest request = new CurrencyExchangeLogsPageRequest();
        final ExchangeCurrencyLogEventQueryData filter = new ExchangeCurrencyLogEventQueryData();

        request.setQueryData(filter);
        ExchangeCurrencyLogEvents result = service.getLoggedEventData(request);
        assertNotNull(result);

    }




}