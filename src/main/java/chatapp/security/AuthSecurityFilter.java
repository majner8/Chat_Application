package chatapp.security;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import chatapp.main.Start;
import chatapp.service.JwtTokenGenerator;
import chatapp.util.RestRequestSession;

@Component
public class AuthSecurityFilter extends OncePerRequestFilter {

	@Value("${security.token.headername}")
	private String tokenHeaderName;

	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private JwtTokenGenerator tokenGenerator;
	@Autowired
	private RestRequestSession session;
	@Override
	protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
			jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
			throws jakarta.servlet.ServletException, IOException {

		String rawToken=request.getHeader(this.tokenHeaderName);
		if(rawToken!=null) {
			Authentication auth=this.tokenGenerator.verifyToken(rawToken);
			auth=this.manager.authenticate(auth);
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			this.session.setAuthData(auth,request.getRemoteAddr());
		}
		
		filterChain.doFilter(request, response);

	}

}
