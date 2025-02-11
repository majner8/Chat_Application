package app.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import chat.app.security.auth.dto.DeviceTokenDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@RequestScope
@Component
@Setter
public class RestRequestSession {

	private long userID;
	@Getter(value=AccessLevel.NONE)	
	private String userIDStr;
	public String getUserIdAsString() {
		return this.userIDStr;
	}
}
