package com.currency.server.pojo.login;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    public static final SimpleGrantedAuthority userRole = new SimpleGrantedAuthority(Roles.USER.getRoleName());
    public static final List<SimpleGrantedAuthority> authorities = Collections.singletonList(userRole);

    UserRegistrationData databaseUser;

    public UserDetailsImpl() {
    }

    public UserDetailsImpl(UserRegistrationData databaseUser) {
        this.databaseUser = databaseUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public List<String> getRolesList() {
        return authorities.stream().map(item -> item.getAuthority()).collect(Collectors.toList());
    };


    public UserRegistrationData getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(
            UserRegistrationData databaseUser) {
        this.databaseUser = databaseUser;
    }

    @Override
    public String getPassword() {
        return databaseUser.getPassword();
    }

    @Override
    public String getUsername() {
        return databaseUser.getUsername();
    }

    public Long getId() {
        return databaseUser.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((databaseUser == null) ? 0 : databaseUser.hashCode());
        return result;
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
        UserDetailsImpl other = (UserDetailsImpl) obj;
        if (databaseUser == null) {
            if (other.databaseUser != null) {
                return false;
            }
        } else if (!databaseUser.equals(other.databaseUser)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserDetailsImpl databaseUser=" + databaseUser;
    }

}