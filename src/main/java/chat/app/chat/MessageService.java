package chat.app.chat;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import app.chat.dto.MessageDTO;
import app.chat.dto.MessageType;
import app.chat.dto.TextMessageDTO;
import chat.app.chat.mongodb.ChatMessageMongoRepository;
import chat.app.chat.mongodb.documents.ChatMessageCollectionsDocument;
import chat.app.chat.mongodb.documents.ChatMessageDocuments;
import chat.app.chat.mongodb.documents.ChatTextMessageDocuments;
@Service
public class MessageService {
	 @Autowired
	    private MongoTemplate mongoTemplate;
	 @Autowired
	 private ChatMessageMongoRepository chatRepo;
	/**Method set order in message and save message to database */
	public MessageDTO saveMessage(MessageDTO dto) {
		this.setNextChatOrder(dto);
		ChatMessageDocuments message=this.messageDocumentMapper(dto);
		this.chatRepo.save(message);
		return dto;
	}
	
	private void setNextChatOrder(MessageDTO message) {
		Query query = new Query(Criteria.where("_id").is(message.getChatID()));
		Update update = new Update()
				.inc("messageCount", 1);
		ChatMessageCollectionsDocument value=this.mongoTemplate.findAndModify(
				query, 
				update,
				FindAndModifyOptions.options().returnNew(true).upsert(true)
				,ChatMessageCollectionsDocument.class);
		message.setOrder(value.getMessageCount());
	}
	
	 ChatMessageDocuments messageDocumentMapper(MessageDTO message) {
		ChatMessageDocuments document= switch (message.getMessageType()) {
	        case TEXTMESSAGE -> {  
	        	TextMessageDTO dto=(TextMessageDTO)message;
	        	var x= new ChatTextMessageDocuments()
	        	.setTextMessage(dto.getText());
	        	x.setType(MessageType.TEXTMESSAGE);
	        	yield x;
	        }
	    };
	    document.setAccepted(LocalDateTime.now());
	    document.setChatId(message.getChatID());
	    document.setOrder(message.getOrder());
	    document.setSender(message.getSenderID());
	    
	    return document;
	}
}
