package com.currency.server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeRate;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyExchangeRatesProviderServiceTest {
    @Autowired
    CurrencyExchangeRatesProviderService provider;
    @MockBean
    private RestTemplate restTemplate;

    public byte[] getResponseArray() throws IOException {
        final InputStream is = CurrencyExchangeRatesProviderServiceNoDataTest.class.getResourceAsStream("/response10.14.xml");
        byte[] targetArray = new byte[is.available()];
        is.read(targetArray);
        return targetArray;
    }

    @Test
    public void testShouldProvideCurrencyExchangeRateData() throws IOException {
        byte[] targetArray = getResponseArray();
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(targetArray);

        final CurrencyExchangeTimeAndRates exchangeData = provider.getExchangeData();
        assertNotNull(exchangeData);
        assertNotNull(exchangeData.getTime());
        assertEquals("14 Oct 2021 00:00:00 GMT", exchangeData.getTime().toGMTString());
        assertNotNull(exchangeData.getExchangeRates());
        assertEquals(33, exchangeData.getExchangeRates().size());
        assertEquals("USD", exchangeData.getExchangeRates().get(0).getCurrency());
        assertEquals("1.1602", exchangeData.getExchangeRates().get(0).getRate().toString());
    }

    @Test
    public void testCurrencyExchangeRateRecordsShouldHaveParentFieldNotNull() throws IOException {
        byte[] targetArray = getResponseArray();
        when(restTemplate.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(targetArray);

        final CurrencyExchangeTimeAndRates exchangeData = provider.getExchangeData();
        final List<CurrencyExchangeRate> exchangeRates = exchangeData.getExchangeRates();
        for (CurrencyExchangeRate item : exchangeRates) {
            assertNotNull(item.getParent(), "CurrencyExchangeRate Record parent field is null");
        }
    }
}