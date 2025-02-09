package chat.app.security.auth;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationProvider implements AuthenticationProvider {

	@Autowired
	private JwtTokenGenerator tokenGenerator;
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if(authentication==null) return null;
		if(!(authentication.getCredentials() instanceof String))return null;
		String token=(String)authentication.getCredentials();
		UserDetails user=this.tokenGenerator.verifyToken(token);
		JwtAuthentication aut=JwtAuthentication.builder()
				.setFinishRegistration(user.isEnabled())
				.setToken(token)
				.setUser(user)
				.setUsername(user.getUsername())
				
				.build();
		return aut;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return false;
	}

	

}
 