package chatapp.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
@AllArgsConstructor
@Getter(AccessLevel.PRIVATE)
@Builder(setterPrefix="set")
public class JwtAuthentication implements Authentication {
	private final boolean finishRegistration;
	private final boolean isAuthentication;
	@NonNull private final String jwtToken;
	@NonNull private final CustomUserDetail user;

	public JwtAuthentication(JwtAuthentication jwtAuthentication ,boolean isAuthentication) {
		this.user = jwtAuthentication.getUser();
		this.jwtToken =jwtAuthentication.getJwtToken();
		this.isAuthentication = isAuthentication;
		this.finishRegistration=jwtAuthentication.isFinishRegistration();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.user.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return this.jwtToken;
	}

	@Override
	public Object getDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.user.getUsername();
	}

	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return this.user;
	}


	@Override
	public boolean isAuthenticated() {
		return this.isAuthentication;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Cannot set authentication directly");

	}
}
