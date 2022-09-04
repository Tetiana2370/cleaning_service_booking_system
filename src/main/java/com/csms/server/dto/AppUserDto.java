package com.csms.server.dto;


import com.csms.server.security.ApplicationUserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Data
// this is not the best solution:
// Lombok generates getters,setters etc at the compile time
// That's why we can't get rid of direct implementation of UserDetails methods.
// But we need those methods to process forms (e.g. register.html).
public class AppUserDto implements UserDetails {

    public Long idAppUser;
    @NotNull
    public long idAppUserRole;
    @NotNull
    @Size(min = 1, max = 100)
    public String firstName;
    @NotNull
    @Size(min = 1, max = 100)
    public String lastName;
    @NotNull
    @Size(min = 3, max = 12)
    public String phoneNumber;
    @NotNull
    @Email
    public String emailAddress;
    @NotNull
    public int failedLoginAttempts;
    public Date lastSucceedLogin;
    @NotNull
    public boolean active;
    @NotNull
    public int version;

    private Set<? extends GrantedAuthority> grantedAuthorities;
    public String password;
    public String username;

    public AppUserDto(
                    @JsonProperty("idAppUserRole") Long idAppUserRole,
                    @JsonProperty("firstName") String firstName,
                    @JsonProperty("lastName") String lastName,
                    @JsonProperty("phoneNumber") String phoneNumber,
                    @JsonProperty("emailAddress") String emailAddress,
                    @JsonProperty("username") String username,
                    @JsonProperty("password") String password
    ) {
        this.idAppUserRole = idAppUserRole;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.failedLoginAttempts = 0;
        this.lastSucceedLogin = null;
        this.active = true;
        this.version = 0;
        this.idAppUser = null;
        this.grantedAuthorities = ApplicationUserRole.ADMIN.getGrantedAuthorities();
    }

    public AppUserDto(){
        this.version = 0;
        this.idAppUser = null;
    }

    @Override
    public String toString() {
        return "AppUserDto{" +
                "idAppUser=" + idAppUser +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", failedLoginAttempts='" + failedLoginAttempts + '\'' +
                ", lastSucceedLogin='" + ((lastSucceedLogin != null) ? DateFormat.getDateTimeInstance().format(lastSucceedLogin) : "never") + '\'' +
                ", active=" + active +
                ", version=" + version +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
