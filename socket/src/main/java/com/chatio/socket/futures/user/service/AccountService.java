package com.chatio.socket.futures.user.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.chatio.socket.enums.Role;
import com.chatio.socket.exception.ResourceNotFoundException;
import com.chatio.socket.futures.user.dto.AccountResponse;
import com.chatio.socket.futures.user.dto.UpdateAccountRequest;
import com.chatio.socket.futures.user.filter.AccountFilter;
import com.chatio.socket.futures.user.mapper.AccountMapper;
import com.chatio.socket.futures.user.model.Account;
import com.chatio.socket.futures.user.repository.AccountRepository;
import com.chatio.socket.payload.PaginationResponse;
import com.chatio.socket.security.CurrentUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;

    private final AccountRepository accountRepository;

    private final CurrentUserService currentUserService;

    public PaginationResponse<List<AccountResponse>> findAll(
            Pageable pageable,
            Long id,
            String fullname,
            String phone,
            String email,
            String avatar,
            Boolean online,
            Boolean active,
            Role role,
            LocalDateTime createdFrom,
            LocalDateTime createdTo,
            LocalDateTime updatedFrom,
            LocalDateTime updatedTo) {

        Specification<Account> spec = AccountFilter.accountFilter(id, fullname, phone, email,
                avatar, online, active, role, createdFrom, createdTo, updatedFrom, updatedTo);

        Page<Account> pages = accountRepository.findAll(spec, pageable);

        List<Account> results = pages.getContent();

        return PaginationResponse.<List<AccountResponse>>builder()
                .currentPage(pages.getNumber())
                .data(accountMapper.toResponses(results))
                .totalPages(pages.getTotalPages())
                .totalItems(pages.getTotalElements())
                .build();

    }

    public AccountResponse update(UpdateAccountRequest accountRequest){

        Account account = currentUserService.getAccount();

        accountMapper.updateAccountFromRequest(accountRequest, account);

        account = accountRepository.save(account);


        return accountMapper.toResponse(account);
    }

    public AccountResponse updateActive(Long id, Boolean status) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        account.setActive(status);

        account = accountRepository.save(account);

        return accountMapper.toResponse(account);

    }

}
