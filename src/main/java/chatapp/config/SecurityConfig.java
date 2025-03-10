package chatapp.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import chatapp.security.AuthSecurityFilter;

@Configuration
@EnableWebSecurity
@Profile("!test-security")
public class SecurityConfig {

	@Autowired

	private AuthSecurityFilter authFilter;





	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
	        .csrf(csrf -> csrf.disable())
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(auth -> auth

	            .requestMatchers("/public/**").permitAll()
	            .requestMatchers("/error").access(
	            		(authentication, context)->{
	            			String ip=context.getRequest().getRemoteAddr();
	            			return new AuthorizationDecision("127.0.0.1".equals(ip));
	            		})

	            .requestMatchers("/authenticated/**")
	                .access(AuthorizationManagers.allOf(
	                    AuthenticatedAuthorizationManager.authenticated(),
	                    AuthorityAuthorizationManager.hasRole("UNAUTHORIZED")
	                ))
	            .anyRequest().authenticated()
	        )
	        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // ✅ Correct placement

	    return http.build();
	}


}