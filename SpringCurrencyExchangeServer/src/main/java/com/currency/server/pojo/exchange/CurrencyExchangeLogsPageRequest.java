package com.currency.server.pojo.exchange;

import lombok.Data;

@Data
public class CurrencyExchangeLogsPageRequest {

    ExchangeCurrencyLogEventQueryData queryData;
    PageRequestData pageRequestData;
}
