package com.mid.app.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.mid.app.politem.model.PolItem;
import com.mid.app.politemben.model.PolItemBen;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polrisk.model.PolRisk;

public class MapCreator {

	static InputStream inputStream;
	static InputStream is;
	static BufferedReader reader;
	static Map mainMap;

	static Properties prop = new Properties();
	static Properties propSchedule = new Properties();
	static String propFileName = "/primeTestConfig.properties";
	static String strNCB = "";
	static String strFRB = "";
	static String strODBA = "";
	static String strTPBA = "";
	static String strESLO = "";
	static String strPA = "";
	static String strZECP = "";
	static String strCCLO = "";
	static String strIDL = "";
	static String strOA = "";
	static String strEBB = "";
	static String strZTPP = "";
	static String strZRPL = "";

	static BigDecimal stamp = new BigDecimal(0.0D);
	static BigDecimal fee = new BigDecimal(0.0D);
	static BigDecimal tax = new BigDecimal(0.0D);

	static BigDecimal NCB = new BigDecimal(0.0D);
	static BigDecimal FRB = new BigDecimal(0.0D);
	static BigDecimal ODBA = new BigDecimal(0.0D);
	static BigDecimal TPBA = new BigDecimal(0.0D);
	static BigDecimal ESLO = new BigDecimal(0.0D);
	static BigDecimal PA = new BigDecimal(0.0D);
	static BigDecimal ZECP = new BigDecimal(0.0D);
	static BigDecimal CCLO = new BigDecimal(0.0D);
	static BigDecimal IDL = new BigDecimal(0.0D);
	static BigDecimal OA = new BigDecimal(0.0D);
	static BigDecimal EBB = new BigDecimal(0.0D);
	static BigDecimal ZTPP = new BigDecimal(0.0D);
	static BigDecimal ZRPL = new BigDecimal(0.0D);

	static BigDecimal TOTAL_DISCOUNTS = new BigDecimal(0.0D);
	static BigDecimal SUB_TOTAL_PREMIUM = new BigDecimal(0.0D);
	static BigDecimal TOTAL_LOADINGS = new BigDecimal(0.0D);
	static BigDecimal ADJUSTED_PREMIUM = new BigDecimal(0.0D);
	static BigDecimal EXTRA_SEATS_CHARGE = new BigDecimal(0.0D);
	static BigDecimal BASIC_PREMIUM = new BigDecimal(0.0D);
	static BigDecimal EXCESS_AMOUNT = new BigDecimal(0.0D);
	static BigDecimal PERSONAL_ACCIDENT_CHARGE = new BigDecimal(20);
	static BigDecimal BROWN_CARD_FEE = new BigDecimal(5);
	static BigDecimal EXTRA_TPPD_CHARGE = new BigDecimal(0.0D);
	static BigDecimal ECOWAS_PERIL_CHARGE = new BigDecimal(0.0D);
	static BigDecimal PERILS = new BigDecimal(0.0D);
	static BigDecimal TOTAL_PREMIUM = new BigDecimal(0.0D);
	static BigDecimal STICKER_FEE = new BigDecimal(20);

	static String cover_type_code = "";

//	private Map mainMap;
//
//	private Properties prop = new Properties();
//	private Properties propSchedule = new Properties();
//	private String strNCB = "";
//	private String strFRB = "";
//	private String strODBA = "";
//	private String strTPBA = "";
//	private String strESLO = "";
//	private String strPA = "";
//	private String strZECP = "";
//	private String strCCLO = "";
//	private String strIDL = "";
//	private String strOA = "";
//	private String strEBB = "";
//	private String strZTPP = "";
//	private String strZRPL = "";
//	private String strTPPD = "";
//
//	private String strNCB2 = "";
//	private String strFRB2 = "";
//	private String strODBA2 = "";
//	private String strTPBA2 = "";
//	private String strESLO2 = "";
//	private String strPA2 = "";
//	private String strZECP2 = "";
//	private String strCCLO2 = "";
//	private String strIDL2 = "";
//	private String strOA2 = "";
//	private String strEBB2 = "";
//	private String strZTPP2 = "";
//	private String strZRPL2 = "";
//	private String strTPPD2 = "";
//
//	private String cover_type_code = "";
//
//	private BigDecimal NCB = new BigDecimal(0.0D);
//	private BigDecimal FRB = new BigDecimal(0.0D);
//	private BigDecimal ODBA = new BigDecimal(0.0D);
//	private BigDecimal TPBA = new BigDecimal(0.0D);
//	private BigDecimal ESLO = new BigDecimal(0.0D);
//	private BigDecimal PA = new BigDecimal(0.0D);
//	private BigDecimal ZECP = new BigDecimal(0.0D);
//	private BigDecimal CCLO = new BigDecimal(0.0D);
//	private BigDecimal IDL = new BigDecimal(0.0D);
//	private BigDecimal OA = new BigDecimal(0.0D);
//	private BigDecimal EBB = new BigDecimal(0.0D);
//	private BigDecimal ZTPP = new BigDecimal(0.0D);
//	private BigDecimal ZRPL = new BigDecimal(0.0D);
//
//	private BigDecimal TOTAL_DISCOUNTS = new BigDecimal(0.0D);
//	private BigDecimal SUB_TOTAL_PREMIUM = new BigDecimal(0.0D);
//	private BigDecimal TOTAL_LOADINGS = new BigDecimal(0.0D);
//	private BigDecimal ADJUSTED_PREMIUM = new BigDecimal(0.0D);
//	private BigDecimal EXTRA_SEATS_CHARGE = new BigDecimal(0.0D);
//	private BigDecimal BASIC_PREMIUM = new BigDecimal(0.0D);
//	private BigDecimal EXCESS_AMOUNT = new BigDecimal(0.0D);
//	private BigDecimal PERSONAL_ACCIDENT_CHARGE = new BigDecimal(20);
//	private BigDecimal BROWN_CARD_FEE = new BigDecimal(10);
//	private BigDecimal EXTRA_TPPD_CHARGE = new BigDecimal(0.0D);
//	private BigDecimal ECOWAS_PERIL_CHARGE = new BigDecimal(0.0D);
//	private BigDecimal ADDITIONAL_PERIL_CHARGE = new BigDecimal(0.0D);
//	private BigDecimal PERILS = new BigDecimal(0.0D);
//	private BigDecimal TOTAL_PREMIUM = new BigDecimal(0.0D);
//	private BigDecimal STICKER_FEE = new BigDecimal(33);

