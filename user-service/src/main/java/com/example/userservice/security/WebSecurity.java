package com.example.userservice.security;

import com.example.userservice.service.UserService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class WebSecurity {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectPostProcessor<Object> objectPostProcessor;
    private final UserService userService;

    private static final String ALLOWED_IP_ADDRESS = "127.0.0.1";
    private static final String[] WHITE_LIST = {
            "/users/**",
            "/",
            "/**"
    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest.requestMatchers(WHITE_LIST).permitAll()
                                        .anyRequest() // 나머지 모든 요청
                                        .access(this::hasIpAddress) // 해당하는 요청이 허용되기 위한 조건
                )
                .addFilter(getAuthenticationFilter())
                .headers(hc ->
                        hc.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // 프레임별
                );

        return http.build();
    }

    private AuthorizationDecision hasIpAddress(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ALLOWED_IP_ADDRESS);
        return new AuthorizationDecision(
                !(authentication.get() instanceof AnonymousAuthenticationToken)
                        && ipAddressMatcher.matches(object.getRequest()
                ));
    }

    private AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
        return auth.build();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        builder.userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);

        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userService);
        authenticationFilter.setAuthenticationManager(authenticationManager(builder));
        return authenticationFilter;
    }

}
