package com.chatio.socket.futures.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatio.socket.futures.user.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByEmail(String email);
}
