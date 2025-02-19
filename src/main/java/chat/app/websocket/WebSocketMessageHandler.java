package chat.app.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {

	@Autowired
	private WebSocketSessionManager sessionManager;
	
	@Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	this.rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "sendMessage",message.getPayload());
    }
    
    @Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	this.sessionManager.afterConnectionEstablished(session);
    	
    }
    @Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	this.sessionManager.afterConnectionClosed(session, status);
    }

}

