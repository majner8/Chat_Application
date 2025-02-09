package chat.app.security.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class UserEntity {
	@Column
	private String email;
	@Column
	private String countryCode;
	@Column
	private String phoneNumber;
	@Column
	private String password;
	@Column
	private boolean finishRegistration;
	@Column
	@Id
	private long id;
	@Column
	private String name;
	@Column
	private String lastName;
	@Column
	private String nickName;
}
