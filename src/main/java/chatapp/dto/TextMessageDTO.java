package chatapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TextMessageDTO  extends MessageDTO{

	private String text;

	public TextMessageDTO() {
		super.setMessageType(MessageType.TEXTMESSAGE);
	}

}
