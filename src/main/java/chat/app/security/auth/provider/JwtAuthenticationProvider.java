package chat.app.security.auth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import app.start.Start;
import chat.app.security.auth.authentication.JwtAuthentication;
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Start.logger.debug("jwtAuthenticationProvider active");
		return new JwtAuthentication((JwtAuthentication)authentication,true);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return  JwtAuthentication.class.isAssignableFrom(authentication);
	}

}
