package chat.app.security.auth.dto;

import lombok.Getter;

@Getter
public abstract class UserAuthorizationDTO {
	private String password;
	private AuthorizationType type;
}
