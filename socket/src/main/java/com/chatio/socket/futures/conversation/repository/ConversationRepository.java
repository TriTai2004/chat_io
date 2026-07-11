package com.chatio.socket.futures.conversation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.chatio.socket.futures.conversation.model.Conversation;

public interface ConversationRepository
                extends JpaRepository<Conversation, Long>, JpaSpecificationExecutor<Conversation> {

        @Query(value = """
                        SELECT c.id
                        FROM conversations c
                        JOIN conversation_members cm
                            ON cm.conversation_id = c.id
                        WHERE c.is_group = false
                          AND cm.user_id IN (:user1, :user2)
                        GROUP BY c.id
                        HAVING COUNT(DISTINCT cm.user_id) = 2
                        """, nativeQuery = true)
        Long checkEmpty(Long user1, Long user2);

}
