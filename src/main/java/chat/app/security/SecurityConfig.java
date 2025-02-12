package chat.app.security;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import chat.app.security.filter.AuthSecurityFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private AuthSecurityFilter authFilter;
	private AuthenticationManager manager;
	@Autowired
	public SecurityConfig(AuthSecurityFilter authFilter,
			List<AuthenticationProvider> authenticationProviders) {
		this.authFilter = authFilter;
		this.manager=new ProviderManager(authenticationProviders);
	}



	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/public/**").permitAll() 
	            .requestMatchers("/authenticated/**").hasRole("UNAUTHORIZED")
	            .anyRequest().authenticated()
	          
	        		)
	        .authenticationManager(manager)
	        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
	@Bean
	public AuthenticationManager createManager() {
		return this.manager;
	}
	
	 
	  
  
}