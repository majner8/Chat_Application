package app.chat.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ChatMemberRepository  extends JpaRepository<ChatMemberEntity,UserIDChatIDCompositeKey>

{

	public List<Integer> getUserIDByPrimaryKey_ChatID(String chatID);
	
}
