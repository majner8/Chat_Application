package chatapp.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import chatapp.controller.rest.AuthorizationController;
import chatapp.dto.ChatDTO;
import chatapp.dto.ChatInformationDTO;
import chatapp.dto.ChatMemberDTO;
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
    static final Logger logger = LogManager.getLogger(ChatService.class);

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
		.setChatName(dto.getChatName())
		.setCreatedBy(new UserEntity().setId(dto.getCreatedBy()));
		try {
		this.chatRepo.persist(ent);
		}
		catch(PersistenceException e) {
			throw new ChatExistException(e,dto);
		}
	
		List<UserEntity> userEnt=this.userRepo.findAllById(dto.getMemberID());
		if(userEnt.size()!=dto.getMemberID().size()) {
			StringBuilder bd=new StringBuilder();
			bd.append("Some users could not be found, required userID: {");
			dto.getMemberID().forEach(v->{
				bd.append(v);
				bd.append(",");
			});
			bd.append("} found userID: {");
			userEnt.forEach(v->{
				bd.append(v.getId());
				bd.append(",");
			});
			bd.append("}");
			logger.error(bd.toString());
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

	public ChatInformationDTO getChatInformation(String chatID) {
		ChatInformationEntity ent=this.chatRepo.findById(chatID).orElseThrow();
		
		List<ChatMemberDTO> member=this.memberRepo.getChatMemberEntityWithDefaultUserName(chatID)
				.stream().map((v)->{
					return new ChatMemberDTO()
							.setUserID((long)v[0])
							.setUserName((String)v[1]);
					
				}).toList();
	
		return new ChatInformationDTO()
				.setChatID(chatID)
				.setCreatedAt(ent.getCreatedAt())
				.setCreatedBy(ent.getCreatedBy().getId())
				.setMember(member);
	}
}
