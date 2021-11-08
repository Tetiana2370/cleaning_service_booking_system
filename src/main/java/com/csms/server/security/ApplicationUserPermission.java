package com.csms.server.security;

import org.springframework.security.core.GrantedAuthority;

public enum ApplicationUserPermission{
    APP_USER_READ("app_user:read"),
    APP_USER_WRITE("app_user:write"),
    ORDER_READ("order:read"),
    ORDER_WRITE("order:write"),
    ADDRESS_READ("address:read"),
    ADDRESS_WRITE("address:write");

    private final String permission;
    ApplicationUserPermission(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
