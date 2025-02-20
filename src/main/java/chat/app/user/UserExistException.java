package chat.app.user;

import chat.app.security.auth.dto.AuthorizationType;
import lombok.Getter;

@Getter
public class UserExistException extends RuntimeException{

	
	private String userName;
	private AuthorizationType authType;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserExistException(String message,String userName,AuthorizationType authType) {
		super(message);
		this.userName=userName;
		this.authType=authType;
	}

}
