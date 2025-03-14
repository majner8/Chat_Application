package chatapp.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import chatapp.mongo.documents.ChatMessageCollectionsDocument;

@Repository
public interface ChatMessageCollectionsRepository extends MongoRepository<ChatMessageCollectionsDocument, String>  {

}
