package chat.app.security.auth.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type") // Rozpoznání podle "type"
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmailUserAuthorizationDTO.class, name = "EMAIL"),
    @JsonSubTypes.Type(value = PhoneUserAuthorizationDTO.class, name = "PHONE")
})
public abstract class UserAuthorizationDTO {
	private String password;
	private AuthorizationType type;
	public abstract String getUserName();
}
