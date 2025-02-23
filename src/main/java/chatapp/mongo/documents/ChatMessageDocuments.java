package chatapp.mongo.documents;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import chatapp.dto.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "messages")
@CompoundIndex(def = "{'chatId': 1, 'order': 1}", unique = true)  // Unikátní kombinace
public abstract class ChatMessageDocuments {
    @Indexed
	private String chatId;
    @Indexed
	private long order;
	@Id
	private String messageID;
	private String sender;
	private LocalDateTime accepted;
	private MessageType type;
}
