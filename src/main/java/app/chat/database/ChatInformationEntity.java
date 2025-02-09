package app.chat.database;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class ChatInformationEntity {
	@Column(name="chat_id")
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
