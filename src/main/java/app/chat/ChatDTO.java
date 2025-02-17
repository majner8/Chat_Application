package app.chat;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ChatDTO {

	private Integer createdBy;
	private List<Long>memberID;
	private boolean groupChat;
	private String chatName;
	private String chatID;
}
