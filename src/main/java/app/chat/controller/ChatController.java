package app.chat.controller;

import java.util.List;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import app.chat.dto.MessageDTO;
import lombok.AllArgsConstructor;

@Controller
public class ChatController {

	
	@Autowired
    private RabbitTemplate rabbitTemplate;
	
	@MessageMapping("/sendMessage")
	public void publishMessage(MessageDTO message) {
			this.rabbitTemplate.convertAndSend("queue-message-incomming",message,new MessagePostProcessorImpl(message));
		
	}
	@AllArgsConstructor
	private static class MessagePostProcessorImpl implements MessagePostProcessor{
		private MessageDTO mes;
		@Override
		public Message postProcessMessage(Message message) throws AmqpException {
			MessageProperties pr=message.getMessageProperties();
			
			return message;
		}
	}
}
