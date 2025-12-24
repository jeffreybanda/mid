package com.mid.app.xmm600.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.mid.app.common.repository.GenericRepository;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.xmm600.model.Xmm600;

public class Xmm600Repository extends GenericRepository<Xmm600> {

	public EntityManager em;
	DbCommandExecutor dbCommandExecutor;

	@Override
	protected Class<Xmm600> getPersistentClass() {

		return Xmm600.class;
	}

	@Override
	protected EntityManager getEntityManager() {

		return em;
	}

	public DbCommandExecutor getDbCommandExecutor() {
		return dbCommandExecutor;
	}

	public void setDbCommandExecutor(final DbCommandExecutor dbCommandExecutor) {
		this.dbCommandExecutor = dbCommandExecutor;
	}
	
	public Optional<Xmm600> findClient(final String clientNo) {

	    try {
	        Xmm600 xmm600 = getEntityManager()
	                .createQuery(
	                    "SELECT e FROM Xmm600 e " +
	                    "WHERE e.clientType = 'IN' AND e.clientNo = :clientNo",
	                    Xmm600.class)
	                .setParameter("clientNo", clientNo)
	                .setMaxResults(1)
	                .getSingleResult();

	        return Optional.of(xmm600);

	    } catch (NoResultException e) {
	        return Optional.empty();
	    }
	}


	@SuppressWarnings("unchecked")
	public List<Xmm600> findClients(final String clientNo) {

		return getEntityManager().createQuery(
				"Select e From Xmm600 e Where e.clientType ='IN' And e.clientNo = :clientNo")
				.setParameter("clientNo", clientNo)
				.setMaxResults(1)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Xmm600> findInterMediary(final String clientNo) {

		return getEntityManager().createQuery(
				"Select e From Xmm600 e Where e.clientNo = :clientNo")
				.setParameter("clientNo", clientNo)
				.setMaxResults(1)
				.getResultList();
	}

}
