package chat.app.security.Controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import app.common.RestRequestSession;
import app.start.Start;
import chat.app.security.auth.authentication.UserNamePasswordAuthentication;
import chat.app.security.auth.dto.EmailUserAuthorizationDTO;
import chat.app.security.auth.dto.PhoneUserAuthorizationDTO;
import chat.app.security.auth.dto.TokenDTO;
import chat.app.security.auth.dto.UserAuthorizationDTO;
import chat.app.security.auth.dto.UserFinishRegistrationDTO;
import chat.app.security.auth.token.JwtTokenGenerator;
import chat.app.security.database.UserEntity;
import chat.app.security.database.UserRepository;
import chat.app.security.database.UserRepository.AuthProjection;
import chat.app.user.UserExistException;
import chat.app.user.UserService;
import chat.app.user.UserServiceAuthDTO;
import jakarta.persistence.EntityNotFoundException;

@RestController
public class AuthorizationController {
	
	
	
	@Autowired
	private JwtTokenGenerator tokenGenerator;
	
	@Autowired
	private  AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@PostMapping("/public/register")
	public ResponseEntity<TokenDTO> register(@RequestBody UserAuthorizationDTO dto){
		UserServiceAuthDTO userID=this.userService.register(dto);
		TokenDTO token=this.tokenGenerator.generateToken(userID);
		return ResponseEntity.ok(token);}
	@PostMapping("/public/login")
	public ResponseEntity<TokenDTO> login(@RequestBody UserAuthorizationDTO dto){
		UserNamePasswordAuthentication nameAuth=new UserNamePasswordAuthentication(dto);
		Authentication auth=this.authenticationManager.authenticate(nameAuth);
		TokenDTO token=this.tokenGenerator.generateToken(auth);
		return ResponseEntity.ok(token);	}
	
	@PostMapping("/authenticated/finishRegistration")
	public ResponseEntity<TokenDTO> finishRegistration(@RequestBody UserFinishRegistrationDTO dto){
		UserServiceAuthDTO userID=this.userService.finishRegistration(dto);
		TokenDTO token=this.tokenGenerator.generateToken(userID);
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	@ExceptionHandler(UserExistException.class)
	public ResponseEntity<?> handleUserExistException(UserExistException e) {
		// log exception
		
		
		
		
		return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();

	}

	
}
