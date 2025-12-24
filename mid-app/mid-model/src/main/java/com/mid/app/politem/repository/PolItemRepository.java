package com.mid.app.politem.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import com.mid.app.common.repository.GenericRepository;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.politem.model.PolItem;
import com.mid.app.politem.model.PolItemId;

public class PolItemRepository extends GenericRepository<PolItem> {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	public EntityManager em;
	DbCommandExecutor dbCommandExecutor;

	public void setDbCommandExecutor(final DbCommandExecutor dbCommandExecutor) {
		this.dbCommandExecutor = dbCommandExecutor;
	}

	@Override
	protected Class<PolItem> getPersistentClass() {

		return PolItem.class;
	}

	@Override
	protected EntityManager getEntityManager() {

		return em;
	}

	public PolItem findByPrimaryKey(final String polNo, final Integer renCnt, final Integer endtCnt,
			final Integer riskGrp, final Integer riskNo, final Integer itemNo) {
		return em.find(PolItem.class, new PolItemId(polNo, renCnt, endtCnt, riskGrp, riskNo, itemNo));
	}

}
