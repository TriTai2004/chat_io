package com.chatio.socket.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.chatio.socket.futures.auth.service.AuthService;
import com.chatio.socket.security.JwtFilter;

import jakarta.servlet.http.HttpServletResponse;
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

                        .requestMatchers("/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**").permitAll()

                        // Cho phép gọi API login mà không cần token
                        .requestMatchers("/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/auth/register").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/v1/accounts").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/v1/accounts").authenticated()
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/accounts/**").hasRole("ADMIN")
                        
                        
                        .requestMatchers("/api/v1/conversations/**").permitAll()


                        .requestMatchers("/api/v1/conversation-members/**").permitAll()
                        
                        .requestMatchers("/api/v1/messages/**").permitAll()

                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/ws").permitAll()




                        // Tất cả API khác phải có JWT hợp lệ
                        .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2.successHandler(authService))
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(
                        (request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        }
                    )
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
                

        httpSecurity.addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5500", "http://localhost:5500"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}