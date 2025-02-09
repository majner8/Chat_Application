package app.common;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;

@Component
public  class PersistRepositoryImpl<ENT,K> implements PersistRepository<ENT, K> {
	@PersistenceContext
	private EntityManager manager;
	@Override
	public void persist(ENT entity) throws EntityExistException {
		try {
		this.manager.persist(entity);
		}
		catch(PersistenceException  e) {
			throw  new EntityExistException();
		}
	}

}
