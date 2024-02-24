package com.dnd.reetplace.app.config;

import com.dnd.reetplace.global.security.JwtAccessDeniedHandler;
import com.dnd.reetplace.global.security.JwtAuthenticationEntryPoint;
import com.dnd.reetplace.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private static final String BASE_URL = "/api";
    private static final String[] AUTH_WHITE_LIST = {
            "/auth/login/kakao",
            "/auth/login/apple",
            "/auth/refresh",
            "/places",
            "/places/search"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                    .mvcMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/terms2/**").permitAll();
                            Arrays.stream(AUTH_WHITE_LIST)
                                    .forEach(authWhiteListElem ->
                                            auth.mvcMatchers(BASE_URL + authWhiteListElem).permitAll());
                            auth.anyRequest().authenticated();
                        }
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
