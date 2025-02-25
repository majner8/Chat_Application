package chatapp.mysql.repository;

import chatapp.exception.EntityExistException;

public interface PersistRepository<ENT,K> {
 void persist(ENT entity)throws  EntityExistException;

	}
