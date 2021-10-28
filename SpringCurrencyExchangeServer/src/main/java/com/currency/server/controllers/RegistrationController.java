package com.currency.server.controllers;

import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.cors.CorsConfiguration;

import com.currency.server.controllers.errors.Errors;
import com.currency.server.pojo.login.TokenResponse;
import com.currency.server.pojo.login.UserRegistrationData;
import com.currency.server.services.UserRegistrationService;

@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.GET, RequestMethod.POST })
@Validated
@CrossOrigin(
        origins = { "*" },
        allowedHeaders = CorsConfiguration.ALL
)
public class RegistrationController {
    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Valid
            UserRegistrationData loginRequest,
            BindingResult errors) {

        final TokenResponse token = userRegistrationService.loginUser(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registration")
    public HttpStatus registerUser(
            @RequestParam @NotEmpty String confirmPassword,
            @Valid UserRegistrationData newUser,
            BindingResult errors) {
        if (userRegistrationService.isUserExists(newUser)) {
            throw Errors.userAlreadyRegistered;
        }
        if (Objects.equals(newUser.getPassword(), confirmPassword)) {
            userRegistrationService.addUser(newUser);
            return HttpStatus.OK;
        }

        throw Errors.providedPasswordsMissMatch;
    }

    @GetMapping(value = "/logout")
    @PostMapping(value = "/logout")
    public HttpStatus logout(
            SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);
        session.setComplete();
        return HttpStatus.OK;
    }
}