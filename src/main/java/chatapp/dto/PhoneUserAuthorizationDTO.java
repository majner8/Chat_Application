package chatapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneUserAuthorizationDTO extends UserAuthorizationDTO {

	private String phoneNumber;

	@Override
	public String getUserName() {
		return this.phoneNumber;
	}
}
