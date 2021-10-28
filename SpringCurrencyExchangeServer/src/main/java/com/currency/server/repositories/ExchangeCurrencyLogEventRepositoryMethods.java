package com.currency.server.repositories;

import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData;
import org.springframework.data.domain.Page;

import com.currency.server.pojo.exchange.CurrencyExchangeLogsPageRequest;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import org.springframework.data.domain.PageRequest;

public interface ExchangeCurrencyLogEventRepositoryMethods {
    public Page<ExchangeCurrencyLogEvent> findAll(
            ExchangeCurrencyLogEventQueryData queryData, PageRequest pageRequest);
}
