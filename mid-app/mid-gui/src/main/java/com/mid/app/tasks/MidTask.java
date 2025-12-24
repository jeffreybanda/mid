package com.mid.app.tasks;

import static com.mid.app.http.utils.ConnectionUtils.checkJobConnectionToPortal;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.validator.GenericValidator;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mid.app.common.json.JsonWriter;
import com.mid.app.common.model.HttpCode;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.http.utils.HttpAuthentication;
import com.mid.app.politem.model.PolItem;
import com.mid.app.politem.repository.PolItemRepository;
import com.mid.app.politemben.model.PolItemBen;
import com.mid.app.politemben.repository.PolItemBenRepository;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmaster.repository.PolMasterRepository;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polmtrveh.repository.PolMtrVehRepository;
import com.mid.app.polrisk.model.PolRisk;
import com.mid.app.polrisk.repository.PolRiskRepository;
import com.mid.app.utils.MapCreator;
import com.mid.app.xmm106.repository.Xmm106Repository;
import com.mid.app.xmm600.model.Xmm600;
import com.mid.app.xmm600.repository.Xmm600Repository;

public class MidTask {
	PolMasterRepository polMasterRepository;
	PolMtrVehRepository polMtrVehRepository;
	PolRiskRepository polRiskRepository;
	PolItemRepository polItemRepository;
	PolItemBenRepository polItemBenRepository;
	Xmm600Repository xmm600Repository;
	Xmm106Repository xmm106Repository;
	DbCommandExecutor dbCommandExecutor;
	EntityManager em;
	EntityManagerFactory emf;
	private boolean skipPolicy = true;
	private BigDecimal pctIncBaseAP = new BigDecimal(0.0D);
	@SuppressWarnings("rawtypes")
	Map mainMap = new LinkedHashMap();
	String result;

	public MidTask() {

	}

	public void perform() throws AuthenticationException, ClientProtocolException, IOException {
		try {
			System.out.println("\tMyTask performed by thread: " + Thread.currentThread().getName());
			getReadyForDataFlow();
			executeJob();
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
			emf.close();
		}

	}

	protected void importJsonRecords(final String json, final String polNo) {
		final CloseableHttpResponse response = HttpAuthentication.getPostPolicyToPortalResponse(json);

		final HttpEntity body = response.getEntity();

		final StatusLine statusLine = response.getStatusLine();
		System.out.println(response.getStatusLine());
		String content;
		try {
			content = EntityUtils.toString(body);
			if (statusLine.getStatusCode() == HttpCode.OK.getCode()) {

				// updatePolMtrVeh(content);
				System.out.println(polNo.toString() + "  " + HttpCode.OK.getCode());

			} else {

				System.out.println(polNo.toString() + "  " + content);
			}
		} catch (org.apache.http.ParseException | IOException e) {

			e.printStackTrace();
		}

	}

	public void getReadyForDataFlow() {

		emf = Persistence.createEntityManagerFactory("midPU");
		em = emf.createEntityManager();

		dbCommandExecutor = new DbCommandExecutor(em);
		xmm600Repository = new Xmm600Repository();
		xmm600Repository.em = em;
		xmm600Repository.setDbCommandExecutor(dbCommandExecutor);

		polMasterRepository = new PolMasterRepository();
		polMasterRepository.em = em;
		polMasterRepository.setDbCommandExecutor(dbCommandExecutor);

		polRiskRepository = new PolRiskRepository();
		polRiskRepository.em = em;
		polRiskRepository.setDbCommandExecutor(dbCommandExecutor);

		polItemRepository = new PolItemRepository();
		polItemRepository.em = em;
		polItemRepository.setDbCommandExecutor(dbCommandExecutor);

		polMtrVehRepository = new PolMtrVehRepository();
		polMtrVehRepository.em = em;
		polMtrVehRepository.setDbCommandExecutor(dbCommandExecutor);

		polItemBenRepository = new PolItemBenRepository();
		polItemBenRepository.em = em;
		polItemBenRepository.setDbCommandExecutor(dbCommandExecutor);

		xmm106Repository = new Xmm106Repository();
		xmm106Repository.em = em;
		xmm106Repository.setDbCommandExecutor(dbCommandExecutor);

	}

