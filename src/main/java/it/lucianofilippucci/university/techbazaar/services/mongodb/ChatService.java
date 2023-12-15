package it.lucianofilippucci.university.techbazaar.services.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.ChatEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.NotificationEntity;
import it.lucianofilippucci.university.techbazaar.helpers.ChatSystem.ChatType;
import it.lucianofilippucci.university.techbazaar.helpers.ChatSystem.Message;
import it.lucianofilippucci.university.techbazaar.helpers.Helpers;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.helpers.model.ResponseModel;
import it.lucianofilippucci.university.techbazaar.repositories.ChatRepository;
import it.lucianofilippucci.university.techbazaar.services.NotificationService;
import it.lucianofilippucci.university.techbazaar.services.UnifiedAccessService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;


    private final UnifiedAccessService unifiedAccessService;


    @Transactional
    public ResponseModel newMessage(int userId, int storeId, String message, int from, ChatType type, String chatId) {
        Optional<ChatEntity> chatEntity = this.chatRepository.findByUserIdAndStoreId(userId, storeId);
        if(chatEntity.isEmpty()) {
            ChatEntity newChat = new ChatEntity();
            newChat.setMessageList(new ArrayList<>());
            newChat.setUserId(userId);
            newChat.setStoreId(storeId);
            newChat.setChatType(type);
            newChat.setMessageList(new ArrayList<>());
            chatEntity = Optional.of(this.chatRepository.save(newChat));
        }
        if(chatEntity.get().getChatId().equals(new ObjectId(chatId)) && chatEntity.get().isClosed()) return
                ResponseModel.builder()
                        .reason("Chat Closed, Open Another One.")
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .status(HttpStatus.BAD_REQUEST)
                        .timeStamp(LocalDateTime.now())
                        .build();
        Message message1 = new Message();
        message1.setMessageId(Helpers.GenerateUID());
        message1.setSent(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        message1.setMessage(message);
        message1.setRead(false);
        message1.setOwnerId(from);
        chatEntity.get().getMessageList().add(message1);
        chatEntity.get().setClosed(false);
        try {
            int to;
            if(from == userId) to = storeId; else to = userId;
            unifiedAccessService.sendNotification(from, to, "New Message.", "You got a new Message.");
        } catch (ObjectNotFoundException e) {
            return ResponseModel.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(HttpStatus.NOT_FOUND)
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .reason("User Not found to Send Notification")
                    .build();
        }
        if(this.chatRepository.save(chatEntity.get()).getChatId() != null) return ResponseModel.builder().timeStamp(LocalDateTime.now()).status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).message("Message Sent.").build();
        return ResponseModel.builder().statusCode(HttpStatus.BAD_REQUEST.value()).status(HttpStatus.BAD_REQUEST).reason("Generic Error.").timeStamp(LocalDateTime.now()).build();
    }

    @Transactional
    public ResponseEntity<ResponseModel> closeChat(String chatId) {
        Optional<ChatEntity> optional = this.chatRepository.findById(new ObjectId(chatId));
        if(optional.isEmpty()) return ResponseEntity.ok(ResponseModel.builder().status(HttpStatus.NOT_FOUND).statusCode(HttpStatus.NOT_FOUND.value()).reason("Chat Not found.").timeStamp(LocalDateTime.now()).build());
        optional.get().setClosed(true);
        try {
            unifiedAccessService.sendNotification(0, optional.get().getUserId(), "Chat closed.", "The Store closed the chat.");
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .reason("User Not found.")
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Chat Closed")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
