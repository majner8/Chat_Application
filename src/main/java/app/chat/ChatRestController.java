package app.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import app.chat.database.ChatInformationEntity;
import app.chat.database.ChatInformationRepository;
import app.chat.database.ChatMemberEntity;
import app.chat.database.ChatMemberRepository;
import app.chat.database.UserIDChatIDCompositeKey;
import app.common.EntityExistException;
import app.common.IdGenerator;

public class ChatRestController {
	
	@Autowired
	private ChatInformationRepository chatRepo;
	@Autowired
	private ChatMemberRepository memberRepo;
	
	@PostMapping("/createChat")
	public ResponseEntity<ChatDTO> createChat(ChatDTO dto) {
		String chatID=IdGenerator.generateChatID(dto.getMemberID(), dto.isGroupChat());
		ChatInformationEntity ent=new ChatInformationEntity();
		ent.setChatID(chatID)
		.setChatName(dto.getChatName());
		
		try {
			this.chatRepo.persist(ent);
		} catch (EntityExistException e) {
			if(!dto.isGroupChat()) {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
		List<ChatMemberEntity> member=
				dto.getMemberID().stream().map((v)->{
					return new ChatMemberEntity()
							.setChatNickName(null)
							.setInformation(ent)
							.setPrimaryKey(UserIDChatIDCompositeKey.of(chatID, v));
					
				}).toList();
		this.memberRepo.saveAll(member);
		dto.setChatID(chatID);
		return ResponseEntity.ok(dto);
		
	}
}