	public synchronized void getReadyForDataUpdate() {

		emf = Persistence.createEntityManagerFactory("midUP");
		em = emf.createEntityManager();
		dbCommandExecutor = new DbCommandExecutor(em);
		polMtrVehRepository = new PolMtrVehRepository();
		polMtrVehRepository.em = em;
		polMtrVehRepository.setDbCommandExecutor(dbCommandExecutor);

	}

	private boolean recordIsValidated(final PolMaster polMaster, final PolRisk polRisk, final PolMtrVeh mtrVeh,
			final List<PolItemBen> itemBens, final List<Xmm600> xmm600ClientList,
			final List<Xmm600> xmm600IntermediaryList) {

		// return true;

		JsonObject jsonObject = new JsonObject();
		String result = "";
		boolean isValid = true;

		if (itemBens.isEmpty()) {
			jsonObject.addProperty("NoItemBenefitsIdentification",
					polMaster.getPolNo() + " " + mtrVeh.getVehRegNo());
			jsonObject.addProperty("NoItemBenefitsDescription", "Record Has No Item Benefits");
			isValid = false;
		}

		for (final Xmm600 xmm6002 : xmm600ClientList) {

			if (polRisk.getComDate() == null) {
				jsonObject.addProperty("riskCommenceDateIdentification",
						polMaster.getPolNo() + " " + mtrVeh.getVehRegNo());
				jsonObject.addProperty("riskCommenceDateDescription", "Risk Commence Date is Null");
				isValid = false;
			}

			if (polRisk.getExpiryDate() == null) {
				jsonObject.addProperty("riskExpiryDateIdentification",
						polMaster.getPolNo() + " " + mtrVeh.getVehRegNo());
				jsonObject.addProperty("riskExpiryDateDescription", "Risk Expiry Date is Null");
				isValid = false;
			}

			if (xmm6002.getName1().isEmpty()) {
				return false;
			}

			if (xmm6002.getBirthday() == null) {

				jsonObject.addProperty("clientBirthdayIdentification",
						polMaster.getPolNo() + "  " + xmm6002.getName1());
				jsonObject.addProperty("clientBirthDayDescription", "Birthday is Null");
				isValid = false;
			}

			if (xmm6002.getBirthday() != null) {
				if (!GenericValidator.isDate(xmm6002.getBirthday().toString().substring(0, 10), "yyyy-MM-dd", true)) {

					jsonObject.addProperty("invalidBirthdayIdentification",
							polMaster.getPolNo() + "  " + xmm6002.getName1().toString() + " "
									+ xmm6002.getBirthday().toString().substring(0, 10));
					jsonObject.addProperty("Invalid BirthdayDescription", "Invalid Birthday");
					isValid = false;
				}
			}

			if (xmm6002.getTelno7() == null || xmm6002.getTelno7().isEmpty() || xmm6002.getTelno7().length() < 10) {

				jsonObject.addProperty("invalidClientTelephoneIdentification",
						polMaster.getPolNo() + "  " + xmm6002.getName1().toString() + " "
								+ xmm6002.getTelno7().toString());
				jsonObject.addProperty("invalidClientTelephoneDescription", "Invalid Client Telephone");
				isValid = false;
			}

			if (mtrVeh.getColour() == null || mtrVeh.getColour().isEmpty()) {
				jsonObject.addProperty("motorColorIdentification",
						polMaster.getPolNo() + " " + xmm6002.getName1().toString() + " " + mtrVeh.getVehRegNo());
				jsonObject.addProperty("motorColoreDescription", "No Motor Colour");
				isValid = false;
			}

			if (mtrVeh.getNoSeats() == null || mtrVeh.getNoSeats() == 0) {
				jsonObject.addProperty("numberOfSeatsIdentification",
						polMaster.getPolNo() + " " + xmm6002.getName1().toString() + " " + mtrVeh.getVehRegNo());
				jsonObject.addProperty("numberOfSeatDescription", "Number of Seats should be greater than Zero!!");
				isValid = false;
			}
		}

		result = new Gson().toJson(jsonObject);
		System.out.println(result);

		if (isValid) {

			return true;
		} else {
			return false;
		}

	}

