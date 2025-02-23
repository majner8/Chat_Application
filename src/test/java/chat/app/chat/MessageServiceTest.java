package chat.app.chat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;

import chatapp.dto.MessageType;
import chatapp.dto.TextMessageDTO;
import chatapp.mongo.documents.ChatMessageCollectionsDocument;
import chatapp.service.MessageService;

@SpringBootTest(classes=chatapp.main.Start.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("TEST")
public class MessageServiceTest {

	private static AtomicInteger in=new AtomicInteger(0);
	@SpyBean
	private MessageService service;
	@SpyBean
    private MongoTemplate mongoTemplate;
	@BeforeEach
	public void init() {
		
	}
	@Test
	public void testSaveMessageToMongo() {
		Mockito.doAnswer(new Answer<ChatMessageCollectionsDocument>() {

			@Override
			public ChatMessageCollectionsDocument answer(InvocationOnMock invocation) throws Throwable {
				ChatMessageCollectionsDocument x=new ChatMessageCollectionsDocument();
				x.setMessageCount(in.getAndIncrement());
				return x;
			}
		
		}).when(this.mongoTemplate)
		.findAndModify(Mockito.any(Query.class), Mockito.any(Update.class), Mockito.any(FindAndModifyOptions.class),Mockito.any());
		
		
		
		;
		this.service.saveMessage(new TextMessageDTO()
				.setText("ahoj")
				.setChatID("asd")
				.setSenderID("0")
				);
	}
	
}
