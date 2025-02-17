package app.common;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import chat.app.security.auth.authentication.CustomUserDetail;
import chat.app.security.auth.dto.DeviceTokenDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@RequestScope
@Component
@Setter
public class RestRequestSession {

	
	public void setAuthData(Authentication auth) {
		if(auth.getPrincipal()==null) {
			throw new NullPointerException();
		}
		if(auth.getPrincipal()instanceof CustomUserDetail) {
			CustomUserDetail x=(CustomUserDetail)auth.getPrincipal();
			this.userID=Long.valueOf(x.getUsername());
			this.userIDStr=x.getUsername();
		}
		else {
			throw new UnsupportedOperationException("Cannot set Auth data inside RestRequestSession, Unknown Principal type: "+auth.getPrincipal().getClass().getName());
		}
	}
	private long userID;
	@Getter(value=AccessLevel.NONE)	
	private String userIDStr;
	public String getUserIdAsString() {
		return this.userIDStr;
	}
}