	private void executeJob() throws AuthenticationException, ClientProtocolException, IOException {

		final List<PolMaster> polMasters = dbCommandExecutor.executeCommand(() -> {

			try {
				return polMasterRepository.findPolMasterRecordCurrentDate();
			} catch (final ParseException e) {

				e.printStackTrace();
			}
			return null;
		});

		if (polMasters != null) {

			for (final PolMaster polMaster : polMasters) {
				skipPolicy = dbCommandExecutor.executeCommand(() -> {
					return polMasterRepository.isSkipPolicy(polMaster.getPolNo(), polMaster.getRenCnt());
				});

				if (!skipPolicy) {
					final List<PolMtrVeh> polMtrVehs = dbCommandExecutor.executeCommand(() -> {
						return polMtrVehRepository.findPolMtrVehicleList(polMaster.getPolNo(),
								polMaster.getRenCnt(), polMaster.getEndtCnt());
					});

					for (final PolMtrVeh mtrVeh : polMtrVehs) {

						final List<PolRisk> polRisks = dbCommandExecutor.executeCommand(() -> {
							return polRiskRepository.findPolRiskRecord(polMaster.getPolNo(),
									polMaster.getRenCnt(), polMaster.getEndtCnt(), mtrVeh.getRiskGrp(),
									mtrVeh.getRiskNo());
						});

						for (final PolRisk polRisk : polRisks) {

							pctIncBaseAP = dbCommandExecutor.executeCommand(() -> {
								return xmm106Repository.findPctIncBaseAPForClass(polRisk.getBusinessClass(),
										mtrVeh.getCoverType());
							});

							PolItem polItem = polItemRepository.findByPrimaryKey(polMaster.getPolNo(),
									polMaster.getRenCnt(),
									polMaster.getEndtCnt(), mtrVeh.getRiskGrp(), mtrVeh.getRiskNo(),
									mtrVeh.getItemNo());

							final List<PolItemBen> itemBens = dbCommandExecutor.executeCommand(() -> {
								return polItemBenRepository.findPolItemBenRecord(polMaster.getPolNo(),
										polMaster.getRenCnt(), polMaster.getEndtCnt(), mtrVeh.getRiskGrp(),
										mtrVeh.getRiskNo(), mtrVeh.getItemNo());
							});
							final List<Xmm600> xmm600IntermediaryList = dbCommandExecutor.executeCommand(() -> {
								return xmm600Repository.findInterMediary(polMaster.getAgent());

							});

							final List<Xmm600> xmm600ClientList = dbCommandExecutor.executeCommand(() -> {
								return xmm600Repository.findClients(polMaster.getInsured());

							});

							for (final Xmm600 xmm600InterMediary : xmm600IntermediaryList) {
								if (recordIsValidated(polMaster, polRisk, mtrVeh, itemBens, xmm600ClientList,
										xmm600IntermediaryList)) {
									mainMap = MapCreator.createPolicyMap(polMaster, polRisk, mtrVeh, polItem, itemBens,
											pctIncBaseAP);
									result = JsonWriter.writeJsonPolicyRecord(mainMap, xmm600InterMediary, polMaster,
											itemBens,
											xmm600ClientList);
									System.out.println(result);

									if (checkJobConnectionToPortal()) {
										importJsonRecords(result, polMaster.getPolNo());
									}
								}
							}
						}
					}
				}
			}

		}
	}

}

