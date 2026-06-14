package com.chatio.socket.futures.conversationRemember.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.chatio.socket.futures.conversationRemember.model.ConversationMember;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberId;

public interface ConversationMemberRepository
        extends JpaRepository<ConversationMember, ConversationMemberId>,
        JpaSpecificationExecutor<ConversationMember> {

}
