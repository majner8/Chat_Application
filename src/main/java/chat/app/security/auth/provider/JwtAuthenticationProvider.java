package chat.app.security.auth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import chat.app.security.auth.authentication.JwtAuthentication;
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		return new JwtAuthentication((JwtAuthentication)authentication,true);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return  JwtAuthentication.class.isAssignableFrom(authentication);
	}

}
