package com.mid.app.polmaster.repository;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mid.app.common.repository.GenericRepository;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.polmaster.model.PolMaster;
import com.toedter.calendar.JDateChooser;

public class PolMasterRepository extends GenericRepository<PolMaster> {

	@PersistenceContext
	public EntityManager em;
	DbCommandExecutor dbCommandExecutor;
	private JDateChooser yesterdayDatePicker;
	private JDateChooser todayDatePicker;

	@Override
	protected Class<PolMaster> getPersistentClass() {

		return PolMaster.class;
	}

	@Override
	protected EntityManager getEntityManager() {

		return em;
	}

	public void setDbCommandExecutor(final DbCommandExecutor dbCommandExecutor) {
		this.dbCommandExecutor = dbCommandExecutor;
	}

	@SuppressWarnings("unchecked")
	public List<PolMaster> findPolMasterRecordByPolNo(final String polNo) {
		return getEntityManager().createQuery(
				"Select e From PolMaster e Where e.tranRel = 1 and e.department = 'M' and e.polStat IN ('IF','RE') and  e.polNo =:polNo")
				.setParameter("polNo", polNo)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PolMaster> findPolMasterRecordByTranDate(final Date startDate, final Date endDate) {
		String jpql = "SELECT p FROM PolMaster p JOIN AcctMaster a " +
				"ON a.branch = p.branch AND a.trantype1 = p.acct1TranType1 AND a.docno = p.acct1DocNo " +
				"AND a.acctno = p.acctNo1 AND a.polno = p.polNo AND a.rencnt = p.renCnt AND a.endtcnt = p.endtCnt " +
				"WHERE p.tranRel = 1 AND p.department = 'M' AND p.polStat IN ('IF', 'RE') " +
				"and p.tranDate between :startDate And :endDate AND (" +
				"a.bal = 0 OR (a.bal IS NOT NULL AND a.netamt IS NOT NULL AND a.bal <> a.netamt)) " +
				"ORDER BY p.tranDate DESC";

		List<PolMaster> result = getEntityManager().createQuery(jpql, PolMaster.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();

		return result;

//		return getEntityManager().createQuery(
//				"Select e From PolMaster e Where e.tranRel = 1 and e.department = 'M' and e.polStat IN ('IF','RE') and e.tranDate between :startDate And :endDate")
//				.setParameter("startDate", startDate)
//				.setParameter("endDate", endDate)
//				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<PolMaster> findAllMotor() {
		return getEntityManager().createQuery(
				"Select e From PolMaster e Where e.tranRel = 1 and e.department = 'M' and e.polStat IN ('IF','RE') order by e.tranDate DESC")
				.setMaxResults(30).getResultList();

	}

	public boolean isSkipPolicy(final String polNo, final Integer renCnt) {
		return getEntityManager().createQuery(
				"Select 1 From PolMaster e Where e.polNo = :polNo And e.renCnt > :renCnt And e.tranRel = 1 ")
				.setParameter("polNo", polNo)
				.setParameter("renCnt", renCnt)
				.getResultList()
				.size() > 0;

	}

	@SuppressWarnings("unchecked")
	public List<PolMaster> findPolMasterRecordCurrentDate() throws ParseException {

		todayDatePicker = new JDateChooser();
		todayDatePicker.setDate(new Date());
		todayDatePicker.setDateFormatString("yyyy-MM-dd");

		yesterdayDatePicker = new JDateChooser();
		yesterdayDatePicker.setDate(new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000));
		yesterdayDatePicker.setDateFormatString("yyyy-MM-dd");

		final LocalDate todayDate = todayDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();

		final LocalDate yesterdayDate = yesterdayDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();

		String jpql = "SELECT p FROM PolMaster p JOIN AcctMaster a " +
				"ON a.branch = p.branch AND a.trantype1 = p.acct1TranType1 AND a.docno = p.acct1DocNo " +
				"AND a.acctno = p.acctNo1 AND a.polno = p.polNo AND a.rencnt = p.renCnt AND a.endtcnt = p.endtCnt " +
				"WHERE p.tranRel = 1 AND p.department = 'M' AND p.polStat IN ('IF', 'RE') " +
				"AND p.tranDate = :today AND (" +
				"a.bal = 0 OR (a.bal IS NOT NULL AND a.netamt IS NOT NULL AND a.bal <> a.netamt)) " +
				"ORDER BY p.tranDate DESC";

		return getEntityManager().createQuery(jpql, PolMaster.class)
				.setParameter("today", com.mid.app.common.utils.DateUtils.convertToDateViaInstant(todayDate))
				.getResultList();

//		return getEntityManager().createQuery(
//				"Select e From PolMaster e Where e.tranRel = 1 and e.department = 'M' and e.polStat IN ('IF','RE') and  e.tranDate = :today Order by e.tranDate desc")
//				// .setParameter("yesterday", com.mid.app.common.utils.DateUtils.convertToDateViaInstant(todayDate))
//				.setParameter("today", com.mid.app.common.utils.DateUtils.convertToDateViaInstant(todayDate))
//				.getResultList();
	}

}
