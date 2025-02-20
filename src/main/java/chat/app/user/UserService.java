package chat.app.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import app.common.RestRequestSession;
import chat.app.security.auth.dto.AuthorizationType;
import chat.app.security.auth.dto.EmailUserAuthorizationDTO;
import chat.app.security.auth.dto.PhoneUserAuthorizationDTO;
import chat.app.security.auth.dto.TokenDTO;
import chat.app.security.auth.dto.UserAuthorizationDTO;
import chat.app.security.auth.dto.UserFinishRegistrationDTO;
import chat.app.security.auth.token.JwtTokenGenerator;
import chat.app.security.database.UserEntity;
import chat.app.security.database.UserRepository;
import jakarta.persistence.EntityNotFoundException;
@Component
public class UserService {
	
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

	public UserServiceAuthDTO register(UserAuthorizationDTO dto)  {
		UserEntity ent=new UserEntity();
		UserExistException ex;
		switch(dto.getType()) {
			case EMAIL: 
				if(this.repo.existsByEmail(dto.getUserName())) throw this.generateUserExistException(dto.getType());
				ent.
				setEmail(dto.getUserName());
				break;
			case PHONE:
				if(this.repo.existsByPhoneNumber(dto.getUserName()))throw this.generateUserExistException(dto.getType());
				ent
				.setPhoneNumber(dto.getUserName());
				break;
		}
		String hashPassword=this.hashService.encode(dto.getPassword());
		ent.setPassword(hashPassword);
		UserEntity persistEntity;
		try {
			persistEntity = this.repo.save(ent);
		} catch (DataIntegrityViolationException e) {
			//log warn
			throw this.generateUserExistException(dto.getType());
		}
		return new UserServiceAuthDTO(String.valueOf(persistEntity.getId()),new ArrayList<>());

	}
	
	public UserServiceAuthDTO finishRegistration(UserFinishRegistrationDTO dto) {
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
		List<GrantedAuthority> autority=new ArrayList<>();
		autority.add(new SimpleGrantedAuthority("AUTHORIZED"));
		return new UserServiceAuthDTO(this.session.getUserIdAsString(),autority);
	}
	private UserExistException generateUserExistException(AuthorizationType autType) {
		return switch(autType) {
		case EMAIL ->new UserExistException("Email is already registered");
		case PHONE->  new UserExistException("Phone is already registered");		
		};	
		
	}	
	
}
