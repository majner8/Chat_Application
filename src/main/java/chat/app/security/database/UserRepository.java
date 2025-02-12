package chat.app.security.database;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

	public boolean existsByEmail(String email);
	public boolean existsByPhoneNumber(String phoneNumber);
	public Optional<AuthProjection> findByEmail(String email);
	public Optional<AuthProjection> findByPhoneNumber(String phoneNumber);
	
	public static interface AuthProjection{
		public Boolean getFinishRegistration();
		public String getPassword();
		public Long getId();
	}
}
