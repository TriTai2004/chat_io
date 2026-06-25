package com.chatio.socket.futures.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {

    @Size(max = 150, message = "fullname must be at most 150 characters")
    private String fullname;

    @Size(max = 20, message = "phone must be at most 20 characters")
    @Pattern(
            regexp = "^[0-9+\\-()\\s]*",
            message = "phone must contain only digits, spaces, +, -, ( and )"
    )
    private String phone;

    @Size(max = 500, message = "avatar must be at most 500 characters")
    private String avatar;

}
