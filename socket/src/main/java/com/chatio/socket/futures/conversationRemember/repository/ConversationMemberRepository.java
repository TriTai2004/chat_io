package com.chatio.socket.futures.conversationRemember.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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

}
