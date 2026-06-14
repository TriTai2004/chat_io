package com.chatio.socket.futures.message.dto;

import com.chatio.socket.futures.message.model.MessageType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {

    @NotNull(message = "conversationId is required")
    private Long conversationId;

    @NotNull(message = "senderId is required")
    private Long senderId;

    @Size(max = 65535, message = "content is too long")
    private String content;

    @Size(max = 255, message = "imageUrl must be at most 255 characters")
    private String imageUrl;

    private MessageType type;
}
