package com.chatio.socket.futures.message.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.chatio.socket.futures.message.dto.MessageRequest;
import com.chatio.socket.futures.message.dto.MessageResponse;
import com.chatio.socket.futures.message.dto.UpdateMessageRequest;
import com.chatio.socket.futures.message.model.Message;
import com.chatio.socket.futures.message.model.MessageType;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    default MessageResponse toResponse(Message message) {
        if (message == null) {
            return null;
        }

        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation() != null ? message.getConversation().getId() : null)
                .senderId(message.getSender() != null ? message.getSender().getId() : null)
                .content(message.getContent())
                .imageUrl(message.getImageUrl())
                .type(message.getType())
                .seen(message.isSeen())
                .createdAt(message.getCreatedAt())
                .avatarMessage(message.getSender() != null ? message.getSender().getAvatar() : null)
                .avatarChat(
                        message.getConversation() != null && message.getConversation().getImageUrl() != null
                                ? message.getConversation().getImageUrl()
                                : (message.getSender() != null ? message.getSender().getAvatar() : null))
                .nameChat(
                        message.getConversation() != null && message.getConversation().getName() != null
                                ? message.getConversation().getName()
                                : (message.getSender() != null ? message.getSender().getFullname() : null))
                .fullname(message.getSender() != null ? message.getSender().getFullname() : null)
                .build();
    }

    default List<MessageResponse> toResponses(List<Message> messages) {
        return messages == null ? List.of() : messages.stream().map(this::toResponse).toList();
    }

    default Message toEntity(MessageRequest request) {
        if (request == null) {
            return null;
        }

        return Message.builder()
                .type(request.getType() != null ? request.getType() : MessageType.TEXT)
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .build();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "seen", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateMessageFromRequest(UpdateMessageRequest request, @MappingTarget Message message);
}
