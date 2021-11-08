package com.csms.server.security;

import org.assertj.core.util.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.csms.server.security.ApplicationUserPermission.*;

public enum ApplicationUserRole{
    ADMIN(Sets.newHashSet(List.of(APP_USER_WRITE, APP_USER_READ, ORDER_WRITE, ADDRESS_WRITE))),
    ADMIN_TRAINEE(Sets.newHashSet(List.of(APP_USER_READ))),
    CUSTOMER(Sets.newHashSet(List.of(APP_USER_READ, ORDER_WRITE, ADDRESS_WRITE))),
    EMPLOYEE (Sets.newHashSet(List.of(APP_USER_WRITE, ORDER_READ, ADDRESS_READ)));

    private final Set<ApplicationUserPermission> permissions;
    ApplicationUserRole(Set<ApplicationUserPermission> permissions){
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
