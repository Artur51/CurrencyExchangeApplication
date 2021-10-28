package com.currency.server.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.currency.server.controllers.errors.Errors;
import com.currency.server.pojo.exchange.CurrencyExchangeLogsPageRequest;
import com.currency.server.pojo.exchange.CurrencyExchangeRequest;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvents;
import com.currency.server.pojo.exchange.PageRequestData;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeRate;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.pojo.login.UserRegistrationData;
import com.currency.server.repositories.CurrencyExchangeTimeAndRatesRepository;
import com.currency.server.repositories.ExchangeCurrencyLogEventRepository;
import com.currency.server.repositories.RegisteredUserRepository;
import com.currency.server.utils.CurrencyExchangeTimeAndRatesRecordValidator;

@Service
public class CurrencyExchangeService {

    public static final String EURO_CURRENCY_NAME = "EUR";

    @Value("${currency.get.data.url}")
    private String currencyServerUrl;

    @Autowired
    private CurrencyExchangeTimeAndRatesRecordValidator recordValidator;
    @Autowired
    private CurrencyExchangeTimeAndRatesRepository repository;
    @Autowired
    RegisteredUserRepository userRepository;
    @Autowired
    private ExchangeCurrencyLogEventRepository filterQueryRepository;
    @Autowired
    ExchangeCurrencyLogEventRepository logEventsRepository;
    @Autowired
    CurrencyExchangeRatesProviderService exchangeDataProvider;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CurrencyExchangeTimeAndRates getCurrencyExchangeInfo() throws IOException {
        CurrencyExchangeTimeAndRates record = repository.findTopByOrderByTimeDesc();
        if (record == null || !recordValidator.isRecordDateValid(record)) {
            record = exchangeDataProvider.getExchangeData();
            if (record != null) {
                record = repository.saveAndFlush(record);
                Objects.requireNonNull(record, "Database must return actual record after save.");
            }
        }
        if (record == null) {
            throw Errors.currencyExchangeRateDataUnavailable;
        }
        return record;
    }

    public ExchangeCurrencyLogEvent makeExchange(
            String userName,
            CurrencyExchangeRequest request) throws IOException {
        UserRegistrationData databaseUser = userRepository.findByUsername(userName);
        if (databaseUser == null) {
            throw Errors.getUserNotFoundException(userName);
        }

        return makeExchange(databaseUser, request);
    }

    public ExchangeCurrencyLogEvent makeExchange(
            UserRegistrationData databaseUser,
            CurrencyExchangeRequest request) throws IOException {
        final CurrencyExchangeTimeAndRates actualData = getCurrencyExchangeInfo();
        if (actualData == null) {
            throw Errors.currencyExchangeRateDataUnavailable;
        }
        ExchangeCurrencyLogEvent result = calculateForUser(databaseUser, request, actualData);
        result = logEventsRepository.saveAndFlush(result);
        return result;
    }

    public static ExchangeCurrencyLogEvent calculateForUser(
            UserRegistrationData databaseUser,
            CurrencyExchangeRequest request,
            final CurrencyExchangeTimeAndRates actualData) throws IOException {
        String currencySold = request.getCurrencySold();
        String currencyPurchase = request.getCurrencyPurchase();
        if (Objects.equals(currencyPurchase, currencySold)) {
            throw Errors.sameTypeCurrencies;
        }

        CurrencyExchangeRate currencySoldRate = getFrom(actualData, currencySold);
        CurrencyExchangeRate currencyPurchaseRate = getFrom(actualData, currencyPurchase);

        final BigDecimal value = request.getCurrencyAmountSold();

        BigDecimal returnValue = value;
        returnValue = toEuro(currencySoldRate, returnValue);
        returnValue = fromEuro(currencyPurchaseRate, returnValue);
        returnValue = returnValue.round(RETURN_PERCISION);

        ExchangeCurrencyLogEvent result = new ExchangeCurrencyLogEvent();
        result.setUser(databaseUser);
        result.setCurrencyAmountSold(value);
        result.setCurrencyAmountReceived(returnValue);
        result.setCurrencySold(currencySoldRate);
        result.setCurrencyPurchase(currencyPurchaseRate);
        return result;
    }

    public static final MathContext DEFAULT_PRECISION = MathContext.DECIMAL128;
    public static final MathContext RETURN_PERCISION = new MathContext(3, RoundingMode.HALF_EVEN);

    private static final CurrencyExchangeRate euroToEuroExchangeRate = new CurrencyExchangeRate("EUR", "1.0");

    private static BigDecimal toEuro(
            CurrencyExchangeRate currencySold,
            BigDecimal value) {
        try {
            return value.divide(currencySold.getRate(), DEFAULT_PRECISION);
        } catch (Exception e) {
            throw new RuntimeException("error calculate value " + value + " / " + currencySold.getRate(), e);
        }
    }

    private static BigDecimal fromEuro(
            CurrencyExchangeRate currencyPurchase,
            BigDecimal value) {
        try {
            return value.multiply(currencyPurchase.getRate(), DEFAULT_PRECISION);
        } catch (Exception e) {
            throw new RuntimeException("error calculate value " + value + " * " + currencyPurchase.getRate(), e);
        }
    }

    static CurrencyExchangeRate getFrom(
            CurrencyExchangeTimeAndRates actualData,
            String currency) {
        if (currency == null) {
            throw Errors.getInvalidCurrencyProvidedException(currency);
        }
        if (euroToEuroExchangeRate.getCurrency().equals(currency)) {
            return euroToEuroExchangeRate;
        }

        final List<CurrencyExchangeRate> items = actualData.getExchangeRates();
        for (CurrencyExchangeRate item : items) {
            if (Objects.equals(item.getCurrency(), currency)) {
                return item;
            }
        }
        throw Errors.getInvalidCurrencyProvidedException(currency);
    }

    public ExchangeCurrencyLogEvents getLoggedEventData(
            CurrencyExchangeLogsPageRequest filterRequest) {
        PageRequest pageRequest = getPageRequest(filterRequest);
        Page<ExchangeCurrencyLogEvent> data = null;
        if (filterRequest != null && filterRequest.getQueryData() != null
                && !filterRequest.getQueryData().isEmpty()) {
            data = filterQueryRepository.findAll(filterRequest.getQueryData(), pageRequest);
        } else {
            data = filterQueryRepository.findAll(pageRequest);
        }
        final ExchangeCurrencyLogEvents result = new ExchangeCurrencyLogEvents();
        result.setPageRequestData(filterRequest.getPageRequestData());
        result.setData(data);
        return result;
    }

    public static PageRequest getPageRequest(CurrencyExchangeLogsPageRequest request) {
        if (request == null || request.getPageRequestData() == null) {
            return PageRequest.of(0, 10);
        }
        Sort sort = getSort(request.getQueryData());
        PageRequestData pageRequestData = request.getPageRequestData();
        return PageRequest.of(pageRequestData.getPageNumber(), pageRequestData.getPageSize(), sort);
    }

    private static final ExchangeCurrencyLogEventQueryData.OrderBy defaultSort = ExchangeCurrencyLogEventQueryData.OrderBy.createdAt;

    public static Sort getSort(ExchangeCurrencyLogEventQueryData request) {
        ExchangeCurrencyLogEventQueryData.OrderBy orderBy = defaultSort;
        if (request != null && request.getOrderBy() != null) {
            orderBy = request.getOrderBy();
        }
        return orderBy.getSort();
    }
}