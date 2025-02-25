package chatapp.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    static final Logger logger = LogManager.getLogger(JwtAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		logger.trace("Call jwtAuthenticationProvider username: "+authentication.getName());
		return new JwtAuthentication((JwtAuthentication)authentication,true);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return  JwtAuthentication.class.isAssignableFrom(authentication);
	}

}
