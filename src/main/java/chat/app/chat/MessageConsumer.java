package chat.app.chat;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import chat.app.websocket.RabbitMQConfig;

@Service
public class MessageConsumer {
	
	
	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(String message) {
		
	}
}
