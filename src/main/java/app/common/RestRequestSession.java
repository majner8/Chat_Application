package app.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import chat.app.security.auth.dto.DeviceTokenDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@RequestScope
@Component
@Setter
public class RestRequestSession {

	private long deviceID;
	private long userID;
	private String uniqueID;

}
