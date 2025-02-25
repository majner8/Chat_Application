package chatapp.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatDTO {

	private Long createdBy;
	private boolean groupChat;
	private String chatID;
	private String chatName;
	private List<Long>memberID;
}
