package com.currency.server.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.currency.server.controllers.common.AbstractRegistrationControllerTestHelper;
import com.currency.server.services.UserRegistrationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationControllerRegistrationTest extends AbstractRegistrationControllerTestHelper {
    @MockBean
    UserRegistrationService userRegistrationService;

    @Autowired
    MockMvc mockMvc;
    String REGISTRATION_URL = "/registration";

    private MockHttpServletRequestBuilder createRegistrationPostRequest(
            String secondaryValidationUserPassword) {
        String userName = TEST_USERNAME;
        String userPassword = TEST_USER_PASSWORD;
        return createRegisterationPostRequest(userName, userPassword, secondaryValidationUserPassword);
    }

    private MockHttpServletRequestBuilder createRegistrationPostRequest() {
        return createRegisterationPostRequest(TEST_USERNAME, TEST_USER_PASSWORD, TEST_USER_PASSWORD);
    }


    private MockHttpServletRequestBuilder createRegisterationPostRequest(String userName, String userPassword, String secondaryValidationUserPassword) {
        return post(REGISTRATION_URL)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .param("username", userName)//
                .param("password", userPassword)//
                .param("confirmPassword", secondaryValidationUserPassword)//
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    }

    @Test
    public void testProvidedPasswordsNotEqualsShouldReturnError() throws Exception {
        mockMvc.perform(createRegistrationPostRequest("wrongPassword")) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(
                        "Bad credentials; please make sure provided passwords are match")));
        Mockito.verify(userRegistrationService, Mockito.times(0)).addUser(Mockito.any());
    }

    @Test
    public void testUserAlreadyRegisteredShouldReturnError() throws Exception {
        Mockito.when(userRegistrationService.isUserExists(Mockito.any())).thenReturn(true);

        mockMvc.perform(createRegistrationPostRequest()) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(
                        "User already exists.")));
        Mockito.verify(userRegistrationService, Mockito.times(0)).addUser(Mockito.any());
        Mockito.verify(userRegistrationService, Mockito.times(1)).isUserExists(Mockito.any());
    }

    @Test
    public void tesShortPasswordUsedShouldReturnError() throws Exception {
        String data = "1";
        mockMvc.perform(createRegisterationPostRequest(TEST_USERNAME, data, data)) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())
                .andExpect(content().string(//
                        containsString("Password must be not less then 6 and not longer then 100 symbols length.")));
    }

    @Test
    public void testInvalidLongPasswordLengthShouldReturnError() throws Exception {
        String data = new Random().ints(2000, 'a', 'z').mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
        mockMvc.perform(createRegisterationPostRequest(TEST_USERNAME, data, data)) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(//
                        containsString("Password must be not less then 6 and not longer then 100 symbols length.")));
    }

    @Test
    public void testUserNameParameterWithCorrectLengthShouldPass() throws Exception {
        String data = new Random().ints(20, 'a', 'z').mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
        mockMvc.perform(createRegisterationPostRequest(data, TEST_USER_PASSWORD, TEST_USER_PASSWORD)) //
                .andDo(print()) //
                .andExpect(status().isOk())//
        ;
    }

    @Test
    public void testWhenUserNameLengthTooShortShouldReturnError() throws Exception {
        String data = new Random().ints(2, 'a', 'z').mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
        mockMvc.perform(createRegisterationPostRequest(data, TEST_USER_PASSWORD, TEST_USER_PASSWORD)) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(//
                        containsString("User name must be not less then 4 and not longer then 100 symbols length.")));
    }

    @Test
    public void testUserNameLengthTooLongShouldReturnError() throws Exception {
        String data = new Random().ints(2000, 'a', 'z').mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
        mockMvc.perform(createRegisterationPostRequest(data, TEST_USER_PASSWORD, TEST_USER_PASSWORD)) //
                .andDo(print()) //
                .andExpect(status().isBadRequest())//
                .andExpect(content().string(//
                        containsString("User name must be not less then 4 and not longer then 100 symbols length.")));
    }
}
