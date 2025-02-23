package chatapp.mysql.repository;


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
	public void persist(ENT entity) {
		this.manager.persist(entity);
		
	}

}
