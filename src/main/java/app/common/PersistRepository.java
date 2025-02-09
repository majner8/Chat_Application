package app.common;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.NoRepositoryBean;


public interface PersistRepository<ENT,K> {
 void persist(ENT entity)throws  EntityExistException;
	}
