package chatapp.dto;


import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailUserAuthorizationDTO extends UserAuthorizationDTO {

	@Email
	private String email;

	@Override
	public String getUserName() {
		return this.email;
	}
}
