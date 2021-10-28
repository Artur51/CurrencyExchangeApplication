package com.currency.server.controllers.common;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.currency.server.pojo.login.UserRegistrationData;
import com.currency.server.repositories.RegisteredUserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractRegistrationControllerTestHelper {
    public static String TEST_USER_PASSWORD = "password1";
    public static String TEST_USERNAME = "userName1";

   
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected RegisteredUserRepository repository;

    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    private void beforEach() {
        when(repository.saveAndFlush(Mockito.any(UserRegistrationData.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    public UserRegistrationData getRegisteredUser() {
        return getRegisteredUser(false);
    }

    public UserRegistrationData getRegisteredUser(
            boolean encodedPassword) {
        final UserRegistrationData user = new UserRegistrationData();
        user.setUsername(AbstractRegistrationControllerTestHelper.TEST_USERNAME);
        if (encodedPassword) {
            user.setPassword(passwordEncoder.encode(AbstractRegistrationControllerTestHelper.TEST_USER_PASSWORD));
        } else {
            user.setPassword(AbstractRegistrationControllerTestHelper.TEST_USER_PASSWORD);
        }
        return user;
    }
}
