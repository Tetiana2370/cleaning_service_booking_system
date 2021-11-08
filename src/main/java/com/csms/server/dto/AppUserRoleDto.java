package com.csms.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class AppUserRoleDto {
    public Long idAppUserRole;
    @NotNull
    @Size(min = 1, max = 100)
    public String name;
    @NotNull
    public boolean admin;
    @NotNull
    public boolean active;

    public AppUserRoleDto(
            @JsonProperty("name") String name,
            @JsonProperty("admin") Boolean admin
    ) {
        this.name = name;
        this.admin = admin;
        this.active = true;
    }

    public AppUserRoleDto(){
        this.admin = false;
        this.active = true;
    }

    /*public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }*/

}
