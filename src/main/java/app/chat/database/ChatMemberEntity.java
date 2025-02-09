package app.chat.database;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class ChatMemberEntity {

    @EmbeddedId
	private UserIDChatIDCompositeKey primaryKey;
    @ManyToOne
    @MapsId("chatID")  
    @JoinColumn(name = "chat_id", referencedColumnName = "chat_id")
	private ChatInformationEntity information;
    @Column
	private String chatNickName;
    @Column
    @Version
    private long version;
	
	
}
