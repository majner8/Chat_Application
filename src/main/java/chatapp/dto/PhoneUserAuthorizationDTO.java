package chatapp.dto;

import lombok.Getter;

@Getter
public class PhoneUserAuthorizationDTO extends UserAuthorizationDTO {

	private String phoneNumber;

	@Override
	public String getUserName() {
		return this.phoneNumber;
	}
}
