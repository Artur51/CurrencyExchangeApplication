package com.currency.server.pojo.exchange;

import lombok.Data;

import java.util.Date;

import org.springframework.data.domain.Sort;

import com.currency.server.utils.Utils;

@Data
public class ExchangeCurrencyLogEventQueryData {

    private String currencySold;
    private String currencyPurchase;
    private Integer currencyAmountSoldMin;
    private Integer currencyAmountSoldMax;

    private Integer currencyAmountReceivedMin;
    private Integer currencyAmountReceivedMax;

    private Date createdAtStart;
    private Date createdAtEnd;
    private String userNameLike;

    private OrderBy orderBy = OrderBy.createdAt;

    public Integer getCurrencyAmountSoldMin() {
        return Utils.min(currencyAmountSoldMin, currencyAmountSoldMax);
    }

    public Integer getCurrencyAmountSoldMax() {
        return Utils.max(currencyAmountSoldMin, currencyAmountSoldMax);
    }

    public Integer getCurrencyAmountReceivedMin() {
        return Utils.min(currencyAmountReceivedMin, currencyAmountReceivedMax);
    }

    public Integer getCurrencyAmountReceivedMax() {
        return Utils.max(currencyAmountReceivedMin, currencyAmountReceivedMax);
    }

    public Date getCreatedAtStart() {
        return Utils.min(createdAtStart, createdAtEnd);
    }

    public Date getCreatedAtEnd() {
        return Utils.max(createdAtStart, createdAtEnd);
    }

    public boolean isEmpty() {
        boolean notEmpty = false;
        notEmpty |= currencySold != null;
        notEmpty |= currencyPurchase != null;
        notEmpty |= currencyAmountSoldMin != null;
        notEmpty |= currencyAmountSoldMax != null;

        notEmpty |= currencyAmountReceivedMin != null;
        notEmpty |= currencyAmountReceivedMax != null;

        notEmpty |= createdAtStart != null;
        notEmpty |= createdAtEnd != null;
        notEmpty |= userNameLike != null;
        return !notEmpty;
    }

    public enum OrderBy {
        createdAt(false),
        currencyAmountSold(true),
        currencyAmountReceived(true);

        public final boolean isAscendingSortOrder;

        OrderBy(boolean isAscendingSortOrder) {
            this.isAscendingSortOrder = isAscendingSortOrder;
        }

        public Sort getSort() {
            if (isAscendingSortOrder) {
                return Sort.by(name()).ascending();
            }
            return Sort.by(name()).descending();
        }
    }
}