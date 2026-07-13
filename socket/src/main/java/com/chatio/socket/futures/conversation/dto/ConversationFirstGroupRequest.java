
package com.chatio.socket.futures.conversation.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversationFirstGroupRequest {

    @NotBlank(message = "Conversation name is required")
    @Size(max = 255, message = "Conversation name must not exceed 255 characters")
    private String name;

    private String avatar;

    private List<Long> members; 


    
}