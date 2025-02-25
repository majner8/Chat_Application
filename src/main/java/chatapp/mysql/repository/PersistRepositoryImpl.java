package chatapp.mysql.repository;


import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public  class PersistRepositoryImpl<ENT,K> implements PersistRepository<ENT, K> {
	@PersistenceContext
	private EntityManager manager;
	@Override
	public void persist(ENT entity) {
		this.manager.persist(entity);

	}

}
