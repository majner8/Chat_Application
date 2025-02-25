package chatapp.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class TokenDTO {
	private LocalDateTime createdAt;
	private LocalDateTime expiration;
	private boolean finishRegistration;
	private String token;
	private String userName;
}