//package com.mid.app.tasks;
//
//import static com.mid.app.http.utils.ConnectionUtils.checkJobConnectionToPortal;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.text.ParseException;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.StatusLine;
//import org.apache.http.auth.AuthenticationException;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.util.EntityUtils;
//
//import com.mid.app.common.json.JsonWriter;
//import com.mid.app.common.model.HttpCode;
//import com.mid.app.common.model.Validations;
//import com.mid.app.common.utils.DbCommandExecutor;
//import com.mid.app.http.utils.HttpAuthentication;
//import com.mid.app.polfees.model.PolFees;
//import com.mid.app.polfees.repository.PolFeesRepository;
//import com.mid.app.politem.model.PolItem;
//import com.mid.app.politem.repository.PolItemRepository;
//import com.mid.app.politemben.model.PolItemBen;
//import com.mid.app.politemben.repository.PolItemBenRepository;
//import com.mid.app.polmaster.model.PolMaster;
//import com.mid.app.polmaster.repository.PolMasterRepository;
//import com.mid.app.polmtrveh.model.PolMtrVeh;
//import com.mid.app.polmtrveh.repository.PolMtrVehRepository;
//import com.mid.app.polrisk.model.PolRisk;
//import com.mid.app.polrisk.repository.PolRiskRepository;
//import com.mid.app.utils.LoggingEngine;
//import com.mid.app.utils.MapCreator;
//import com.mid.app.xmm106.repository.Xmm106Repository;
//import com.mid.app.xmm600.model.Xmm600;
//import com.mid.app.xmm600.repository.Xmm600Repository;
//
//public class MidTask {
//
//	private static LoggingEngine logging;
//	PolFees polFees;
//	PolMasterRepository polMasterRepository;
//	PolFeesRepository polFeesRepository;
//	PolMtrVehRepository polMtrVehRepository;
//	PolRiskRepository polRiskRepository;
//	PolItemRepository polItemRepository;
//	PolItemBenRepository polItemBenRepository;
//	Xmm600Repository xmm600Repository;
//	Xmm106Repository xmm106Repository;
//	DbCommandExecutor dbCommandExecutor;
//	EntityManager em;
//	EntityManagerFactory emf;
//	private boolean skipPolicy = true;
//	private BigDecimal pctIncBaseAP = new BigDecimal(0.0D);
//	@SuppressWarnings("rawtypes")
//	Map mainMap = new LinkedHashMap();
//	String result;
//
//	public MidTask() {
//		logging = LoggingEngine.getInstance();
//		logging.setReady(MidTask.class.getName());
//		logging.changeLoggingLevel(Level.FINE);
//		logging.setConsoleLogging(false);
//	}
//
//	public void perform() throws AuthenticationException, ClientProtocolException, IOException {
//		try {
//			System.out.println("\tMyTask performed by thread: " + Thread.currentThread().getName());
//			getReadyForDataFlow();
//			executeJob();
//		} catch (final Exception e) {
//			e.printStackTrace();
//		} finally {
//			em.close();
//			emf.close();
//		}
//
//	}
//
//	protected void importJsonRecords(final String json, final String polNo) {
//		final CloseableHttpResponse response = HttpAuthentication.getPostPolicyToPortalResponse("policy", json);
//
//		final HttpEntity body = response.getEntity();
//
//		final StatusLine statusLine = response.getStatusLine();
//		System.out.println(response.getStatusLine());
//		String content;
//		try {
//			content = EntityUtils.toString(body);
//			if (statusLine.getStatusCode() == HttpCode.OK.getCode()) {
//
//				// updatePolMtrVeh(content);
//				System.out.println(polNo.toString() + "  " + HttpCode.OK.getCode());
//
//			} else {
//
//				System.out.println(polNo.toString() + "  " + content);
//			}
//		} catch (org.apache.http.ParseException | IOException e) {
//
//			e.printStackTrace();
//		}
//
//	}
//
//	public void getReadyForDataFlow() {
//
//		emf = Persistence.createEntityManagerFactory("midPU");
//		em = emf.createEntityManager();
//
//		dbCommandExecutor = new DbCommandExecutor(em);
//		xmm600Repository = new Xmm600Repository();
//		xmm600Repository.em = em;
//		xmm600Repository.setDbCommandExecutor(dbCommandExecutor);
//
//		polMasterRepository = new PolMasterRepository();
//		polMasterRepository.em = em;
//		polMasterRepository.setDbCommandExecutor(dbCommandExecutor);
//
//		polFeesRepository = new PolFeesRepository();
//		polFeesRepository.em = em;
//		polFeesRepository.setDbCommandExecutor(dbCommandExecutor);
//
//		polRiskRepository = new PolRiskRepository();
//		polRiskRepository.em = em;
//		polRiskRepository.setDbCommandExecutor(dbCommandExecutor);
//
//		polItemRepository = new PolItemRepository();
//		polItemRepository.em = em;
//		polItemRepository.setDbCommandExecutor(dbCommandExecutor);
//
//		polMtrVehRepository = new PolMtrVehRepository();
//		polMtrVehRepository.em = em;
//		polMtrVehRepository.setDbCommandExecutor(dbCommandExecutor);
//
//		polItemBenRepository = new PolItemBenRepository();
//		polItemBenRepository.em = em;
//		polItemBenRepository.setDbCommandExecutor(dbCommandExecutor);
//
//		xmm106Repository = new Xmm106Repository();
//		xmm106Repository.em = em;
//		xmm106Repository.setDbCommandExecutor(dbCommandExecutor);
//
//	}
//
//	public synchronized void getReadyForDataUpdate() {
//
//		emf = Persistence.createEntityManagerFactory("midUP");
//		em = emf.createEntityManager();
//		dbCommandExecutor = new DbCommandExecutor(em);
//		polMtrVehRepository = new PolMtrVehRepository();
//		polMtrVehRepository.em = em;
//		polMtrVehRepository.setDbCommandExecutor(dbCommandExecutor);
//
//	}
//
//	/*
//	 * private boolean recordIsValidated(final PolMaster polMaster, final PolRisk polRisk, final PolMtrVeh mtrVeh, final
//	 * List<PolItemBen> itemBens, final List<Xmm600> xmm600ClientList, final List<Xmm600> xmm600IntermediaryList) {
//	 * 
//	 * // return true;
//	 * 
//	 * JsonObject jsonObject = new JsonObject(); String result = ""; boolean isValid = true;
//	 * 
//	 * if (itemBens.isEmpty()) { jsonObject.addProperty("NoItemBenefitsIdentification", polMaster.getPolNo() + " " +
//	 * mtrVeh.getVehRegNo()); jsonObject.addProperty("NoItemBenefitsDescription", "Record Has No Item Benefits");
//	 * isValid = false; }
//	 * 
//	 * for (final Xmm600 xmm6002 : xmm600ClientList) {
//	 * 
//	 * if (polRisk.getComDate() == null) { jsonObject.addProperty("riskCommenceDateIdentification", polMaster.getPolNo()
//	 * + " " + mtrVeh.getVehRegNo()); jsonObject.addProperty("riskCommenceDateDescription",
//	 * "Risk Commence Date is Null"); isValid = false; }
//	 * 
//	 * if (polRisk.getExpiryDate() == null) { jsonObject.addProperty("riskExpiryDateIdentification",
//	 * polMaster.getPolNo() + " " + mtrVeh.getVehRegNo()); jsonObject.addProperty("riskExpiryDateDescription",
//	 * "Risk Expiry Date is Null"); isValid = false; }
//	 * 
//	 * if (xmm6002.getName1().isEmpty()) { return false; }
//	 * 
//	 * if (xmm6002.getBirthday() == null) {
//	 * 
//	 * jsonObject.addProperty("clientBirthdayIdentification", polMaster.getPolNo() + "  " + xmm6002.getName1());
//	 * jsonObject.addProperty("clientBirthDayDescription", "Birthday is Null"); isValid = false; }
//	 * 
//	 * if (xmm6002.getBirthday() != null) { if (!GenericValidator.isDate(xmm6002.getBirthday().toString().substring(0,
//	 * 10), "yyyy-MM-dd", true)) {
//	 * 
//	 * jsonObject.addProperty("invalidBirthdayIdentification", polMaster.getPolNo() + "  " +
//	 * xmm6002.getName1().toString() + " " + xmm6002.getBirthday().toString().substring(0, 10));
//	 * jsonObject.addProperty("Invalid BirthdayDescription", "Invalid Birthday"); isValid = false; } }
//	 * 
//	 * if (xmm6002.getTelno7() == null || xmm6002.getTelno7().isEmpty() || xmm6002.getTelno7().length() < 10) {
//	 * 
//	 * jsonObject.addProperty("invalidClientTelephoneIdentification", polMaster.getPolNo() + "  " +
//	 * xmm6002.getName1().toString() + " " + xmm6002.getTelno7().toString());
//	 * jsonObject.addProperty("invalidClientTelephoneDescription", "Invalid Client Telephone"); isValid = false; }
//	 * 
//	 * if (mtrVeh.getColour() == null || mtrVeh.getColour().isEmpty()) {
//	 * jsonObject.addProperty("motorColorIdentification", polMaster.getPolNo() + " " + xmm6002.getName1().toString() +
//	 * " " + mtrVeh.getVehRegNo()); jsonObject.addProperty("motorColoreDescription", "No Motor Colour"); isValid =
//	 * false; }
//	 * 
//	 * if (mtrVeh.getNoSeats() == null || mtrVeh.getNoSeats() == 0) {
//	 * jsonObject.addProperty("numberOfSeatsIdentification", polMaster.getPolNo() + " " + xmm6002.getName1().toString()
//	 * + " " + mtrVeh.getVehRegNo()); jsonObject.addProperty("numberOfSeatDescription",
//	 * "Number of Seats should be greater than Zero!!"); isValid = false; } }
//	 * 
//	 * result = new Gson().toJson(jsonObject); System.out.println(result);
//	 * 
//	 * if (isValid) {
//	 * 
//	 * return true; } else { return false; }
//	 * 
//	 * }
//	 */
//
//	private void executeJob() throws AuthenticationException, ClientProtocolException, IOException {
//
//		final List<PolMaster> polMasters = dbCommandExecutor.executeCommand(() -> {
//
//			try {
//				return polMasterRepository.findPolMasterRecordCurrentDate();
//			} catch (final ParseException e) {
//
//				e.printStackTrace();
//			}
//			return null;
//		});
//
//		if (polMasters != null) {
//
//			for (final PolMaster polMaster : polMasters) {
//				skipPolicy = dbCommandExecutor.executeCommand(() -> {
//					return polMasterRepository.isSkipPolicy(polMaster.getPolNo(), polMaster.getRenCnt());
//				});
//
//				if (!skipPolicy) {
//					final List<PolMtrVeh> polMtrVehs = dbCommandExecutor.executeCommand(() -> {
//						return polMtrVehRepository.findPolMtrVehicleList(polMaster.getPolNo(),
//								polMaster.getRenCnt(), polMaster.getEndtCnt());
//					});
//
//					for (final PolMtrVeh mtrVeh : polMtrVehs) {
//
//						final List<PolRisk> polRisks = dbCommandExecutor.executeCommand(() -> {
//							return polRiskRepository.findPolRiskRecord(polMaster.getPolNo(),
//									polMaster.getRenCnt(), polMaster.getEndtCnt(), mtrVeh.getRiskGrp(),
//									mtrVeh.getRiskNo());
//						});
//
//						for (final PolRisk polRisk : polRisks) {
//
//							pctIncBaseAP = dbCommandExecutor.executeCommand(() -> {
//								return xmm106Repository.findPctIncBaseAPForClass(polRisk.getBusinessClass(),
//										mtrVeh.getCoverType());
//							});
//
//							PolItem polItem = polItemRepository.findByPrimaryKey(polMaster.getPolNo(),
//									polMaster.getRenCnt(),
//									polMaster.getEndtCnt(), mtrVeh.getRiskGrp(), mtrVeh.getRiskNo(),
//									mtrVeh.getItemNo());
//
//							polFees = dbCommandExecutor.executeCommand(() -> {
//								return polFeesRepository.findByPrimaryKey(polMaster.getPolNo(),
//										polMaster.getRenCnt(), 0);
//							});
//
//							final List<PolItemBen> itemBens = dbCommandExecutor.executeCommand(() -> {
//								return polItemBenRepository.findPolItemBenRecord(polMaster.getPolNo(),
//										polMaster.getRenCnt(), polMaster.getEndtCnt(), mtrVeh.getRiskGrp(),
//										mtrVeh.getRiskNo(), mtrVeh.getItemNo());
//							});
//							final List<Xmm600> xmm600IntermediaryList = dbCommandExecutor.executeCommand(() -> {
//								return xmm600Repository.findInterMediary(polMaster.getAgent());
//
//							});
//
//							final List<Xmm600> xmm600ClientList = dbCommandExecutor.executeCommand(() -> {
//								return xmm600Repository.findClient(polMaster.getInsured());
//
//							});
//
//							for (final Xmm600 xmm600InterMediary : xmm600IntermediaryList) {
//								if (Validations.recordIsValidated(polMaster, polRisk, mtrVeh, itemBens,
//										xmm600ClientList,
//										xmm600IntermediaryList, logging)) {
//									MapCreator mapCreator = new MapCreator();
//									mainMap = new LinkedHashMap();
//									mainMap = mapCreator.createPolicyMap(polMaster, polFees, polRisk, mtrVeh, polItem,
//											itemBens,
//											pctIncBaseAP);
//									result = JsonWriter.writeJsonPolicyRecord(mainMap, xmm600InterMediary, polMaster,
//											itemBens,
//											xmm600ClientList);
//									System.out.println(result);
//
//									if (checkJobConnectionToPortal()) {
//										importJsonRecords(result, polMaster.getPolNo());
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//
//		}
//	}
//
//}
