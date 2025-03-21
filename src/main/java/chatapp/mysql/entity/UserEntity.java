package chatapp.mysql.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private boolean finishRegistration;
	@Column
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column
	private String lastName;
	@Column
	private String name;
	@Column
	private String nickName;
	@Column
	private String password;
	@Column
	private String phoneNumber;
}
