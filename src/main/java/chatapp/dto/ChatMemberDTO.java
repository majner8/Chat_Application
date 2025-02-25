package chatapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatMemberDTO {

	private long userID;
	private String userName;
}
