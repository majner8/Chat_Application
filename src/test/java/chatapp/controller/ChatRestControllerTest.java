package chatapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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

import chatapp.dto.AuthorizationType;
import chatapp.dto.ChatDTO;
import chatapp.dto.EmailUserAuthorizationDTO;
import chatapp.dto.PhoneUserAuthorizationDTO;
import chatapp.dto.TokenDTO;
import chatapp.dto.UserAuthorizationDTO;
import chatapp.dto.UserFinishRegistrationDTO;
import chatapp.mysql.entity.ChatInformationEntity;
import chatapp.mysql.entity.ChatMemberEntity;
import chatapp.mysql.repository.ChatInformationRepository;
import chatapp.mysql.repository.ChatMemberRepository;
import chatapp.mysql.repository.UserRepository;

@SpringBootTest(classes=chatapp.main.Start.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({"test","test-security"})
public class ChatRestControllerTest {
	@Autowired
    private TestRestTemplate restTemplate;
	@LocalServerPort
  	private Integer port;
	@Autowired
	private ChatMemberRepository memberRepo;
	@Autowired
	private ChatInformationRepository chatRepo;
	@Autowired
	private UserRepository userRepo;
	@Test
	public void createChat() {
		  EmailUserAuthorizationDTO user1=new EmailUserAuthorizationDTO();
		  user1.setEmail("antonin.bicalasadsdas@gmail.com");
		  user1.setPassword("dadasdsasdsa");
		  user1.setType(AuthorizationType.EMAIL);
		  
		  EmailUserAuthorizationDTO user2=new EmailUserAuthorizationDTO();
		  user2.setEmail("antonin.bicaladsasadsadsdas@gmail.com");
		  user2.setPassword("dadasdsasdsa");
		  user2.setType(AuthorizationType.EMAIL);
		  String userName1=this.register(user1);
		  String userName2=this.register(user2);

		  ChatDTO dto=new ChatDTO()
				  .setChatName("Chat")
				  .setCreatedBy(Long.valueOf(userName1))
				  .setMemberID(List.of(Long.valueOf(userName2),Long.valueOf(userName1)))
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
			   assertEquals(Long.valueOf(userName1),chat.get().getUserMember().get(0).getUser().getId());
			   assertEquals(Long.valueOf(userName2),chat.get().getUserMember().get(1).getUser().getId());

	}
	
	public String register(UserAuthorizationDTO dto) {
		  ResponseEntity<TokenDTO> response=this.restTemplate.postForEntity("http://localhost:"+port+"/public/register", 
				dto
				, TokenDTO.class);
		    assertEquals(HttpStatus.OK, response.getStatusCode());
		    return response.getBody().getUserName();
		    
	}
	
	@Test
	public void getChatInformationTest() {
		 EmailUserAuthorizationDTO user1=new EmailUserAuthorizationDTO();
		 user1.setEmail("antonin.dsadasbicalasadsdas@gmail.com");
		 user1.setPassword("dadasdsasdsa");
		 user1.setType(AuthorizationType.EMAIL);
		  
		  PhoneUserAuthorizationDTO user2=new PhoneUserAuthorizationDTO();
		  user2.setPhoneNumber("43254342");
		  user2.setPassword("dadasdsasdsa");
		  user2.setType(AuthorizationType.PHONE);
		  String userName1= this.register(user1);
		  String userName2=this.register(user2);
		   UserFinishRegistrationDTO userDTO1=new UserFinishRegistrationDTO()
				   .setLastName("Bičák")
				   .setName("Tonda")
				   .setNickName("tonda");
		   UserFinishRegistrationDTO userDTO2=new UserFinishRegistrationDTO()
				   .setLastName("Bičák")
				   .setName("Michal")
				   .setNickName("michal");
		   HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON); 
		    HttpEntity<UserFinishRegistrationDTO> requestEntity1 = new HttpEntity<>(userDTO1, headers);
		    HttpEntity<UserFinishRegistrationDTO> requestEntity2 = new HttpEntity<>(userDTO2, headers);

		    ResponseEntity<Void> finishRegistration = restTemplate.exchange(
		        "http://localhost:" + port + "/authenticated/finishRegistration",
		        HttpMethod.POST,
		        requestEntity1,
		        Void.class);
		    assertEquals(HttpStatus.OK,finishRegistration.getStatusCode());
		    finishRegistration = restTemplate.exchange(
		        "http://localhost:" + port + "/authenticated/finishRegistration",
		        HttpMethod.POST,
		        requestEntity2,
		        Void.class);
		    assertEquals(HttpStatus.OK,finishRegistration.getStatusCode());
		   
	}
}
