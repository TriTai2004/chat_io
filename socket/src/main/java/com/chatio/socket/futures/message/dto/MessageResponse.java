package com.chatio.socket.futures.message.dto;

import java.time.LocalDateTime;

import com.chatio.socket.futures.message.model.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private Long id;

    private Long conversationId;

    private Long senderId;

    private String content;

    private String imageUrl;

    private MessageType type;

    private String avatarMessage;

    private String avatarChat;

    private String nameChat;

    private String fullname;

    private boolean seen;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
