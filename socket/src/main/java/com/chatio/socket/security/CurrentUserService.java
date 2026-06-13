package com.chatio.socket.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.chatio.socket.exception.ResourceNotFoundException;
import com.chatio.socket.futures.user.model.Account;
import com.chatio.socket.futures.user.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentUserService {

    private final AccountRepository accountRepository;

    public String getUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public Account getAccount() {

        Account account = accountRepository.findByEmail(getUsername());
        if (account == null) {
            throw new ResourceNotFoundException("User not found with email: " + getUsername());
        }

        return account;
    }
}
