package chat.app.security.auth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import app.common.IdGenerator;
import chat.app.security.auth.dto.TokenDTO;
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
	public TokenDTO generateToken(UserDetails userDetails) {
		LocalDateTime createdAt=LocalDateTime.now();
		LocalDateTime expiration=createdAt.plus(userExpiration);
		boolean finishRegistration=userDetails.isEnabled();
		String userName=userDetails.getUsername();
		List<? extends GrantedAuthority> role=userDetails.getAuthorities().stream().
				map(v->{
					return v;
				}).toList();
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
	
	public UserDetails verifyToken(String token) {

		
		DecodedJWT jwt=JWT.require(tokenAlg)
				.build().verify(token);
		boolean finishRegistration=jwt.getClaim(this.finishRegistrationHeader).asBoolean();
		String userName=jwt.getClaim(this.userNameHeader).asString();
		Collection<? extends GrantedAuthority> role=jwt.getClaim(this.authRoleHeader).asList(GrantedAuthority.class);
		
				UserDetails user=User.builder()
						.username(userName)
						.disabled(!finishRegistration)
						.authorities(role)
						.build();
				return user;	}
}
