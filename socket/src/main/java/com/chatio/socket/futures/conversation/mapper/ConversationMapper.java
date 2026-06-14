package com.chatio.socket.futures.conversation.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.chatio.socket.futures.conversation.dto.ConversationRequest;
import com.chatio.socket.futures.conversation.dto.ConversationResponse;
import com.chatio.socket.futures.conversation.model.Conversation;

@Mapper(componentModel = "spring")
public interface ConversationMapper {

    ConversationResponse toResponse(Conversation conversation);

    List<ConversationResponse> toResponses(List<Conversation> conversations);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Conversation toEntity(ConversationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateConversationFromRequest(ConversationRequest request, @MappingTarget Conversation conversation);
}
