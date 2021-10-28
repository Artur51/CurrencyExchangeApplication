package com.currency.server.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.pojo.login.UserDetailsImpl;
import com.currency.server.pojo.login.UserRegistrationData;
import com.currency.server.repositories.CurrencyExchangeTimeAndRatesRepository;
import com.currency.server.repositories.ExchangeCurrencyLogEventRepository;
import com.currency.server.repositories.RegisteredUserRepository;
import com.currency.server.security.jwt.AuthTokenFilter;
import com.currency.server.security.jwt.JsonTokenGenerator;
import com.currency.server.services.UserRegistrationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CurrencyExchangeControllerWithNoAccessTest {
    @MockBean
    JsonTokenGenerator tokenValidator;
    @Autowired
    UserRegistrationService userRegistrationService;

    @MockBean
    private RegisteredUserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    String MAKE_EXCHANGE_URL = "/makeExchange";

    UserRegistrationData registeredUser = new UserRegistrationData("registerdUser", "correctPassword");
    UserDetailsImpl reigsteredUserDetails = new UserDetailsImpl(registeredUser);
    @MockBean
    private CurrencyExchangeTimeAndRatesRepository currencyExchangeTimeAndRatesRepository;
    @MockBean
    ExchangeCurrencyLogEventRepository logEventsRepository;
    @MockBean
    AuthenticationManager authenticationManager;

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
        when(tokenValidator.validateJsonToken(Mockito.anyString())).thenReturn(false);
    }

    private MockHttpServletRequestBuilder createExchangePostFormDataRequest(
            String url,
            String... params) {
        final String token = "Invalid token";

        final MockHttpServletRequestBuilder builder = post(url)//
                .with(SecurityMockMvcRequestPostProcessors.csrf())//
                .header(AuthTokenFilter.HEADER_NAME, AuthTokenFilter.HEADER_PREFIX + token);

        return CurrencyExchangeControllerTest.toFormDataParam(builder, params);
    }

    @Test
    public void testShouldFailToLoginExistedUserWithIncorrectPassword() throws Exception {
        final UserRegistrationData user = new UserRegistrationData();
        
        // when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(new
        // TestAuthentication(user));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        mockMvc.perform(RegistrationControllerLoginTest.createLoginPostRequest(user.getUsername(), "some incorrect password")) //
                .andDo(print()) //
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMakeExchangeShouldFailWithExceptionIfUserIsNotAuthorized() throws Exception {
        mockMvc.perform(createExchangePostFormDataRequest(MAKE_EXCHANGE_URL, //
                "currencyAmountSold", "20", //
                "currencySold", "USD", //
                "currencyPurchase", "JPY"//
        )) //
                .andDo(print()) //
                .andExpect(status().isUnauthorized());
    }
}
