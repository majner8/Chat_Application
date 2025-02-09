package app.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import app.chat.database.ChatInformationEntity;

@Configuration
public class PersitRepositoryConfig {

	@Bean
	public PersistRepository<ChatInformationEntity,String> createBean(){
		return new PersistRepositoryImpl<ChatInformationEntity,String>();
	}
}
