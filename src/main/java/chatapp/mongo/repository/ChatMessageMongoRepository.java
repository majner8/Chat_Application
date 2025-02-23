package chatapp.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import chatapp.mongo.documents.ChatMessageDocuments;
@Repository
public interface ChatMessageMongoRepository extends MongoRepository<ChatMessageDocuments, String> {

	
}
