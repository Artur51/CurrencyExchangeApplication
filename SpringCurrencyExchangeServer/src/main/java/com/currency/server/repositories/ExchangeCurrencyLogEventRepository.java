package com.currency.server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;

public interface ExchangeCurrencyLogEventRepository
        extends JpaRepository<ExchangeCurrencyLogEvent, Integer>, ExchangeCurrencyLogEventRepositoryMethods {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {
            "user", "currencySold", "currencyPurchase"
    })
    @Override
    List<ExchangeCurrencyLogEvent> findAll();
}
