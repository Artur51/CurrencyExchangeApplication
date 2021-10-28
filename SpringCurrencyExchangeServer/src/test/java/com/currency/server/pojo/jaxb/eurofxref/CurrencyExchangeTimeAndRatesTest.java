package com.currency.server.pojo.jaxb.eurofxref;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.currency.server.JunitTestHelperMethods;
import com.currency.server.repositories.CurrencyExchangeTimeAndRatesRepository;
import com.currency.server.utils.TimeUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@SpringBootTest
@ActiveProfiles("test")
public class CurrencyExchangeTimeAndRatesTest {
    @Autowired
    XmlMapper xmlObjectMapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CurrencyExchangeTimeAndRatesRepository repository;

    @Test
    public void testReadXmlDataParseRecordTimeAndSaveIntoDatabaseAfterShouldRestoreSameDate()  throws IOException {
        CurrencyExchangeTimeAndRates data1 = JunitTestHelperMethods.readData(xmlObjectMapper, "/response10.14.xml");
        assertEquals(TimeUtils.getDate(2021, Calendar.OCTOBER, 14), data1.getTime());

        CurrencyExchangeTimeAndRates data2 = JunitTestHelperMethods.readData(xmlObjectMapper, "/response10.25.xml");
        assertEquals(TimeUtils.getDate(2021, Calendar.OCTOBER, 25), data2.getTime());

        assertEquals("14 Oct 2021 00:00:00 GMT", data1.getTime().toGMTString());
        assertEquals("25 Oct 2021 00:00:00 GMT", data2.getTime().toGMTString());

        assertEquals(0, repository.count());
        repository.saveAndFlush(data1);
        repository.saveAndFlush(data2);
        assertEquals(2, repository.count());

        CurrencyExchangeTimeAndRates databaseRecord1 = repository.findById(data1.getTime()).get();
        CurrencyExchangeTimeAndRates databaseRecord2 = repository.findById(data2.getTime()).get();

        assertEquals(TimeUtils.getDate(2021, Calendar.OCTOBER, 14), databaseRecord1.getTime());
        assertEquals(TimeUtils.getDate(2021, Calendar.OCTOBER, 25), databaseRecord2.getTime());
        assertEquals("14 Oct 2021 00:00:00 GMT", databaseRecord1.getTime().toGMTString());
        assertEquals("25 Oct 2021 00:00:00 GMT", databaseRecord2.getTime().toGMTString());
    }
}