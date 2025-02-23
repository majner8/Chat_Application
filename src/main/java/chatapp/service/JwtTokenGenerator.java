package chatapp.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import chatapp.dto.TokenDTO;
import chatapp.dto.UserServiceAuthDTO;
import chatapp.main.Start;
import chatapp.security.CustomUserDetail;
import chatapp.security.JwtAuthentication;
import chatapp.security.UserNamePasswordAuthentication;
import chatapp.util.IdGenerator;
import jakarta.annotation.PostConstruct;
@Service
public class JwtTokenGenerator {

    static final Logger logger = LogManager.getLogger(JwtTokenGenerator.class);

	@Value("${security.token.userexpiration}")
	private Duration userExpiration;
	@Value("${security.token.deviceExpiration}")
	private Duration deviceExpiration;
	@Value("${security.token.key}")
    private String tokenKey;
	@Value("${security.token.preflix}")
	private String tokenPreflix;

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
		
		return this.generateToken(dto.isFinishRegistration(), dto.getId(),dto.getRole());
	}
	
	
	private TokenDTO generateToken(boolean completeRegistration,String userName,List<? extends GrantedAuthority> role) {
		LocalDateTime createdAt=LocalDateTime.now();
		LocalDateTime expiration=createdAt.plus(userExpiration);
		boolean finishRegistration=completeRegistration;
		
		String token=JWT.create()
		.withClaim(this.finishRegistrationHeader, finishRegistration)
		.withClaim(this.userNameHeader, userName)
		.withClaim(this.authRoleHeader, role
				.stream().map((r)->{
					return r.getAuthority();
				}).toList()
				)
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

		token=token.replaceFirst(this.tokenPreflix, "").trim();
		DecodedJWT jwt=JWT.require(tokenAlg)
				.build().verify(token);
		boolean finishRegistration=jwt.getClaim(this.finishRegistrationHeader).asBoolean();
		String userName=jwt.getClaim(this.userNameHeader).asString();
		StringBuilder build=new StringBuilder();
		if(logger.isTraceEnabled()) {
			build.append(String.format("Verify jwt token username:"
					+ " %s finishRegistration: Role: {", userName,finishRegistration));
		}
		Collection<? extends GrantedAuthority> role=jwt.getClaim(this.authRoleHeader).asList(String.class).stream().map((v)->{
			SimpleGrantedAuthority x= new SimpleGrantedAuthority(v);
			if(logger.isTraceEnabled()) {
				build.append(x.toString()+",");
			}
			return x;
			
		}).toList();
		if(logger.isTraceEnabled()) {
			build.append("}");
		}
		if(logger.isTraceEnabled()) {
			logger.trace(build.toString());
		}
		CustomUserDetail user=CustomUserDetail.builder()
				.setUserName(userName)
				.setPermission(role)
				.setCompleteRegistration(finishRegistration)
				.build();
			return JwtAuthentication.builder()
					.setFinishRegistration(finishRegistration)
					.setJwtToken(token)
					.setIsAuthentication(false)
					.setUser(user).build();
				}
}
