package com.mid.app.xmm023.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.mid.app.common.repository.GenericRepository;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.xmm023.model.Xmm023;

public class Xmm023Repository extends GenericRepository<Xmm023> {

	public EntityManager em;
	DbCommandExecutor dbCommandExecutor;

	@Override
	protected Class<Xmm023> getPersistentClass() {

		return Xmm023.class;
	}

	@Override
	protected EntityManager getEntityManager() {

		return em;
	}

	@SuppressWarnings("unchecked")
	public List<Xmm023> findXmm023() {
		return getEntityManager().createQuery(
				"Select e From Xmm023 e ")
				.getResultList();
	}

	public void setDbCommandExecutor(final DbCommandExecutor dbCommandExecutor) {
		this.dbCommandExecutor = dbCommandExecutor;
	}

}
