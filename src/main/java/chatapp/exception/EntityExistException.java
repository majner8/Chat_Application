package chatapp.exception;

import lombok.Getter;

@Getter
public class EntityExistException extends RuntimeException {
	public static enum EntityExistExceptionType{
		CREATE_CHAT_GROUP,	CREATE_CHAT_SINGLE
	}
	private EntityExistExceptionType exceptionType;
	private String chatID;

	public EntityExistException(Exception e,EntityExistExceptionType exceptionType,String chatID) {
		super (e);
		this.exceptionType=exceptionType;
		this.chatID=chatID;
	}
}
