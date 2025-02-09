package app.chat.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.common.PersistRepository;

@Repository
public interface ChatInformationRepository extends JpaRepository<ChatInformationEntity, String>,PersistRepository<ChatInformationEntity,String> {

}
