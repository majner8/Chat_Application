package app.chat.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)

public abstract class MessageDTO implements WebsocketMessageInterface {
	private MessageType messageType;
	private String chatID;
	private String senderID;
	private LocalDateTime accepted;
	private long order;
	private String messageID;
}
