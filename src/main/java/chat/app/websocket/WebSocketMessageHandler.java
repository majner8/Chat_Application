package chat.app.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {
    static final Logger logger = LogManager.getLogger(WebSocketMessageHandler.class);

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
    	if(logger.isDebugEnabled()) {
    		logger.debug(String.format("Websocket connection established session id: %s userID: %s ", 
    				session.getId(),session.getAttributes().get("username")
    				));
    	}
    	this.sessionManager.afterConnectionEstablished(session);
    	
    }
    @Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	if(logger.isDebugEnabled()) {
    		logger.debug(String.format("Websocket connection closed session id: %s userID: %s reason %s", 
    				session.getId(),session.getAttributes().get("username"),status.toString()
    				));
    	}
    	this.sessionManager.afterConnectionClosed(session, status);
    }

}

