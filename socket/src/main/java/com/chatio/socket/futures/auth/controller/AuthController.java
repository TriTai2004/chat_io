package com.chatio.socket.futures.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatio.socket.futures.auth.dto.AuthRequest;
import com.chatio.socket.futures.auth.dto.AuthResponse;
import com.chatio.socket.futures.auth.dto.RegisterRequest;
import com.chatio.socket.futures.auth.service.AuthService;
import com.chatio.socket.futures.user.dto.AccountResponse;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody @Valid AuthRequest loginRequest) {

        AuthResponse authResponse = authService.login(request, response, loginRequest);

        if (authResponse == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody @Valid RegisterRequest registerRequest) {

        return ResponseEntity.ok(authService.register(request, response, registerRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        authService.refreshToken(response, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponse> getMe() {

        return ResponseEntity.ok(authService.getMe());
    }

}
