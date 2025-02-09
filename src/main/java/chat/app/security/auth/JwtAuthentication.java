package chat.app.security.auth;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

import java.util.Collection;
@AllArgsConstructor
@Builder(setterPrefix="set")
public class JwtAuthentication implements Authentication {
	private final UserDetails user;
    private final String username;
    private boolean finishRegistration;
    private final String token;
    private final Collection<? extends GrantedAuthority> authorities;
    @Override
    public String getName() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return this.finishRegistration;
    }

    

    @Override
    public Object getDetails() {
        return this.user;
    }

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.finishRegistration=true;		
	}
}
