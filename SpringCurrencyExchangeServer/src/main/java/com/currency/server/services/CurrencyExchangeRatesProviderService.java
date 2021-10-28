package com.currency.server.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.currency.server.controllers.errors.Errors;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeRate;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.pojo.jaxb.org.gesmes.xml.EnvelopeType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class CurrencyExchangeRatesProviderService {

    @Value("${currency.get.data.url}")
    private String getCurrencyDataUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private XmlMapper objectMapper;


    public CurrencyExchangeTimeAndRates getExchangeData() throws IOException {
        byte[] data = restTemplate.getForObject(getCurrencyDataUrl, byte[].class);
        EnvelopeType item = objectMapper.readValue(data, EnvelopeType.class);
        if (item == null || item.getCube() == null || item.getCube().getCube() == null || item.getCube().getCube().isEmpty()) {
            throw Errors.currencyExchangeRateDataUnavailable;
        }
        final CurrencyExchangeTimeAndRates record = item.getCube().getCube().get(0);

        // as soon as record must be in database we propagate it here.
        // it allows exchanging into/from euro currency.
        CurrencyExchangeRate euroCurrencyExchangeRate = new CurrencyExchangeRate(CurrencyExchangeService.EURO_CURRENCY_NAME, "1.0");
        record.getExchangeRates().add(euroCurrencyExchangeRate);
        record.setupParentField();
        return record;
    }
}