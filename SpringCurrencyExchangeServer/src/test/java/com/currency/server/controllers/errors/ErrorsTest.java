package com.currency.server.controllers.errors;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ErrorsTest {

    @Test
    public void testGetInvalidCurrencyProvidedExceptionMethodExceptionTextShouldContainTextExchangeCurrencyIsInvalid() {
        final String message = "Exchange currency is invalid: USD";
        assertTrue(Errors.getInvalidCurrencyProvidedException("USD").toString().contains(message));
    }

    @Test
    public void testGetLoginUserNotExceptionMethodExceptionTextShouldContainUserWithNameNotRegistered() {
        String message = "User with name USER is not registered.";
        assertTrue(Errors.getUserNotFoundException("USER").toString().contains(message));
    }
}