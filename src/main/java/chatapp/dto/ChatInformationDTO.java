package chatapp.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatInformationDTO {

	private LocalDateTime createdAt;
	private long createdBy;
	private String chatID;
	private List<ChatMemberDTO> member;
}
