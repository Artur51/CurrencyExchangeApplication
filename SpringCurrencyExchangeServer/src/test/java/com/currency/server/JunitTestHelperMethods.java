package com.currency.server;

import java.io.IOException;
import java.io.InputStream;

import com.currency.server.controllers.JacksonToReadXmlTest;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;
import com.currency.server.pojo.jaxb.org.gesmes.xml.EnvelopeType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JunitTestHelperMethods {

    public static CurrencyExchangeTimeAndRates readData(
            XmlMapper xmlObjectMapper,
            String name) throws IOException {
        InputStream stream = JacksonToReadXmlTest.class.getResourceAsStream(name);
        EnvelopeType value1 = xmlObjectMapper.readValue(stream, EnvelopeType.class);
        CurrencyExchangeTimeAndRates rates = value1.getCube().getCube().get(0);
        rates.setupParentField();
        return rates;
    }
}
