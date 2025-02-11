package chat.app.security.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import chat.app.security.auth.dto.AuthorizationType;
import chat.app.security.auth.dto.EmailUserAuthorizationDTO;
import chat.app.security.auth.dto.TokenDTO;
import chat.app.security.auth.dto.UserAuthorizationDTO;
import chat.app.security.auth.dto.UserFinishRegistrationDTO;
import chat.app.user.UserServiceAuthDTO;

@SpringBootTest(classes=app.start.Start.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthorizationControllerTest {
	  @Autowired
	    private TestRestTemplate restTemplate;
	  @LocalServerPort
	  private Integer port;
	  @Test
	  public void RegisterTest() {
		  EmailUserAuthorizationDTO x=new EmailUserAuthorizationDTO();
		  x.setEmail("antonin.bicalasads@gmail.com");
		  x.setPassword("dadasdsasdsa");
		  x.setType(AuthorizationType.EMAIL);
		
		  ResponseEntity<TokenDTO> response=this.restTemplate.postForEntity("http://localhost:"+port+"/public/register", 
				x
				
				, TokenDTO.class);
		    assertEquals(HttpStatus.OK, response.getStatusCode());
		    assertNotNull(response.getBody());
	  }
	  @Test
	  public void FinishRegistrationTest() {
		  UserFinishRegistrationDTO dto=new UserFinishRegistrationDTO();
		  ResponseEntity<TokenDTO> response=this.restTemplate.postForEntity("http://localhost:"+port+"/authenticated/finishRegistration", 
				  dto,TokenDTO.class);
		    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  
	  }

		

}
