package chat.app.security.auth.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import app.start.Start;
import chat.app.security.auth.authentication.CustomUserDetail;
import chat.app.security.auth.authentication.UserNamePasswordAuthentication;
import chat.app.security.database.UserRepository;
import chat.app.security.database.UserRepository.AuthProjection;
@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository repo;
	@Autowired
	private PasswordEncoder hashService;
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {   
		UserNamePasswordAuthentication aut=(UserNamePasswordAuthentication)authentication;
		Optional<AuthProjection> entPr=Optional.empty();
		switch(aut.getAuthorizationType()) {
			case EMAIL: 
				entPr=this.repo.findByEmail(aut.getName());
				break;
			case PHONE:
				entPr=this.repo.findByPhoneNumber(aut.getName());
				break;
		}
		if(entPr.isEmpty()) {
			throw new AuthenticationCredentialsNotFoundException("User is not found");
		}
		AuthProjection pr=entPr.get();
		if(!this.hashService.matches(aut.getCredentials().toString(), pr.getPassword())) {
	        throw new BadCredentialsException("Invalid username or password");
		}
		List<GrantedAuthority> permision=new ArrayList<>();
		if(!pr.getFinishRegistration()) {
			permision.add(new SimpleGrantedAuthority("ROLE_UNAUTHORIZED"));
		}
		CustomUserDetail user=CustomUserDetail.builder()
				.setUserName(String.valueOf(pr.getId()))
				.setPermission(permision)
				.setCompleteRegistration(pr.getFinishRegistration()?true:false)
				.build();
			return new UserNamePasswordAuthentication(user);
			
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return  UserNamePasswordAuthentication.class.isAssignableFrom(authentication);

	}

}
