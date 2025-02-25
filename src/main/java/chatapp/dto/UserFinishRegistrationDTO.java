package chatapp.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserFinishRegistrationDTO {

	private String lastName;
	private String name;
	private String nickName;

}
