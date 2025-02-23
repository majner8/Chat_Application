package chatapp.websocket;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import chatapp.security.JwtAuthenticationProvider;
import chatapp.util.RestRequestSession;
@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor  {
    static final Logger logger = LogManager.getLogger(CustomHandshakeInterceptor.class);

	@Autowired
	private RestRequestSession session;
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		attributes.put("username", this.session.getUserIdAsString());
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		
	}

}
