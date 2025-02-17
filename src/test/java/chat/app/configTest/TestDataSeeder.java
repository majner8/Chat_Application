package chat.app.configTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import chat.app.security.auth.dto.AuthorizationType;
import chat.app.security.auth.dto.EmailUserAuthorizationDTO;
import chat.app.security.auth.dto.TokenDTO;
import chat.app.security.auth.dto.UserFinishRegistrationDTO;

@Component
@Profile("TEST")
public class TestDataSeeder {
	 @Autowired
	    private TestRestTemplate restTemplate;

	  public List<TokenDTO> createUser(int numberOfUser,int port) {
		  List<TokenDTO> list=new ArrayList<>();
		  for(int i=0;i<numberOfUser;i++) {
			  EmailUserAuthorizationDTO x=new EmailUserAuthorizationDTO();
			  x.setEmail("antonin.bicalasads@gmail.com"+i);
			  x.setPassword("dadasdsasdsa");
			  x.setType(AuthorizationType.EMAIL);

			  ResponseEntity<TokenDTO> response=this.restTemplate.postForEntity("http://localhost:"+port+"/public/register", 
					x
					
					, TokenDTO.class);
			    assertEquals(HttpStatus.OK, response.getStatusCode());
			    assertNotNull(response.getBody());


			    UserFinishRegistrationDTO dto=new UserFinishRegistrationDTO();
			    dto.setLastName("ahoj");
			    dto.setName("ahoj");
			    dto.setNickName("ahoj");
			   
			    HttpHeaders headers = new HttpHeaders();
			    headers.setBearerAuth(response.getBody().getToken()); 
			    headers.setContentType(MediaType.APPLICATION_JSON); 
			    HttpEntity<UserFinishRegistrationDTO> requestEntity = new HttpEntity<>(dto, headers);

			    ResponseEntity<TokenDTO> response1 = restTemplate.exchange(
			        "http://localhost:" + port + "/authenticated/finishRegistration",
			        HttpMethod.POST,
			        requestEntity,
			        TokenDTO.class
			    );
			   

			    
			    
			list.add(response1.getBody());
		  }
		  return list;
				   
}
}