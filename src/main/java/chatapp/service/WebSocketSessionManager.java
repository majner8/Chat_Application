package chatapp.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import chatapp.dto.WebsocketMessageInterface;

@Component
public class WebSocketSessionManager {

    static final Logger logger = LogManager.getLogger(WebSocketSessionManager.class);

	private Map<String,WebSocketSession> connectedSession=Collections.synchronizedMap(new HashMap<>());
	@Autowired
	private TaskExecutor executor;
	private ObjectMapper mapper=new ObjectMapper();

	 void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	String userID=(String)session.getAttributes().get("username");

    	this.connectedSession.remove(userID, session);
    }
	 void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	String userID=(String)session.getAttributes().get("username");
    	this.connectedSession.put(userID, session);
    }
	 public void sendMessageToUser(WebsocketMessageInterface message,List<String> userName)  {
			try {
				if(logger.isDebugEnabled()&&!logger.isTraceEnabled()) {
		    		logger.debug(String.format("Sending websocket message messageID %s users id: {%s}",
		    			message.getMessageID(),String.join(",", userName)));
		    	}




				String	objekt = this.mapper.writeValueAsString(message);

				userName.forEach((v)->{
					WebSocketSession ses=	this.connectedSession.get(v);
					if(ses==null||!ses.isOpen()) {
						if(logger.isDebugEnabled()) {
				    		logger.debug("Websocket session is closed user id: "+v);
				    	}
						return;
					}
					this.executor.execute(()->{
						if(!ses.isOpen()) {
							if(logger.isDebugEnabled()) {
					    		logger.debug("Websocket session is closed user id: "+v);
					    		return;
					    	}
						}
						synchronized(ses) {
								try {
									ses.sendMessage(new TextMessage(objekt));
								} catch (IOException e) {
									logger.error(String.format("Error during sending websocket message session id: %s userID: %s ",
											ses.getId(),v,e
											));								}

						}
					});
				});

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

	 }

	 public void sendMessageToUser(WebsocketMessageInterface message,String userName)  {
		WebSocketSession ses=this.connectedSession.get(userName);

		if(logger.isDebugEnabled()&&!logger.isTraceEnabled()) {
    		logger.debug(String.format("Sending websocket message userID: %s messageID %s",
    				userName,message.getMessageID()));
    	}
		if(logger.isTraceEnabled()) {
			logger.trace("Sending websocket message: {"+message.toString()+"}");
		}
		if(ses==null||!ses.isOpen()) {
			if(logger.isDebugEnabled()) {
	    		logger.debug("Websocket session is closed user id: "+userName);
	    	}


			return;
		}
		try {
			String	objekt = this.mapper.writeValueAsString(message);
			this.executor.execute(()->{
				if(!ses.isOpen()) {
					if(logger.isDebugEnabled()) {
			    		logger.debug("Websocket session is closed user id: "+userName);
			    		return;
			    	}
				}
				synchronized(ses) {
						try {
							ses.sendMessage(new TextMessage(objekt));
						} catch (IOException e) {
							logger.error(String.format("Error during sending websocket message session id: %s userID: %s ",
									ses.getId(),userName,e
									));
						}

				}
			});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

	 }
}
