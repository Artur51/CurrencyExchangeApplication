package com.currency.server.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlDateAdapter extends XmlAdapter<String, Date> {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    {
        dateFormat.setTimeZone(TimeUtils.defaultTimeZone);
    }
    @Override
    public synchronized Date unmarshal(
            String v) throws Exception {
        final Date parse = dateFormat.parse(v);
        return parse;
    }

    @Override
    public synchronized String marshal(
            Date v) throws Exception {
        return dateFormat.format(v);
    }
}