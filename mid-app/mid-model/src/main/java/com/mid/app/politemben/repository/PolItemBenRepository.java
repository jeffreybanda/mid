package com.mid.app.politemben.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mid.app.common.repository.GenericRepository;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.politemben.model.PolItemBen;

public class PolItemBenRepository extends GenericRepository<PolItemBen> {

	@PersistenceContext
	public EntityManager em;
	DbCommandExecutor dbCommandExecutor;

	@Override
	protected Class<PolItemBen> getPersistentClass() {

		return PolItemBen.class;
	}

	@Override
	protected EntityManager getEntityManager() {

		return em;
	}

	@SuppressWarnings("unchecked")
	public List<PolItemBen> findPolItemBenRecord(final String polNo, final Integer renCnt, final Integer endtCnt,
			final Integer riskGrp, final Integer riskNo, final Integer itemNo) {
		return getEntityManager().createQuery(
				"Select e From PolItemBen e Where e.polNo = :polNo And e.renCnt = :renCnt and e.endtCnt = :endtCnt And e.riskGrp =: riskGrp And e.riskNo = : riskNo and e.itemNo = :itemNo ")
				.setParameter("polNo", polNo)
				.setParameter("renCnt", renCnt)
				.setParameter("endtCnt", endtCnt)
				.setParameter("riskGrp", riskGrp)
				.setParameter("riskNo", riskNo)
				.setParameter("itemNo", itemNo)
				.getResultList();
	}

	public void setDbCommandExecutor(final DbCommandExecutor dbCommandExecutor) {
		this.dbCommandExecutor = dbCommandExecutor;
	}

}
