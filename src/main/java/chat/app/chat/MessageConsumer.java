package chat.app.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import chat.app.websocket.RabbitMQConfig;
import chat.app.websocket.WebSocketSessionManager;

@Service
public class MessageConsumer {
    static final Logger logger = LogManager.getLogger(MessageConsumer.class);

	
	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(String message) {
		
	}
}
