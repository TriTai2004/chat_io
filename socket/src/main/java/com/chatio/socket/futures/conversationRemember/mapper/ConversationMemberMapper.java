package com.chatio.socket.futures.conversationRemember.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.chatio.socket.futures.conversationRemember.dto.ConversationMemberRequest;
import com.chatio.socket.futures.conversationRemember.dto.ConversationMemberResponse;
import com.chatio.socket.futures.conversationRemember.dto.UpdateConversationMemberRequest;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberRole;


@Mapper(componentModel = "spring")
public interface ConversationMemberMapper {

    default ConversationMemberResponse toResponse(ConversationMember member) {
        if (member == null) {
            return null;
        }

        return ConversationMemberResponse.builder()
                .conversationId(member.getConversationId())
                .userId(member.getUserId())
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }

    default List<ConversationMemberResponse> toResponses(List<ConversationMember> members) {
        return members == null ? List.of() : members.stream().map(this::toResponse).toList();
    }

    default ConversationMember toEntity(ConversationMemberRequest request) {
        if (request == null) {
            return null;
        }

        ConversationMember member = new ConversationMember();
        member.setConversationId(request.getConversationId());
        member.setUserId(request.getUserId());
        member.setRole(request.getRole() != null ? request.getRole() : ConversationMemberRole.MEMBER);
        return member;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "conversationId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "conversation", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "joinedAt", ignore = true)
    void updateConversationMemberFromRequest(UpdateConversationMemberRequest request, @MappingTarget ConversationMember member);
}
