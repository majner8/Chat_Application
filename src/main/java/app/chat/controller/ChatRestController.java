package app.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import app.chat.ChatDTO;
import app.chat.ChatService;
import app.chat.database.ChatInformationEntity;
import app.chat.database.ChatInformationRepository;
import app.chat.database.ChatMemberEntity;
import app.chat.database.ChatMemberRepository;
import app.chat.database.UserIDChatIDCompositeKey;
import app.chat.exception.ChatExistException;
import app.common.EntityExistException;
import app.common.IdGenerator;
import chat.app.user.UserExistException;
@Controller
public class ChatRestController {
	
	@Autowired
	private ChatService chatService;
	@PostMapping("/createChat")
	public ResponseEntity<ChatDTO> createChat(@RequestBody ChatDTO dto) {
		ChatDTO chat=this.chatService.createChat(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(chat);
	}
	
	@ExceptionHandler(ChatExistException.class)
	public ResponseEntity<ChatDTO> handleChatExistException(ChatExistException e) {
		// log exception
		
		
		if(e.getDto().isGroupChat()) {
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	    		body(e.getDto());
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).
				body(e.getDto());

	}
}
