package com.currency.server.pojo.login;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
public class UserRegistrationData {
    @Id
    @GeneratedValue
    Long id;

    @Size(min = 4, max = 100, message = "User name must be not less then {min} and not longer then {max} symbols length.")
    @Column(unique = true, nullable = false)
    @NotNull
    private String username;

    @Size(min = 6, max = 100, message = "Password must be not less then {min} and not longer then {max} symbols length.")
    @Column(nullable = false)
    @NotNull
    private String password;

    public UserRegistrationData() {
    }

    public UserRegistrationData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(
            Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserRegistrationData other = (UserRegistrationData) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}