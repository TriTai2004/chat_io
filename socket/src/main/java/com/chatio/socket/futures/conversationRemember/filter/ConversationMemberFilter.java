package com.chatio.socket.futures.conversationRemember.filter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.chatio.socket.futures.conversationRemember.model.ConversationMember;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberRole;

import jakarta.persistence.criteria.Predicate;

public class ConversationMemberFilter {

    public static Specification<ConversationMember> conversationMemberFilter(
            Long conversationId,
            Long userId,
            ConversationMemberRole role,
            LocalDateTime joinedFrom,
            LocalDateTime joinedTo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (conversationId != null) {
                predicates.add(cb.equal(root.get("id").get("conversationId"), conversationId));
            }

            if (userId != null) {
                predicates.add(cb.equal(root.get("id").get("userId"), userId));
            }

            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }

            if (joinedFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("joinedAt"), joinedFrom));
            }

            if (joinedTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("joinedAt"), joinedTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
