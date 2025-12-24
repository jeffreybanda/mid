package com.mid.app.polmtrveh.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import com.mid.app.common.repository.GenericRepository;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polmtrveh.model.PolMtrVehId;

public class PolMtrVehRepository extends GenericRepository<PolMtrVeh> {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	public EntityManager em;
	DbCommandExecutor dbCommandExecutor;

	@Override
	protected Class<PolMtrVeh> getPersistentClass() {

		return PolMtrVeh.class;
	}

	@Override
	protected EntityManager getEntityManager() {

		return em;
	}

	public void setDbCommandExecutor(final DbCommandExecutor dbCommandExecutor) {
		this.dbCommandExecutor = dbCommandExecutor;
	}

	@SuppressWarnings("unchecked")
	public List<PolMtrVeh> findPolMtrVehicleList(final String polNo, final Integer renCnt, final Integer endtCnt) {
		try {
		List<PolMtrVeh> polMtrVehs =
		 getEntityManager().createQuery(
				"Select e From PolMtrVeh e Where e.polNo = :polNo  And e.renCnt = :renCnt and e.endtCnt = :endtCnt")
				.setParameter("polNo", polNo)
				.setParameter("renCnt", renCnt)
				.setParameter("endtCnt", endtCnt)
				.getResultList();
		
		
		return polMtrVehs;}catch(Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PolMtrVeh> findPolMtrVehicleByVehRegNo(final String polNo, final Integer renCnt, final Integer endtCnt,
			final String vehRegNo) {
		return getEntityManager().createQuery(
				"Select e From PolMtrVeh e Where e.polNo = :polNo  And e.renCnt = :renCnt and e.endtCnt = :endtCnt and e.vehRegNo = :vehRegNo")
				.setParameter("polNo", polNo)
				.setParameter("renCnt", renCnt)
				.setParameter("endtCnt", endtCnt)
				.setParameter("vehRegNo", vehRegNo)
				.getResultList();
	}

	public String findPolNoByVehReg(final String vehRegNo) {
		try {
			return getEntityManager().createQuery(
					"Select e.polNo From PolMtrVeh e Where e.vehRegNo = :vehRegNo")
					.setParameter("vehRegNo", vehRegNo)
					.setMaxResults(1)
					.getSingleResult().toString();
		} catch (final javax.persistence.NoResultException e) {
			return null;

		}
	}

	public boolean isExistantMotorVehicle(final String polNo, final Integer renCnt, final Integer endtCnt,
			final String vehRegNo) {

		return getEntityManager().createQuery(
				"Select  1 From PolMtrVeh e Where e.polNo = :polNo And e.renCnt = :renCnt and e.endtCnt = :endtCnt and e.vehRegNo = :vehRegNo")
				.setMaxResults(1)
				.setParameter("polNo", polNo)
				.setParameter("renCnt", renCnt)
				.setParameter("endtCnt", endtCnt)
				.setParameter("vehRegNo", vehRegNo)
				.getResultList()
				.size() > 0;

	}

	@SuppressWarnings("unchecked")
	public PolMtrVeh findPolMtrVehicleRecordByCompositeKey(final String polNo, final Integer renCnt,
			final Integer endtCnt,
			final String vehRegNo) {

		final PolMtrVehId polMtrVehId = new PolMtrVehId(polNo, renCnt, endtCnt, vehRegNo);
		return getEntityManager().find(PolMtrVeh.class, polMtrVehId);
	}

}
