package chat.app.chat.mongodb.documents;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "order")

public class ChatMessageCollectionsDocument {
	@Id
	private String chatId;
	private int messageCount;
	private LocalDateTime lastMessage;
}
