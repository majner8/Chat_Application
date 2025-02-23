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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "messageType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TextMessageDTO.class, name = "TextMessage")
})
public abstract class MessageDTO implements WebsocketMessageInterface {
	private String messageType;
	private String chatID;
	private String senderID;
	private LocalDateTime accepted;
	private long order;
	private String messageID;
}
