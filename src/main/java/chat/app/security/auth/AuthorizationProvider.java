package chat.app.security.auth;

import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthorizationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if(authentication==null) return null;
		if(!(authentication.getCredentials() instanceof String))return null;
		String token=(String)authentication.getCredentials();
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return false;
	}

	

}
 