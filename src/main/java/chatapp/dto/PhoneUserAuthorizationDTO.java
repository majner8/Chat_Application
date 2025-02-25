package chatapp.dto;

import lombok.Data;

@Data
public class PhoneUserAuthorizationDTO extends UserAuthorizationDTO {

	private String phoneNumber;

	@Override
	public String getUserName() {
		return this.phoneNumber;
	}
}
