package chatapp.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TextMessageDTO.class, name = "text"),
})
public abstract class MessageDTO implements WebsocketMessageInterface {
	
	private MessageType messageType;
	private String chatID;
	private String senderID;
	private LocalDateTime accepted;
	private long order;
	private String messageID;
}
