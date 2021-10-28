package com.currency.server.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeTimeAndRates;

public interface CurrencyExchangeTimeAndRatesRepository extends JpaRepository<CurrencyExchangeTimeAndRates, Date> {
    public CurrencyExchangeTimeAndRates findTopByOrderByTimeDesc();
}
