package chatapp.mysql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import chatapp.dto.ChatMemberDTO;
import chatapp.mysql.entity.ChatMemberEntity;
import chatapp.mysql.entity.UserIDChatIDCompositeKey;
@Repository
public interface ChatMemberRepository  extends JpaRepository<ChatMemberEntity,UserIDChatIDCompositeKey>

{

	public List<Integer> findUserIDByPrimaryKey_ChatID(String chatID);
	public default List<String> findUserMemberId(String chatID){
		
		return this.findUserIDByPrimaryKey_ChatID(chatID).stream()
				.map((v)->{return String.valueOf(v);}).toList();
		 
	}
	@Query(value="SELECT x.userid as user_id, "
			+ "COALESCE(x.chat_nick_name, ent.nick_name) AS user_name "
			+ "FROM chat_member_entity x "
			+ "LEFT JOIN user_entity ent ON x.userid = ent.id AND x.chat_nick_name IS NULL;"
			,nativeQuery=true)
	public List<Object []> getChatMemberEntityWithDefaultUserName(String chatID);
}
