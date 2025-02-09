package chat.app.security.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.common.PersistRepositoryImpl;

@Configuration
public class PersistRepositoryBeanConfig {

	@Bean
	public PersistRepositoryImpl<UserEntity,Long> createUserBean(){
		return new PersistRepositoryImpl<UserEntity,Long>();
	}
}
