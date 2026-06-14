package com.chatio.socket.futures.conversationRemember.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.chatio.socket.futures.conversation.model.Conversation;
import com.chatio.socket.futures.user.model.Account;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conversation_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationMember {

    @EmbeddedId
    private ConversationMemberId id;

    @MapsId("conversationId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Account account;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private ConversationMemberRole role = ConversationMemberRole.MEMBER;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    public Long getConversationId() {
        return id != null ? id.getConversationId() : null;
    }

    public Long getUserId() {
        return id != null ? id.getUserId() : null;
    }

    public void setConversationId(Long conversationId) {
        if (id == null) {
            id = new ConversationMemberId();
        }
        id.setConversationId(conversationId);
    }

    public void setUserId(Long userId) {
        if (id == null) {
            id = new ConversationMemberId();
        }
        id.setUserId(userId);
    }

    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationMemberId implements Serializable {

        private static final long serialVersionUID = 1L;

        private Long conversationId;

        private Long userId;
    }

    public enum ConversationMemberRole {
        OWNER,
        MEMBER
    }
}
