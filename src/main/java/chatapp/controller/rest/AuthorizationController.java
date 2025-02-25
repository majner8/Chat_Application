package chatapp.controller.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chatapp.dto.TokenDTO;
import chatapp.dto.UserAuthorizationDTO;
import chatapp.dto.UserFinishRegistrationDTO;
import chatapp.dto.UserServiceAuthDTO;
import chatapp.exception.UserExistException;
import chatapp.security.UserNamePasswordAuthentication;
import chatapp.service.JwtTokenGenerator;
import chatapp.service.UserService;
import chatapp.util.RestRequestSession;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthorizationController {
    static final Logger logger = LogManager.getLogger(AuthorizationController.class);



	@Autowired
	private  AuthenticationManager authenticationManager;
	@Autowired
	private RestRequestSession session;
	@Autowired
	private JwtTokenGenerator tokenGenerator;
	@Autowired
	private UserService userService;
	@PostMapping("/authenticated/finishRegistration")
	public ResponseEntity<TokenDTO> finishRegistration(@RequestBody UserFinishRegistrationDTO dto){

		UserServiceAuthDTO userID=this.userService.finishRegistration(dto);
		TokenDTO token=this.tokenGenerator.generateToken(userID);
		  logger.info("User finishRegistration id: %s",
	        		this.session.getUserIdAsString());
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	@ExceptionHandler(UserExistException.class)
	public ResponseEntity<?> handleUserExistException(UserExistException e, HttpServletRequest http) {
		logger.error("Registration failed: username already exist in database.");
		if(logger.isDebugEnabled()) {
			logger.debug("UserName: %s type: %s",e.getUserName(),e.getAuthType().toString());
		}
		return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();

	}

	@PostMapping("/public/login")
	public ResponseEntity<TokenDTO> login(@RequestBody UserAuthorizationDTO dto){
		if(logger.isDebugEnabled()) {
			logger.debug(String.format("Accept login request username: %s auth type: %s ipAdress: %s",
					dto.getUserName(),dto.getType().toString(),
					this.session.getIpAdress()));
		}

		UserNamePasswordAuthentication nameAuth=new UserNamePasswordAuthentication(dto);
		Authentication auth=this.authenticationManager.authenticate(nameAuth);
		TokenDTO token=this.tokenGenerator.generateToken(auth);
        logger.info("User login, send auth token. IP address: %s id: %s user complete registration: %s",
        		this.session.getIpAdress(),
        		this.session.getUserIdAsString(),
        		token.isFinishRegistration());

		return ResponseEntity.ok(token);	}
	@PostMapping("/public/register")
	public ResponseEntity<TokenDTO> register(@RequestBody UserAuthorizationDTO dto){
		if(logger.isDebugEnabled()) {
			logger.debug(String.format("Accept register request username: %s auth type: %s ipAdress: %s",
					dto.getUserName(),dto.getType().toString(),
					this.session.getIpAdress()));

		}
		UserServiceAuthDTO userID=this.userService.register(dto);
		TokenDTO token=this.tokenGenerator.generateToken(userID);
        logger.info("User created, send auth token. IP address: %s id: %s", this.session.getIpAdress(),this.session.getUserIdAsString());
		return ResponseEntity.ok(token);}


}
