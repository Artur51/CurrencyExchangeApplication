package com.currency.server.controllers;

import com.currency.server.controllers.errors.Errors;
import com.currency.server.pojo.exchange.CurrencyExchangeLogsPageRequest;
import com.currency.server.pojo.exchange.CurrencyExchangeRequest;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvents;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.services.CurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.POST})
@CrossOrigin(
        origins = {"*"},
        maxAge = 3600,
        allowedHeaders = CorsConfiguration.ALL
)
public class CurrencyExchangeController {

    @Autowired
    private CurrencyExchangeService currencyExchange;

    @GetMapping(value = "/exchangeInfo")
    public CurrencyExchangeTimeAndRates getCurrenciesExchangeInfo() throws Exception {
        final CurrencyExchangeTimeAndRates data = currencyExchange.getCurrencyExchangeInfo();
        if (data == null) {
            throw Errors.currencyExchangeRateDataUnavailable;
        }
        return data;
    }

    @PostMapping("/makeExchange")
    public ExchangeCurrencyLogEvent makeExchange(
            @Valid CurrencyExchangeRequest request,
            BindingResult bindingResult) throws Exception {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String name = authentication.getName();
        return currencyExchange.makeExchange(name, request);
    }

    @PostMapping(path = "/exchangeLogs")
    public ExchangeCurrencyLogEvents getCurrenciesExchangeLogs(
            @RequestBody CurrencyExchangeLogsPageRequest logsRequest) {
        System.out.println(" logsRequest " + logsRequest);
        ExchangeCurrencyLogEvents data = currencyExchange.getLoggedEventData(logsRequest);
        System.out.println(data);
        return data;
    }
}