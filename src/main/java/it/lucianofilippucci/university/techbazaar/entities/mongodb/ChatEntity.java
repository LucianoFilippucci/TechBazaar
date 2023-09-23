package it.lucianofilippucci.university.techbazaar.entities.mongodb;

import it.lucianofilippucci.university.techbazaar.helpers.ChatSystem.ChatType;
import it.lucianofilippucci.university.techbazaar.helpers.ChatSystem.Message;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chats")
public class ChatEntity {
    @MongoId
    private ObjectId chatId;
    private List<Message> messageList;
    private int storeId;
    private int userId;
    private ChatType chatType;
    private boolean isClosed;
}
