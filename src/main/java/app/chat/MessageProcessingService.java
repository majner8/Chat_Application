package app.chat;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import app.chat.dto.MessageDTO;
import app.chat.dto.MessageType;

//@Service
public class MessageProcessingService {

	//@Autowired
	//private SimpleMessageBroker messageBroker;
	
	@RabbitListener(queues="queue-message-incomming")
	public void receiveMessage(@Payload MessageDTO body,Message rawMessage) {
		MessageProperties pr=rawMessage.getMessageProperties();
		MessageType tp=MessageType.valueOf(pr.getType());
		
		
		switch(tp) {
		case TEXTMESSAGE :
			
			break;
		
		default:
			break;
		
		}
		
	}
	
	
}
