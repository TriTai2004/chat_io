package com.chatio.socket.futures.conversation.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.chatio.socket.exception.ResourceNotFoundException;
import com.chatio.socket.futures.conversation.dto.ConversationFirstGroupRequest;
import com.chatio.socket.futures.conversation.dto.ConversationRequest;
import com.chatio.socket.futures.conversation.dto.ConversationResponse;
import com.chatio.socket.futures.conversation.filter.ConversationFiler;
import com.chatio.socket.futures.conversation.mapper.ConversationMapper;
import com.chatio.socket.futures.conversation.model.Conversation;
import com.chatio.socket.futures.conversation.repository.ConversationRepository;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberRole;
import com.chatio.socket.futures.conversationRemember.repository.ConversationMemberRepository;
import com.chatio.socket.futures.user.model.Account;
import com.chatio.socket.payload.PaginationResponse;
import com.chatio.socket.security.CurrentUserService;
import com.chatio.socket.utils.UploadImage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationMapper conversationMapper;

    private final ConversationRepository conversationRepository;

    private final CurrentUserService currentUserService;

    private final ConversationMemberRepository conversationMemberRepository;

    private final UploadImage imageUploadImage;

    public PaginationResponse<List<ConversationResponse>> findAll(
            Pageable pageable,
            Long id,
            String name,
            Boolean group,
            LocalDateTime createdFrom,
            LocalDateTime createdTo) {

        Specification<Conversation> spec = ConversationFiler.conversationFilter(id, name, group, createdFrom, createdTo);

        Page<Conversation> pages = conversationRepository.findAll(spec, pageable);

        List<Conversation> results = pages.getContent();

        return PaginationResponse.<List<ConversationResponse>>builder()
                .currentPage(pages.getNumber())
                .data(conversationMapper.toResponses(results))
                .totalPages(pages.getTotalPages())
                .totalItems(pages.getTotalElements())
                .build();
    }

    public ConversationResponse findById(Long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with id: " + id));

        return conversationMapper.toResponse(conversation);
    }

    @Transactional
    public ConversationResponse create(ConversationRequest request) {
        Conversation conversation = conversationMapper.toEntity(request);
        conversation = conversationRepository.save(conversation);

        return conversationMapper.toResponse(conversation);
    }

    @Transactional
    public ConversationResponse update(Long id, ConversationRequest request) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with id: " + id));

        conversationMapper.updateConversationFromRequest(request, conversation);
        conversation = conversationRepository.save(conversation);

        return conversationMapper.toResponse(conversation);
    }

    @Transactional
    public void delete(Long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with id: " + id));

        conversationRepository.delete(conversation);
    }

    public Long checkEmpty(Long userId){

        Account account = currentUserService.getAccount();

        Long conversationId = conversationRepository.checkEmpty(account.getId(), userId);

        return conversationId;
    }

    @Transactional
    public ConversationResponse createGroup( ConversationFirstGroupRequest conversationFirstGroupRequest, MultipartFile avatar) throws IOException {

        Conversation conversation = new Conversation();
        conversation.setName(conversationFirstGroupRequest.getName());
        conversation.setGroup(true);

        if (avatar != null && !avatar.isEmpty()) {

            Map<String, Object> map = imageUploadImage.uploadImage(avatar);

            String url = (String) map.get("url");

            conversation.setImageUrl(url);

        } else {

            conversation.setImageUrl(conversationFirstGroupRequest.getAvatar());

        }


        conversation = conversationRepository.save(conversation);

        Account account = currentUserService.getAccount();
        ConversationMember cv = new ConversationMember();
            cv.setAccount(account);
            cv.setConversation(conversation);
            cv.setConversationId(conversation.getId());
            cv.setUserId(account.getId());
            cv.setRole(ConversationMemberRole.OWNER);

        List<ConversationMember> list = new ArrayList<>();
        list.add(cv);

        for(Long id : conversationFirstGroupRequest.getMembers()){
            ConversationMember conversationMember = new ConversationMember();
            conversationMember.setAccount(Account.builder().id(id).build());
            conversationMember.setConversation(conversation);
            conversationMember.setConversationId(conversation.getId());
            conversationMember.setUserId(id);
            conversationMember.setRole(ConversationMemberRole.MEMBER);
            list.add(conversationMember);
        }

        conversationMemberRepository.saveAll(list);

        return conversationMapper.toResponse(conversation);
    }

 
}
