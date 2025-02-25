package chatapp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ChatMemberDTO {

	private long userID;
	private String userName;
}
