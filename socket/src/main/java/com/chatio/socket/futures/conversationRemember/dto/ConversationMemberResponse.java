package com.chatio.socket.futures.conversationRemember.dto;

import java.time.LocalDateTime;

import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberRole;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationMemberResponse {

    private Long conversationId;

    private Long userId;

    private ConversationMemberRole role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinedAt;
}
