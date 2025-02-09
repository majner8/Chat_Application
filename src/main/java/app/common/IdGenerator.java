package app.common;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
	public static String generateUniqueUserDeviceId(long deviceID,long userID) {
		return null;
	}
	
	public long generateDeviceID() {
		return -1;
	}
	
	public static String generateChatID(List<Integer>memberID,boolean groupChat) {
		if(!groupChat&&memberID.size()!=2) {
			throw new IllegalArgumentException();
		}
		if(!groupChat) {
			memberID.sort(Comparator.reverseOrder());
			memberID.get(0);
			memberID.get(1);
			String chatID=String.join("-", 
					Integer.toString(memberID.get(0), 32),
					Integer.toString(memberID.get(1), 32)
					);
			return chatID;
		}
		return UUID.randomUUID().toString();
	}
	
}
