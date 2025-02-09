package chat.app.security.auth.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
@Getter
@Setter
@Accessors(chain = true)

public class TokenDTO {

	private String token;
	private LocalDateTime expiration;
	private LocalDateTime createdAt;
	private long deviceID;
	private long userID;
	private String userName;
	private boolean finishRegistration;
}
