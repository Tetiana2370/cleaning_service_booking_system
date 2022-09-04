package com.csms.server.security;

import com.csms.server.dto.AppUserDto;
import com.csms.server.exception.ObjectDoesNotExistException;
import com.csms.server.exception.ValidationException;
import com.csms.server.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class AuthProvider
        implements AuthenticationProvider
{
    private static final int ATTEMPTS_LIMIT = 5;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AppUserService appUserService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException
    {
        String username = authentication.getName();
        AppUserDto appUserDto = (AppUserDto) appUserService.loadUserByUsername(username);
        if (appUserDto == null) {
            throw new AuthenticationCredentialsNotFoundException("Invalid credentials");
        }

        String enteredPassword = authentication.getCredentials().toString();

        if (passwordEncoder.matches(enteredPassword, appUserDto.getPassword())) {
            if (appUserDto.failedLoginAttempts <= ATTEMPTS_LIMIT) {
                appUserDto.failedLoginAttempts = 0;
                appUserDto.lastSucceedLogin = new Date();
            } else {
                throw new AuthenticationServiceException("User is blocked due to reaching login attempts limit");
            }
        } else {
            if (appUserDto.failedLoginAttempts <= ATTEMPTS_LIMIT) {
                appUserDto.failedLoginAttempts += 1;
                throw new AuthenticationServiceException("Wrong password");
            } else {
                throw new AuthenticationServiceException("User is blocked due to reaching login attempts limit");
            }
        }
        updateAppUser(appUserDto);
        return new UsernamePasswordAuthenticationToken(
              username, enteredPassword, new ArrayList<>(ApplicationUserRole.ADMIN.getGrantedAuthorities()));
    }

    protected void updateAppUser(AppUserDto appUserDto)
    {
        try {
            appUserService.update(appUserDto);
        } catch (ObjectDoesNotExistException e) {
            throw new AuthenticationCredentialsNotFoundException("User doesn't exist no more");
        } catch (ValidationException impossible) {
            impossible.printStackTrace();
            // this should never happen
        }
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return true;
//       return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
