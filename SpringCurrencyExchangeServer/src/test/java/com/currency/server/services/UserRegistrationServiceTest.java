package com.currency.server.services;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import com.currency.server.controllers.common.AbstractRegistrationControllerTestHelper;
import com.currency.server.pojo.login.UserRegistrationData;

public class UserRegistrationServiceTest extends AbstractRegistrationControllerTestHelper {

    @Autowired
    UserRegistrationService userRegistrationService;

    @Test
    public void testShouldAddUserWithEncodePasswordIntoDatabase() {
        final UserRegistrationData userData = getRegisteredUser(false);
        final String rawPassword = userData.getPassword();
        final UserRegistrationData result = userRegistrationService.addUser(userData);

        final String resultPassword = result.getPassword();
        assertTrue(passwordEncoder.matches(rawPassword, resultPassword));
        assertNotEquals(rawPassword, resultPassword);
    }

    @Test
    public void testShouldFindExistedUser() {
        final UserRegistrationData userData = getRegisteredUser(false);

        when(repository.existsByUsername(userData.getUsername())).thenReturn(true);

        boolean result = userRegistrationService.isUserExists(userData);
        assertTrue(result);
    }

    @Test
    public void testShouldNotFindNotExistedUser() {
        when(repository.findByUsername(Mockito.anyString())).thenReturn(null);
        final UserRegistrationData found = userRegistrationService.findUser("not existed user name");
        assertNull(found);
    }

    @Test
    public void testShouldFindUserByUserNameInDatabase() {
        final UserRegistrationData userData = getRegisteredUser(false);

        final UserRegistrationData databaseUserData = getRegisteredUser(true);
        when(repository.findByUsername(userData.getUsername())).thenReturn(databaseUserData);

        final UserRegistrationData found = userRegistrationService.findUser(userData.getUsername());

        assertNotNull(found);
        assertEquals(databaseUserData, found);
    }

    @Test
    public void testMethodVerifyLoginUserUserWithCorrectPasswordShouldPass() {
        final UserRegistrationData data = getRegisteredUser();
        when(repository.findByUsername(data.getUsername())).thenReturn(getRegisteredUser(true));

        try {
            userRegistrationService.loginUser(data);
            assertTrue(true, "verify user login user pass");
        } catch (Exception e) {
            assertTrue(false, "verify user login user failed with exception " + e);
        }
    }

    @Test
    public void testNotExistedInDatabaseUserShouldFailWithException() {
        final UserRegistrationData data = new UserRegistrationData();
        when(repository.findByUsername(data.getUsername())).thenReturn(null);

        data.setUsername("USER");
        data.setPassword("some password");
        BadCredentialsException thrown = Assertions.assertThrows(BadCredentialsException.class, () -> {
            userRegistrationService.loginUser(data);
        });
    }

}