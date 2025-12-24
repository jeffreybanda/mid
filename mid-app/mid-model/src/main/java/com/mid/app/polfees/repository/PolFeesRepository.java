package com.mid.app.polfees.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mid.app.common.repository.GenericRepository;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.polfees.model.PolFees;
import com.mid.app.polmaster.model.PolMasterId;

public class PolFeesRepository extends GenericRepository<PolFees> {

	@PersistenceContext
	public EntityManager em;
	DbCommandExecutor dbCommandExecutor;

	public PolFees findByPrimaryKey(final String polNo, final Integer renCnt, final Integer endtCnt) {
		return em.find(PolFees.class, new PolMasterId(polNo, renCnt, endtCnt));
	}

	@Override
	protected Class<PolFees> getPersistentClass() {

		return PolFees.class;
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

}
