package chat.app.chat.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import chat.app.chat.mongodb.documents.ChatMessageDocuments;
@Repository
public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocuments, String> {

	
}
