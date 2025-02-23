package chatapp.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class TextMessageDTO  extends MessageDTO{

	private String text;

	public TextMessageDTO() {
		super.setMessageType(MessageType.TEXTMESSAGE);
	}
	
}
