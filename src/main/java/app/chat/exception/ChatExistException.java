package app.chat.exception;

import app.chat.ChatDTO;
import app.common.EntityExistException.EntityExistExceptionType;
import lombok.Getter;

@Getter
public class ChatExistException extends RuntimeException {
	private ChatDTO dto;
	public ChatExistException(Exception e,ChatDTO dto) {
		super (e);
		this.dto=dto;
	}
	
}