	public MapCreator() {
		initializeBenefits();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static LinkedHashMap createPolicyMap(final PolMaster polMaster, final PolRisk polRisk,
			final PolMtrVeh mtrVeh,
			final PolItem polItem, final List<PolItemBen> itemBens, final BigDecimal pctIncBaseAP) {

		DateTime start;
		DateTime end;
		Days d;
		Integer days;
		cover_type_code = getCoverType(polMaster, mtrVeh, polItem);

		BigDecimal premDue = new BigDecimal(0.0D);

		BigDecimal totalPremium = new BigDecimal(0.0D);
		String scheduleCode = "X.1PRIVATEINDIVIDUAL";
		boolean foundScheduleCode = false;

		Boolean doImport = true;

		days = calculateDays(polMaster, polRisk);

		fee = polMaster.getPolFee();
		stamp = polMaster.getPolStamp();
		tax = polMaster.getPolTax();
		premiumCalculations(itemBens);
		populateMap(polMaster, polRisk, mtrVeh, polItem, pctIncBaseAP, days, scheduleCode, foundScheduleCode);

		// totalPremium = premDue.add(tax).add(fee).add(stamp);

		return (LinkedHashMap) mainMap;

	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public synchronized LinkedHashMap createPolicyMap(final PolMaster polMaster, final PolFees polFees,
//			final PolRisk polRisk,
//			final PolMtrVeh mtrVeh,
//			final PolItem polItem, final List<PolItemBen> itemBens, final BigDecimal pctIncBaseAP) {
//
//		Integer days;
//		cover_type_code = getCoverType(polMaster, mtrVeh, polItem);
//
//		String scheduleCode = "X.1PRIVATEINDIVIDUAL";
//		boolean foundScheduleCode = false;
//
//		days = calculateDays(polMaster, polRisk);
//		if (mtrVeh.getTariff().equalsIgnoreCase("N")) {
//			premiumCalculationsTariifN(itemBens, polFees);
//		} else {
//			premiumCalculations(itemBens, polFees);
//
//		}
//
//		populateMap(polMaster, polRisk, mtrVeh, polItem, pctIncBaseAP, days, scheduleCode, foundScheduleCode);
//		return (LinkedHashMap) mainMap;
//
//	}

//	private void premiumCalculations(final List<PolItemBen> itemBens, final PolFees polFees) {
//
//		final InputStream is = MapCreator.class.getResourceAsStream(System.getProperty("BenefitsFile"));
//		new BufferedReader(new InputStreamReader(is));
//
//		prop = new Properties();
//
//		try {
//			prop.load(is);
//		} catch (IOException e1) {
//
//			e1.printStackTrace();
//		}
//		initializeBenefits();
//		strNCB = prop.getProperty("strNCB");
//		strFRB = prop.getProperty("strFRB");
//		strODBA = prop.getProperty("strODBA");
//		strTPBA = prop.getProperty("strTPBA");
//		strESLO = prop.getProperty("strESLO");
//		strPA = prop.getProperty("strPA");
//		strZECP = prop.getProperty("strZECP");
//		strCCLO = prop.getProperty("strCCLO");
//		strIDL = prop.getProperty("strIDL");
//		strOA = prop.getProperty("strOA");
//		strEBB = prop.getProperty("strEBB");
//		strZTPP = prop.getProperty("strZTPP");
//		strZRPL = prop.getProperty("strZRPL");
//
//		strNCB2 = prop.getProperty("strNCB2");
//		strFRB2 = prop.getProperty("strFRB2");
//		strODBA2 = prop.getProperty("strODBA2");
//		strTPBA2 = prop.getProperty("strTPBA2");
//		strESLO2 = prop.getProperty("strESLO2");
//		strPA2 = prop.getProperty("strPA2");
//		strZECP2 = prop.getProperty("strZECP2");
//		strCCLO2 = prop.getProperty("strCCLO2");
//		strIDL2 = prop.getProperty("strIDL2");
//		strOA2 = prop.getProperty("strOA2");
//		strEBB2 = prop.getProperty("strEBB");
//		strZTPP2 = prop.getProperty("strZTPP2");
//		strZRPL2 = prop.getProperty("strZRPL2");
//
//		STICKER_FEE = new BigDecimal(prop.getProperty("STICKER_FEE"));
//		PERSONAL_ACCIDENT_CHARGE = new BigDecimal(prop.getProperty("PERSONAL_ACCIDENT_CHARGE"));
//		BROWN_CARD_FEE = new BigDecimal(prop.getProperty("BROWN_CARD_FEE"));
//
//		for (PolItemBen polItemBen : itemBens) {
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strODBA.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strODBA2.trim().toUpperCase()))) {
//				ODBA = polItemBen.getPremDue();
//			}
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strNCB.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strNCB2.trim().toUpperCase()))) {
//				NCB = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strFRB.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strFRB2.trim().toUpperCase()))) {
//				FRB = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strTPBA.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strTPBA2.trim().toUpperCase()))) {
//				TPBA = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strESLO.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strESLO2.trim().toUpperCase()))) {
//				ESLO = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strPA.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strPA2.trim().toUpperCase()))) {
//				PA = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strZECP.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strZECP2.trim().toUpperCase()))) {
//				ZECP = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strCCLO.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strCCLO2.trim().toUpperCase()))) {
//				CCLO = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strIDL.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strIDL2.trim().toUpperCase()))) {
//				IDL = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strOA.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strOA2.trim().toUpperCase()))) {
//				OA = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strEBB.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strEBB2.trim().toUpperCase()))) {
//				EBB = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strZTPP.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strZTPP2.trim().toUpperCase()))) {
//				ZTPP = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strZRPL.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strZRPL2.trim().toUpperCase()))) {
//				ZRPL = polItemBen.getPremDue();
//			}
//
//		}
//
//		TOTAL_DISCOUNTS = NCB.add(FRB);
//		TOTAL_LOADINGS = CCLO.add(IDL).add(OA);
//		EXTRA_SEATS_CHARGE = ESLO;
//		EXCESS_AMOUNT = EBB;
//		PERSONAL_ACCIDENT_CHARGE = PA;
//		EXTRA_TPPD_CHARGE = ZTPP;
//		ECOWAS_PERIL_CHARGE = ZECP;
//		PERILS = new BigDecimal(0);
//
//		ADDITIONAL_PERIL_CHARGE = ZRPL;
//		BASIC_PREMIUM = TPBA;
//		if (cover_type_code.equals("TP")) {
//			ADJUSTED_PREMIUM = BASIC_PREMIUM;
//		} else {
//			ADJUSTED_PREMIUM = BASIC_PREMIUM.add(ODBA);
//		}
//
//		SUB_TOTAL_PREMIUM = ADJUSTED_PREMIUM.add(EXTRA_SEATS_CHARGE).add(PERSONAL_ACCIDENT_CHARGE)
//				.add(ECOWAS_PERIL_CHARGE).add(BROWN_CARD_FEE).add(PERILS).add(EXTRA_TPPD_CHARGE)
//				.add(ADDITIONAL_PERIL_CHARGE).add(TOTAL_LOADINGS).add(TOTAL_DISCOUNTS).add(EXCESS_AMOUNT);
//		TOTAL_PREMIUM = SUB_TOTAL_PREMIUM.add(STICKER_FEE);
//
//	}

	private static void premiumCalculations(final List<PolItemBen> itemBens) {
		is = MapCreator.class.getResourceAsStream("/primeBenefitsStructure.properties");
		reader = new BufferedReader(new InputStreamReader(is));

		prop = new Properties();

		try {
			prop.load(is);
		} catch (IOException e1) {

			e1.printStackTrace();
		}

		strNCB = prop.getProperty("strNCB");
		strFRB = prop.getProperty("strFRB");
		strODBA = prop.getProperty("strODBA");
		strTPBA = prop.getProperty("strTPBA");
		strESLO = prop.getProperty("strESLO");
		strPA = prop.getProperty("strPA");
		strZECP = prop.getProperty("strZECP");
		strCCLO = prop.getProperty("strCCLO");
		strIDL = prop.getProperty("strIDL");
		strOA = prop.getProperty("strOA");
		strEBB = prop.getProperty("strEBB");
		strZTPP = prop.getProperty("strZTPP");
		strZRPL = prop.getProperty("strZRPL");

		for (PolItemBen polItemBen : itemBens) {

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strODBA.trim().toUpperCase())) {
				ODBA = polItemBen.getPremDue();
			}
			if (polItemBen.getBenCode().trim().toUpperCase().equals(strNCB.trim().toUpperCase())) {
				NCB = new BigDecimal(0);
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strFRB.trim().toUpperCase())) {
				FRB = new BigDecimal(0);
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strTPBA.trim().toUpperCase())) {
				TPBA = polItemBen.getPremDue();
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strESLO.trim().toUpperCase())) {
				ESLO = polItemBen.getPremDue();
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strPA.trim().toUpperCase())) {
				PA = polItemBen.getPremDue();
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strZECP.trim().toUpperCase())) {
				ZECP = polItemBen.getPremDue();
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strCCLO.trim().toUpperCase())) {
				CCLO = polItemBen.getPremDue();
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strIDL.trim().toUpperCase())) {
				IDL = polItemBen.getPremDue();
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strOA.trim().toUpperCase())) {
				OA = polItemBen.getPremDue();
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strEBB.trim().toUpperCase())) {
				EBB = polItemBen.getPremDue();
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strZTPP.trim().toUpperCase())) {
				ZTPP = polItemBen.getPremDue();
			}

			if (polItemBen.getBenCode().trim().toUpperCase().equals(strZRPL.trim().toUpperCase())) {
				ZRPL = polItemBen.getPremDue();
			}

		}

		TOTAL_DISCOUNTS = NCB.add(FRB);
		TOTAL_LOADINGS = CCLO.add(IDL).add(OA);
		EXTRA_SEATS_CHARGE = ESLO;
		EXCESS_AMOUNT = EBB;
		PERSONAL_ACCIDENT_CHARGE = new BigDecimal(20);
		EXTRA_TPPD_CHARGE = ZTPP;
		ECOWAS_PERIL_CHARGE = new BigDecimal(5);
		PERILS = new BigDecimal(5);
		;
		BASIC_PREMIUM = TPBA.add(fee).add(stamp);
		if (cover_type_code.equals("TP")) {
			ADJUSTED_PREMIUM = BASIC_PREMIUM;
		} else {
			ADJUSTED_PREMIUM = BASIC_PREMIUM.add(ODBA);
		}
		SUB_TOTAL_PREMIUM = ADJUSTED_PREMIUM.add(EXTRA_SEATS_CHARGE).add(PERSONAL_ACCIDENT_CHARGE)
				.add(ECOWAS_PERIL_CHARGE).add(BROWN_CARD_FEE).add(PERILS).add(EXTRA_TPPD_CHARGE);
		TOTAL_PREMIUM = SUB_TOTAL_PREMIUM.add(STICKER_FEE);

	}

