package com.chatio.socket.futures.conversationRemember.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatio.socket.exception.ResourceNotFoundException;
import com.chatio.socket.futures.conversation.model.Conversation;
import com.chatio.socket.futures.conversation.repository.ConversationRepository;
import com.chatio.socket.futures.conversationRemember.dto.ConversationMemberRequest;
import com.chatio.socket.futures.conversationRemember.dto.ConversationMemberResponse;
import com.chatio.socket.futures.conversationRemember.dto.UpdateConversationMemberRequest;
import com.chatio.socket.futures.conversationRemember.filter.ConversationMemberFilter;
import com.chatio.socket.futures.conversationRemember.mapper.ConversationMemberMapper;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberId;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberRole;
import com.chatio.socket.futures.conversationRemember.repository.ConversationMemberRepository;
import com.chatio.socket.futures.user.model.Account;
import com.chatio.socket.futures.user.repository.AccountRepository;
import com.chatio.socket.payload.PaginationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationMemberService {

    private final ConversationMemberMapper conversationMemberMapper;

    private final ConversationMemberRepository conversationMemberRepository;

    private final ConversationRepository conversationRepository;

    private final AccountRepository accountRepository;

    public PaginationResponse<List<ConversationMemberResponse>> findAll(
            Pageable pageable,
            Long conversationId,
            Long userId,
            ConversationMemberRole role,
            LocalDateTime joinedFrom,
            LocalDateTime joinedTo) {

        Specification<ConversationMember> spec = ConversationMemberFilter.conversationMemberFilter(
                conversationId, userId, role, joinedFrom, joinedTo);

        Page<ConversationMember> pages = conversationMemberRepository.findAll(spec, pageable);

        List<ConversationMember> results = pages.getContent();

        return PaginationResponse.<List<ConversationMemberResponse>>builder()
                .currentPage(pages.getNumber())
                .data(conversationMemberMapper.toResponses(results))
                .totalPages(pages.getTotalPages())
                .totalItems(pages.getTotalElements())
                .build();
    }

    public ConversationMemberResponse findById(Long conversationId, Long userId) {
        ConversationMember member = conversationMemberRepository.findById(new ConversationMemberId(conversationId, userId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conversation member not found with conversationId: " + conversationId + " and userId: " + userId));

        return conversationMemberMapper.toResponse(member);
    }

    @Transactional
    public ConversationMemberResponse create(ConversationMemberRequest request) {
        ConversationMemberId id = new ConversationMemberId(request.getConversationId(), request.getUserId());

        if (conversationMemberRepository.existsById(id)) {
            throw new IllegalArgumentException("Conversation member already exists");
        }

        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conversation not found with id: " + request.getConversationId()));

        Account account = accountRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account not found with id: " + request.getUserId()));

        ConversationMember member = conversationMemberMapper.toEntity(request);
        member.setConversation(conversation);
        member.setAccount(account);
        member = conversationMemberRepository.save(member);

        return conversationMemberMapper.toResponse(member);
    }

    @Transactional
    public ConversationMemberResponse update(Long conversationId, Long userId, UpdateConversationMemberRequest request) {
        ConversationMember member = conversationMemberRepository.findById(new ConversationMemberId(conversationId, userId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conversation member not found with conversationId: " + conversationId + " and userId: " + userId));

        conversationMemberMapper.updateConversationMemberFromRequest(request, member);
        member = conversationMemberRepository.save(member);

        return conversationMemberMapper.toResponse(member);
    }

    @Transactional
    public void delete(Long conversationId, Long userId) {
        ConversationMember member = conversationMemberRepository.findById(new ConversationMemberId(conversationId, userId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conversation member not found with conversationId: " + conversationId + " and userId: " + userId));

        conversationMemberRepository.delete(member);
    }
}
