package com.currency.server.pojo.exchange;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.currency.server.pojo.jaxb.eurofxref.CurrencyExchangeRate;
import com.currency.server.pojo.login.UserRegistrationData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
@JsonIgnoreProperties({"password", "id", "user"})
@EntityListeners(AuditingEntityListener.class)

public class ExchangeCurrencyLogEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    UserRegistrationData user;

    @JsonProperty
    public String getUserName() {
        return user == null ? null : user.getUsername();
    }

    @Column(precision = 19, scale = 5, updatable = false)
    BigDecimal currencyAmountSold;
    /**
     * User currency sold.
     */
    @OneToOne
    @JoinColumn(name = "currencySold_id")
    CurrencyExchangeRate currencySold;
    /**
     * User currency purchased.
     */
    @OneToOne
    @JoinColumn(name = "currencyPurchase_id")
    CurrencyExchangeRate currencyPurchase;

    @Column(precision = 19, scale = 5, updatable = false)
    BigDecimal currencyAmountReceived;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @CreatedDate
    Date createdAt;

    @Override
    public String toString() {
        return "ExchangeCurrencyLogEvent{" +
                "id=" + id +
                ", user=" + user +
                ", currencyAmountSold=" + currencyAmountSold +
                ", currencySold=" + currencySold +
                ", currencyPurchase=" + currencyPurchase +
                ", currencyAmountReceived=" + currencyAmountReceived +
                ", createdAt=" + createdAt +
                '}';
    }
}