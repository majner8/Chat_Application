package chat.app.websocket;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import chat.app.configTest.TestDataSeeder;
@SpringBootTest(classes=chatapp.main.Start.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("TEST")
public class HandshakeAndSendMessageTest {
	  @LocalServerPort
	  private Integer port;
		  @Autowired
		  private TestDataSeeder test;
	@Test
	public void test() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        WebSocketHandler handler=new WebSocketHandler() {

			@Override
			public void afterConnectionEstablished(WebSocketSession session) throws Exception {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
				// TODO Auto-generated method stub
				
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
        
        headers.add("Authorization", this.test.createUser(1, this.port).get(0).getToken());
        ListenableFuture<WebSocketSession> future =  client.doHandshake(handler, headers, URI.create( "ws://localhost:" + port +"/ws"));
        WebSocketSession ses=future.get(10,TimeUnit.SECONDS);
        assertTrue(ses.isOpen());
        ses.sendMessage(new TextMessage("ahooj"));
        
	} 
}
