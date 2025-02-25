package chatapp.util;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class IdGenerator {
	/**
	 * Generates a unique chat ID based on the provided member IDs.
	 * <p>
	 * If the chat is not a group chat, the list must contain exactly two IDs.
	 * Otherwise, an {@link IllegalArgumentException} will be thrown.
	 * </p>
	 *
	 * <p><b>Note:</b> This method modifies the provided list and is <b>not</b> thread-safe.
	 * Ensure that a mutable list is provided.</p>
	 *
	 * @param memberID a mutable list of member IDs; must contain exactly two IDs for non-group chats
	 * @param groupChat {@code true} if this is a group chat, {@code false} otherwise
	 * @return a unique chat ID in base-32 encoding for non-group chats, or a UUID for group chats
	 * @throws IllegalArgumentException if the list does not contain exactly two IDs for a non-group chat
	 */	public static String generateChatID(List<Long>memberID,boolean groupChat) {
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
