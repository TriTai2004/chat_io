package com.chatio.socket.futures.user.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatio.socket.enums.Role;
import com.chatio.socket.futures.user.dto.AccountResponse;
import com.chatio.socket.futures.user.dto.UpdateAccountRequest;
import com.chatio.socket.futures.user.service.AccountService;
import com.chatio.socket.payload.PaginationResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @GetMapping
    public ResponseEntity<PaginationResponse<List<AccountResponse>>> getAll(
        @PageableDefault(page = 0, size = 10)
        Pageable pageable,
        @RequestParam(required = false)   Long id,
        @RequestParam(defaultValue = "", required = false)    String fullname,
        @RequestParam(defaultValue = "", required = false)     String phone,
        @RequestParam(defaultValue = "", required = false)   String email,
        @RequestParam(defaultValue = "", required = false)   String avatar,
        @RequestParam(required = false)  Boolean online,
        @RequestParam(required = false)  Boolean active,
        @RequestParam (required = false)   Role role,
        @RequestParam(required = false)   LocalDateTime createdFrom,
        @RequestParam(required = false)  LocalDateTime createdTo,
        @RequestParam(required = false)  LocalDateTime updatedFrom,
        @RequestParam(required = false)  LocalDateTime updatedTo
    ) {

        return ResponseEntity.ok(accountService.findAll(pageable, id, fullname, phone, 
            email, avatar, online,active, role, createdFrom, createdTo, updatedFrom, updatedTo));
    }


    @PutMapping
    public ResponseEntity<AccountResponse> update(
        @RequestBody @Valid UpdateAccountRequest accountRequest
    ){

        return ResponseEntity.ok(accountService.update(accountRequest));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponse> updateActive(
        @PathVariable(name = "id") Long id,
        @RequestBody(required = true) Boolean status
    ){

        return ResponseEntity.ok(accountService.updateActive(id, status));
    }
}
