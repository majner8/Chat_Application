package chatapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import chatapp.mysql.entity.UserEntity;
import chatapp.mysql.repository.PersistRepositoryImpl;

@Configuration
public class PersistRepositoryBeanConfig {

	@Bean
	public PersistRepositoryImpl<UserEntity,Long> createUserBean(){
		return new PersistRepositoryImpl<>();
	}
}
