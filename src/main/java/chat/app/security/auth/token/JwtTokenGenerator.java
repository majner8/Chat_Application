package chat.app.security.auth.token;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import app.common.IdGenerator;
import chat.app.security.auth.authentication.CustomUserDetail;
import chat.app.security.auth.authentication.JwtAuthentication;
import chat.app.security.auth.authentication.UserNamePasswordAuthentication;
import chat.app.security.auth.dto.TokenDTO;
import chat.app.user.UserServiceAuthDTO;
import jakarta.annotation.PostConstruct;
@Service
public class JwtTokenGenerator {

	@Value("${security.token.userexpiration}")
	private Duration userExpiration;
	@Value("${security.token.deviceExpiration}")
	private Duration deviceExpiration;
	@Value("${security.token.key}")
    private String tokenKey;

	private Algorithm tokenAlg;

	private final String finishRegistrationHeader="finishRegistration";
	private final String userNameHeader="userName";
	private final String authRoleHeader="role";
	
	@PostConstruct
	public void postConstruct() {
		this.tokenAlg=Algorithm.HMAC512(tokenKey);
	}
	public TokenDTO generateToken(Authentication auth) {
		if(!auth.isAuthenticated()) {
			throw new IllegalArgumentException("Cannot create token for unauthenticated user");
		}
		CustomUserDetail user=((CustomUserDetail)auth.getPrincipal());
		return this.generateToken(user.isCompleteRegistration(), user.getUsername(), 
				user.getAuthorities().stream().map((v)->{
					return v;
				}).toList()
				);
	}
	
	public TokenDTO generateToken(UserServiceAuthDTO dto) {
		return this.generateToken(false, dto.getId(),dto.getRole());
	}
	
	
	private TokenDTO generateToken(boolean completeRegistration,String userName,List<? extends GrantedAuthority> role) {
		LocalDateTime createdAt=LocalDateTime.now();
		LocalDateTime expiration=createdAt.plus(userExpiration);
		boolean finishRegistration=completeRegistration;
	
		String token=JWT.create()
		.withClaim(this.finishRegistrationHeader, finishRegistration)
		.withClaim(this.userNameHeader, userName)
		.withClaim(this.authRoleHeader, role)
		.sign(this.tokenAlg);
		
		TokenDTO tokenDTO=		new TokenDTO()
				.setCreatedAt(createdAt)
				.setExpiration(expiration)
				.setFinishRegistration(finishRegistration)
				.setToken(token)
				.setUserName(userName)
				;
		return tokenDTO;
	}
	public JwtAuthentication verifyToken(String token) {

		
		DecodedJWT jwt=JWT.require(tokenAlg)
				.build().verify(token);
		boolean finishRegistration=jwt.getClaim(this.finishRegistrationHeader).asBoolean();
		String userName=jwt.getClaim(this.userNameHeader).asString();
		Collection<? extends GrantedAuthority> role=jwt.getClaim(this.authRoleHeader).asList(GrantedAuthority.class);
		
		CustomUserDetail user=CustomUserDetail.builder()
				.setUserName(userName)
				.setPermission(role)
				.setCompleteRegistration(finishRegistration)
				.build();
			return JwtAuthentication.builder()
					.setFinishRegistration(finishRegistration)
					.setJwtToken(token)
					.setUser(user).build();
				}
}
