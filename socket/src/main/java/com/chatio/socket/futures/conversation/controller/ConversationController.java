package com.chatio.socket.futures.conversation.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chatio.socket.futures.conversation.dto.ConversationFirstGroupRequest;
import com.chatio.socket.futures.conversation.dto.ConversationRequest;
import com.chatio.socket.futures.conversation.dto.ConversationResponse;
import com.chatio.socket.futures.conversation.service.ConversationService;
import com.chatio.socket.payload.PaginationResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @GetMapping
    public ResponseEntity<PaginationResponse<List<ConversationResponse>>> getAll(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "", required = false) String name,
            @RequestParam(required = false) Boolean group,
            @RequestParam(required = false) LocalDateTime createdFrom,
            @RequestParam(required = false) LocalDateTime createdTo) {

        return ResponseEntity.ok(conversationService.findAll(pageable, id, name, group, createdFrom, createdTo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversationResponse> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(conversationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ConversationResponse> create(
            @RequestBody @Valid ConversationRequest request) {

        return ResponseEntity.ok(conversationService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConversationResponse> update(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid ConversationRequest request) {

        return ResponseEntity.ok(conversationService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        conversationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-empty/{userId}")
    public ResponseEntity<?> checkEmpty(
        @PathVariable(name = "userId") Long userId

    ) {

        return ResponseEntity.ok(conversationService.checkEmpty(userId));
    }

    @PostMapping(value = "/create-first", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<ConversationResponse> createFirst(

        @Valid @RequestPart("data") ConversationFirstGroupRequest request,
        @RequestPart(value = "avatar", required = false) MultipartFile avatar

    ) throws IOException{

        return ResponseEntity.ok(conversationService.createGroup(request, avatar));
    }
}