//	private void premiumCalculationsTariifN(final List<PolItemBen> itemBens, final PolFees polFees) {
//
//		final InputStream is = MapCreator.class.getResourceAsStream(System.getProperty("BenefitsFile"));
//		new BufferedReader(new InputStreamReader(is));
//
//		prop = new Properties();
//
//		try {
//			prop.load(is);
//		} catch (IOException e1) {
//
//			e1.printStackTrace();
//		}
//		initializeBenefits();
//		strNCB = prop.getProperty("strNCB");
//		strFRB = prop.getProperty("strFRB");
//		strODBA = prop.getProperty("strODBA");
//		strTPBA = prop.getProperty("strTPBA");
//		strESLO = prop.getProperty("strESLO");
//		strPA = prop.getProperty("strPA");
//		strZECP = prop.getProperty("strZECP");
//		strCCLO = prop.getProperty("strCCLO");
//		strIDL = prop.getProperty("strIDL");
//		strOA = prop.getProperty("strOA");
//		strEBB = prop.getProperty("strEBB");
//		strZTPP = prop.getProperty("strZTPP");
//		strZRPL = prop.getProperty("strZRPL");
//
//		strNCB2 = prop.getProperty("strNCB2");
//		strFRB2 = prop.getProperty("strFRB2");
//		strODBA2 = prop.getProperty("strODBA2");
//		strTPBA2 = prop.getProperty("strTPBA2");
//		strESLO2 = prop.getProperty("strESLO2");
//		strPA2 = prop.getProperty("strPA2");
//		strZECP2 = prop.getProperty("strZECP2");
//		strCCLO2 = prop.getProperty("strCCLO2");
//		strIDL2 = prop.getProperty("strIDL2");
//		strOA2 = prop.getProperty("strOA2");
//		strEBB2 = prop.getProperty("strEBB2");
//		strZTPP2 = prop.getProperty("strZTPP2");
//		strZRPL2 = prop.getProperty("strZRPL2");
//
//		STICKER_FEE = new BigDecimal(prop.getProperty("STICKER_FEE"));
//		PERSONAL_ACCIDENT_CHARGE = new BigDecimal(prop.getProperty("PERSONAL_ACCIDENT_CHARGE"));
//		BROWN_CARD_FEE = new BigDecimal(prop.getProperty("BROWN_CARD_FEE"));
//
//		for (PolItemBen polItemBen : itemBens) {
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strODBA.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strODBA2.trim().toUpperCase()))) {
//				ODBA = polItemBen.getPremDue();
//			}
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strNCB.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strNCB2.trim().toUpperCase()))) {
//				NCB = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strFRB.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strFRB2.trim().toUpperCase()))) {
//				FRB = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strTPBA.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strTPBA2.trim().toUpperCase()))) {
//				TPBA = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strESLO.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strESLO2.trim().toUpperCase()))) {
//				ESLO = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strPA.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strPA2.trim().toUpperCase()))) {
//				PA = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strZECP.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strZECP2.trim().toUpperCase()))) {
//				ZECP = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strCCLO.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strCCLO2.trim().toUpperCase()))) {
//				CCLO = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strIDL.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strIDL2.trim().toUpperCase()))) {
//				IDL = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strOA.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strOA2.trim().toUpperCase()))) {
//				OA = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strEBB.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strEBB2.trim().toUpperCase()))) {
//				EBB = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strZTPP.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strZTPP2.trim().toUpperCase()))) {
//				ZTPP = polItemBen.getPremDue();
//			}
//
//			if ((polItemBen.getBenCode().trim().toUpperCase().equals(strZRPL.trim().toUpperCase()))
//					|| (polItemBen.getBenCode().trim().toUpperCase().equals(strZRPL2.trim().toUpperCase()))) {
//				ZRPL = polItemBen.getPremDue();
//			}
//
//		}
//
//		TOTAL_DISCOUNTS = NCB.add(FRB);
//		TOTAL_LOADINGS = CCLO.add(IDL).add(OA);
//		EXTRA_SEATS_CHARGE = ESLO;
//		EXCESS_AMOUNT = EBB;
//		PERSONAL_ACCIDENT_CHARGE = PA;
//		EXTRA_TPPD_CHARGE = ZTPP;
//		ECOWAS_PERIL_CHARGE = ZECP;
//		PERILS = new BigDecimal(0);
//
//		ADDITIONAL_PERIL_CHARGE = ZRPL;
//
//		if (cover_type_code.equalsIgnoreCase("TP")) {
//			BASIC_PREMIUM = ODBA;
//			ADJUSTED_PREMIUM = BASIC_PREMIUM;
//		} else {
//			BASIC_PREMIUM = TPBA;
//			ADJUSTED_PREMIUM = BASIC_PREMIUM.add(ODBA);
//		}
//
//		SUB_TOTAL_PREMIUM = ADJUSTED_PREMIUM.add(EXTRA_SEATS_CHARGE).add(PERSONAL_ACCIDENT_CHARGE)
//				.add(ECOWAS_PERIL_CHARGE).add(BROWN_CARD_FEE).add(PERILS).add(EXTRA_TPPD_CHARGE)
//				.add(ADDITIONAL_PERIL_CHARGE).add(TOTAL_LOADINGS).add(TOTAL_DISCOUNTS).add(EXCESS_AMOUNT);
//		TOTAL_PREMIUM = SUB_TOTAL_PREMIUM.add(STICKER_FEE);
//
//	}

	private void initializeBenefits() {

		strNCB = "";
		strFRB = "";
		strODBA = "";
		strTPBA = "";
		strESLO = "";
		strPA = "";
		strZECP = "";
		strCCLO = "";
		strIDL = "";
		strOA = "";
		strEBB = "";
		strZTPP = "";
		strZRPL = "";
//		strTPPD = "";

//		strNCB2 = "";
//		strFRB2 = "";
//		strODBA2 = "";
//		strTPBA2 = "";
//		strESLO2 = "";
//		strPA2 = "";
//		strZECP2 = "";
//		strCCLO2 = "";
//		strIDL2 = "";
//		strOA2 = "";
//		strEBB2 = "";
//		strZTPP2 = "";
//		strZRPL2 = "";
//		strTPPD2 = "";

		new BigDecimal(0.0D);
		new BigDecimal(0.0D);
		new BigDecimal(0.0D);

		NCB = new BigDecimal(0.0D);
		FRB = new BigDecimal(0.0D);
		ODBA = new BigDecimal(0.0D);
		TPBA = new BigDecimal(0.0D);
		ESLO = new BigDecimal(0.0D);
		PA = new BigDecimal(0.0D);
		ZECP = new BigDecimal(0.0D);
		CCLO = new BigDecimal(0.0D);
		IDL = new BigDecimal(0.0D);
		OA = new BigDecimal(0.0D);
		EBB = new BigDecimal(0.0D);
		ZTPP = new BigDecimal(0.0D);
		ZRPL = new BigDecimal(0.0D);

		TOTAL_DISCOUNTS = new BigDecimal(0.0D);
		SUB_TOTAL_PREMIUM = new BigDecimal(0.0D);
		TOTAL_LOADINGS = new BigDecimal(0.0D);
		ADJUSTED_PREMIUM = new BigDecimal(0.0D);
		EXTRA_SEATS_CHARGE = new BigDecimal(0.0D);
		BASIC_PREMIUM = new BigDecimal(0.0D);
		EXCESS_AMOUNT = new BigDecimal(0.0D);

		EXTRA_TPPD_CHARGE = new BigDecimal(0.0D);
		ECOWAS_PERIL_CHARGE = new BigDecimal(0.0D);
		// ADDITIONAL_PERIL_CHARGE = new BigDecimal(0.0D);
		PERILS = new BigDecimal(0.0D);
		TOTAL_PREMIUM = new BigDecimal(0.0D);
		// STICKER_FEE = new BigDecimal(33);
	}

//	@SuppressWarnings({ "unchecked", "deprecation" })
//	private void populateMap(final PolMaster polMaster, final PolRisk polRisk, final PolMtrVeh mtrVeh,
//			final PolItem polItem, final BigDecimal pctIncBaseAP, final Integer days, String scheduleCode,
//			boolean foundScheduleCode) {
//		String cover_type_code;
//		// ConnectionUtils.class.getResourceAsStream(System.getProperty("PropFile"));
//		// InputStream inputStream =
//		// MapCreator.class.getClassLoader().getResourceAsStream("milleniumLiveConfig.properties");
//		final InputStream inputStream = MapCreator.class.getResourceAsStream(System.getProperty("PropFile"));
//		// reader = new BufferedReader(new InputStreamReader(inputStream));
//
//		propSchedule = new Properties();
//
//		try {
//			propSchedule.load(inputStream);
//		} catch (IOException e1) {
//
//			e1.printStackTrace();
//		}
//
//		final InputStream is = MapCreator.class.getResourceAsStream(System.getProperty("BenefitsFile"));
//		new BufferedReader(new InputStreamReader(is));
//
//		prop = new Properties();
//
//		try {
//			prop.load(is);
//		} catch (IOException e1) {
//
//			e1.printStackTrace();
//		}
//
//		mainMap = new LinkedHashMap();
//
//		mainMap.put("insurerPolicyNumber",
//				polMaster.getPolNo());
//
//		mainMap.put("customerNumber",
//				polMaster.getInsured());
//
//		mainMap.put("bodyTypeId",
//				"0");
//
//		mainMap.put("chasisNumber",
//				mtrVeh.getChassisNo());
//
//		if ((mtrVeh.getColour() == null) || (mtrVeh.getColour().isEmpty())) {
//			mainMap.put("color",
//					"Black");
//		} else {
//			mainMap.put("color",
//					mtrVeh.getColour());
//		}
//
//		mainMap.put("cubicCapacity",
//				mtrVeh.getEngineCC().toString());
//
//		mainMap.put("make",
//				mtrVeh.getVehMake());
//
//		mainMap.put("mileage",
//				mtrVeh.getMileage().toString());
//
//		mainMap.put("model",
//				mtrVeh.getModelDesc());
//
//		mainMap.put("registrationNumber",
//				mtrVeh.getVehRegNo());
//
//		if (mtrVeh.getNoSeats() == 0) {
//			mainMap.put("seats",
//					"1");
//		} else {
//
//			mainMap.put("seats",
//					mtrVeh.getNoSeats().toString());
//		}
//
//		mainMap.put("currencyCode",
//				polMaster.getBillCurr());
//
//		mainMap.put("exchangeRate",
//
//				polMaster.getBillCurrRate().toString());
//
//		mainMap.put("computationTypeId",
//				"03");
//
//		mainMap.put("days",
//				days.toString());
//
//		if (polRisk.getComDate() == null) {
//			mainMap.put("inceptionDate",
//					polMaster.getComDate().toString().substring(0, 10));
//			mainMap.put("expiryDate",
//					polMaster.getExpiryDate().toString().substring(0, 10));
//		} else {
//
//			mainMap.put("inceptionDate",
//					polRisk.getComDate().toString().substring(0, 10));
//			mainMap.put("expiryDate",
//					polRisk.getExpiryDate().toString().substring(0, 10));
//		}
//
//		cover_type_code = getCoverType(polMaster, mtrVeh, polItem);
//
//		switch (cover_type_code) {
//		case "COMP":
//
//			mainMap.put("sumInsured",
//					(polItem.getuOM1Val().toString()));
//
//			break;
//		case "TFT":
//
//			mainMap.put("sumInsured",
//					(polItem.getuOM1Val().toString()));
//
//			break;
//		default:
//
//			mainMap.put("sumInsured",
//					"0");
//
//		}
//		mainMap.put("excessTypeCode",
//				"");
//
//		mainMap.put("excessRate",
//				"");
//
//		mainMap.put("coverType",
//				cover_type_code);
//
//		mainMap.put("coInsuranceCompanyId",
//				"");
//
//		mainMap.put("coInsureRate",
//				"0");
//		mainMap.put("coInsureAmount",
//				"0");
//
//		@SuppressWarnings("unchecked")
//		Enumeration<String> enums = (Enumeration<String>) propSchedule.propertyNames();
//
//		while (enums.hasMoreElements() && (!foundScheduleCode)) {
//			String key = enums.nextElement();
//			String value = propSchedule.getProperty(key);
//			// System.out.println(key);
//			if (mtrVeh.getCertRef().replace(" ", "").replace("  ", "").replace("   ", "").trim()
//					.equalsIgnoreCase(key)) {
//				foundScheduleCode = true;
//				scheduleCode = value;
//			}
//
//		}
//
//		if (!foundScheduleCode) {
//			scheduleCode = "X.1PRIVATEINDIVIDUAL";
//			foundScheduleCode = false;
//		}
//
//		System.out.println(" MID CERTIFICATE : " + scheduleCode + " OV2 CERT REF : " + mtrVeh.getCertRef());
//		mainMap.put("scheduleId",
//				scheduleCode);
//
//		mainMap.put("comment",
//				polRisk.getRiskUppTxt());
//
//		mainMap.put("branchId",
//				polMaster.getBranch());
//
//		mainMap.put("total_premium",
//				TOTAL_PREMIUM.toString());
//
//		String strTPPD = prop.getProperty("TPPD_LIMIT");
//		String[] uomValues = { polItem.getuOM2(), polItem.getuOM3(), polItem.getuOM4(), polItem.getuOM5(),
//				polItem.getuOM6() };
//		String[] uomValValues = { polItem.getuOM2Val().toString(), polItem.getuOM3Val().toString(),
//				polItem.getuOM4Val().toString(), polItem.getuOM5Val().toString(), polItem.getuOM6Val().toString() };
//
//		for (int i = 0; i < uomValues.length; i++) {
//			if (uomValues[i].equalsIgnoreCase(strTPPD)) {
//				mainMap.put("tppdLimit", uomValValues[i]);
//				break;
//			}
//		}
//
//		mainMap.put("ncdRate",
//				"0");
//
//		mainMap.put("brownCardNumber",
//				"");
//
//		mainMap.put("loadings",
//				"");
//
//		mainMap.put("yearofManufactor",
//				mtrVeh.getYrManu());
//
//		mainMap.put("interMediaryNumber",
//				"");
//
//		mainMap.put("isSelfAuthorizing",
//				"true");
//
////		mainMap.put("fleet",
////				"");
////
////		mainMap.put("sum_insured_rate",
////				pctIncBaseAP.toString());
////
////		// mainMap.put("excess_amount",
////		// (EXCESS_AMOUNT.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));
////		mainMap.put("excess_amount",
////				(EXCESS_AMOUNT.toString()));
////
////		mainMap.put("basic_premium",
////				(BASIC_PREMIUM.toString()));
////		mainMap.put("adjusted_premium",
////				(ADJUSTED_PREMIUM.toString()));
////
////		mainMap.put("sticker_fee",
////				(STICKER_FEE.toString()));
////
////		mainMap.put("levies",
////				(polRisk.getFee().toString()));
////		mainMap.put("perils",
////				(PERILS.toString()));
////
////		mainMap.put("ecowas_peril_charge",
////				(ECOWAS_PERIL_CHARGE.toString()));
////
////		mainMap.put("personal_accident_charge",
////				(PERSONAL_ACCIDENT_CHARGE.toString()));
////
////		mainMap.put("extra_seats_charge",
////				(EXTRA_SEATS_CHARGE.toString()));
////
////		mainMap.put("extra_tppd_charge",
////				(EXTRA_TPPD_CHARGE.toString()));
////
////		mainMap.put("additional_peril_charge",
////				(ADDITIONAL_PERIL_CHARGE.toString()));
////
////		mainMap.put("total_discounts",
////				(TOTAL_DISCOUNTS.toString()));
////
////		mainMap.put("sub_total_premium",
////				(SUB_TOTAL_PREMIUM.toString()));
////
////		mainMap.put("reference",
////				polMaster.getPolNo() + polMaster.getRenCnt() + polMaster.getEndtCnt() + mtrVeh.getRiskGrp()
////						+ mtrVeh.getRiskNo() + mtrVeh.getVehRegNo());
////
////		mainMap.put("excess_charge",
////				"");
////
////		mainMap.put("total_loadings",
////				(TOTAL_LOADINGS.toString()));
////
////		mainMap.put("server_reference",
////				"");
////
////		mainMap.put("discounts",
////				"");
////
////		mainMap.put("customer",
////				"");
////	
////
////		mainMap.put("sticker_number",
////				"");
////
////		mainMap.put("body_type_code",
////				"01");
////
////		if (inputStream != null) {
////			try {
////				prop.load(inputStream);
////			} catch (IOException e) {
////
////			}
////		} else {
////
////		}
////
////		mainMap.put("policy_number",
////				"");
//	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private static void populateMap(final PolMaster polMaster, final PolRisk polRisk, final PolMtrVeh mtrVeh,
			final PolItem polItem, final BigDecimal pctIncBaseAP, final Integer days, String scheduleCode,
			boolean foundScheduleCode) {
		String cover_type_code;
		String result = "";

		inputStream = MapCreator.class.getClassLoader().getResourceAsStream("primeTestConfig.properties");
		// reader = new BufferedReader(new InputStreamReader(inputStream));

		propSchedule = new Properties();

		try {
			propSchedule.load(inputStream);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		
		
//		{
//			  "data": {
//			    "branchCode": "OKO202",
//			    "productCode": "MTCOMP",
//			    "intermediaryCode": null,
//			    "subintermediaryCode": null,
//			    "riskTypeCode": "PRIVATE_CORPORATE_X4",
//			    "vehicleData": {
//			      "registrationNumber": "GR-1234-23",
//			      "chassisNumber": "WAUZZZ8V0HA012345",
//			      "make": "Toyota",
//			      "model": "Corolla",
//			      "manufacturingYear": 2021,
//			      "registrationYear": 2022,
//			      "vehicleColour": "Blue",
//			      "bodyType": "Saloon",
//			      "seatingCapacity": 5,
//			      "cubicCapacity": 1598,
//			      "vehicleUsage": "Private",
//			      "fuelType": "Petrol",
//			      "vehicleValue": 150000.0,
//			      "verified": true
//			    },
//			    "customerData": {
//			      "isActive": true,
//			      "type": "INDIVIDUAL",
//			      "title": "Mr",
//			      "firstName": "Kofi",
//			      "lastName": "Mensah",
//			      "gender": "MALE",
//			      "dateOfBirth": "1990-05-14",
//			      "nationality": "GH",
//			      "ghanaCardNumber": "GHA-994456789-0",
//			      "email": "kofi.mensah@example.com",
//			      "phoneNumber": "0543071875",
//			      "digitalAddress": "GA-123-4567",
//			      "residentialAddress": "12 First Street, Accra",
//			      "occupation": "Engineer"
//			    },
//			    "policyData": {
//			      "startDate": "2026-01-01",
//			      "expiryDate": "2026-12-31",
//			      "sumInsured": 150000.0,
//			      "grossPremium": 7280.0,
//			      "type": "NEW_BUSINESS",
//			      "transactionDate": "2024-12-15",
//			      "excessBought": true,
//			      "noClaimDiscount": 0,
//			      "earnedNoClaimDiscount": 0,
//			      "calculationType": "FULL_YEAR"
//			    }
//			  }
//			}

		mainMap = new LinkedHashMap();

		mainMap.put("vehicle_registration",
				mtrVeh.getVehRegNo());
		mainMap.put("make",
				mtrVeh.getVehMake());

		mainMap.put("chasis_number",
				mtrVeh.getChassisNo());
		mainMap.put("year_of_manufacture",
				mtrVeh.getYrManu());

		mainMap.put("cubic_capacity",
				mtrVeh.getEngineCC().toString());

		if ((mtrVeh.getColour() == null) || (mtrVeh.getColour().isEmpty())) {
			mainMap.put("color",
					"Black");
		} else {
			mainMap.put("color",
					mtrVeh.getColour());
		}

		mainMap.put("fleet",
				"");

		mainMap.put("exchange_rate",

				polMaster.getBillCurrRate().toString());

		mainMap.put("days",
				days.toString());

		if (polRisk.getComDate() == null) {
			mainMap.put("inception_date",
					polMaster.getComDate().toString().substring(0, 10));
			mainMap.put("expiry_date",
					polMaster.getExpiryDate().toString().substring(0, 10));
		} else {

			mainMap.put("inception_date",
					polRisk.getComDate().toString().substring(0, 10));
			mainMap.put("expiry_date",
					polRisk.getExpiryDate().toString().substring(0, 10));
		}

		mainMap.put("sum_insured_rate",
				pctIncBaseAP.toString());

		mainMap.put("tppd_limit",
				"");
		mainMap.put("tppd_rate",
				"");

		mainMap.put("excess_rate",
				"");
		mainMap.put("excess_amount",
				"");

		if (polMaster.getTranDate().before(new Date(2021, 2, 1))) {
			BASIC_PREMIUM = new BigDecimal(272);
		}

		mainMap.put("basic_premium",
				(BASIC_PREMIUM.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));
		mainMap.put("adjusted_premium",
				(ADJUSTED_PREMIUM.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("sticker_fee",
				(STICKER_FEE.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("levies",
				(polRisk.getFee().divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));
		mainMap.put("perils",
				(PERILS.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("ecowas_peril_charge",
				(ECOWAS_PERIL_CHARGE.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("brown_card_fee",
				(BROWN_CARD_FEE.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("personal_accident_charge",
				(PERSONAL_ACCIDENT_CHARGE.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("extra_seats_charge",
				(EXTRA_SEATS_CHARGE.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("extra_tppd_charge",
				(EXTRA_TPPD_CHARGE.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("additional_peril_charge",
				"0");

		mainMap.put("total_discounts",
				(TOTAL_DISCOUNTS.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("sub_total_premium",
				(SUB_TOTAL_PREMIUM.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		TOTAL_PREMIUM = TOTAL_PREMIUM.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP);
		mainMap.put("total_premium",
				TOTAL_PREMIUM.toString());

		mainMap.put("model",
				mtrVeh.getModelDesc());

		mainMap.put("co_insure_rate",
				"0");
		mainMap.put("co_insure_amount",
				"0");

		if (mtrVeh.getNoSeats() == 0) {
			mainMap.put("seats",
					"1");
		} else {

			mainMap.put("seats",
					mtrVeh.getNoSeats().toString());
		}

		mainMap.put("legacy_policy_number",
				polMaster.getPolNo());

		mainMap.put("comment",
				"");

		mainMap.put("mileage",
				mtrVeh.getMileage().toString());

		mainMap.put("reference",
				polMaster.getPolNo() + polMaster.getRenCnt() + polMaster.getEndtCnt() + mtrVeh.getRiskGrp()
						+ mtrVeh.getRiskNo() + mtrVeh.getVehRegNo());

		mainMap.put("excess_charge",
				"");

		mainMap.put("total_loadings",
				(TOTAL_LOADINGS.divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

		mainMap.put("server_reference",
				"");

		mainMap.put("co_insure_code",
				"");

		mainMap.put("discounts",
				"");

		mainMap.put("loadings",
				"");

		mainMap.put("customer",
				"");
		mainMap.put("intermediary",
				"");

		mainMap.put("sticker_number",
				"");

		mainMap.put("body_type_code",
				"01");

		mainMap.put("currency_code",
				polMaster.getBillCurr());

		mainMap.put("computation_type_code",
				"03");

		if (inputStream != null) {
			try {
				prop.load(inputStream);
			} catch (IOException e) {

			}
		} else {

		}
		@SuppressWarnings("unchecked")
		Enumeration<String> enums = (Enumeration<String>) propSchedule.propertyNames();
		while (enums.hasMoreElements() && (!foundScheduleCode)) {
			String key = enums.nextElement();
			String value = propSchedule.getProperty(key);
			if (mtrVeh.getCertRef().replace(" ", "-").trim().equalsIgnoreCase(key)) {
				foundScheduleCode = true;
				scheduleCode = value;
			}

		}

		if (!foundScheduleCode) {
			scheduleCode = "X.1PRIVATEINDIVIDUAL";
			foundScheduleCode = false;
		}

		System.out.println(scheduleCode);
		mainMap.put("schedule_code",
				scheduleCode);

		cover_type_code = getCoverType(polMaster, mtrVeh, polItem);

		switch (mtrVeh.getCoverType()) {
		case "COMP":

			mainMap.put("sum_insured",
					(polItem.getuOM1Val().divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

			break;
		case "TFT":

			mainMap.put("sum_insured",
					(polItem.getuOM1Val().divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP).toString()));

			break;
		default:

			mainMap.put("sum_insured",
					"0");

		}

		mainMap.put("cover_type_code",
				cover_type_code);

		mainMap.put("excess_type_code",
				"");

		mainMap.put("branch_code",
				polMaster.getBranch());

		mainMap.put("policy_number",
				"");
	}

//	private String getCoverType(final PolMaster polMaster, final PolMtrVeh mtrVeh, final PolItem polItem) {
//		String cover_type_code;
//		switch (mtrVeh.getCoverType()) {
//		case "C":
//			cover_type_code = "COMP";
//
//			break;
//		case "F":
//			cover_type_code = "TFT";
//
//		case "T":
//			cover_type_code = "TP";
//
//			break;
//		default:
//			cover_type_code = "TP";
//
//		}
//		return cover_type_code;
//	}

	private static String getCoverType(final PolMaster polMaster, final PolMtrVeh mtrVeh, final PolItem polItem) {
		String cover_type_code;
		switch (mtrVeh.getCoverType()) {
		case "C":
			cover_type_code = "COMP";

			break;
		case "F":
			cover_type_code = "TFT";

			break;
		default:
			cover_type_code = "TP";

		}
		return cover_type_code;
	}

//	private Integer calculateDays(final PolMaster polMaster, final PolRisk polRisk) {
//		DateTime start;
//		DateTime end;
//		Days d;
//		Integer days;
//		if (polRisk.getComDate() == null) {
//			start = new DateTime(polRisk.getComDate());
//			end = new DateTime(polRisk.getExpiryDate());
//		} else {
//			start = new DateTime(polMaster.getComDate());
//			end = new DateTime(polMaster.getExpiryDate());
//		}
//
//		d = Days.daysBetween(start, end).plus(1);
//		days = d.getDays();
//		return days;
//	}

	private static Integer calculateDays(final PolMaster polMaster, final PolRisk polRisk) {
		DateTime start;
		DateTime end;
		Days d;
		Integer days;
		if (polRisk.getComDate() == null) {
			start = new DateTime(polRisk.getComDate());
			end = new DateTime(polRisk.getExpiryDate());
		} else {
			start = new DateTime(polMaster.getComDate());
			end = new DateTime(polMaster.getExpiryDate());
		}

		d = Days.daysBetween(start, end).plus(1);
		days = d.getDays();
		return days;
	}

}
