package app.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import app.chat.database.ChatInformationEntity;
import app.chat.database.ChatInformationRepository;
import app.chat.database.ChatMemberEntity;
import app.chat.database.ChatMemberRepository;
import app.chat.database.UserIDChatIDCompositeKey;
import app.chat.exception.ChatExistException;
import app.common.EntityExistException;
import app.common.EntityExistException.EntityExistExceptionType;
import app.common.IdGenerator;
import app.start.Start;
import chat.app.security.database.UserEntity;
import chat.app.security.database.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

@Service
public class ChatService {
	@Autowired
	private ChatInformationRepository chatRepo;
	@Autowired
	private ChatMemberRepository memberRepo;
	@Autowired
	private UserRepository userRepo;
	@Transactional
	public ChatDTO createChat(ChatDTO dto) {
		String chatID=IdGenerator.generateChatID(dto.getMemberID(), dto.isGroupChat());
		ChatInformationEntity ent=new ChatInformationEntity();
		ent.setChatID(chatID)
		.setChatName(dto.getChatName());
		try {
		this.chatRepo.persist(ent);
		}
		catch(PersistenceException e) {
			throw new ChatExistException(e,dto);
		}
	
		List<UserEntity> userEnt=this.userRepo.findAllById(dto.getMemberID());
		if(userEnt.size()!=dto.getMemberID().size()) {
			//add debug log, with ID
			throw new EntityNotFoundException("Some users could not be found.");
		}
		List<ChatMemberEntity> member=
				userEnt.stream()
				.map((v)->{

					return new ChatMemberEntity()
							.setChatNickName(null)
							.setInformation(ent)
							.setUser(v)
							;
					
				}).toList();
				;
			
		
		this.memberRepo.saveAll(member);
		dto.setChatID(chatID);
		return dto;
	}
}
