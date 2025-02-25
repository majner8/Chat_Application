package chatapp.mysql.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chatapp.mysql.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

	public static interface AuthProjection{
		public Boolean getFinishRegistration();
		public Long getId();
		public String getPassword();
	}
	public boolean existsByEmail(String email);
	public boolean existsByPhoneNumber(String phoneNumber);
	public Optional<AuthProjection> findByEmail(String email);

	public Optional<AuthProjection> findByPhoneNumber(String phoneNumber);
}
