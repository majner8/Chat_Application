package chat.app.security.database;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

	public boolean existsByEmail(String email);
	public boolean existsByPhoneNumberAndCountryCode(String phoneNumber,String countryCode);
	public Optional<AuthProjection> findByEmail(String email);
	public Optional<AuthProjection> findByPhoneNumberAndCountryCode(String phoneNumber,String countryCode);
	
	public static interface AuthProjection{
		public boolean getFinishRegistration();
		public String getPassword();
		public long getId();
	}
}
