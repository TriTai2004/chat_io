package com.chatio.socket.futures.user.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.chatio.socket.futures.user.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    Account findByEmail(String email);

    Account findByPhone(String phone);

    @Query(value = """
            SELECT DISTINCT a.*
            FROM accounts a
            JOIN conversation_members cm
                ON a.id = cm.user_id
            JOIN conversations c
                ON cm.conversation_id = c.id
            WHERE cm.conversation_id IN (
                SELECT conversation_id
                FROM conversation_members
                WHERE user_id = :userId
            )
            AND c.is_group = false
            AND a.id <> :userId
            """, nativeQuery = true)
    Page<Account> findChatPartners(@Param("userId") Long userId, Pageable pageable);



    @Query(value = """
                select a.email
                from accounts a join conversation_members cm
                on a.id = cm.user_id
                where cm.conversation_id = :conversationId
            """, nativeQuery = true)
    List<String> findEmailUsersByConversationId(@Param("conversationId") Long conversationId);


}
