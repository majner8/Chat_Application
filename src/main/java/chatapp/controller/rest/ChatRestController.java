package chatapp.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import chatapp.dto.ChatDTO;
import chatapp.dto.ChatInformationDTO;
import chatapp.exception.ChatExistException;
import chatapp.service.ChatService;
@Controller
public class ChatRestController {

	@Autowired
	private ChatService chatService;
	@PostMapping("/createChat")
	public ResponseEntity<ChatDTO> createChat(@RequestBody ChatDTO dto) {
		ChatDTO chat=this.chatService.createChat(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(chat);
	}
	@GetMapping("/chatInformation/{chatID}")
	public ResponseEntity<ChatInformationDTO> getChatInformation(@PathVariable("chatID") String chatID){
		ChatInformationDTO dto=this.chatService.getChatInformation(chatID);
		return ResponseEntity.ok(dto);
	}

	@ExceptionHandler(ChatExistException.class)
	public ResponseEntity<ChatDTO> handleChatExistException(ChatExistException e) {
		if(e.getDto().isGroupChat()) {
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
	    		body(e.getDto());
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).
				body(e.getDto());

	}
}
