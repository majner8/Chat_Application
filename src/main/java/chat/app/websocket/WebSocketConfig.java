package chat.app.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	@Autowired
    private  WebSocketMessageHandler webSocketHandler;
	@Autowired
    private  CustomHandshakeInterceptor handshakeInterceptor;

    

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws") // WebSocket endpoint
                .addInterceptors(handshakeInterceptor) // Přidáme handshake interceptor
                .setAllowedOrigins("*"); // Povolit všechny originy (pro testování)
    }
}
