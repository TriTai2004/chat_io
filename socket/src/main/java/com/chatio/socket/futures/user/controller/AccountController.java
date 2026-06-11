package com.chatio.socket.futures.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    @GetMapping
    public ResponseEntity<?> getAll() {

        return ResponseEntity.ok("ok");
    }
}
