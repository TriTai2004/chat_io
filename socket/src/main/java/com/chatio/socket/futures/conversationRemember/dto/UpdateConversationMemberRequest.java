package com.chatio.socket.futures.conversationRemember.dto;

import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConversationMemberRequest {

    @NotNull(message = "role is required")
    private ConversationMemberRole role;
}
