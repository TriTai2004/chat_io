package com.chatio.socket.futures.message.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chatio.socket.exception.ResourceNotFoundException;
import com.chatio.socket.futures.conversation.repository.ConversationRepository;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember;
import com.chatio.socket.futures.conversationRemember.repository.ConversationMemberRepository;
import com.chatio.socket.futures.message.dto.MessageRequest;
import com.chatio.socket.futures.message.dto.MessageResponse;
import com.chatio.socket.futures.message.dto.UpdateMessageRequest;
import com.chatio.socket.futures.message.filter.MessageFilter;
import com.chatio.socket.futures.message.mapper.MessageMapper;
import com.chatio.socket.futures.message.model.Message;
import com.chatio.socket.futures.message.model.MessageType;
import com.chatio.socket.futures.message.repository.MessageRepository;
import com.chatio.socket.futures.user.repository.AccountRepository;
import com.chatio.socket.payload.PaginationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;

    private final MessageRepository messageRepository;

    private final ConversationRepository conversationRepository;

    private final ConversationMemberRepository conversationMemberRepository;

    private final AccountRepository accountRepository;

    public PaginationResponse<List<MessageResponse>> findAll(
            Pageable pageable,
            Long id,
            Long conversationId,
            Long senderId,
            MessageType type,
            Boolean seen,
            LocalDateTime createdFrom,
            LocalDateTime createdTo) {

        Specification<Message> spec = MessageFilter.messageFilter(id, conversationId, senderId, type, seen, createdFrom, createdTo);

        Page<Message> pages = messageRepository.findAll(spec, pageable);

        return PaginationResponse.<List<MessageResponse>>builder()
                .currentPage(pages.getNumber())
                .data(messageMapper.toResponses(pages.getContent()))
                .totalPages(pages.getTotalPages())
                .totalItems(pages.getTotalElements())
                .build();
    }

    public MessageResponse findById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        return messageMapper.toResponse(message);
    }

    @Transactional
    public MessageResponse create(MessageRequest request) {
        if (request.getType() == null) {
            request.setType(MessageType.TEXT);
        }

        validateRequest(request.getType(), request.getContent(), request.getImageUrl());

        Message message = messageMapper.toEntity(request);
        message.setConversation(conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with id: " + request.getConversationId())));
        message.setSender(accountRepository.findById(request.getSenderId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + request.getSenderId())));
        if (!conversationMemberRepository.existsById(
                new ConversationMember.ConversationMemberId(request.getConversationId(), request.getSenderId()))) {
            throw new IllegalArgumentException("Sender is not a member of this conversation");
        }
        message = messageRepository.save(message);

        return messageMapper.toResponse(message);
    }

    @Transactional
    public MessageResponse update(Long id, UpdateMessageRequest request) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        MessageType nextType = request.getType() != null ? request.getType() : message.getType();
        String nextContent = request.getContent() != null ? request.getContent() : message.getContent();
        String nextImageUrl = request.getImageUrl() != null ? request.getImageUrl() : message.getImageUrl();
        validateRequest(nextType, nextContent, nextImageUrl);

        messageMapper.updateMessageFromRequest(request, message);
        if (request.getType() != null) {
            message.setType(request.getType());
        }
        if (request.getSeen() != null) {
            message.setSeen(request.getSeen());
        }
        message = messageRepository.save(message);

        return messageMapper.toResponse(message);
    }

    @Transactional
    public void delete(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        messageRepository.delete(message);
    }

    private void validateRequest(MessageType type, String content, String imageUrl) {
        if (type == MessageType.TEXT && (content == null || content.isBlank())) {
            throw new IllegalArgumentException("content is required for text message");
        }

        if ((type == MessageType.IMAGE || type == MessageType.FILE) && (imageUrl == null || imageUrl.isBlank())) {
            throw new IllegalArgumentException("imageUrl is required for image/file message");
        }
    }
}
