package com.chatio.socket.futures.conversationRemember.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chatio.socket.futures.conversationRemember.model.ConversationMember;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberId;

public interface ConversationMemberRepository
                extends JpaRepository<ConversationMember, ConversationMemberId>,
                JpaSpecificationExecutor<ConversationMember> {

        @Query("""
                                SELECT cm.account.email
                                FROM ConversationMember cm
                                WHERE cm.conversation.id = :conversationId
                        """)
        List<String> findEmailsByConversationId(Long conversationId);

        @Query(value = """
                                SELECT
                                        c.id AS conversationId,
                                        CASE
                                                WHEN c.is_group = 1 THEN c.name
                                                ELSE COALESCE(partner.fullname, c.name)
                                        END AS conversationName,
                                        CASE
                                                WHEN c.is_group = 1 THEN true
                                                ELSE false
                                        END AS groupChat,
                                        c.created_at AS conversationCreatedAt,
                                        lm.content AS latestMessage,
                                        lm.created_at AS latestMessageTime,
                                        lm.type AS latestMessageType,
                                        CASE
                                                WHEN c.is_group = 1 THEN c.img_url
                                                ELSE partner.avatar
                                        END AS avatarChat,
                                        CASE
                                                WHEN c.is_group = 1 THEN NULL
                                                WHEN partner.is_online = 1 THEN true
                                                ELSE false
                                        END AS online,
                                        (
                                                SELECT COUNT(*)
                                                FROM conversation_members cm_total
                                                WHERE cm_total.conversation_id = c.id
                                        ) AS memberCount
                                FROM conversation_members me
                                JOIN conversations c ON c.id = me.conversation_id
                                LEFT JOIN messages lm ON lm.id = (
                                        SELECT m.id
                                        FROM messages m
                                        WHERE m.conversation_id = c.id
                                        ORDER BY m.created_at DESC, m.id DESC
                                        LIMIT 1
                                )
                                LEFT JOIN accounts latest_sender ON latest_sender.id = lm.sender_id
                                LEFT JOIN accounts partner ON partner.id = (
                                        SELECT cm2.user_id
                                        FROM conversation_members cm2
                                        WHERE cm2.conversation_id = c.id
                                                AND cm2.user_id <> me.user_id
                                        LIMIT 1
                                )
                                WHERE me.user_id = :userId
                                ORDER BY COALESCE(lm.created_at, c.created_at) DESC, c.id DESC
                        """, countQuery = """
                                SELECT COUNT(*)
                                FROM conversation_members cm
                                WHERE cm.user_id = :userId
                        """, nativeQuery = true)
        Page<ChatListProjection> findChatListByUserId(Long userId, Pageable pageable);

        interface ChatListProjection {

                Long getConversationId();

                String getConversationName();

                Integer getGroupChat();

                LocalDateTime getConversationCreatedAt();

                String getLatestMessage();

                LocalDateTime getLatestMessageTime();

                String getLatestMessageType();

                String getAvatarChat();

                Integer getOnline();

                Integer getMemberCount();
        }

}
