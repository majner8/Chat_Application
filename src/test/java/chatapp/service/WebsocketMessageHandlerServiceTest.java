package chatapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import chatapp.config.SecurityConfig;
import chatapp.dto.MessageDTO;
import chatapp.dto.TextMessageDTO;
import chatapp.mongo.documents.ChatMessageCollectionsDocument;
import chatapp.mongo.repository.ChatMessageCollectionsRepository;
import chatapp.mysql.repository.ChatMemberRepository;
import chatapp.util.RestRequestSession;
@SpringBootTest(classes=chatapp.main.Start.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-security")
public class WebsocketMessageHandlerServiceTest {
	  static final Logger logger = LogManager.getLogger(WebsocketMessageHandlerServiceTest.class);
	  @MockBean
	  private SecurityConfig config;
	  @MockBean
	  private ChatMemberRepository chatMember;
	  @LocalServerPort
	  private Integer port;
	  @Autowired
	  private ChatMessageCollectionsRepository repo;
	    @MockBean
		  private  RestRequestSession session;




	@Test
	public void handleTextMessage() throws InterruptedException, ExecutionException, TimeoutException, IOException {
		   ObjectMapper mapper=new ObjectMapper();

		StandardWebSocketClient client = new StandardWebSocketClient();
	    WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

	    MessageDTO mes=new TextMessageDTO()
	    		.setText("ahooj jak to je?")
	    		.setChatID("0-1")
	    		.setMessageID("adsklyadsxckl")
	    		.setSenderID("1")
	    		;
		CompletableFuture<MessageDTO> futu=new CompletableFuture();

		TextWebSocketHandler handler=new TextWebSocketHandler() {

			@Override
			public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
				MessageDTO m=mapper.readValue((String)message.getPayload(), MessageDTO.class);
				futu.complete(m);
			}



	    };

	    ListenableFuture<WebSocketSession> future =  client.doHandshake(handler, headers, URI.create( "ws://localhost:" + port +"/ws"));
	    WebSocketSession ses=future.get(10,TimeUnit.SECONDS);
	    assertTrue(ses.isOpen());
	    TextMessage x=new TextMessage(mapper.writeValueAsString(mes));

	    ses.sendMessage(x);

	    MessageDTO ret=futu.get(5, TimeUnit.SECONDS);
	    assertTrue(future.isDone());
	    assertTrue(ret!=null);
		assertEquals(mes.getChatID(),ret.getChatID());
		Optional<ChatMessageCollectionsDocument>xx=this.repo.findById("0-1");
		assertTrue(xx.isPresent());
		assertEquals(xx.get().getMessageCount(),ret.getOrder());
	}
	@BeforeEach
	public void init() throws Exception {
		Mockito.doAnswer(new Answer<List<String>>() {

			@Override
			public List<String> answer(InvocationOnMock invocation) throws Throwable {
				return List.of("1");
			}

		}).when(this.chatMember).findUserMemberId(ArgumentMatchers.anyString());
		Mockito.doAnswer(new Answer<String>() {

			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return "1";
			}}).when(this.session).getUserIdAsString();
	}

}
