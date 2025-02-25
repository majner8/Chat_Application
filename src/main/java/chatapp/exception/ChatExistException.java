package chatapp.exception;

import chatapp.dto.ChatDTO;
import lombok.Getter;

@Getter
public class ChatExistException extends RuntimeException {
	private ChatDTO dto;
	public ChatExistException(Exception e,ChatDTO dto) {
		super (e);
		this.dto=dto;
	}

}