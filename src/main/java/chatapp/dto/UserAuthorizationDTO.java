package chatapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EmailUserAuthorizationDTO.class, name = "EMAIL"),
    @JsonSubTypes.Type(value = PhoneUserAuthorizationDTO.class, name = "PHONE")
})
public abstract class UserAuthorizationDTO {
	private String password;
	private AuthorizationType type;
	@JsonIgnore
	public abstract String getUserName();
}
