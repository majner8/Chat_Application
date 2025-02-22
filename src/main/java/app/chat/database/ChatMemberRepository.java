package app.chat.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ChatMemberRepository  extends JpaRepository<ChatMemberEntity,UserIDChatIDCompositeKey>

{

	public List<Integer> findUserIDByPrimaryKey_ChatID(String chatID);
	public default List<String> findUserMemberId(String chatID){
		
		return this.findUserIDByPrimaryKey_ChatID(chatID).stream()
				.map((v)->{return String.valueOf(v);}).toList();
		 
	}
}
