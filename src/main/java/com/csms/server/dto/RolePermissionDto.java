package com.csms.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class RolePermissionDto {
    public Long idRolePermission;
    @NotNull
    public long idRole;
    @NotNull
    public long idPermission;
    @NotNull
    public boolean read;
    @NotNull
    public boolean write;

    public RolePermissionDto(
            @JsonProperty("idRole") Long idRole,
            @JsonProperty("idPermission") Long idPermission,
            @JsonProperty("read") Boolean read,
            @JsonProperty("write") Boolean write
    ) {
        this.idRole = idRole;
        this.idPermission = idPermission;
        this.read = read;
        this.write = write;
    }

    public RolePermissionDto(){
    }
}
