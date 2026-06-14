package com.chatio.socket.futures.conversationRemember.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatio.socket.futures.conversationRemember.dto.ConversationMemberRequest;
import com.chatio.socket.futures.conversationRemember.dto.ConversationMemberResponse;
import com.chatio.socket.futures.conversationRemember.dto.UpdateConversationMemberRequest;
import com.chatio.socket.futures.conversationRemember.model.ConversationMember.ConversationMemberRole;
import com.chatio.socket.futures.conversationRemember.service.ConversationMemberService;
import com.chatio.socket.payload.PaginationResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/conversation-members")
public class ConversationMemberController {

    @Autowired
    private ConversationMemberService conversationMemberService;

    @GetMapping
    public ResponseEntity<PaginationResponse<List<ConversationMemberResponse>>> getAll(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) ConversationMemberRole role,
            @RequestParam(required = false) LocalDateTime joinedFrom,
            @RequestParam(required = false) LocalDateTime joinedTo) {

        return ResponseEntity.ok(conversationMemberService.findAll(
                pageable, conversationId, userId, role, joinedFrom, joinedTo));
    }

    @GetMapping("/{conversationId}/{userId}")
    public ResponseEntity<ConversationMemberResponse> getById(
            @PathVariable(name = "conversationId") Long conversationId,
            @PathVariable(name = "userId") Long userId) {

        return ResponseEntity.ok(conversationMemberService.findById(conversationId, userId));
    }

    @PostMapping
    public ResponseEntity<ConversationMemberResponse> create(
            @RequestBody @Valid ConversationMemberRequest request) {

        return ResponseEntity.ok(conversationMemberService.create(request));
    }

    @PutMapping("/{conversationId}/{userId}")
    public ResponseEntity<ConversationMemberResponse> update(
            @PathVariable(name = "conversationId") Long conversationId,
            @PathVariable(name = "userId") Long userId,
            @RequestBody @Valid UpdateConversationMemberRequest request) {

        return ResponseEntity.ok(conversationMemberService.update(conversationId, userId, request));
    }

    @DeleteMapping("/{conversationId}/{userId}")
    public ResponseEntity<Void> delete(
            @PathVariable(name = "conversationId") Long conversationId,
            @PathVariable(name = "userId") Long userId) {

        conversationMemberService.delete(conversationId, userId);
        return ResponseEntity.noContent().build();
    }
}
