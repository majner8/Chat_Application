package chatapp.mongo.documents;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import chatapp.dto.MessageType;
import lombok.Data;

@Data
@Document(collection = "messages")
@CompoundIndex(def = "{'chatId': 1, 'order': 1}", unique = true)
public abstract class ChatMessageDocuments {
    private LocalDateTime accepted;
    @Indexed
	private String chatId;
	@Id
	private String messageID;
	@Indexed
	private long order;
	private String sender;
	private MessageType type;
}
