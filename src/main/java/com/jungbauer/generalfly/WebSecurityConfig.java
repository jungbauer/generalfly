package com.jungbauer.generalfly;

import com.jungbauer.generalfly.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

    /**
     * Admin role SecurityFilterChain
     */
    @Bean
    @Order(1)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        String[] approvalsPaths = { "/actuator/**", "/user/**" };
        http
            .securityMatcher(approvalsPaths)
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().hasRole("ADMIN") // "ROLE_" is auto prepended, apparently hasAuthority("ROLE_ADMIN") can be used.
            )
            .formLogin(form -> form
                    .defaultSuccessUrl("/", false).permitAll()
            );
        return http.build();
    }

    /**
     * Default SecurityFilterChain. AllowedPaths are open. Other requests require authentication.
     */
    @Bean
    public SecurityFilterChain defaultFilterChain(HttpSecurity http) throws Exception {
        String[] allowedPaths = { "/", "/login*", "/logout*", "/error*", "/js/**", "/css/**", "/robots.txt" };
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(allowedPaths).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .defaultSuccessUrl("/", false).permitAll()
            )
            .logout(logout -> logout
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/")
                .permitAll()
            );
        return http.build();
    }
}
