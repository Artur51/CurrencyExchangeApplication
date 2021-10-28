package com.currency.server.pojo.exchange;

import lombok.Data;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class CurrencyExchangeRequest {

    @Digits(integer = Integer.MAX_VALUE, fraction = 3)
    @Positive
    @Column(precision = 19, scale = 4)
    @NotNull
    BigDecimal currencyAmountSold;
    /**
     * User currency sold.
     */
    @NotNull
    String currencySold;
    /**
     * User currency purchased.
     */
    @NotNull
    String currencyPurchase;
}
