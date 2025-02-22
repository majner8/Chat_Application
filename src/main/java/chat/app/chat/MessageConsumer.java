package chat.app.chat;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.chat.database.ChatMemberRepository;
import app.chat.dto.MessageDTO;
import chat.app.websocket.RabbitMQConfig;
import chat.app.websocket.WebSocketSessionManager;

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
    public void consumeMessage(MessageDTO message) {
		message=this.mesServis.saveMessage(message);
		List<String> member=this.memberRepo.findUserMemberId(message.getChatID());
		this.websocketSession.sendMessageToUser(message, member);
	}
}
