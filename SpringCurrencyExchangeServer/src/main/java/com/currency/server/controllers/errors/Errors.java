package com.currency.server.controllers.errors;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Errors {


    public static class BadRequestResponseStatusException extends ResponseStatusException {
        private static final long serialVersionUID = -4823802926576279398L;

        public BadRequestResponseStatusException(String reason) {
            super(HttpStatus.BAD_REQUEST, reason);
        }

        @Override
        public String getMessage() {
            String msg = this.getReason() != null ? this.getReason() : "";
            return NestedExceptionUtils.buildMessage(msg, getCause());
        }
    }

    public static final RuntimeException userAlreadyRegistered = new BadRequestResponseStatusException("User already exists.");
    public static final RuntimeException providedPasswordsMissMatch = new BadRequestResponseStatusException("Bad credentials; please make sure provided passwords are match.");
    public static final RuntimeException currencyExchangeRateDataUnavailable = new BadRequestResponseStatusException("Exchange rate data is not available");
    public static final RuntimeException sameTypeCurrencies = new BadRequestResponseStatusException("Exchange currencies must be not the same type");

    public static final RuntimeException loginUserIncorrectPassword = new BadRequestResponseStatusException("Please make sure provided password is correct.");

    public static RuntimeException getInvalidCurrencyProvidedException(
            String currency) {
        return new BadRequestResponseStatusException("Exchange currency is invalid: " + currency);
    }

    public static RuntimeException getUserNotFoundException(String userName) {
        return new BadRequestResponseStatusException("User with name " + userName + " is not registered.");
    }

}