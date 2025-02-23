package chatapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import chatapp.mysql.entity.ChatInformationEntity;
import chatapp.mysql.repository.PersistRepository;
import chatapp.mysql.repository.PersistRepositoryImpl;

@Configuration
public class PersitRepositoryConfig {

	@Bean
	public PersistRepository<ChatInformationEntity,String> createBean(){
		return new PersistRepositoryImpl<ChatInformationEntity,String>();
	}
}
