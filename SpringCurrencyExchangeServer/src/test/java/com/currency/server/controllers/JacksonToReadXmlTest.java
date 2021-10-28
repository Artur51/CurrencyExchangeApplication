package com.currency.server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.pojo.jaxb.org.gesmes.xml.EnvelopeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public class JacksonToReadXmlTest {
    @Test
    public void testReadAndParseClassFromXml() throws IOException {
        InputStream stream = JacksonToReadXmlTest.class.getResourceAsStream("/response10.14.xml");
        try (stream) {
            JacksonXmlModule xmlModule = new JacksonXmlModule();
            xmlModule.setDefaultUseWrapper(false);
            ObjectMapper objectMapper = new XmlMapper(xmlModule);
            JaxbAnnotationModule jaxbAnnotationModule = new JaxbAnnotationModule();
            objectMapper.registerModule(jaxbAnnotationModule);
            EnvelopeType envelope = objectMapper.readValue(stream, EnvelopeType.class);

            assertEquals("European Central Bank", envelope.getSender().getName());
            assertNotNull(envelope.getCube());
            final List<CurrencyExchangeTimeAndRates> records = envelope.getCube().getCube();
            assertEquals(1, records.size());

            CurrencyExchangeTimeAndRates exchangeData = records.get(0);

            assertEquals("14 Oct 2021 00:00:00 GMT", exchangeData.getTime().toGMTString());
            assertNotNull(exchangeData.getExchangeRates());
            assertEquals(32, exchangeData.getExchangeRates().size());
            assertEquals("USD", exchangeData.getExchangeRates().get(0).getCurrency());
            assertEquals("1.1602", exchangeData.getExchangeRates().get(0).getRate().toString());

        }
    }
}
