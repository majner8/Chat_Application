package app.chat.database;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class ChatInformationEntity {
	@Column()
	@Id
	private String chatID;
	@Column
	private String chatName;
	@Version
	private long version;
	@Column
    @OneToMany(mappedBy = "information", cascade = CascadeType.ALL)
	private List<ChatMemberEntity> userMember;
}
