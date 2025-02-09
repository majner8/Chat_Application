package chat.app.security;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import app.common.IdGenerator;
import chat.app.security.auth.dto.DeviceTokenDTO;
import chat.app.security.auth.dto.TokenDTO;


@Component
public class JwtTokenGeneratorImpl implements TokenGenerator{

	@Value("${security.token.userexpiration}")
	private Duration userExpiration;
	@Value("${security.token.deviceExpiration}")
	private Duration deviceExpiration;
	@Value("${security.token.key}")
    private String tokenKey;
	@Autowired
	private IdGenerator idGenerator;
	private Algorithm tokenAlg;

	private final String finishRegistrationHeader="finishRegistration";
	private final String userIDHeader="userID";
	private final String deviceIDHeader="deviceID";
	private final String userHeader="userName";

	@PostConstruct
	public void postConstruct() {
		this.tokenAlg=Algorithm.HMAC512(tokenKey);
	}
	
	@Override
	public TokenDTO generateToken(long userID, long deviceID, boolean finishRegistration) {
		LocalDateTime createdAt=LocalDateTime.now();
		LocalDateTime expiration=createdAt.plus(userExpiration);
		String userName=IdGenerator.generateUniqueUserDeviceId(deviceID, userID);

		String token=JWT.create()
		.withClaim(this.finishRegistrationHeader, finishRegistration)
		.withClaim(this.userHeader, userID)
		.withClaim(this.deviceIDHeader, deviceID)
		.withClaim(this.userIDHeader, userName)
		
		.sign(this.tokenAlg);
		
		TokenDTO tokenDTO=		new TokenDTO()
				.setCreatedAt(createdAt)
				.setDeviceID(deviceID)
				.setExpiration(expiration)
				.setFinishRegistration(finishRegistration)
				.setUserID(userID)
				.setUserName(userName)
				.setToken(token)
				;
		return tokenDTO;	}

	@Override
	public DeviceTokenDTO generateToken() {
		LocalDateTime createdAt=LocalDateTime.now();
		LocalDateTime expiration=createdAt.plus(this.deviceExpiration);
		long deviceID=this.idGenerator.generateDeviceID();

		String token=JWT.create()
				.withClaim(this.deviceIDHeader, deviceID)
				.withExpiresAt(expiration.toInstant(ZoneOffset.UTC))
				.sign(this.tokenAlg);
		
		DeviceTokenDTO tokenDTO=new DeviceTokenDTO()
				.setCreatedAt(createdAt)
				.setExpiredAt(expiration)
				.setDeviceID(deviceID)
				.setToken(token);			
				return tokenDTO;
	}

	@Override
	public TokenDTO validateUserToken(String token) {
		DecodedJWT jwt=JWT.require(tokenAlg)
		.build().verify(token)
		;
		LocalDateTime expiration=jwt.getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();		
		long userID=jwt.getClaim(this.userIDHeader).asLong();
		long deviceID=jwt.getClaim(this.deviceIDHeader).asLong();
		boolean finishRegistration=jwt.getClaim(this.finishRegistrationHeader)
				.asBoolean();
		String userName=jwt.getClaim(IdGenerator.generateUniqueUserDeviceId(deviceID, userID)).asString();
		TokenDTO tokenDTO=		new TokenDTO()
				.setDeviceID(deviceID)
				.setExpiration(expiration)
				.setFinishRegistration(finishRegistration)
				.setUserID(userID)
				.setUserName(userName)
				.setToken(token)
				;
		return tokenDTO;
	}

	@Override
	public DeviceTokenDTO validateDeviceToken(String token) {
		DecodedJWT jwt=JWT.require(tokenAlg)
				.build().verify(token)
				;	
		LocalDateTime expiration=jwt.getExpiresAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		long deviceID=jwt.getClaim(this.deviceIDHeader).asLong();

		DeviceTokenDTO tokenDTO=new DeviceTokenDTO()
				.setExpiredAt(expiration)
				.setDeviceID(deviceID)
				.setToken(token);			
			return tokenDTO;
		
	}

}
