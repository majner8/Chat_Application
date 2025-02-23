package chatapp.exception;

import chatapp.dto.ChatDTO;
import chatapp.exception.EntityExistException.EntityExistExceptionType;
import lombok.Getter;

@Getter
public class ChatExistException extends RuntimeException {
	private ChatDTO dto;
	public ChatExistException(Exception e,ChatDTO dto) {
		super (e);
		this.dto=dto;
	}
	
}