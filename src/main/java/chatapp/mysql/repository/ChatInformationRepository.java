package chatapp.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chatapp.mysql.entity.ChatInformationEntity;

@Repository
public interface ChatInformationRepository extends JpaRepository<ChatInformationEntity, String>,PersistRepository<ChatInformationEntity,String> {

}
