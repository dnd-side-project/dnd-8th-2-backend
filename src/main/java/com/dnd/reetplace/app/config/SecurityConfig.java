package com.dnd.reetplace.app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private static final String BASE_URL = "/api";
    private static final String[] AUTH_WHITE_LIST = {
            // TODO: 추후 로그인 권한이 필요하지 않은 API의 uri 기입
            "/auth/login/kakao"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                    .mvcMatchers("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll();
                            Arrays.stream(AUTH_WHITE_LIST)
                                    .forEach(authWhiteListElem ->
                                            auth.mvcMatchers(BASE_URL + authWhiteListElem).permitAll());
                            auth.anyRequest().authenticated();
                        }
                )
                .build();
    }
}
