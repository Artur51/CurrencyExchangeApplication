package com.currency.server.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindingResult;

import com.currency.server.pojo.exchange.CurrencyExchangeRequest;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeRate;
import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;

public class Utils {
    public static List<String> collectErrors(
            BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors().stream().map(MessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return errors;
    }

    public static <T> T getRandomValue(
            final List<T> list) {
        int index = ThreadLocalRandom.current().nextInt(list.size());
        T item = list.get(index);
        return item;
    }

    public static CurrencyExchangeRequest getRandomExchangeRequest(
            CurrencyExchangeTimeAndRates actualData) {

        CurrencyExchangeRate any = Utils.getRandomValue(actualData.getExchangeRates());
        CurrencyExchangeRate any2 = null;
        while (any.equals(any2) || any2 == null) {
            any2 = Utils.getRandomValue(actualData.getExchangeRates());
            if (any2.getCurrency().equals(any.getCurrency())) {
                any2 = null;
            }
        }

        return getRandomExchangeRequest(any, any2);
    }

    public static CurrencyExchangeRequest getRandomExchangeRequest(
            CurrencyExchangeRate any,
            CurrencyExchangeRate any2) {
        Random r = new Random();
        int value = r.nextInt(50) * 10;

        CurrencyExchangeRequest request = new CurrencyExchangeRequest();
        request.setCurrencySold(any.getCurrency());
        request.setCurrencyPurchase(any2.getCurrency());
        request.setCurrencyAmountSold(new BigDecimal(String.valueOf(value)));
        return request;
    }

    public static Date max(
            Date min,
            Date max) {
        if (min == null) {
            return max;
        }
        if (max == null) {
            return min;
        }
        return max.getTime() > min.getTime()//
                ? max
                : min;
    }

    public static Date min(
            Date min,
            Date max) {
        if (min == null) {
            return max;
        }
        if (max == null) {
            return min;
        }
        return min.getTime() < max.getTime() ? min : max;
    }

    public static Integer max(
            Integer min,
            Integer max) {
        if (min == null) {
            return max;
        }
        if (max == null) {
            return min;
        }
        return Math.max(min, max);
    }

    public static Integer min(
            Integer min,
            Integer max) {
        if (min == null) {
            return max;
        }
        if (max == null) {
            return min;
        }
        return Math.min(min, max);
    }
}