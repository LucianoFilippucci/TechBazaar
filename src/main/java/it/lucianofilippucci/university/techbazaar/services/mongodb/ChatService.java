package it.lucianofilippucci.university.techbazaar.services.mongodb;

import it.lucianofilippucci.university.techbazaar.entities.mongodb.ChatEntity;
import it.lucianofilippucci.university.techbazaar.entities.mongodb.NotificationEntity;
import it.lucianofilippucci.university.techbazaar.helpers.ChatSystem.ChatType;
import it.lucianofilippucci.university.techbazaar.helpers.ChatSystem.Message;
import it.lucianofilippucci.university.techbazaar.helpers.Helpers;
import it.lucianofilippucci.university.techbazaar.helpers.ResponseMessage;
import it.lucianofilippucci.university.techbazaar.helpers.exceptions.ObjectNotFoundException;
import it.lucianofilippucci.university.techbazaar.repositories.ChatRepository;
import it.lucianofilippucci.university.techbazaar.services.NotificationService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private NotificationService notificationService;


    public ChatService(ChatRepository chatRepository, NotificationService notificationService) {
        this.chatRepository = chatRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public ResponseEntity<ResponseMessage<String>> newMessage(int userId, int storeId, String message, int from, ChatType type, String chatId) {
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
        if(chatEntity.get().getChatId().equals(new ObjectId(chatId)) && chatEntity.get().isClosed()) return new ResponseEntity<>(new ResponseMessage<>("Store Closed Chat. Open Another one.").setIsError(true), HttpStatus.OK);
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
            this.notificationService.sendMessage(from, to, "New Message.", "You got a new Message.");
        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage<>("GenericError -> newMessage()").setIsError(true), HttpStatus.BAD_REQUEST);
        }
        if(this.chatRepository.save(chatEntity.get()).getChatId() != null) return new ResponseEntity<>(new ResponseMessage<>("OK").setIsError(false), HttpStatus.OK);
        return new ResponseEntity<>(new ResponseMessage<>("GenericError -> newMessage()").setIsError(true), HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public ResponseEntity<ResponseMessage<String>> closeChat(String chatId) {
        Optional<ChatEntity> optional = this.chatRepository.findById(new ObjectId(chatId));
        if(optional.isEmpty()) return new ResponseEntity<>(new ResponseMessage<>("ChatNotFound").setIsError(true), HttpStatus.BAD_REQUEST);
        optional.get().setClosed(true);
        try {
            this.notificationService.sendMessage(0, optional.get().getUserId(), "Chat closed.", "The Store closed the chat.");
        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage<>("ChatClosed.").setIsError(false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage<>("ChatClosed.").setIsError(false), HttpStatus.OK);
    }
}
