package chatapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import chatapp.dto.ChatDTO;
import chatapp.dto.ChatInformationDTO;
import chatapp.mysql.entity.ChatInformationEntity;
import chatapp.mysql.entity.UserEntity;
import chatapp.mysql.repository.ChatInformationRepository;
import chatapp.mysql.repository.ChatMemberRepository;
import chatapp.mysql.repository.UserRepository;
import chatapp.util.IdGenerator;

@SpringBootTest(classes=chatapp.main.Start.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({"test","test-security"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChatRestControllerTest {
	private static UserEntity ent1,ent2;
	@BeforeAll
	public static void init(@Autowired
		UserRepository userRepo) {
		 ent1=new UserEntity()
				.setEmail("dsa@das.ct")
				.setFinishRegistration(true)
				.setLastName("Bicak")
				.setName("Tonda")
				.setNickName("majner8")
				.setPassword("dasds");
		 ent2=new UserEntity()
				.setEmail("dsssa@das.ct")
				.setFinishRegistration(true)
				.setLastName("Bicak")
				.setName("Michal")
				.setNickName("oskerus")
				.setPassword("dasds");
		ent1=userRepo.saveAndFlush(ent1);
		ent2=userRepo.saveAndFlush(ent2);
	}
	@Autowired
	private ChatInformationRepository chatRepo;
	@Autowired
	private ChatMemberRepository memberRepo;
	@LocalServerPort
  	private Integer port;

	@Autowired
    private TestRestTemplate restTemplate;
	@Autowired
	private UserRepository userRepo;
	@Order(1)
	@Test
	public void createChat() {
				  ChatDTO dto=new ChatDTO()
				  .setChatName("Chat")
				  .setCreatedBy(ent1.getId())
				  .setMemberID(List.of(ent1.getId(),ent2.getId()))
				  .setGroupChat(false);

		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    HttpEntity<ChatDTO> requestEntity = new HttpEntity<>(dto, headers);
		    ResponseEntity<ChatDTO> response = restTemplate.exchange(
			        "http://localhost:" + port + "/createChat",
			        HttpMethod.POST,
			        requestEntity,
			        ChatDTO.class
			    );
			    assertEquals(HttpStatus.CREATED, response.getStatusCode());
			   Optional<ChatInformationEntity>chat=this.chatRepo.findById(response.getBody().getChatID());
			   assertTrue(chat.isPresent());
			   assertTrue(!chat.get().getUserMember().isEmpty());
			   assertEquals(2,chat.get().getUserMember().size());
			   chat.get().getUserMember().sort(Comparator.comparingLong(v -> v.getUser().getId())
					   );
			   assertEquals(ent1.getId(),chat.get().getUserMember().get(0).getUser().getId());
			   assertEquals(ent2.getId(),chat.get().getUserMember().get(1).getUser().getId());

	}


	@Order(2)
	@Test
	public void getChatInformationTest() {
		ArrayList<Long> l=new ArrayList<>();
		l.add(ent1.getId());
		l.add(ent2.getId());
		String chatID=IdGenerator.generateChatID(l, false);
	    ResponseEntity<ChatInformationDTO> response = restTemplate.exchange(
		        "http://localhost:" + port + "/chatInformation/"+chatID,
		        HttpMethod.GET,
		        null,
		        ChatInformationDTO.class
		    );
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    response.getBody().getMember().sort(null);

	}


}
