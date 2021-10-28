package com.currency.server.pojo.exchange;

import lombok.Data;

import org.springframework.data.domain.Page;

@Data
public class ExchangeCurrencyLogEvents {
    PageRequestData pageRequestData;
    Page<ExchangeCurrencyLogEvent> data;
}
