package com.mid.app.polrisk.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.mid.app.common.repository.GenericRepository;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.polrisk.model.PolRisk;

public class PolRiskRepository extends GenericRepository<PolRisk> {

	public EntityManager em;
	DbCommandExecutor dbCommandExecutor;

	@Override
	protected Class<PolRisk> getPersistentClass() {

		return PolRisk.class;
	}

	@Override
	protected EntityManager getEntityManager() {

		return em;
	}

	public void setDbCommandExecutor(final DbCommandExecutor dbCommandExecutor) {
		this.dbCommandExecutor = dbCommandExecutor;
	}

	@SuppressWarnings("unchecked")
	public List<PolRisk> findPolRiskRecord(final String polNo, final Integer renCnt, final Integer endtCnt,
			final Integer riskGrp, final Integer riskNo) {
		return getEntityManager().createQuery(
				"Select e From PolRisk e Where e.polNo = :polNo And e.renCnt = :renCnt and e.endtCnt = :endtCnt And e.riskGrp =: riskGrp And e.riskNo = : riskNo")
				.setParameter("polNo", polNo)
				.setParameter("renCnt", renCnt)
				.setParameter("endtCnt", endtCnt)
				.setParameter("riskGrp", riskGrp)
				.setParameter("riskNo", riskNo)
				.getResultList();
	}

	public Integer getMaxEndtCount(final PolRisk polRisk) {
		try {
			return (Integer) getEntityManager().createQuery(
					"Select max(endtCnt) From PolRisk e where e.premDue > 0 and e.polNo = :polNo And e.renCnt = :renCnt And e.riskGrp =: riskGrp And e.riskNo = : riskNo")
					.setParameter("polNo", polRisk.getPolNo())
					.setParameter("renCnt", polRisk.getRenCnt())
					.setParameter("riskGrp", polRisk.getRiskGrp())
					.setParameter("riskNo", polRisk.getRiskNo())
					.getSingleResult();
		} catch (NoResultException e) {
			return 0;
		}
	}

}
