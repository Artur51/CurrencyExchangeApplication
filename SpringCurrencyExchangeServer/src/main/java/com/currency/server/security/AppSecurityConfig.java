package com.currency.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.currency.server.repositories.RegisteredUserRepository;
import com.currency.server.security.jwt.AuthTokenFilter;
import com.currency.server.security.jwt.JwtAuthenticationEntryPoint;
import com.currency.server.services.UserRegistrationService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    UserRegistrationService registrationService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(registrationService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(
            WebSecurity web) throws Exception {
        // h2 database console
        web.ignoring().antMatchers("/db/**");
    }

    @Override
    protected void configure(
            HttpSecurity http) throws Exception {
        http.headers().cacheControl();

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)//
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
                .and().authorizeRequests()
                .antMatchers("/db**").permitAll()//
                .antMatchers("/login", "/registration", "/logout").permitAll()//
                .anyRequest().authenticated();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}