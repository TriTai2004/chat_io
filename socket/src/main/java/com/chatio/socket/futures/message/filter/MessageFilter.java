package com.chatio.socket.futures.message.filter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.chatio.socket.futures.message.model.Message;
import com.chatio.socket.futures.message.model.MessageType;

import jakarta.persistence.criteria.Predicate;

public class MessageFilter {

    public static Specification<Message> messageFilter(
            Long id,
            Long conversationId,
            Long senderId,
            MessageType type,
            Boolean seen,
            LocalDateTime createdFrom,
            LocalDateTime createdTo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (conversationId != null) {
                predicates.add(cb.equal(root.get("conversation").get("id"), conversationId));
            }

            if (senderId != null) {
                predicates.add(cb.equal(root.get("sender").get("id"), senderId));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (seen != null) {
                predicates.add(cb.equal(root.get("seen"), seen));
            }

            if (createdFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom));
            }

            if (createdTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
