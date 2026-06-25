package com.chatio.socket.futures.websocket.service;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.chatio.socket.exception.ResourceNotFoundException;
import com.chatio.socket.futures.conversationRemember.repository.ConversationMemberRepository;
import com.chatio.socket.futures.message.dto.MessageRequest;
import com.chatio.socket.futures.message.dto.MessageResponse;
import com.chatio.socket.futures.message.service.MessageService;
import com.chatio.socket.futures.user.model.Account;
import com.chatio.socket.futures.user.repository.AccountRepository;
import com.chatio.socket.futures.websocket.listener.SessionRegistry;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SocketService {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageService messageService;

    private final SessionRegistry sessionRegistry;

    private final AccountRepository accountRepository;

    private final ConversationMemberRepository conversationMemberRepository;

    public void sendMessage(MessageRequest request, Principal principal) {

        String email = principal.getName();

        Account account = accountRepository.findByEmail(email);

        if (account == null) {
            throw new ResourceNotFoundException("user not found");
        }

        if (!account.getId().equals(request.getSenderId())) {
            throw new AccessDeniedException("You are not allowed to create this message"); 
        }

        MessageResponse messageResponse = messageService.create(request);

        Long conversationId = request.getConversationId();

        List<String> memberEmails = conversationMemberRepository.findEmailsByConversationId(conversationId);

        for (String e : memberEmails) {
            if (sessionRegistry.isOnline(e)) {
                
                messagingTemplate.convertAndSendToUser(
                        e,
                        "/queue/messages",
                        messageResponse);
            } else {
                // offline push notification
            }
        }
    }

}
