package it.lucianofilippucci.university.techbazaar.controllers;

import it.lucianofilippucci.university.techbazaar.helpers.ChatSystem.ChatType;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.model.ResponseModel;
import it.lucianofilippucci.university.techbazaar.services.NotificationService;
import it.lucianofilippucci.university.techbazaar.services.UserService;
import it.lucianofilippucci.university.techbazaar.services.mongodb.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('STORE')")
    @PostMapping("/new")
    public ResponseEntity<ResponseModel> newMessage(@RequestParam("message") String message, @RequestParam("userId") int userId, @RequestParam("storeId") int storeId, @RequestParam("from") int from, @RequestParam("requestType") ChatType type, @RequestParam(value = "chatId", required = false) String chatId) {

        return ResponseEntity.ok(chatService.newMessage(userId, storeId, message, from, type, chatId));
    }

    @PreAuthorize("hasRole('STORE')")
    @PostMapping("/close")
    public ResponseEntity<ResponseModel> closeChat(@RequestParam("chatId") String chatId) {
        return this.chatService.closeChat(chatId);
    }
}
