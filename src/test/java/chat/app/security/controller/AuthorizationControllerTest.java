package chat.app.security.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
	  public void AuthenticationAndAuthorizationTest() {
		  EmailUserAuthorizationDTO x=new EmailUserAuthorizationDTO();
		  x.setEmail("antonin.bicalasads@gmail.com");
		  x.setPassword("dadasdsasdsa");
		  x.setType(AuthorizationType.EMAIL);
		
		  ResponseEntity<TokenDTO> response=this.restTemplate.postForEntity("http://localhost:"+port+"/public/register", 
				x
				
				, TokenDTO.class);
		    assertEquals(HttpStatus.OK, response.getStatusCode());
		    assertNotNull(response.getBody());
		   
		    response=this.restTemplate.postForEntity("http://localhost:"+port+"/public/register", 
					x
					
					, TokenDTO.class);
			    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
			   
			    
		    ResponseEntity<TokenDTO> response1=this.restTemplate.postForEntity("http://localhost:"+port+"/public/login", 
					x
					
					, TokenDTO.class);
		    
			    assertEquals(HttpStatus.OK, response1.getStatusCode());
			    assertNotNull(response1.getBody());
			    
			    UserFinishRegistrationDTO dto=new UserFinishRegistrationDTO();
			    dto.setLastName("ahoj");
			    dto.setName("ahoj");
			    dto.setNickName("ahoj");
			    HttpHeaders headers = new HttpHeaders();
			    headers.setBearerAuth(response1.getBody().getToken()); 
			    headers.setContentType(MediaType.APPLICATION_JSON); 
			    HttpEntity<UserFinishRegistrationDTO> requestEntity = new HttpEntity<>(dto, headers);

			    ResponseEntity<TokenDTO> response2 = restTemplate.exchange(
			        "http://localhost:" + port + "/authenticated/finishRegistration",
			        HttpMethod.POST,
			        requestEntity,
			        TokenDTO.class
			    );
			   
			    assertEquals(HttpStatus.OK, response2.getStatusCode());
			      
	  }
	

		

}
