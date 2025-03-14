package chatapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import chatapp.config.RabbitMQConfig;
import chatapp.dto.MessageDTO;

@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {
    static final Logger logger = LogManager.getLogger(WebSocketMessageHandler.class);

	@Autowired
	private   ObjectMapper mapper;

	@Autowired
    private RabbitTemplate rabbitTemplate;
	@Autowired
	private WebSocketSessionManager sessionManager;
    @Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	logger.info(String.format("Websocket connection closed session id: %s userID: %s reason %s",
				session.getId(),session.getAttributes().get("username"),status.toString()
				));
    	this.sessionManager.afterConnectionClosed(session, status);
    }

    @Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    		logger.info(String.format("Websocket connection established session id: %s userID: %s ",
    				session.getId(),session.getAttributes().get("username")
    				));

    	this.sessionManager.afterConnectionEstablished(session);

    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	MessageDTO mes=this.mapper.readValue(message.getPayload(),MessageDTO.class);
    	this.rabbitTemplate
    	.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "sendMessage",mes);
    }

}

