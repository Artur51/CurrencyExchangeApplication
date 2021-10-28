package com.currency.server.repositories;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import com.currency.server.pojo.exchange.ExchangeCurrencyLogEvent;
import com.currency.server.pojo.exchange.ExchangeCurrencyLogEventQueryData;

public class ExchangeCurrencyLogEventDataFetcher {

    StringBuilder query = new StringBuilder();
    HashMap<String, Object> parameters = new HashMap<>();
    private ExchangeCurrencyLogEventQueryData queryData;

    public Page<ExchangeCurrencyLogEvent> getResultPage(
            ExchangeCurrencyLogEventQueryData queryData,
            EntityManager entityManager,
            PageRequest pageRequest) {
        setQueryObject(queryData);
        final List<ExchangeCurrencyLogEvent> list = getResultList(entityManager, pageRequest);
        final Long total = getResultTotalCount(entityManager);
        return new PageImpl<>(list, pageRequest, total == null ? 0 : total);
    }

    String getQuery() {
        return query.toString();
    }

    HashMap<String, Object> getParameters() {
        return parameters;
    }

    void setQueryObject(
            ExchangeCurrencyLogEventQueryData queryData) {
        this.queryData = queryData;
    }

    public List<ExchangeCurrencyLogEvent> getResultList(
            EntityManager entityManager,
            PageRequest pageRequest) {
        generateObjectsQuery();
        String query = getQuery();
        final TypedQuery<ExchangeCurrencyLogEvent> typedQuery = entityManager.createQuery(//
                query, ExchangeCurrencyLogEvent.class);
        getParameters().forEach(typedQuery::setParameter);
        if (pageRequest != null) {
            typedQuery.setFirstResult((int) pageRequest.getOffset());
            typedQuery.setMaxResults(pageRequest.getPageSize());
        }
        return typedQuery.getResultList();
    }

    public Long getResultTotalCount(
            EntityManager entityManager) {
        generateTotalCountQuery();
        String query = getQuery();
        final TypedQuery<Long> typedQuery = entityManager.createQuery(//
                query, Long.class);
        getParameters().forEach(typedQuery::setParameter);
        return typedQuery.getSingleResult();
    }

    void generateObjectsQuery() {
        reset();
        initSelectQuery();
        generateWhereClause(queryData);

        addOrderBy(queryData);
    }

    void initSelectQuery() {
        query.append(" SELECT ex FROM ExchangeCurrencyLogEvent ex ");
        query.append(" JOIN FETCH ex.currencySold currency_sold ");
        query.append(" JOIN FETCH ex.currencyPurchase currency_purchase ");
        query.append(" JOIN FETCH ex.user user ");
    }

    private void generateTotalCountQuery() {
        reset();
        query.append("SELECT COUNT (c) FROM ExchangeCurrencyLogEvent c WHERE c IN (");
        query.append(" SELECT ex FROM ExchangeCurrencyLogEvent ex ");
        query.append(" JOIN ex.currencySold currency_sold ");
        query.append(" JOIN ex.currencyPurchase currency_purchase ");
        query.append(" JOIN ex.user user ");
        generateWhereClause(queryData);
        query.append(")");
    }

    public void generateWhereClause(
            ExchangeCurrencyLogEventQueryData queryData) {
        query.append(" WHERE ");

        boolean added = addEqualsQuery("currency_sold.currency", "currencySold", queryData.getCurrencySold(), false);
        addEqualsQuery("currency_purchase.currency", "currencyPurchase", queryData.getCurrencyPurchase(), added);
        
        
        addLikeQuery("ex.userName", "userNameLike", queryData.getUserNameLike());

        addRangeQuery("ex.currencyAmountSold", "currencyAmountSoldMin", "currencyAmountSoldMax", //
                queryData.getCurrencyAmountSoldMin(), //
                queryData.getCurrencyAmountSoldMax());

        addRangeQuery("ex.currencyAmountReceived", "currencyAmountReceivedMin", "currencyAmountReceivedMax", //
                queryData.getCurrencyAmountReceivedMin(), //
                queryData.getCurrencyAmountReceivedMax());

        addRangeQuery("ex.createdAt", "createdAtStart", "createdAtEnd", //
                queryData.getCreatedAtStart(), //
                queryData.getCreatedAtEnd());
    }

    public void addOrderBy(
            ExchangeCurrencyLogEventQueryData queryData) {
        if (queryData.getOrderBy() != null) {
            query.append(" ORDER BY ex.").append(queryData.getOrderBy().name()).append(" ");
            if (queryData.getOrderBy().isAscendingSortOrder) {
                query.append(" ASC ");
            } else {
                query.append(" DESC ");
            }
        }
    }

    public void reset() {
        query.setLength(0);
        parameters.clear();
    }

    boolean addEqualsQuery(
            String compareWithField,
            String filedName,
            String fieldValue,
            boolean addAnd) {
        if (StringUtils.hasText(fieldValue)) {
            if (addAnd) {
                query.append(" AND ");
            }
            query.append(" ").append(compareWithField).append(" = :").append(filedName).append(" ");
            addParameter(filedName, fieldValue);
            return true;
        }
        return false;
    }

    void addLikeQuery(
            String compareWithField,
            String filedName,
            String fieldValue) {
        if (StringUtils.hasText(fieldValue)) {
            query.append(" ").append(compareWithField).append(" like %:").append(filedName).append("% ");
            addParameter(filedName, fieldValue);
        }
    }

    void addRangeQuery(
            String compareWithField,
            String rangeMinFieldName,
            String rangeMaxFieldName,
            Object rangeMinValue,
            Object rangeMaxValue) {
        if (rangeMinValue != null && rangeMaxValue != null) {
            query.append(" ").append(compareWithField).append(" BETWEEN :").append(rangeMinFieldName)//
                    .append(" AND :").append(rangeMaxFieldName).append(" ");

            addParameter(rangeMinFieldName, rangeMinValue);
            addParameter(rangeMaxFieldName, rangeMaxValue);
        } else if (rangeMinValue != null) {
            query.append(" ").append(compareWithField).append(" > :").append( rangeMinFieldName).append(" ");
            addParameter(rangeMinFieldName, rangeMinValue);
        } else if (rangeMaxValue != null) {
            query.append(" ").append(compareWithField).append(" < :").append( rangeMaxValue).append(" ");
            addParameter(rangeMaxFieldName, rangeMaxValue);
        }
    }

    public void addParameter(
            String key,
            Object value) {
        parameters.put(key, value);
    }

}