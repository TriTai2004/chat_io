package com.chatio.socket.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.chatio.socket.config.UserDetailConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter{

    private final JwtUtil jwtUtil;
    private final UserDetailConfig userDetailConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String header = request.getHeader("Authorization");

        // Không có token → cho qua
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Chỉ xử lý khi có Bearer token
        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7);

                // 1. Giải mã token
                String email = jwtUtil.extractEmail(token);

                // 2. Load user từ DB
                UserDetails userDetails = userDetailConfig.loadUserByUsername(email);

                // 3. Tạo Authentication
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // 4. Set vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                // Token sai / hết hạn / user không tồn tại
                System.out.println(e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        // Cho request đi tiếp
        filterChain.doFilter(request, response);

        
    }



}
