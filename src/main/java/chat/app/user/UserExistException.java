package chat.app.user;

public class UserExistException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserExistException(String message) {
		super(message);
	}

}
