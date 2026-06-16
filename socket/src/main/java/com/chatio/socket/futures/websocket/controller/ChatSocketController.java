package com.chatio.socket.futures.websocket.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.chatio.socket.futures.message.dto.MessageRequest;
import com.chatio.socket.futures.websocket.service.SocketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final SocketService messageService;

    @MessageMapping("/chat.send")
    public void sendMessage(@Valid MessageRequest request, Principal principal) {

        messageService.sendMessage(request, principal);
        
    }
}
