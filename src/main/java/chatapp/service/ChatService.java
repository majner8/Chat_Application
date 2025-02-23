package chatapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import chatapp.dto.ChatDTO;
import chatapp.exception.ChatExistException;
import chatapp.exception.EntityExistException;
import chatapp.exception.EntityExistException.EntityExistExceptionType;
import chatapp.main.Start;
import chatapp.mysql.entity.ChatInformationEntity;
import chatapp.mysql.entity.ChatMemberEntity;
import chatapp.mysql.entity.UserEntity;
import chatapp.mysql.entity.UserIDChatIDCompositeKey;
import chatapp.mysql.repository.ChatInformationRepository;
import chatapp.mysql.repository.ChatMemberRepository;
import chatapp.mysql.repository.UserRepository;
import chatapp.util.IdGenerator;
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
