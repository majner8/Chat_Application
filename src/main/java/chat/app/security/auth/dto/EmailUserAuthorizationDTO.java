package chat.app.security.auth.dto;

import javax.validation.constraints.Email;

import lombok.Getter;

@Getter
public class EmailUserAuthorizationDTO extends UserAuthorizationDTO {

	@Email
	private String email;
}
