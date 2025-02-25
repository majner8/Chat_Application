package chatapp.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TextMessageDTO.class, name = "text"),
})
public abstract class MessageDTO implements WebsocketMessageInterface {

	private LocalDateTime accepted;
	private String chatID;
	private String messageID;
	private MessageType messageType;
	private long order;
	private String senderID;
}
