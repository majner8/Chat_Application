package chat.app.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import chat.app.security.TokenGenerator;
import chat.app.security.auth.dto.TokenDTO;

@Component
public class AuthSecurityFilter extends OncePerRequestFilter {

	@Value("${security.token.userheaderpreflix}")
	private String tokenPreflix;
	@Autowired
	private TokenGenerator tokenGenerator;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String userToken=request.getHeader(tokenPreflix);
		TokenDTO token=this.tokenGenerator.validateUserToken(userToken);
		UserDetails user=User.builder()
		.username(token.getUserName())
		.disabled(!token.isFinishRegistration()).build();
		Authentication auth=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		filterChain.doFilter(request, response);
		return;
	}

	
}
