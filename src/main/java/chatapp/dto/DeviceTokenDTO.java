package chatapp.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)

public class DeviceTokenDTO {

	private String token;
	private long deviceID;
	private LocalDateTime createdAt;
	private LocalDateTime expiredAt;
}
