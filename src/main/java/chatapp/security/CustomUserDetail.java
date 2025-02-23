package chatapp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Builder(setterPrefix="set")
public class CustomUserDetail implements UserDetails {

	@NonNull private final Collection<? extends GrantedAuthority> permission;
	@NonNull private final String userName;
	@Getter
	private final boolean completeRegistration;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.permission;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.completeRegistration;
	}

}
