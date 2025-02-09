package app.chat.database;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserIDChatIDCompositeKey implements Serializable  {

	@Column
	private String chatID;
	@Column
	private long userID;
	
	public static UserIDChatIDCompositeKey of(String chatID,long userID) {
		return new UserIDChatIDCompositeKey(chatID,userID);
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIDChatIDCompositeKey that = (UserIDChatIDCompositeKey) o;
        return userID == that.userID && Objects.equals(chatID, that.chatID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatID, userID);
    }
}
