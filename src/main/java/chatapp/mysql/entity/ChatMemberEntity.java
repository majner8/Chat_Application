package chatapp.mysql.entity;

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

    @Column
	private String chatNickName;
    @ManyToOne
    @MapsId("chatID")
    @JoinColumn(name = "chatid", referencedColumnName = "chatid")
	private ChatInformationEntity information;
    @EmbeddedId
	private UserIDChatIDCompositeKey primaryKey=new UserIDChatIDCompositeKey();
    @ManyToOne
    @MapsId("userID")
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private UserEntity user;
    @Column
    @Version
    private long version;


}
