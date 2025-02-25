package chatapp.util;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import chatapp.security.CustomUserDetail;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Stores and shares data across different layers during an HTTP request.
 * <p>
 * This class is request-scoped, meaning a new instance is created for each HTTP request.
 * </p>
 * <p><b>Note:</b> This class is not automatically populated. The caller must ensure that
 * data is set in the first layer of request processing.</p>
 */
@Getter
@RequestScope
@Component
public class RestRequestSession {


	private String ipAdress;
	private long userID;
	@Getter(value=AccessLevel.NONE)
	private String userIDStr;

	public String getUserIdAsString() {
		return this.userIDStr;
	}
	public void setAuthData(Authentication auth,String ipAdress) {
		if(auth.getPrincipal()==null) {
			throw new NullPointerException();
		}
		if(auth.getPrincipal()instanceof CustomUserDetail) {
			CustomUserDetail x=(CustomUserDetail)auth.getPrincipal();
			this.userID=Long.valueOf(x.getUsername());
			this.userIDStr=x.getUsername();
			this.ipAdress=ipAdress;
		}
		else {
			throw new UnsupportedOperationException("Cannot set Auth data inside RestRequestSession, Unknown Principal type: "+auth.getPrincipal().getClass().getName());
		}
	}
}
