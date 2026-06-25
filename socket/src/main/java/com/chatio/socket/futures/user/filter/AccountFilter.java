package com.chatio.socket.futures.user.filter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.chatio.socket.enums.Role;
import com.chatio.socket.futures.user.model.Account;

import jakarta.persistence.criteria.Predicate;

public class AccountFilter {

    public static Specification<Account> accountFilter(
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
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (fullname != null && !fullname.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("fullname")),
                        "%" + fullname.toLowerCase() + "%"));
            }

            if (phone != null && !phone.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("phone")),
                        "%" + phone.toLowerCase() + "%"));
            }

            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("email")),
                        "%" + email.toLowerCase() + "%"));
            }

            if (avatar != null && !avatar.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("avatar")),
                        "%" + avatar.toLowerCase() + "%"));
            }

            if (online != null) {
                predicates.add(cb.equal(root.get("online"), online));
            }

            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }

            if (createdFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom));
            }

            if (createdTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdTo));
            }

            if (updatedFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), updatedFrom));
            }

            if (updatedTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), updatedTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
