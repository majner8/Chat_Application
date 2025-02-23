package chatapp.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import chatapp.config.RabbitMQConfig;
import chatapp.dto.MessageDTO;
import chatapp.mysql.repository.ChatMemberRepository;

@Service
public class MessageConsumer {
    static final Logger logger = LogManager.getLogger(MessageConsumer.class);
    
    @Autowired
    private MessageService mesServis;
	@Autowired
	private ChatMemberRepository memberRepo;
	@Autowired
	private WebSocketSessionManager websocketSession;
	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consumeMessage(MessageDTO message)  {
		try {
		message=this.mesServis.saveMessage(message);
		List<String> member=this.memberRepo.findUserMemberId(message.getChatID());
		this.websocketSession.sendMessageToUser(message, member);
		}
		catch(DuplicateKeyException e) {
			logger.error("DuplicateKeyException occurs during processing message, sender: "+message.getSenderID(),e);

		}
		catch(Exception e) {
			logger.error("Error during processing message, sender: "+message.getSenderID(),e);
		}
	}
	
	private void sendError(Exception e, MessageDTO message) {
		
	}
	private void sendDuplicateKeyError(DuplicateKeyException e,MessageDTO message) {
		
	}
	
}
