package com.chatio.socket.futures.websocket.service;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.chatio.socket.futures.conversationRemember.repository.ConversationMemberRepository;
import com.chatio.socket.futures.message.dto.MessageRequest;
import com.chatio.socket.futures.message.dto.MessageResponse;
import com.chatio.socket.futures.message.service.MessageService;
import com.chatio.socket.futures.websocket.listener.SessionRegistry;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SocketService {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageService messageService;

    private final SessionRegistry sessionRegistry;

    private final ConversationMemberRepository conversationMemberRepository;

    public void sendMessage(MessageRequest request, Principal principal) {


        MessageResponse messageResponse = messageService.create(request);

        Long conversationId = request.getConversationId();

        List<String> memberEmails = conversationMemberRepository.findEmailsByConversationId(conversationId);

        for (String email : memberEmails) {
            if (sessionRegistry.isOnline(email)) {
                
                messagingTemplate.convertAndSendToUser(
                        email,
                        "/queue/messages",
                        messageResponse);
            } else {
                // offline push notification
            }
        }
    }

}
