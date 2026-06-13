package com.chatio.socket.futures.auth.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.chatio.socket.enums.Role;
import com.chatio.socket.exception.ResourceNotFoundException;
import com.chatio.socket.exception.UnauthorizedException;
import com.chatio.socket.futures.auth.dto.AuthRequest;
import com.chatio.socket.futures.auth.dto.AuthResponse;
import com.chatio.socket.futures.auth.dto.RegisterRequest;
import com.chatio.socket.futures.auth.mapper.AuthMapper;
import com.chatio.socket.futures.user.model.Account;
import com.chatio.socket.futures.user.repository.AccountRepository;
import com.chatio.socket.security.JwtUtil;
import com.chatio.socket.utils.CookieUtil;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService extends SimpleUrlAuthenticationSuccessHandler {

        private final JwtUtil jwtUtil;
        private final PasswordEncoder passwordEncoder;
        private final AccountRepository accountRepository;
        private final AuthMapper authMapper;
        private final CookieUtil cookieUtil;

        @Value("${frontend.url}")
        private String URL_FRONTEND;


        public AuthResponse login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                        AuthRequest authRequest) {

                Account account = accountRepository.findByEmail(authRequest.getEmail().trim());

                if (account == null || !passwordEncoder.matches(authRequest.getPassword(), account.getPassword())) {
                        return null;
                }

                attachTokens(httpServletResponse, account);

                String refreshToken = jwtUtil.generateRefreshToken(account.getEmail());
                account.setRefreshToken(refreshToken);
                account = accountRepository.save(account);

                return authMapper.toResponse(account);

        }

        public AuthResponse register(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                        RegisterRequest registerRequest) {

                String normalizedEmail = registerRequest.getEmail().trim();

                Account account = accountRepository.findByEmail(normalizedEmail);

                if (account != null) {
                        throw new DataIntegrityViolationException("Email is exist");
                }

                Account entity = Account.builder()
                                .email(normalizedEmail)
                                .password(passwordEncoder.encode(registerRequest.getPassword().trim()))
                                .fullname(registerRequest.getFullname().trim())
                                .active(true)
                                .online(true)
                                .build();

                String refreshToken = attachTokens(httpServletResponse, entity);

                entity.setRefreshToken(refreshToken);

                Account accountSaved = accountRepository.save(entity);

                return authMapper.toResponse(accountSaved);

        }

        public void logout(HttpServletResponse response) {

                String email = SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getName();

                Account account = accountRepository.findByEmail(email);

                if (account != null) {
                        account.setRefreshToken(null);
                        accountRepository.save(account);
                }

                response.addHeader(
                                "Set-Cookie",
                                "accessToken=; HttpOnly; Path=/; Max-Age=0; SameSite=Lax");

                response.addHeader(
                                "Set-Cookie",
                                "refreshToken=; HttpOnly; Path=/; Max-Age=0; SameSite=Lax");
        }

        public void refreshToken(HttpServletResponse response, HttpServletRequest request) {

                String tokenRefresh = cookieUtil.getRefreshToken(request);

                if (tokenRefresh == null || tokenRefresh.isBlank()) {
                        throw new UnauthorizedException("Refresh token is missing");
                }

                try {
                        String email = jwtUtil.extractEmailFromRefreshToken(tokenRefresh);
                        Account account = accountRepository.findByEmail(email);

                        if (account == null) {
                                throw new ResourceNotFoundException("Account not found with email: " + email);
                        }

                        if (account.getRefreshToken() == null || !account.getRefreshToken().equals(tokenRefresh)) {
                                throw new UnauthorizedException("Invalid refresh token");
                        } else {
                                String tokenNew = jwtUtil.generateToken(account.getEmail(), account.getRole().name());
                                response.addHeader(
                                                "Set-Cookie",
                                                "accessToken=" + tokenNew
                                                                + "; HttpOnly; Path=/; Max-Age=86400; SameSite=Lax");
                        }

                } catch (JwtException e) {
                        throw new UnauthorizedException("Refresh token is missing");
                }

        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication) throws IOException {

                OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

                String email = oauthUser.getAttribute("email");

                Account account = accountRepository.findByEmail(email);

                if (account == null) {
                        account = Account.builder()
                                        .email(email)
                                        .fullname(oauthUser.getAttribute("name"))
                                        .avatar(oauthUser.getAttribute("picture"))
                                        .role(Role.USER)
                                        .active(true)
                                        .online(true)
                                        .password("")
                                        .build();
                }

                String refreshToken = attachTokens(response, account);

                account.setRefreshToken(refreshToken);

                accountRepository.save(account);

                response.sendRedirect(URL_FRONTEND);

        }

        private String attachTokens(
                        HttpServletResponse response,
                        Account account) {

                String accessToken = jwtUtil.generateToken(
                                account.getEmail(),
                                account.getRole().name());

                String refreshToken = jwtUtil.generateRefreshToken(
                                account.getEmail());

                response.addHeader(
                                "Set-Cookie",
                                "accessToken=" + accessToken
                                                + "; HttpOnly; Path=/; Max-Age=86400; SameSite=Lax");

                response.addHeader(
                                "Set-Cookie",
                                "refreshToken=" + refreshToken
                                                + "; HttpOnly; Path=/; Max-Age=604800; SameSite=Lax");

                account.setRefreshToken(refreshToken);

                return refreshToken;
        }

}
