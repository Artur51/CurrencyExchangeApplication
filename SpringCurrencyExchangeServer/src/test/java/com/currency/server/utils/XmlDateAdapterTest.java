package com.currency.server.utils;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class XmlDateAdapterTest {

    @Test
    public void testUnmarshalMethodShouldParseStringToDate() throws Exception {
        final XmlDateAdapter xmlDateAdapter = new XmlDateAdapter();
        final Date date = xmlDateAdapter.unmarshal("2021-06-22");
        assertEquals(TimeUtils.getDate(2021, Calendar.JUNE, 22), date);
    }

    @Test
    public void testMarshalMethodShouldParseDateToString() throws Exception {
        final XmlDateAdapter xmlDateAdapter = new XmlDateAdapter();
        final Date date = TimeUtils.getDate(2021, Calendar.JUNE, 22);
        String result = xmlDateAdapter.marshal(date);
        assertEquals("2021-06-22", result);
    }
}