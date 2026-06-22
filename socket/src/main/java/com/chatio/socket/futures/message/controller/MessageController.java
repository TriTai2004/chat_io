package com.chatio.socket.futures.message.controller;

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

import com.chatio.socket.futures.message.dto.MessageRequest;
import com.chatio.socket.futures.message.dto.MessageResponse;
import com.chatio.socket.futures.message.dto.UpdateMessageRequest;
import com.chatio.socket.futures.message.model.MessageType;
import com.chatio.socket.futures.message.service.MessageService;
import com.chatio.socket.payload.PaginationResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public ResponseEntity<PaginationResponse<List<MessageResponse>>> getAll(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long conversationId,
            @RequestParam(required = false) Long senderId,
            @RequestParam(required = false) MessageType type,
            @RequestParam(required = false) Boolean seen,
            @RequestParam(required = false) LocalDateTime createdFrom,
            @RequestParam(required = false) LocalDateTime createdTo) {

        return ResponseEntity.ok(messageService.findAll(
                pageable, id, conversationId, senderId, type, seen, createdFrom, createdTo));
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(messageService.findById(id));
    }

    @GetMapping("/chat/{conversationId}")
    public ResponseEntity<?> getMessages(
        @PathVariable(name = "conversationId") Long conversationId,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ){

        return ResponseEntity.ok(messageService.getMessages(conversationId, pageable));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> create(@RequestBody @Valid MessageRequest request) {
        return ResponseEntity.ok(messageService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid UpdateMessageRequest request) {

        return ResponseEntity.ok(messageService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
