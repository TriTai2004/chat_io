package com.chatio.socket.futures.message.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.chatio.socket.futures.message.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

    @Query(value = """
            SELECT
                m.id AS id,
                m.sender_id AS senderId,
                m.content AS content,
                m.type AS type,
                m.created_at AS createdAt,

                a.avatar AS avatar,
                a.fullname AS fullname,

                CASE
                    WHEN c.is_group = 1 THEN (
                        SELECT COUNT(*)
                        FROM conversation_members cm
                        WHERE cm.conversation_id = c.id
                    )
                    ELSE NULL
                END AS memberCount,

                CASE
                    WHEN c.is_group = 1 THEN c.name
                    ELSE NULL
                END AS nameGroup,

                CASE
                    WHEN c.is_group = 1 THEN 1
                    ELSE 0
                END AS isGroup

            FROM messages m

            JOIN accounts a
                ON m.sender_id = a.id

            JOIN conversations c
                ON m.conversation_id = c.id

            WHERE m.conversation_id = :conversationId

            ORDER BY m.created_at DESC
            """,

            countQuery = """
                        SELECT COUNT(*)
                        FROM messages m
                        WHERE m.conversation_id = :conversationId
                    """,

            nativeQuery = true)
    Page<MessageProjection> findMessagesByConversationId(
            Long conversationId,
            Pageable pageable);

    interface MessageProjection {

        Long getId();

        Long getSenderId();

        String getContent();

        String getType();

        LocalDateTime getCreatedAt();

        String getAvatar();

        String getFullname();

        Integer getMemberCount();

        String getNameGroup();

        Integer getIsGroup();
    }

}
