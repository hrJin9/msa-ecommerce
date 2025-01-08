package com.example.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class WebSecurity {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(ar -> ar.requestMatchers(
                                                        AntPathRequestMatcher.antMatcher("/auth/**")
                                                ).authenticated()
                                                .requestMatchers(
//                                                        AntPathRequestMatcher.antMatcher("/h2-console/**"),
//                                                        AntPathRequestMatcher.antMatcher("/users/**")
                                                        AntPathRequestMatcher.antMatcher("/**")
                                                ).permitAll()
                )
                .headers(
                        hc -> hc.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // 프레임별
                );

        return http.build();
    }
}
