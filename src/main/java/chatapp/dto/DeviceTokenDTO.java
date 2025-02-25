package chatapp.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DeviceTokenDTO {

	private LocalDateTime createdAt;
	private long deviceID;
	private LocalDateTime expiredAt;
	private String token;
}
