package app.common;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

public class IdGenerator {

	
	public static String generateChatID(List<Long>memberID,boolean groupChat) {
		if(!groupChat&&memberID.size()!=2) {
			throw new IllegalArgumentException();
		}
		if(!groupChat) {
			memberID.sort(Comparator.reverseOrder());
			memberID.get(0);
			memberID.get(1);
			String chatID=String.join("-", 
					Long.toString(memberID.get(0), 32),
					Long.toString(memberID.get(1), 32)
					);
			return chatID;
		}
		return UUID.randomUUID().toString();
	}
	
}
