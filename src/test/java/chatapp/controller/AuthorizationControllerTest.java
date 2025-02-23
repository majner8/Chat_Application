package chatapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

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

import chatapp.dto.AuthorizationType;
import chatapp.dto.ChatDTO;
import chatapp.dto.EmailUserAuthorizationDTO;
import chatapp.dto.PhoneUserAuthorizationDTO;
import chatapp.dto.TokenDTO;
import chatapp.dto.UserAuthorizationDTO;
import chatapp.dto.UserFinishRegistrationDTO;
import chatapp.mysql.entity.UserEntity;
import chatapp.mysql.repository.UserRepository;


@SpringBootTest(classes=chatapp.main.Start.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorizationControllerTest {

	 @Autowired
	    private TestRestTemplate restTemplate;
	  @LocalServerPort
	  private Integer port;
	  @Autowired
	  private UserRepository userRepo;

	@Test
	public void loginRegistrationTest() {
		  EmailUserAuthorizationDTO x=new EmailUserAuthorizationDTO();
		  x.setEmail("antonin.bicalasadsdas@gmail.com");
		  x.setPassword("dadasdsasdsa");
		  x.setType(AuthorizationType.EMAIL);
		  
		  TokenDTO register=this.register(x);
		  TokenDTO login=this.login(x);
		  assertEquals(register.getUserName(),login.getUserName());
	}
	public TokenDTO register(UserAuthorizationDTO dto) {
		  ResponseEntity<TokenDTO> response=this.restTemplate.postForEntity("http://localhost:"+port+"/public/register", 
				dto
				, TokenDTO.class);
		    assertEquals(HttpStatus.OK, response.getStatusCode());
		    assertNotNull(response.getBody());
		    TokenDTO token=response.getBody();
		    assertEquals(false,token.isFinishRegistration());  
		    return token;
	}
	private TokenDTO login(UserAuthorizationDTO dto) {
		  ResponseEntity<TokenDTO> response=this.restTemplate.postForEntity("http://localhost:"+port+"/public/login", 
				dto
				
				, TokenDTO.class);
		    assertEquals(HttpStatus.OK, response.getStatusCode());
		    assertNotNull(response.getBody());
		    TokenDTO token=response.getBody();
		    assertEquals(false,token.isFinishRegistration());   
		    return token;
	}
	@Test
	public void FinishRegistrationTest() {
		  EmailUserAuthorizationDTO loginDto=new EmailUserAuthorizationDTO();
		  loginDto.setEmail("antonin.dsadasbicalasadsdas@gmail.com");
		  loginDto.setPassword("dadasdsasdsa");
		  loginDto.setType(AuthorizationType.EMAIL);
		  
		  PhoneUserAuthorizationDTO registerDto=new PhoneUserAuthorizationDTO();
		  registerDto.setPhoneNumber("43254342");
		  registerDto.setPassword("dadasdsasdsa");
		  registerDto.setType(AuthorizationType.PHONE);

		  TokenDTO register=this.register(registerDto);
		  //have to be create profile before
		  this.register(loginDto);
		  TokenDTO login=this.login(loginDto);
		   UserFinishRegistrationDTO loginDTO=new UserFinishRegistrationDTO()
				   .setLastName("Bičák")
				   .setName("Tonda")
				   .setNickName("tonda");
		   UserFinishRegistrationDTO registerDTO=new UserFinishRegistrationDTO()
				   .setLastName("Bičák")
				   .setName("Michal")
				   .setNickName("michal");

		    ResponseEntity<TokenDTO> registerResponse = restTemplate.exchange(
		        "http://localhost:" + port + "/authenticated/finishRegistration",
		        HttpMethod.POST,
		        this.createUserFinishRegistrationHttpEntity(register, registerDTO),
		        TokenDTO.class
		    );
		    

		    ResponseEntity<TokenDTO> loginResponse = restTemplate.exchange(
		        "http://localhost:" + port + "/authenticated/finishRegistration",
		        HttpMethod.POST,
		        this.createUserFinishRegistrationHttpEntity(login, loginDTO),
		        TokenDTO.class
		    );
		    assertEquals(HttpStatus.OK,registerResponse.getStatusCode());
		    assertEquals(HttpStatus.OK,loginResponse.getStatusCode());
		    assertTrue(registerResponse.getBody()!=null);
		    assertTrue(loginResponse.getBody()!=null);
		    Optional<UserEntity> loginEnt=this.userRepo.findById(Long.valueOf(loginResponse.getBody().getUserName()));
		    Optional<UserEntity> registerEnt=this.userRepo.findById(Long.valueOf(registerResponse.getBody().getUserName()));
		    assertTrue(loginEnt.isPresent());
		    assertTrue(registerEnt.isPresent());

	}
	
	
    private  HttpEntity<UserFinishRegistrationDTO> createUserFinishRegistrationHttpEntity(TokenDTO token,UserFinishRegistrationDTO dto){
    	  HttpHeaders headers = new HttpHeaders();
		    headers.setBearerAuth(token.getToken()); 
		    headers.setContentType(MediaType.APPLICATION_JSON); 
		    HttpEntity<UserFinishRegistrationDTO> requestEntity = new HttpEntity<>(dto, headers);
		    return requestEntity;
	}
}
