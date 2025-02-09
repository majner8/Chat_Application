package chat.app.security;

import chat.app.security.auth.dto.DeviceTokenDTO;
import chat.app.security.auth.dto.TokenDTO;

public interface TokenGenerator {

	public TokenDTO generateToken(long userID,long deviceID,boolean finishRegistration);
	public DeviceTokenDTO generateToken();
	
	public TokenDTO validateUserToken(String token);
	public DeviceTokenDTO validateDeviceToken(String token);

}
