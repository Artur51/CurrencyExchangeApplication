package com.currency.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.currency.server.controllers.errors.Errors;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.pojo.jaxb.org.gesmes.xml.EnvelopeType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyExchangeRatesProviderServiceNoDataTest {
    @Autowired
    CurrencyExchangeRatesProviderService provider;
    @MockBean
    private XmlMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testShouldFailWithExceptionIfNoDataReturned() throws JsonParseException, JsonMappingException, IOException {
        final byte[] value = new byte[0];
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(value);
        when(objectMapper.readValue(value, EnvelopeType.class)).thenReturn(null);

        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> {
            final CurrencyExchangeTimeAndRates exchangeData = provider.getExchangeData();
        });
        assertEquals(Errors.currencyExchangeRateDataUnavailable, thrown);
    }
}