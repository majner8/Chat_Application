package chat.app.security.auth.dto;


import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class EmailUserAuthorizationDTO extends UserAuthorizationDTO {

	@Email
	private String email;
}
