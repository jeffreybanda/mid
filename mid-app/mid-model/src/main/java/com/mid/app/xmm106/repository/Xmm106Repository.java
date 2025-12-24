package com.mid.app.xmm106.repository;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.mid.app.common.repository.GenericRepository;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.xmm106.model.Xmm106;

public class Xmm106Repository extends GenericRepository<Xmm106> {

	public EntityManager em;
	DbCommandExecutor dbCommandExecutor;

	@Override
	protected Class<Xmm106> getPersistentClass() {

		return Xmm106.class;
	}

	@Override
	protected EntityManager getEntityManager() {

		return em;
	}

	public void setDbCommandExecutor(final DbCommandExecutor dbCommandExecutor) {
		this.dbCommandExecutor = dbCommandExecutor;
	}

	@SuppressWarnings("unchecked")
	public BigDecimal findPctIncBaseAPForClass(final String businessClass, final String coverType) {
		try {
			return (BigDecimal) getEntityManager().createQuery("select e.pctIncBaseAP from Xmm106 e " +
					"where e.benCod = 'BASE' and substring(e.businessClass,1,1)='M' And e.businessClass = :businessClass and e.cvrType = :coverType "
					+
					"order by e.effDate desc")
					.setMaxResults(1)
					.setParameter("businessClass", businessClass)
					.setParameter("coverType", coverType)
					.getSingleResult();
		} catch (final NoResultException e) {
			return BigDecimal.ONE;
		}
	}

}
