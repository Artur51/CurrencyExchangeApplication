package com.currency.server.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData;

import java.util.Objects;

@Repository
public class ExchangeCurrencyLogEventRepositoryMethodsImpl implements ExchangeCurrencyLogEventRepositoryMethods {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Page<ExchangeCurrencyLogEvent> findAll(
            ExchangeCurrencyLogEventQueryData queryData,
            PageRequest pageRequest) {
        Objects.requireNonNull(queryData, "Query data must not be null");
        ExchangeCurrencyLogEventDataFetcher fetcher = new ExchangeCurrencyLogEventDataFetcher();
        return fetcher.getResultPage(queryData, entityManager, pageRequest);
    }
}