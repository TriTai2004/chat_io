package com.chatio.socket.futures.conversation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRequest {

    @NotBlank(message = "Conversation name is required")
    @Size(max = 255, message = "Conversation name must not exceed 255 characters")
    private String name;

    @NotNull(message = "Conversation group flag is required")
    private Boolean group;
}
