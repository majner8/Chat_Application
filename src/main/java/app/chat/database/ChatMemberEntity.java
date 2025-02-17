package app.chat.database;

import java.util.List;

import chat.app.security.database.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class ChatMemberEntity {

    @EmbeddedId
	private UserIDChatIDCompositeKey primaryKey=new UserIDChatIDCompositeKey();
    @ManyToOne
    @MapsId("chatID")  
    @JoinColumn(name = "chatid", referencedColumnName = "chatid")
	private ChatInformationEntity information;
    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "userid", referencedColumnName = "id")  
    private UserEntity user;
    @Column
	private String chatNickName;
    @Column
    @Version
    private long version;
	
	
}
