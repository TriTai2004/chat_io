package com.chatio.socket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.chatio.socket.futures.auth.service.AuthService;
import com.chatio.socket.security.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthService authService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity

                // Tắt CSRF
                .csrf(csft -> csft.disable())

                // Không tạo session
                // Mỗi request đều phải gửi JWT
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Cấu hình phân quyền request
                .authorizeHttpRequests(r -> r
                        // Cho phép gọi API login mà không cần token
                        .requestMatchers("/api/v1/**").permitAll()
                        // Tất cả API khác phải có JWT hợp lệ
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.successHandler(authService));

        httpSecurity.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }

}