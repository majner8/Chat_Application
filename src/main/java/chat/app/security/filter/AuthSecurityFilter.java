package chat.app.security.filter;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import app.start.Start;
import chat.app.security.auth.token.JwtTokenGenerator;

@Component
public class AuthSecurityFilter extends OncePerRequestFilter {

	@Value("${security.token.headername}")
	private String tokenHeaderName;

	@Autowired
	private JwtTokenGenerator tokenGenerator;
	
	@Override
	protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
			throws jakarta.servlet.ServletException, IOException {

		String rawToken=request.getHeader(this.tokenHeaderName);
		if(rawToken!=null) {
			Authentication auth=this.tokenGenerator.verifyToken(rawToken);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		
		filterChain.doFilter(request, response);

	}

}
