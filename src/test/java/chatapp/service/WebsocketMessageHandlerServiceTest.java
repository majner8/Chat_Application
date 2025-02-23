package chatapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import chatapp.config.SecurityConfig;
import chatapp.dto.MessageDTO;
import chatapp.dto.MessageType;
import chatapp.dto.TextMessageDTO;
import chatapp.mysql.repository.ChatMemberRepository;
import chatapp.util.RestRequestSession;
@SpringBootTest(classes=chatapp.main.Start.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test-security")
public class WebsocketMessageHandlerServiceTest {
	  @LocalServerPort
	  private Integer port;
	  @MockBean
	  private ChatMemberRepository chatMember;
	  @MockBean
	  private  RestRequestSession session;
	  @MockBean
	  private SecurityConfig config;
	  private ObjectMapper mapper=new ObjectMapper();
	  


	
	@BeforeEach
	public void init() throws Exception {
		
		Mockito.doAnswer(new Answer<SecurityFilterChain>() {

			@Override
			public SecurityFilterChain answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgument(0, HttpSecurity.class)
				        .csrf(csrf -> csrf.disable())
				        .authorizeHttpRequests(auth->auth
				        		.anyRequest().permitAll()
				        		).build();			}
			
		}).when(this.config).securityFilterChain(Mockito.any());
		Mockito.doAnswer(new Answer<List<String>>() {

			@Override
			public List<String> answer(InvocationOnMock invocation) throws Throwable {
				return List.of("1");
			}
		
		}).when(this.chatMember).findUserMemberId(Mockito.anyString());
		Mockito.doAnswer(new Answer<String>() {

			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return "1";
			}}).when(this.session).getUserIdAsString();
	}
	@Test
	public void handleTextMessage() throws InterruptedException, ExecutionException, TimeoutException, IOException {
		StandardWebSocketClient client = new StandardWebSocketClient();
	    WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
	    
	    MessageDTO mes=new TextMessageDTO()
	    		.setText("ahooj jak to je?")
	    		.setChatID("0-1")
	    		.setMessageID("adsklyxckl")
	    		.setSenderID("1")
	    		;
		CompletableFuture<Void> futu=new CompletableFuture();

	    WebSocketHandler handler=new WebSocketHandler() {

			@Override
			public void afterConnectionEstablished(WebSocketSession session) throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
				assertTrue(message.getPayload()instanceof String);
				MessageDTO m=mapper.readValue((String)message.getPayload(), MessageDTO.class);
				assertEquals(mes.getChatID(),m.getChatID());
				assertEquals(1,m.getOrder());
				futu.complete(null);
			}

			@Override
			public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean supportsPartialMessages() {
				// TODO Auto-generated method stub
				return false;
			}
	    	
	    };
	    
	    ListenableFuture<WebSocketSession> future =  client.doHandshake(handler, headers, URI.create( "ws://localhost:" + port +"/ws"));
	    WebSocketSession ses=future.get(10,TimeUnit.SECONDS);
	    assertTrue(ses.isOpen());
	    TextMessage x=new TextMessage(this.mapper.writeValueAsString(mes));
	    ses.sendMessage(x);
	    futu.get(5, TimeUnit.SECONDS);
	    assertTrue(future.isDone());
	    //ses.sendMessage(new TextMessage("ahooj"));
	}
	
}
