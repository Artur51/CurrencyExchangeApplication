package com.currency.server.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.currency.server.controllers.common.AbstractRegistrationControllerTestHelper;
import com.currency.server.pojo.login.UserRegistrationData;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegistrationControllerLoginTest extends AbstractRegistrationControllerTestHelper {

    static String LOGIN_URL = "/login";
    @Autowired
    protected RegistrationController registrationController;

    public static MockHttpServletRequestBuilder createLoginPostRequest(
            String userName,
            String userPassword) {
        return post(LOGIN_URL)//
                .with(SecurityMockMvcRequestPostProcessors.csrf())//
                .param("username", userName)//
                .param("password", userPassword)//
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    }

    @Test
    public void testWhenUserNameLengthTooShortShouldReturnError() throws Exception {
        String data = new Random().ints(2, 'a', 'z').mapToObj(i -> String.valueOf((char) i)).collect(Collectors.joining());
        mockMvc.perform(createLoginPostRequest(data, AbstractRegistrationControllerTestHelper.TEST_USER_PASSWORD)) //
                .andDo(print()) //
                .andExpect(status().is4xxClientError())//
                .andExpect(content().string(//
                        containsString("User name must be not less then 4 and not longer then 100 symbols length.")));
    }

    @MockBean
    AuthenticationManager authenticationManager;

    @Test
    public void testLoginForExistedUserShouldWork() throws Exception {

        final UserRegistrationData databaseUser = getRegisteredUser(true);
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new TestAuthentication(databaseUser));
        when(repository.findByUsername(databaseUser.getUsername())).thenReturn(databaseUser);
        when(repository.existsByUsername(databaseUser.getUsername())).thenReturn(true);

        final UserRegistrationData user = getRegisteredUser();
        mockMvc.perform(createLoginPostRequest(user.getUsername(), user.getPassword())) //
                .andDo(print()) //
                .andExpect(status().isOk());
    }


}