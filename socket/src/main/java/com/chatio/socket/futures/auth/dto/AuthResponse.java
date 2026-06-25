package com.chatio.socket.futures.auth.dto;

import java.time.LocalDateTime;

import com.chatio.socket.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
        private Long id;
    private String fullname;
    private String phone;
    private String email;
    private String avatar;
    private boolean online;
    private boolean active;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
