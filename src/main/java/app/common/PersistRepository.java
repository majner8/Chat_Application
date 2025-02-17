package app.common;


public interface PersistRepository<ENT,K> {
 void persist(ENT entity)throws  EntityExistException;
 
	}
