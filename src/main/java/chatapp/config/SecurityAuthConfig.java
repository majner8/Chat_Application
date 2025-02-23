package chatapp.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

@Configuration
public class SecurityAuthConfig {
	@Autowired
	private List<AuthenticationProvider> authenticationProviders;
	
	 @Bean
		public AuthenticationManager createManager() {
			return new ProviderManager(this.authenticationProviders);
		}
}
