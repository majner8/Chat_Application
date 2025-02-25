package chatapp.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import chatapp.dto.AuthorizationType;
import chatapp.dto.UserAuthorizationDTO;
import chatapp.dto.UserFinishRegistrationDTO;
import chatapp.dto.UserServiceAuthDTO;
import chatapp.exception.UserExistException;
import chatapp.mysql.entity.UserEntity;
import chatapp.mysql.repository.UserRepository;
import chatapp.util.RestRequestSession;
import jakarta.persistence.EntityNotFoundException;
@Component
public class UserService {

	static final Logger logger = LogManager.getLogger(UserService.class);
	@Autowired
	private PasswordEncoder hashService;
	@Autowired
	private UserRepository repo;
	@Autowired
	private RestRequestSession session;
    @Autowired
	private TransactionTemplate transaction;

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
		return new UserServiceAuthDTO(true,this.session.getUserIdAsString(),autority);
	}

	private UserExistException generateUserExistException(AuthorizationType autType,String userName) {
		return switch(autType) {
		case EMAIL ->new UserExistException("Email is already registered",userName,autType);
		case PHONE->  new UserExistException("Phone is already registered",userName,autType);
		};

	}
	public UserServiceAuthDTO register(UserAuthorizationDTO dto)  {
		UserEntity ent=new UserEntity();
		switch(dto.getType()) {
			case EMAIL:
				if(this.repo.existsByEmail(dto.getUserName())) {
					throw this.generateUserExistException(dto.getType(),dto.getUserName());
				}
				ent.
				setEmail(dto.getUserName());
				break;
			case PHONE:
				if(this.repo.existsByPhoneNumber(dto.getUserName())) {
					throw this.generateUserExistException(dto.getType(),dto.getUserName());
				}
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
			throw this.generateUserExistException(dto.getType(),dto.getUserName());
		}
		List<GrantedAuthority> autority=new ArrayList<>();
		autority.add(new SimpleGrantedAuthority("ROLE_UNAUTHORIZED"));

		return new UserServiceAuthDTO(false,String.valueOf(persistEntity.getId()),autority);

	}

}
