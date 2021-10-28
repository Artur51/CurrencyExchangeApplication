package com.currency.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.currency.server.pojo.login.UserRegistrationData;

public interface RegisteredUserRepository extends JpaRepository<UserRegistrationData, String> {
    UserRegistrationData findByUsername(
            String name);

    boolean existsByUsername(
            String username);
}