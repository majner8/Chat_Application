package chatapp.dto;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserServiceAuthDTO {

	private final boolean finishRegistration;
	private final String id;
	private final List<? extends GrantedAuthority> role;
}
