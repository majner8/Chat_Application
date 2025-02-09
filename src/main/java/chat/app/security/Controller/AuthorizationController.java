package chat.app.security.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;

import app.common.RestRequestSession;
import chat.app.security.auth.JwtTokenGenerator;
import chat.app.security.auth.dto.EmailUserAuthorizationDTO;
import chat.app.security.auth.dto.PhoneUserAuthorizationDTO;
import chat.app.security.auth.dto.TokenDTO;
import chat.app.security.auth.dto.UserAuthorizationDTO;
import chat.app.security.auth.dto.UserFinishRegistrationDTO;
import chat.app.security.database.UserEntity;
import chat.app.security.database.UserRepository;
import chat.app.security.database.UserRepository.AuthProjection;
import jakarta.persistence.EntityNotFoundException;
@Controller
public class AuthorizationController {
	
	@Autowired
	private TransactionTemplate transaction;
	@Autowired
	private UserRepository repo;
	@Autowired
	private PasswordEncoder hashService;
	@Autowired
	private JwtTokenGenerator tokenGenerator;
	@Autowired
	private RestRequestSession session;

	@PostMapping("/public/register")
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
		TokenDTO token=this.tokenGenerator.generateToken(this.createUser(false, String.valueOf(persistEntity.getId())));
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	@PostMapping("/public/login")
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
		AuthProjection pr=entPr.get();
		TokenDTO token=this.tokenGenerator.generateToken(this.createUser(pr.getFinishRegistration(), String.valueOf(pr.getId())));

		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	
	@PostMapping("/authenticated/finishRegistration")
	public ResponseEntity<TokenDTO> finishRegistration(UserFinishRegistrationDTO dto){
		long id=this.session.getUserID();
		UserEntity entity=this.transaction.execute((status)->{
			
			UserEntity entPr=this.repo.findById(id)		
					.orElseThrow(()->new EntityNotFoundException ());
			
			entPr.setLastName(dto.getLastName())
			.setName(dto.getName())
			.setNickName(dto.getNickName())
			.setFinishRegistration(true);
		
			return entPr;
		});
		TokenDTO token=this.tokenGenerator.generateToken(this.createUser(true, String.valueOf(id)));

		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	

	private UserDetails createUser(boolean finishRegistration,String userName) {
		ArrayList<GrantedAuthority> list=new ArrayList<>();
		if(finishRegistration) {list.add(new SimpleGrantedAuthority("AUTHORIZED"));}
		
		return User.builder()
				.disabled(!finishRegistration)
				.username(userName)
				.authorities(list)
				.build()
				;
	}
}
