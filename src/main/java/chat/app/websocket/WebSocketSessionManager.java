package chat.app.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WebSocketSessionManager {
	
	@Autowired
	private TaskExecutor executor;
	private Map<String,WebSocketSession> connectedSession=Collections.synchronizedMap(new HashMap<>());
	private ObjectMapper mapper=new ObjectMapper();
	
	 void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	String userID=(String)session.getAttributes().get("username");
    	this.connectedSession.put(userID, session);
    }
	 void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	String userID=(String)session.getAttributes().get("username");

    	this.connectedSession.remove(userID, session);
    }
	 public void sendMessageToUser(Object message,String userName)  {
		WebSocketSession ses=this.connectedSession.get(userName);
		if(ses==null||!ses.isOpen()) {
			return;
		}
		try {
			String	objekt = this.mapper.writeValueAsString(message);
			this.executor.execute(()->{
				synchronized(ses) {
						try {
							ses.sendMessage(new TextMessage(objekt));
						} catch (IOException e) {
							e.printStackTrace();
						}
					
				}
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	 }

	 public void sendMessageToUser(Object message,List<String> userName)  {
			try {
				String	objekt = this.mapper.writeValueAsString(message);
				userName.forEach((v)->{
					WebSocketSession ses=	this.connectedSession.get(v);
					if(ses==null||!ses.isOpen()) {
						return;
					}
					this.executor.execute(()->{
						synchronized(ses) {
								try {
									ses.sendMessage(new TextMessage(objekt));
								} catch (IOException e) {
									e.printStackTrace();
								}
							
						}
					});
				});
				
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

	 }
}
