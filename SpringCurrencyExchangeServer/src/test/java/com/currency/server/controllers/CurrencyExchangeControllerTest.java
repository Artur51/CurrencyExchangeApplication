package com.currency.server.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.currency.server.pojo.exchange.CurrencyExchangeLogsPageRequest;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvents;
import com.currency.server.pojo.exchange.PageRequestData;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.pojo.login.UserDetailsImpl;
import com.currency.server.pojo.login.UserRegistrationData;
import com.currency.server.repositories.CurrencyExchangeTimeAndRatesRepository;
import com.currency.server.repositories.ExchangeCurrencyLogEventRepository;
import com.currency.server.repositories.RegisteredUserRepository;
import com.currency.server.security.jwt.AuthTokenFilter;
import com.currency.server.security.jwt.JsonTokenGenerator;
import com.currency.server.services.UserRegistrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CurrencyExchangeControllerTest {

    @Autowired
    UserRegistrationService userRegistrationService;

    @MockBean
    private RegisteredUserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JsonTokenGenerator jsonTokenGenerator;

    String EXCHANGE_LOGS_URL = "/exchangeLogs";
    String EXCHANGE_INFO_URL = "/exchangeInfo";
    String MAKE_EXCHANGE_URL = "/makeExchange";

    UserRegistrationData registeredUser = new UserRegistrationData("registerdUser", "correctPassword");
    UserDetailsImpl reigsteredUserDetails = new UserDetailsImpl(registeredUser);
    @MockBean
    private CurrencyExchangeTimeAndRatesRepository currencyExchangeTimeAndRatesRepository;
    @MockBean
    ExchangeCurrencyLogEventRepository logEventsRepository;

    @BeforeEach
    public void beforeEach() {
        when(logEventsRepository.save(Mockito.any(ExchangeCurrencyLogEvent.class))).thenAnswer(i -> i.getArguments()[0]);
        when(logEventsRepository.saveAndFlush(Mockito.any(ExchangeCurrencyLogEvent.class))).thenAnswer(i -> i.getArguments()[0]);
        when(currencyExchangeTimeAndRatesRepository.save(Mockito.any(CurrencyExchangeTimeAndRates.class))).thenAnswer(i -> i.getArguments()[0]);
        when(currencyExchangeTimeAndRatesRepository.saveAndFlush(Mockito.any(CurrencyExchangeTimeAndRates.class))).thenAnswer(i -> i
                .getArguments()[0]);
        when(userRepository.saveAndFlush(Mockito.any(UserRegistrationData.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.save(Mockito.any(UserRegistrationData.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.findByUsername(registeredUser.getUsername())).thenReturn(registeredUser);
    }

    private MockHttpServletRequestBuilder createExchangePostFormDataRequest(
            String url,
            String... params) {
        final String token = jsonTokenGenerator.generateJsonToken(reigsteredUserDetails, new Date());

        final MockHttpServletRequestBuilder builder = post(url)//
                .with(SecurityMockMvcRequestPostProcessors.csrf())//
                .header(AuthTokenFilter.HEADER_NAME, AuthTokenFilter.HEADER_PREFIX + token);

        toFormDataParam(builder, params);
        return builder;
    }

    private MockHttpServletRequestBuilder createExchangePostJsonRequest(
            String url,
            Object param) throws JsonProcessingException {

        final String token = jsonTokenGenerator.generateJsonToken(reigsteredUserDetails, new Date());
        final MockHttpServletRequestBuilder builder = post(url)//
                .with(SecurityMockMvcRequestPostProcessors.csrf())//
                .header(AuthTokenFilter.HEADER_NAME, AuthTokenFilter.HEADER_PREFIX + token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        if (param != null) {
            String content = objectMapper.writeValueAsString(param);
            builder.content(content);
        }

        builder.contentType(MediaType.APPLICATION_JSON);
        return builder;
    }

    @Test
    public void testExchangeInfoEndPointShouldReturnValidData() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(createExchangePostFormDataRequest(EXCHANGE_INFO_URL)) //
                .andDo(print()) //
                .andExpect(status().isOk()).andReturn();

        final CurrencyExchangeTimeAndRates result = parseResult(mvcResult, CurrencyExchangeTimeAndRates.class);
        assertNotNull(result);
        assertNotNull(result.getTime());
        assertNotNull(result.getExchangeRates());
        assertEquals(33, result.getExchangeRates().size());
    }

    @Test
    public void testMakeExchangeEndPointShouldReturnValidData() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20", //
                "currencySold", "USD", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isOk());
    }

    @Test
    public void testMakeExchangeShouldFailIfCurrencySoldIsNotProvidedAsParamter() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("makeExchange.request.currencySold: must not be null")));

    }

    @Test
    public void testMakeExchangeShouldFailIfCurrencyPurchaseIsNotProvidedAsParameter() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20", //
                "currencySold", "USD" //
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("makeExchange.request.currencyPurchase: must not be null")));
    }

    @Test
    public void testMakeExchangeShouldFailIfCurrencyAmountSoldIsNotProvidedAsParameter() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencySold", "USD", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("makeExchange.request.currencyAmountSold: must not be null")));
    }

    @Test
    public void testMakeExchangeTestShouldPassIfProvidedAmountFractionIsBiggerThen3() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20.0001", //
                "currencySold", "USD", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("<3 digits> expected")))//
                .andExpect(content().string(containsString("numeric value out of bounds")));
    }

    @Test
    public void testMakeExchangeTestShouldFailWithExceptionIfRequestCurrencyIsInvalid() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20", //
                "currencySold", "USD3", //
                "currencyPurchase", "JPY2"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("Exchange currency is invalid: USD3")));
    }

    @Test
    public void testMakeExchangeShouldFailWithExceptionIfRequestCurrencyAmountIsNegativeValue() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "0", //
                "currencySold", "USD", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("makeExchange.request.currencyAmountSold: must be greater than 0")));
    }

    @Test
    public void testMakeExchangeShouldFailWithExceptionIfRequestCurrencyAmountIsZeroValue() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "-20", //
                "currencySold", "USD", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("makeExchange.request.currencyAmountSold: must be greater than 0")));
    }

    @Test
    public void testExchangeLogsEndPointShouldReturnData() throws Exception {
        final CurrencyExchangeLogsPageRequest requestData = new CurrencyExchangeLogsPageRequest();
        requestData.setPageRequestData(new PageRequestData(10, 0, 0));
        final ExchangeCurrencyLogEventQueryData data = new ExchangeCurrencyLogEventQueryData();
        data.setCreatedAtEnd(new Date());

        MvcResult mvcResult = mockMvc.perform(createExchangePostJsonRequest(EXCHANGE_LOGS_URL, requestData)) //
                .andDo(print()) //
                .andExpect(status().isOk())//
                .andReturn();

        final ExchangeCurrencyLogEvents result = parseResult(mvcResult, ExchangeCurrencyLogEvents.class);
        assertNotNull(result);
    }

    public <T> T parseResult(
            MvcResult mvcResult,
            final Class<T> valueType) throws UnsupportedEncodingException, JsonProcessingException, JsonMappingException {
        String responseBody = mvcResult.getResponse().getContentAsString();
        T responseResult = objectMapper.readValue(responseBody, valueType);
        return responseResult;
    }

    @Test
    public void testShouldFailIfCurrencySoldIsNotFromDatabaseList() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20", //
                "currencySold", "USD--", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("Exchange currency is invalid: USD--")));
    }

    @Test
    public void testShouldFailIfCurrencyPurchaseIsNotFromDatabaseList() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20", //
                "currencySold", "USD", //
                "currencyPurchase", "JPY--"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("Exchange currency is invalid: JPY--")));
    }

    @Test
    public void testShouldFailIfCurrencySoldIsNotProvided() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("makeExchange.request.currencySold: must not be null")));
    }

    @Test
    public void testShouldFailIfCurrencyPurchaseIsNotProvided() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20", //
                "currencySold", "USD" //
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("makeExchange.request.currencyPurchase: must not be null")));
    }

    @Test
    public void testShouldFailIfCurrencyAmountSoldIsNotProvided() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencySold", "USD", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(containsString("makeExchange.request.currencyAmountSold: must not be null")));
    }

    public static MockHttpServletRequestBuilder toFormDataParam(
            final MockHttpServletRequestBuilder builder,
            String... params) {
        for (int i = 0, size = params.length; i < size; i += 2) {
            String param1 = params[i];
            String param2 = params[i + 1];
            builder.param(param1, param2);
        }
        return builder;
    }
}