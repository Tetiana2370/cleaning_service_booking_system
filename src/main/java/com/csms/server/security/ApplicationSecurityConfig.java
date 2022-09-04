package com.csms.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig
        extends WebSecurityConfigurerAdapter
{
    @Autowired
    AuthProvider authProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index", "/register")
                .permitAll()
                //.antMatchers("/com/csms/server/api/**").hasRole(ApplicationUserRole.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .formLogin().defaultSuccessUrl("/customerOrders")
                .and()
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true).permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
    {
        auth.authenticationProvider(authProvider);
    }
}
