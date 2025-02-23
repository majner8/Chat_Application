package chatapp.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import chatapp.dto.ChatDTO;
import chatapp.exception.ChatExistException;
import chatapp.exception.EntityExistException;
import chatapp.exception.UserExistException;
import chatapp.mysql.entity.ChatInformationEntity;
import chatapp.mysql.entity.ChatMemberEntity;
import chatapp.mysql.entity.UserIDChatIDCompositeKey;
import chatapp.mysql.repository.ChatInformationRepository;
import chatapp.mysql.repository.ChatMemberRepository;
import chatapp.service.ChatService;
import chatapp.util.IdGenerator;
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
