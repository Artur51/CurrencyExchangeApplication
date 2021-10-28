package com.currency.server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.currency.server.pojo.login.TokenResponse;
import com.currency.server.pojo.login.UserDetailsImpl;
import com.currency.server.pojo.login.UserRegistrationData;
import com.currency.server.repositories.RegisteredUserRepository;
import com.currency.server.security.jwt.JsonTokenGenerator;

@Service
public class UserRegistrationService implements UserDetailsService {
    @Autowired
    private RegisteredUserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(
            String username) throws UsernameNotFoundException {
        final UserRegistrationData user = findUser(username);
        if (user != null) {
            return new UserDetailsImpl(user);
        }
        throw new UsernameNotFoundException("Cannot find registered user with name: " + username);
    }

    public UserRegistrationData addUser(
            UserRegistrationData newUser) {
        String encoded = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encoded);
        return userRepository.saveAndFlush(newUser);
    }

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JsonTokenGenerator jwtUtils;

    public boolean isUserExists(
            UserRegistrationData user) {
        return userRepository.existsByUsername(user.getUsername());
    }

    public UserRegistrationData findUser(String username) {
        return userRepository.findByUsername(username);
    }


    public TokenResponse loginUser(
            UserRegistrationData loginUser) {

        Authentication authentication = authenticationManager.authenticate(//
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJsonToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getRolesList();

        final TokenResponse token = new TokenResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles);
        return token;
    }
}