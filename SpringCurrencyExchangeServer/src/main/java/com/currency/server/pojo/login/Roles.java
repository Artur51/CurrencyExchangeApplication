package com.currency.server.pojo.login;

public enum Roles {
    USER,
    ;

    public String getRoleName() {
       return "ROLE_" + name();
    }
}
