package chatapp.dto;


import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EmailUserAuthorizationDTO extends UserAuthorizationDTO {

	@Email
	private String email;

	@Override
	public String getUserName() {
		return this.email;
	}
}
