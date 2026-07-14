package com.chatio.socket.utils;

import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.chatio.socket.futures.user.repository.AccountRepository;
import com.chatio.socket.futures.websocket.listener.SessionRegistry;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AccountRepository accountRepository;

    private final SessionRegistry sessionRegistry;

    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public void sendConversation(Long conversationId, String emailCurrentUser) {


        List<String> emails = accountRepository.findEmailUsersByConversationId(conversationId);

        for (String email : emails) {

            if (email.equals(emailCurrentUser)) {
                continue;
            }

            if ( sessionRegistry.isOnline(email)) {

                messagingTemplate.convertAndSendToUser(
                    email,
                    "/queue/messages",
                    Map.of(
                        "type", "conversation",
                        "data", conversationId
                    )
                );


                System.out.println("===================================================================================");
                
            }else{
                //push notification
            }
        }




    }
    
}
