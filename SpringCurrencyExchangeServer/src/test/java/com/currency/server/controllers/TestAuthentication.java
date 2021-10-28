package com.currency.server.controllers;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.currency.server.pojo.login.UserDetailsImpl;
import com.currency.server.pojo.login.UserRegistrationData;

public class TestAuthentication implements Authentication {
    private static final long serialVersionUID = 1L;

    UserDetailsImpl details;
    private boolean isAuthenticated;

    public TestAuthentication(UserDetailsImpl details) {
        this.details = details;
    }

    public TestAuthentication(UserRegistrationData details) {
        this(new UserDetailsImpl(details));
    }

    @Override
    public String getName() {
        return details.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return details.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getDetails() {
        return details;
    }

    @Override
    public Object getPrincipal() {
        return details;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(
            boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

}