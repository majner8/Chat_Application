package chat.app.security.auth;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;

import app.common.RestRequestSession;
import chat.app.security.TokenGenerator;
import chat.app.security.auth.dto.EmailUserAuthorizationDTO;
import chat.app.security.auth.dto.PhoneUserAuthorizationDTO;
import chat.app.security.auth.dto.TokenDTO;
import chat.app.security.auth.dto.UserAuthorizationDTO;
import chat.app.security.auth.dto.UserFinishRegistrationDTO;
import chat.app.security.database.UserEntity;
import chat.app.security.database.UserRepository;
import chat.app.security.database.UserRepository.AuthProjection;
@Controller
public class AuthorizationController {
	
	@Autowired
	private TransactionTemplate transaction;
	@Autowired
	private UserRepository repo;
	@Autowired
	private PasswordEncoder hashService;
	@Autowired
	private TokenGenerator tokenGenerator;
	@Autowired
	private RestRequestSession session;
	@PostMapping("/register")
	public ResponseEntity<TokenDTO> register(UserAuthorizationDTO dto){
		UserEntity ent=new UserEntity();
		switch(dto.getType()) {
			case EMAIL: 
				EmailUserAuthorizationDTO email=(EmailUserAuthorizationDTO)dto;
				if(this.repo.existsByEmail(email.getEmail())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				ent.
				setEmail(email.getEmail());
				break;
			case PHONE:
				PhoneUserAuthorizationDTO phone=(PhoneUserAuthorizationDTO)dto;
				if(this.repo.existsByPhoneNumberAndCountryCode(phone.getPhoneNumber(),phone.getCountryCode())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				ent.
				setPhoneNumber(phone.getPhoneNumber())
				.setCountryCode(phone.getCountryCode());
				break;
		}
		String hashPassword=this.hashService.encode(dto.getPassword());
		ent.setPassword(hashPassword);
		UserEntity persistEntity;
		try {
			persistEntity = this.repo.save(ent);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		TokenDTO token=this.tokenGenerator.generateToken(persistEntity.getId(), this.session.getDeviceID(),false);
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(UserAuthorizationDTO dto){
		Optional<AuthProjection> entPr=Optional.empty();
		switch(dto.getType()) {
			case EMAIL: 
				EmailUserAuthorizationDTO email=(EmailUserAuthorizationDTO)dto;
				entPr=this.repo.findByEmail(email.getEmail());
				break;
			case PHONE:
				PhoneUserAuthorizationDTO phone=(PhoneUserAuthorizationDTO)dto;
				entPr=this.repo.findByPhoneNumberAndCountryCode(phone.getPhoneNumber(), phone.getCountryCode());
				break;
		}
		if(entPr.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		if(!this.hashService.matches(dto.getPassword(),entPr.get().getPassword())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		TokenDTO token=this.tokenGenerator.generateToken(entPr.get().getId(), this.session.getDeviceID(),entPr.get().getFinishRegistration());
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}

	public ResponseEntity<TokenDTO> finishRegistration(UserFinishRegistrationDTO dto){
		long id=this.session.getUserID();
		UserEntity entity=this.transaction.execute((status)->{
			UserEntity entPr=this.repo.findById(id).orElseThrow(()->new EntityNotFoundException ());
			
			entPr.setLastName(dto.getLastName())
			.setName(dto.getName())
			.setNickName(dto.getNickName())
			.setFinishRegistration(true);
		
			return entPr;
		});
		TokenDTO token=this.tokenGenerator.generateToken(id, this.session.getDeviceID(),true);
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	
}
