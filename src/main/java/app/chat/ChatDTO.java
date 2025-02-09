package app.chat;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDTO {

	private Integer createdBy;
	private List<Integer>memberID;
	private boolean groupChat;
	private String chatName;
	private String chatID;
}
