package chat.app.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import app.common.RestRequestSession;
import chat.app.security.TokenGenerator;
import chat.app.security.auth.dto.DeviceTokenDTO;
@Component
public class AuthenticationSecurityFilter extends OncePerRequestFilter {
	@Value("${security.token.deviceheaderpreflix}")
	private String tokenPreflix;
	@Autowired
	private TokenGenerator tokenGenerator;
	@Autowired
	private RestRequestSession session;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String deviceToken=request.getHeader(tokenPreflix);
		DeviceTokenDTO token=this.tokenGenerator.validateDeviceToken(deviceToken);
		filterChain.doFilter(request, response);

	}

}
