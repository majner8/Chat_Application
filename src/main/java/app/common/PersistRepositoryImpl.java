package app.common;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

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
