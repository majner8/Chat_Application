package chat.app.chat.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
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

import app.chat.ChatDTO;
import chat.app.configTest.TestDataSeeder;
import chat.app.security.auth.dto.AuthorizationType;
import chat.app.security.auth.dto.EmailUserAuthorizationDTO;
import chat.app.security.auth.dto.TokenDTO;
import chat.app.security.auth.dto.UserFinishRegistrationDTO;

@SpringBootTest(classes=app.start.Start.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("TEST")
public class ChatRestController {
	@Autowired
    private TestRestTemplate restTemplate;
  @LocalServerPort
  private Integer port;
	  @Autowired
	  private TestDataSeeder test;

	  @Test
	  public void createChat() {
		  List<TokenDTO> user=this.test.createUser(2,port);
		    assertEquals(true,user.size()>0);
		    ChatDTO dto=new ChatDTO();
		    
		    dto.setChatName("Ahhoj")
		    .setCreatedBy(0)
		    .setGroupChat(false)
		    .setMemberID(List.of((long)2,(long)1))
		    .setGroupChat(false);
		    
		    HttpHeaders headers = new HttpHeaders();
		    headers.setBearerAuth(user.get(0).getToken()); 
		    headers.setContentType(MediaType.APPLICATION_JSON); 
		    HttpEntity<ChatDTO> requestEntity = new HttpEntity<>(dto, headers);
		    ResponseEntity<TokenDTO> response2 = restTemplate.exchange(
			        "http://localhost:" + port + "/createChat",
			        HttpMethod.POST,
			        requestEntity,
			        TokenDTO.class
			    );
			   
			    assertEquals(HttpStatus.CREATED, response2.getStatusCode());
			   
		   

			   
		    
	  }

}
