package chat.app.chat.mongodb.documents;

import java.time.LocalDateTime;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import app.chat.dto.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Document(collection = "messages")
@EqualsAndHashCode(callSuper = true)
@TypeAlias("TextMessage")
@Accessors(chain = true)
public class ChatTextMessageDocuments  extends ChatMessageDocuments{

	private String textMessage;

	public ChatTextMessageDocuments() {
		super.setType(MessageType.TEXTMESSAGE);
	}
	
}
