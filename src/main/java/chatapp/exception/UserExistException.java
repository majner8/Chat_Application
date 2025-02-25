package chatapp.exception;

import chatapp.dto.AuthorizationType;
import lombok.Getter;

@Getter
public class UserExistException extends RuntimeException{


	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private AuthorizationType authType;
	private String userName;
	public UserExistException(String message,String userName,AuthorizationType authType) {
		super(message);
		this.userName=userName;
		this.authType=authType;
	}

}
