package chat.app.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import chat.app.security.TokenGenerator;
import chat.app.security.auth.dto.DeviceTokenDTO;

@Controller
public class AuthentizationController {
	
	@Autowired
	private TokenGenerator generator;
	@PostMapping("/generateDeviceToken")
	public ResponseEntity<DeviceTokenDTO> getDeviceToken(){
		
		DeviceTokenDTO token=this.generator.generateToken();
		return ResponseEntity.ok(token);
	}
}
