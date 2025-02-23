package chatapp.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import chatapp.dto.AuthorizationType;
import chatapp.dto.UserAuthorizationDTO;
import lombok.Builder;

public class UserNamePasswordAuthentication implements Authentication {



	public UserNamePasswordAuthentication(CustomUserDetail userDetail) {
		this.userDetail = userDetail;
		this.authenticationDTO = null;
		this.isAuthenticated=true;
	
	}public UserNamePasswordAuthentication(UserAuthorizationDTO authenticationDTO
			) {
		this.userDetail=null;
		this.authenticationDTO=authenticationDTO;
		this.isAuthenticated=false;

			
		
	}

	private final CustomUserDetail userDetail;
	private final boolean isAuthenticated;
	private final UserAuthorizationDTO authenticationDTO;

	public AuthorizationType getAuthorizationType() {
		if(this.authenticationDTO==null) return null;
		return this.authenticationDTO.getType();
	}
	
	public boolean isFinishRegistration() {
		if(!this.isAuthenticated) return false;
		return this.userDetail.isCompleteRegistration();
	}
	@Override
	public String getName() {
		if(!isAuthenticated) {
			return this.authenticationDTO.getUserName();
		}
		return this.userDetail.getUsername();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(!this.isAuthenticated) {
			return this.userDetail.getAuthorities();
		}
		return null;
	}

	@Override
	public Object getCredentials() {

		if(this.isAuthenticated) {
			return null;
		}
		return this.authenticationDTO.getPassword();

	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.userDetail;
	}

	@Override
	public boolean isAuthenticated() {
		return this.isAuthenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

        throw new IllegalArgumentException("Cannot set authentication directly");

	}

}
