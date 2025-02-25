package chatapp.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ChatInformationDTO {

	private long createdBy;
	private String chatID;
	private List<ChatMemberDTO> member;
	private LocalDateTime createdAt;
}
