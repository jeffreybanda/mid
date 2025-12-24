
package com.mid.app.swing.gui;

import static com.mid.app.http.utils.ConnectionUtils.checkConnectionToPortal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import org.apache.commons.validator.GenericValidator;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.mid.app.common.json.JsonReader;
import com.mid.app.common.json.JsonWriter;
import com.mid.app.common.model.HttpCode;
import com.mid.app.common.utils.DbCommandExecutor;
import com.mid.app.customerdata.model.CustomerData;
import com.mid.app.data.model.Data;
import com.mid.app.http.utils.ConnectionUtils;
import com.mid.app.http.utils.HttpAuthentication;
import com.mid.app.policydata.model.PolicyData;
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
import com.mid.app.swing.utils.ChangeComponentOrientation;
import com.mid.app.ui.extras.CustomTableHeaderRenderer;
import com.mid.app.ui.extras.PolicyTableRenderer;
import com.mid.app.utils.DateUtils;
import com.mid.app.utils.LoggingEngine;
import com.mid.app.utils.MapCreator;
import com.mid.app.xmm023.model.Xmm023;
import com.mid.app.xmm023.repository.Xmm023Repository;
import com.mid.app.xmm106.repository.Xmm106Repository;
import com.mid.app.xmm600.model.Xmm600;
import com.mid.app.xmm600.repository.Xmm600Repository;
import com.toedter.calendar.JDateChooser;

public class Main_Screen extends JPanel implements ActionListener, PropertyChangeListener {

	private JTable table;
	private Date convertedDate;
	private BigDecimal pctIncBaseAP = new BigDecimal(0.0D);
	private JPanel buttonPanel;
	private JProgressBar progressBar;
	private Task task;
	private JTextField polNoField;

	private JScrollPane scrollPane;

	private DefaultTableModel model;
	private JTextField vehRegRefField;

	private static final long serialVersionUID = 1L;

	private JButton importBtn, findBtn, validateBtn, historyBtn;
	private JDateChooser startDatePicker, endDatePicker, tranDatePicker;
	private ChangeComponentOrientation componentOrientation;
	private JLabel startdateLbl, endDateLbl, polNoLbl, agencyRefLbl;
	private String result;
	private final String[] rezColsName = { "TRAN DATE", "POLICY NUMBER", "STICKER NUMBER", "INCEPTION", "EXPIRY",
			"CUSTOMER NAME", "COVER TYPE", "VEHICLE REG", "MAKE", "MODEL" };
	private final CustomTableHeaderRenderer THR = new CustomTableHeaderRenderer();
	private final PolicyTableRenderer customTCR = new PolicyTableRenderer();
	private static LoggingEngine logging;
	
	PolMasterRepository polMasterRepository;
	PolMtrVehRepository polMtrVehRepository;
	PolRiskRepository polRiskRepository;
	PolItemRepository polItemRepository;
	PolItemBenRepository polItemBenRepository;
	Xmm600Repository xmm600Repository;
	Xmm106Repository xmm106Repository;
	Xmm023Repository xmm023Repository;
	DbCommandExecutor dbCommandExecutor;
	EntityManager em;
	EntityManagerFactory emf;
	private String polNo = null;
	String myDocuments = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();

	private boolean skipPolicy = true;
	private boolean branchExists = true;

	@SuppressWarnings("rawtypes")
	Map mainMap = new LinkedHashMap();

	public Main_Screen() {

		logging = LoggingEngine.getInstance();
		logging.setReady(Main_Screen.class.getName());
		logging.changeLoggingLevel(Level.FINE);
		logging.setConsoleLogging(false);

		componentOrientation = new ChangeComponentOrientation();
		componentOrientation.setThePanel(this);

		setLayout(new BorderLayout(0, 0));

		buttonPanel = new JPanel();
		buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		buttonPanel.setAutoscrolls(true);
		buttonPanel.setPreferredSize(new Dimension(10, 65));
		add(buttonPanel, BorderLayout.NORTH);

		importBtn = new JButton("Import");
		importBtn.setBounds(6, 12, 155, 45);
		importBtn.setIcon(new ImageIcon(Main_Screen.class.getResource("/icons/main_new_rez.png")));
		importBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
		importBtn.setPreferredSize(new Dimension(150, 33));
		importBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		importBtn.setFont(new Font("Arial", Font.BOLD, 12));
		importBtn.setEnabled(false);
		importBtn.setActionCommand("start");
		importBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				try {

					if (checkConnectionToPortal()) {
						createJsonRecords();
					} else {
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

					}

				} catch (final AuthenticationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (final ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (final IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

		buttonPanel.setLayout(null);
		buttonPanel.add(importBtn);

		buttonPanel.setLayout(null);
		// buttonPanel.add(historyBtn);

		validateBtn = new JButton("Validate Records");
		// validateBtn.setBounds(810, 9, 400, 45);
		// 690, 8, 114, 48
		validateBtn.setBounds(810, 9, 200, 48);
		validateBtn.setIcon(new ImageIcon(Main_Screen.class.getResource("/icons/main_new_rez.png")));
		validateBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
		validateBtn.setPreferredSize(new Dimension(150, 33));
		validateBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		validateBtn.setFont(new Font("Arial", Font.BOLD, 12));
		validateBtn.setEnabled(true);
		validateBtn.setActionCommand("start");
		validateBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				try {
					validateJsonRecords();
				} catch (AuthenticationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClientProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		});

		buttonPanel.setLayout(null);
		buttonPanel.add(validateBtn);

		final JSeparator separator = new JSeparator();
		separator.setBackground(Color.DARK_GRAY);
		separator.setBounds(175, 12, 13, 45);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setFocusable(true);
		separator.setForeground(Color.DARK_GRAY);
		separator.setAutoscrolls(true);
		separator.setPreferredSize(new Dimension(10, 20));
		buttonPanel.add(separator);

		startdateLbl = new JLabel("Start date : ");
		startdateLbl.setBounds(192, 8, 79, 26);
		buttonPanel.add(startdateLbl);

		startDatePicker = new JDateChooser();
		startDatePicker.setDate(convertedDate);
		startDatePicker.setDateFormatString("yyyy-MM-dd");
		startDatePicker.setBounds(275, 8, 155, 26);
		buttonPanel.add(startDatePicker);

		endDateLbl = new JLabel("End date : ");
		endDateLbl.setBounds(192, 35, 79, 26);
		buttonPanel.add(endDateLbl);

		endDatePicker = new JDateChooser();
		endDatePicker.setDate(convertedDate);
		endDatePicker.setDateFormatString("yyyy-MM-dd");
		endDatePicker.setBounds(275, 35, 155, 26);
		buttonPanel.add(endDatePicker);

		polNoLbl = new JLabel("Policy Number : ");
		polNoLbl.setBounds(442, 6, 94, 26);
		buttonPanel.add(polNoLbl);

		agencyRefLbl = new JLabel("Veh Reg No : ");
		agencyRefLbl.setBounds(442, 33, 94, 26);
		buttonPanel.add(agencyRefLbl);

		findBtn = new JButton("Search");
		findBtn.setIcon(new ImageIcon(Main_Screen.class.getResource("/icons/main_find.png")));
		findBtn.setPreferredSize(new Dimension(150, 33));
		findBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
		findBtn.setFont(new Font("Arial", Font.BOLD, 12));
		findBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		findBtn.setBounds(690, 8, 114, 48);
		findBtn.setActionCommand("start");
		findBtn.addActionListener(this);
		buttonPanel.add(findBtn);

		JPopupMenu menu = new JPopupMenu();
		Action cut = new DefaultEditorKit.CutAction();
		cut.putValue(Action.NAME, "Cut");
		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		menu.add(cut);

		Action copy = new DefaultEditorKit.CopyAction();
		copy.putValue(Action.NAME, "Copy");
		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		menu.add(copy);

		Action paste = new DefaultEditorKit.PasteAction();
		paste.putValue(Action.NAME, "Paste");
		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		menu.add(paste);

		Action selectAll = new SelectAll();
		menu.add(selectAll);

		polNoField = new JTextField();
		polNoField.setBounds(535, 6, 143, 26);
		polNoField.setFont(new Font("Arial", Font.BOLD, 13));
		polNoField.setColumns(10);
		buttonPanel.add(polNoField);
		polNoField.setComponentPopupMenu(menu);

		vehRegRefField = new JTextField();
		vehRegRefField.setBounds(535, 33, 143, 26);
		vehRegRefField.setFont(new Font("Arial", Font.BOLD, 13));
		vehRegRefField.setColumns(10);
		buttonPanel.add(vehRegRefField);

		model = new DefaultTableModel(rezColsName, 0);

		customTCR.setHorizontalAlignment(SwingConstants.CENTER);
		THR.setHorizontalAlignment(SwingConstants.CENTER);

		table = new JTable(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
		table.getTableHeader().setDefaultRenderer(THR);
		table.setDefaultRenderer(Object.class, customTCR);
		table.setFont(new Font("Dialog", Font.PLAIN, 14));
		table.setBackground(UIManager.getColor("InternalFrame.borderColor"));

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		add(scrollPane, BorderLayout.CENTER);

	}

	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			final Random random = new Random();
			int progress = 0;
			// Initialize progress property.
			setProgress(0);
			while (progress < 100) {
				// Sleep for up to one second.
				try {

					Thread.sleep(random.nextInt(1000));
				} catch (final InterruptedException ignore) {
				}
				// Make random progress.
				progress += random.nextInt(10);
				setProgress(Math.min(progress, 100));
			}
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			findBtn.setEnabled(true);
			setCursor(null); // turn off the wait cursor

		}

	}

	@Override
	public void actionPerformed(final ActionEvent evt) {

		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		task = new Task();
		task.addPropertyChangeListener(this);
		findRecord();
		// task.execute();
		findBtn.setEnabled(true);

	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			final int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);

		}
	}

	public List<PolMaster> selectionHistoryCriteria() {
		List<PolMaster> polMasters = null;

		final LocalDate startDate = LocalDate.parse("2020-01-01");
		final LocalDate endDate = LocalDate.parse("2020-01-20");

		prepareDataFlow();
		return polMasters = dbCommandExecutor.executeCommand(() -> {

			return polMasterRepository.findPolMasterRecordByTranDate(DateUtils.convertToDateViaInstant(startDate),
					DateUtils.convertToDateViaInstant(endDate));
		});

	}

	public List<PolMaster> selectionCriteria() {
		List<PolMaster> polMasters = null;

		if (polNoField.getText().length() > 0) {
			prepareDataFlow();
			polMasters = dbCommandExecutor.executeCommand(() -> {

				return polMasterRepository.findPolMasterRecordByPolNo(polNoField.getText());
			});

		} else if (vehRegRefField.getText().length() > 0) {
			prepareDataFlow();
			polNo = dbCommandExecutor.executeCommand(() -> {
				return polMtrVehRepository.findPolNoByVehReg(vehRegRefField.getText());
			});
			if (polNo != null) {
				polMasters = dbCommandExecutor.executeCommand(() -> {

					return polMasterRepository.findPolMasterRecordByPolNo(polNo);
				});
			}
		} else if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {

			// get dates from date pickers
			final LocalDate startDate = startDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			final LocalDate endDate = endDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			// compare if start date greater than end date
			if (startDate.isAfter(endDate)) {
				JOptionPane.showMessageDialog(null, "Start date is after end date!", JOptionPane.MESSAGE_PROPERTY,
						JOptionPane.WARNING_MESSAGE);
			} else {
				prepareDataFlow();
				polMasters = dbCommandExecutor.executeCommand(() -> {

					return polMasterRepository.findPolMasterRecordByTranDate(
							DateUtils.convertToDateViaInstant(startDate), DateUtils.convertToDateViaInstant(endDate));
				});
			}
		} else {

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			findBtn.setEnabled(true);
			JOptionPane.showMessageDialog(null, "Please Select Criteria to search on!", JOptionPane.MESSAGE_PROPERTY,
					JOptionPane.WARNING_MESSAGE);
		}

		return polMasters;

	}

	

	@SuppressWarnings("unchecked")
	public void createJsonRecords() throws AuthenticationException, ClientProtocolException, IOException {

		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		importBtn.setEnabled(false);

		// createBranches();

		final List<PolMaster> polMasters = selectionCriteria();

		model.setRowCount(0);

		if (polMasters != null) {

			for (final PolMaster polMaster : polMasters) {
				skipPolicy = dbCommandExecutor.executeCommand(() -> {
					return polMasterRepository.isSkipPolicy(polMaster.getPolNo(), polMaster.getRenCnt());
				});

				if (!skipPolicy) {
					List<PolMtrVeh> polMtrVehs;

					if (vehRegRefField.getText().length() > 0) {

						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
							return polMtrVehRepository.findPolMtrVehicleByVehRegNo(polMaster.getPolNo(),
									polMaster.getRenCnt(), polMaster.getEndtCnt(), vehRegRefField.getText());
						});
					} else {
						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
							return polMtrVehRepository.findPolMtrVehicleList(polMaster.getPolNo(),
									polMaster.getRenCnt(), polMaster.getEndtCnt());
						});
					}

					for (final PolMtrVeh mtrVeh : polMtrVehs) {

						final List<PolRisk> polRisks = dbCommandExecutor.executeCommand(() -> {
							return polRiskRepository.findPolRiskRecord(polMaster.getPolNo(), polMaster.getRenCnt(),
									polMaster.getEndtCnt(), mtrVeh.getRiskGrp(), mtrVeh.getRiskNo());
						});

						for (final PolRisk polRisk : polRisks) {

							pctIncBaseAP = dbCommandExecutor.executeCommand(() -> {
								return xmm106Repository.findPctIncBaseAPForClass(polRisk.getBusinessClass(),
										mtrVeh.getCoverType());
							});

							PolItem polItem = polItemRepository.findByPrimaryKey(polMaster.getPolNo(),
									polMaster.getRenCnt(), polMaster.getEndtCnt(), mtrVeh.getRiskGrp(),
									mtrVeh.getRiskNo(), mtrVeh.getItemNo());

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

									System.out.println(polMaster.getPolNo());

									result = buildMotorPolicyJson(polMaster, polRisk, mtrVeh, polItem, itemBens);
									importJsonRecords(result, polMaster.getPolNo());
									System.out.println(result);
								} else {

								}
							}
						}
					}
				}
			}

		}

		;

		importBtn.setEnabled(false);

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		mainMap.clear();
		// printWriter.close();
		em.close();
		emf.close();
		JOptionPane.showMessageDialog(null, "Import Process Completed !", "Data Import",
				JOptionPane.INFORMATION_MESSAGE);

	}

	private String buildMotorPolicyJson(PolMaster polMaster, PolRisk polRisk, PolMtrVeh polMtrVeh, PolItem polItem, List<PolItemBen> itemBens) {

		// Root
		JsonObject root = new JsonObject();
		
		String productCode =getCoverType( polMtrVeh);

		// =====================
		// data object
		// =====================
		JsonObject data = new JsonObject();
		data.addProperty("branchCode", polMaster.getBranch());
		data.addProperty("productCode", productCode);
		data.addProperty("intermediaryCode", polMaster.getAcctNo1());
		data.addProperty("subintermediaryCode", "null");
		data.addProperty("riskTypeCode",getScheduleCode( polMtrVeh));
		data.addProperty("companyAssignedPolicyNumber", polMaster.getPolNo());
		// =====================
		// vehicleData
		// =====================
		JsonObject vehicleData = new JsonObject();
		vehicleData.addProperty("registrationNumber", polMtrVeh.getVehRegNo());
		vehicleData.addProperty("chassisNumber", polMtrVeh.getChassisNo());
		vehicleData.addProperty("make", polMtrVeh.getVehMake());
		vehicleData.addProperty("model", polMtrVeh.getModelDesc());
		vehicleData.addProperty("manufacturingYear",Integer.parseInt(polMtrVeh.getYrManu()));
		vehicleData.addProperty("registrationYear", polMtrVeh.getRegYr());
		vehicleData.addProperty("vehicleColour", polMtrVeh.getColour());
		vehicleData.addProperty("bodyType", polMtrVeh.getVehBody());
		vehicleData.addProperty("seatingCapacity", polMtrVeh.getNoSeats());
		vehicleData.addProperty("cubicCapacity", polMtrVeh.getEngineCC());
		vehicleData.addProperty("vehicleUsage", polMtrVeh.getVehUsg());
		vehicleData.addProperty("fuelType", "NA");
		vehicleData.addProperty("vehicleValue", polItem.getuOM1Val());
		vehicleData.addProperty("verified", true);

		data.add("vehicleData", vehicleData);

		// =====================
		// customerData
		// =====================
		JsonObject customerData = new JsonObject();
		
		Optional<Xmm600> optClient = xmm600Repository.findClient(polMaster.getInsured());
		Xmm600 xmm600 = null;
		if(optClient.isPresent()) {
			xmm600 = optClient.get();
		}
		
		//Xmm600 xmm600 = xmm600Repository.findClient(productCode):
		customerData.addProperty("isActive", true);
		customerData.addProperty("type", "INDIVIDUAL");
		customerData.addProperty("title", getTitle(xmm600));
		customerData.addProperty("firstName", resolveFirstName(polMaster));
		customerData.addProperty("lastName", polMaster.getInsdName1());
		customerData.addProperty("gender",getGender(xmm600));
		customerData.addProperty("dateOfBirth",convertToMIDDate( xmm600.getBirthday().toString()));
		customerData.addProperty("nationality", "GH");
		customerData.addProperty("ghanaCardNumber", "GHA-994456789-0");
		customerData.addProperty("email", xmm600.getEmail().equals("") ? null : xmm600.getEmail());
		customerData.addProperty("phoneNumber", xmm600.getTelno7());
		customerData.addProperty("digitalAddress", "GA-123-4567");
		customerData.addProperty("residentialAddress", polMaster.getInsdAddr1());
		customerData.addProperty("occupation", polMaster.getOccupation().equals("")? "NA":polMaster.getOccupation());

		data.add("customerData", customerData);

		// =====================
		// policyData
		// =====================
		JsonObject policyData = new JsonObject();
		policyData.addProperty("startDate",convertToMIDDate( polMaster.getComDate().toString()));
		policyData.addProperty("expiryDate",convertToMIDDate( polMaster.getExpiryDate().toString()));
		policyData.addProperty("sumInsured", polItem.getuOM1Val().divide(polMaster.getBillCurrRate(), 2, RoundingMode.HALF_UP));
		policyData.addProperty("grossPremium", 7280.0);
		policyData.addProperty("type", "NEW_BUSINESS");
		policyData.addProperty("transactionDate",convertToMIDDate(  polMaster.getTranDate().toString()));
		policyData.addProperty("excessBought", false);
		policyData.addProperty("noClaimDiscount", 0);
		policyData.addProperty("earnedNoClaimDiscount", 0);
		policyData.addProperty("calculationType", "FULL_YEAR");

		data.add("policyData", policyData);

		// =====================
		// attach data to root
		// =====================
		root.add("data", data);

		return root.toString();
	}
	
	private String resolveFirstName(PolMaster polMaster) {
	    if (polMaster.getInsdName2() != null && !polMaster.getInsdName2().trim().isEmpty()) {
	        return polMaster.getInsdName2();
	    }

	    String insdName1 = polMaster.getInsdName1();
	    if (insdName1 != null && !insdName1.trim().isEmpty()) {
	        return insdName1.trim().split("\\s+")[0];
	    }

	    return "";
	}

	
	private String getScheduleCode(PolMtrVeh mtrVeh) {
		boolean foundScheduleCode =false;
		String scheduleCode = null;
		
		
		 final InputStream is = Main_Screen.class.getResourceAsStream(System.getProperty("PropFile"));
	        final Properties propSchedule = new Properties();
	        
	        try {
				propSchedule.load(is);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	        
	        
	        
	        
	        @SuppressWarnings("unchecked")
		Enumeration<String> enums = (Enumeration<String>) propSchedule.propertyNames();
		while (enums.hasMoreElements() && (!foundScheduleCode)) {
			String key = enums.nextElement();
			System.out.println("Key is : " + key + " Schedule Code is : " + mtrVeh.getCertRef());
			String value = propSchedule.getProperty(key);
			if (mtrVeh.getCertRef().equalsIgnoreCase(key)) {
				foundScheduleCode = true;
				scheduleCode = value;
			}

		}

		if (!foundScheduleCode) {
			scheduleCode = "PRIVATE_INDIVIDUAL_X1";
			foundScheduleCode = false;
		}

		return scheduleCode;
		
	}
	
private static String convertToMIDDate(String input) {
        DateTimeFormatter inputFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        DateTimeFormatter outputFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime dateTime =
                LocalDateTime.parse(input, inputFormatter);

        return dateTime.format(outputFormatter);
    }
	
	
	private static String getCoverType(final PolMtrVeh mtrVeh) {
		String cover_type_code;
		switch (mtrVeh.getCoverType()) {
		case "C":
			cover_type_code = "MTCOMP";

			break;
		case "F":
			cover_type_code = "MTPFT";

			break;
		default:
			cover_type_code = "MTP";

		}
		return cover_type_code;
	}
	
	private static String getGender(final Xmm600 xmm600) {
		String gender;
		switch (xmm600.getGender()) {
		case "M":
			gender = "MALE";

			break;
		case "F":
			gender = "FEMALE";

			break;
		default:
			gender = null;

		}
		return gender;
	}
	
	private static String getTitle(final Xmm600 xmm600) {
		String title;
		switch (xmm600.getTitleName()) {
		case "":
			title = "NA";

			break;
	
		default:
			title = xmm600.getTitleName();

		}
		return title;
	}



	@SuppressWarnings({ "unchecked", "resource" })
	public void validateJsonRecords() throws AuthenticationException, ClientProtocolException, IOException {
		JsonObject jsonObject = new JsonObject();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		FileWriter file = new FileWriter(myDocuments + "\\validation.json");
		BufferedWriter bw = null;
		PrintWriter pw = null;

		final List<PolMaster> polMasters = selectionCriteria();

		model.setRowCount(0);

		if (polMasters != null) {

			for (final PolMaster polMaster : polMasters) {
				skipPolicy = dbCommandExecutor.executeCommand(() -> {
					return polMasterRepository.isSkipPolicy(polMaster.getPolNo(), polMaster.getRenCnt());
				});

				if (!skipPolicy) {
					List<PolMtrVeh> polMtrVehs;

					if (vehRegRefField.getText().length() > 0) {

						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
							return polMtrVehRepository.findPolMtrVehicleByVehRegNo(polMaster.getPolNo(),
									polMaster.getRenCnt(), polMaster.getEndtCnt(), vehRegRefField.getText());
						});
					} else {
						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
							return polMtrVehRepository.findPolMtrVehicleList(polMaster.getPolNo(),
									polMaster.getRenCnt(), polMaster.getEndtCnt());
						});
					}

					for (final PolMtrVeh mtrVeh : polMtrVehs) {

						final List<PolRisk> polRisks = dbCommandExecutor.executeCommand(() -> {
							return polRiskRepository.findPolRiskRecord(polMaster.getPolNo(), polMaster.getRenCnt(),
									polMaster.getEndtCnt(), mtrVeh.getRiskGrp(), mtrVeh.getRiskNo());
						});

						for (final PolRisk polRisk : polRisks) {

							pctIncBaseAP = dbCommandExecutor.executeCommand(() -> {
								return xmm106Repository.findPctIncBaseAPForClass(polRisk.getBusinessClass(),
										mtrVeh.getCoverType());
							});

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

							for (final Xmm600 xmm600 : xmm600ClientList) {

								jsonObject = (JsonObject) validateRecord(polMaster, polRisk, mtrVeh, xmm600);

								bw = new BufferedWriter(file);
								pw = new PrintWriter(bw);
								pw.println(jsonObject.toString());
								pw.flush();
							}

						}
					}
				}
			}

			Desktop.getDesktop().open(new File(myDocuments + "\\validation.json"));

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

			em.close();
			emf.close();

			pw.close();
			bw.close();
			file.close();
			JOptionPane.showMessageDialog(null, "Validation Process Completed !", "Data Validation",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "No records To be Validated !", "Data Validation",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	private JsonElement validateRecord(final PolMaster polMaster, final PolRisk polRisk, final PolMtrVeh mtrVeh,
			final Xmm600 xmm600) {
		JsonObject jsonObject = new JsonObject();
		String result = "";
		boolean isValid = true;

		if (polRisk.getComDate() == null) {
			jsonObject.addProperty("riskCommenceDateIdentification", polMaster.getPolNo());
			jsonObject.addProperty("riskCommenceDateDescription", "Risk Commence Date is Null");
			isValid = false;
		}

		if (polRisk.getExpiryDate() == null) {
			jsonObject.addProperty("riskExpiryDateIdentification", polMaster.getPolNo());
			jsonObject.addProperty("riskExpiryDateDescription", "Risk Expiry Date is Null");
			isValid = false;
		}

		if (xmm600.getName1().isEmpty()) {
			isValid = false;
		}

		if (xmm600.getBirthday() == null) {

			jsonObject.addProperty("clientBirthdayIdentification",
					xmm600.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm600.getName1());
			jsonObject.addProperty("clientBirthDayDescription", "Birthday is Null");
			isValid = false;
		}

		if (xmm600.getBirthday() != null) {
			if (!GenericValidator.isDate(xmm600.getBirthday().toString().substring(0, 10), "yyyy-MM-dd", true)) {

				jsonObject.addProperty("invalidBirthdayIdentification",
						xmm600.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm600.getName1().toString() + " "
								+ xmm600.getBirthday().toString().substring(0, 10));
				jsonObject.addProperty("Invalid BirthdayDescription", "Invalid Birthday");
				isValid = false;
			}
		}

		if (xmm600.getTelno7() == null || xmm600.getTelno7().isEmpty() || xmm600.getTelno7().length() < 10) {

			jsonObject.addProperty("invalidClientTelephoneIdentification", xmm600.getClientNo() + " "
					+ polMaster.getPolNo() + "  " + xmm600.getName1().toString() + " " + xmm600.getTelno7().toString());
			jsonObject.addProperty("invalidClientTelephoneDescription", "Invalid Client Telephone");
			isValid = false;
		}

		if (mtrVeh.getColour() == null || mtrVeh.getColour().isEmpty()) {
			jsonObject.addProperty("motorColorIdentification",
					polMaster.getPolNo() + " " + xmm600.getName1().toString() + " " + mtrVeh.getVehRegNo());
			jsonObject.addProperty("motorColoreDescription", "No Motor Colour");
			isValid = false;
		}

		if (mtrVeh.getNoSeats() == null || mtrVeh.getNoSeats() == 0) {
			jsonObject.addProperty("numberOfSeatsIdentification",
					polMaster.getPolNo() + " " + xmm600.getName1().toString() + " " + mtrVeh.getVehRegNo());
			jsonObject.addProperty("numberOfSeatDescription", "Number of Seats should be greater than Zero!!");
			isValid = false;
		}
		return jsonObject;
	}

	private void writeJsonRecords(final String polNo, final Integer renCnt, final Integer endtCnt) {

		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("polNo", polNo);
		jsonObject.addProperty("renCnt", renCnt);
		jsonObject.addProperty("endtCnt", endtCnt);
		jsonObject.addProperty("trandate", endtCnt);

		String result = new Gson().toJson(jsonObject);

		// System.out.println(result);

	}

	private boolean recordIsValidated(final PolMaster polMaster, final PolRisk polRisk, final PolMtrVeh mtrVeh,
			final List<PolItemBen> itemBens, final List<Xmm600> xmm600ClientList,
			final List<Xmm600> xmm600IntermediaryList) {

		// return true;

		JsonObject jsonObject = new JsonObject();
		String result = "";
		boolean isValid = true;

		if (itemBens.isEmpty()) {
			jsonObject.addProperty("NoItemBenefitsIdentification", polMaster.getPolNo() + " " + mtrVeh.getVehRegNo());
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
						xmm6002.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm6002.getName1());
				jsonObject.addProperty("clientBirthDayDescription", "Birthday is Null");
				isValid = false;
			}

			if (xmm6002.getBirthday() != null) {
				if (!GenericValidator.isDate(xmm6002.getBirthday().toString().substring(0, 10), "yyyy-MM-dd", true)) {

					jsonObject.addProperty("invalidBirthdayIdentification",
							xmm6002.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm6002.getName1().toString()
									+ " " + xmm6002.getBirthday().toString().substring(0, 10));
					jsonObject.addProperty("Invalid BirthdayDescription", "Invalid Birthday");
					isValid = false;
				}
			}

			if (xmm6002.getTelno7() == null || xmm6002.getTelno7().isEmpty() || xmm6002.getTelno7().length() < 10) {

				jsonObject.addProperty("invalidClientTelephoneIdentification",
						xmm6002.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm6002.getName1().toString() + " "
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

	

	protected void importJsonRecords(final String json, final String polNo)
			throws ClientProtocolException, IOException, AuthenticationException {

		final CloseableHttpResponse response = HttpAuthentication.getPostPolicyToPortalResponse(json);

		final HttpEntity body = response.getEntity();

		final StatusLine statusLine = response.getStatusLine();
		System.out.println(response.getStatusLine());
		final String content = EntityUtils.toString(body);

		if (statusLine.getStatusCode() == HttpCode.OK.getCode()) {

			
			System.out.println(polNo.toString() + " " + HttpCode.OK.getCode());

		} else {
			logging.setMessage(polNo.toString() + " " + content.toString());
			System.out.println(polNo.toString() + " " + content.toString());
		}

	}

	protected void getMIDSchedules() throws AuthenticationException {

		final String branches = HttpAuthentication.getMIDBranches();

	}

	protected JsonArray getMIDBranches() throws AuthenticationException {

		final String branches = HttpAuthentication.getMIDBranches();

		return JsonReader.readAsJsonArray(branches);

	}

	private void updatePolMtrVeh(final String content) {

		final String polNo;

		final Integer renCnt;

		final Integer endtCnt;

		final String vehRegNo;

		final String referenceNumber;

		String stickerNumber;

		final String refExp = "$.motor_policy.reference";
		final String stickerExp = "$.motor_policy.sticker_number";
		final String vehRegNumberExp = "$.motor_policy.vehicle_registration";
		referenceNumber = JsonPath.parse(content).read(refExp);
		stickerNumber = JsonPath.parse(content).read(stickerExp);
		if (!(stickerNumber == null)) {
			final Matcher matcher = Pattern.compile("\\d+").matcher(stickerNumber);
			matcher.find();
			final Long i = Long.valueOf(matcher.group());
			stickerNumber = i.toString();
			polNo = referenceNumber.substring(0, 16);

			renCnt = Integer.parseInt(referenceNumber.substring(16, 17));

			endtCnt = Integer.parseInt(referenceNumber.substring(17, 18));

			vehRegNo = JsonPath.parse(content).read(vehRegNumberExp);
			;
			getReadyForDataUpdate();
			final PolMtrVeh polMtrVeh = dbCommandExecutor.executeCommand(() -> {
				return polMtrVehRepository.findPolMtrVehicleRecordByCompositeKey(polNo, renCnt, endtCnt, vehRegNo);
			});

			if (polMtrVeh != null) {
				polMtrVeh.setOwnName(stickerNumber);

				dbCommandExecutor.executeCommand(() -> {

					polMtrVehRepository.update(polMtrVeh);
					return null;
				});
			}
		}
	}

	public synchronized void prepareDataFlow() {

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

		xmm023Repository = new Xmm023Repository();
		xmm023Repository.em = em;
		xmm023Repository.setDbCommandExecutor(dbCommandExecutor);
	}

	public synchronized void getReadyForDataUpdate() {

		emf = Persistence.createEntityManagerFactory("midUP");
		em = emf.createEntityManager();
		dbCommandExecutor = new DbCommandExecutor(em);
		polMtrVehRepository = new PolMtrVehRepository();
		polMtrVehRepository.em = em;
		polMtrVehRepository.setDbCommandExecutor(dbCommandExecutor);

	}

	public void findRecord() {

		final List<PolMaster> polMasters = selectionCriteria();
		model.setRowCount(0);

		if (polMasters != null) {

			for (final PolMaster polMaster : polMasters) {

				skipPolicy = dbCommandExecutor.executeCommand(() -> {
					return polMasterRepository.isSkipPolicy(polMaster.getPolNo(), polMaster.getRenCnt());
				});

				if (!skipPolicy) {
					List<PolMtrVeh> polMtrVehs;

					if (vehRegRefField.getText().length() > 0) {

						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
							return polMtrVehRepository.findPolMtrVehicleByVehRegNo(polMaster.getPolNo(),
									polMaster.getRenCnt(), polMaster.getEndtCnt(), vehRegRefField.getText());
						});
					} else {
						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
							return polMtrVehRepository.findPolMtrVehicleList(polMaster.getPolNo(),
									polMaster.getRenCnt(), polMaster.getEndtCnt());
						});
					}

					for (final PolMtrVeh mtrVeh : polMtrVehs) {

						final List<PolRisk> polRisks = dbCommandExecutor.executeCommand(() -> {
							return polRiskRepository.findPolRiskRecord(polMaster.getPolNo(), polMaster.getRenCnt(),
									polMaster.getEndtCnt(), mtrVeh.getRiskGrp(), mtrVeh.getRiskNo());
						});

						for (final PolRisk polRisk : polRisks) {

							final Object[] colRowVect = new Object[] { polMaster.getTranDate().toString(),
									polMaster.getPolNo(), mtrVeh.getOwnName(), polRisk.getComDate(),
									polRisk.getExpiryDate(),
									polMaster.getInsdName1() + polMaster.getInsdName2() + polMaster.getInsdName3(),
									mtrVeh.getCoverType(), mtrVeh.getVehRegNo(), mtrVeh.getVehMake(),
									mtrVeh.getModelDesc() };
							model.addRow(colRowVect);

						}

					}

					// for (final PolMtrVeh mtrVeh : polMtrVehs) {

					// final Object[] colRowVect = new Object[] {
					// polMaster.getTranDate().toString(),
					// polMaster.getPolNo(), mtrVeh.getOwnName(), polMaster.getComDate(),
					// polMaster.getExpiryDate(),
					// polMaster.getInsdName1() + polMaster.getInsdName2() +
					// polMaster.getInsdName3(),
					// mtrVeh.getCoverType(), mtrVeh.getVehRegNo(), mtrVeh.getVehMake(),
					// mtrVeh.getModelDesc() };
					// model.addRow(colRowVect);
					// }
				}
			}
			em.close();
			emf.close();
			importBtn.setEnabled(true);

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		}

	}

	static class SelectAll extends TextAction {
		public SelectAll() {
			super("Select All");
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			JTextComponent component = getFocusedComponent();
			component.selectAll();
			component.requestFocusInWindow();
		}
	}

}

//
//package com.mid.app.swing.gui;
//
//import static com.mid.app.http.utils.ConnectionUtils.checkConnectionToPortal;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Cursor;
//import java.awt.Desktop;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.Enumeration;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Random;
//import java.util.logging.Level;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import javax.swing.Action;
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JFileChooser;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JPopupMenu;
//import javax.swing.JProgressBar;
//import javax.swing.JScrollPane;
//import javax.swing.JSeparator;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//import javax.swing.KeyStroke;
//import javax.swing.SwingConstants;
//import javax.swing.SwingWorker;
//import javax.swing.UIManager;
//import javax.swing.border.BevelBorder;
//import javax.swing.border.SoftBevelBorder;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.text.DefaultEditorKit;
//import javax.swing.text.JTextComponent;
//import javax.swing.text.TextAction;
//
//import org.apache.commons.validator.GenericValidator;
//import org.apache.http.HttpEntity;
//import org.apache.http.StatusLine;
//import org.apache.http.auth.AuthenticationException;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.util.EntityUtils;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.jayway.jsonpath.JsonPath;
//import com.mid.app.common.json.JsonReader;
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
//import com.mid.app.swing.utils.ChangeComponentOrientation;
//import com.mid.app.ui.extras.CustomTableHeaderRenderer;
//import com.mid.app.ui.extras.PolicyTableRenderer;
//import com.mid.app.utils.DateUtils;
//import com.mid.app.utils.LoggingEngine;
//import com.mid.app.utils.MapCreator;
//import com.mid.app.xmm023.model.Xmm023;
//import com.mid.app.xmm023.repository.Xmm023Repository;
//import com.mid.app.xmm106.repository.Xmm106Repository;
//import com.mid.app.xmm600.model.Xmm600;
//import com.mid.app.xmm600.repository.Xmm600Repository;
//import com.toedter.calendar.JDateChooser;
//
//public class Main_Screen extends JPanel implements ActionListener, PropertyChangeListener {
//
//	private JTable table;
//	private Date convertedDate;
//	private BigDecimal pctIncBaseAP = new BigDecimal(0.0D);
//	private JPanel buttonPanel;
//	private JProgressBar progressBar;
//	private Task task;
//	private JTextField polNoField;
//
//	private JScrollPane scrollPane;
//
//	private DefaultTableModel model;
//	private JTextField vehRegRefField;
//
//	private static final long serialVersionUID = 1L;
//
//	private JButton importBtn, findBtn, validateBtn, historyBtn;
//	private JDateChooser startDatePicker, endDatePicker, tranDatePicker;
//	private ChangeComponentOrientation componentOrientation;
//	private JLabel startdateLbl, endDateLbl, polNoLbl, agencyRefLbl;
//	private String result;
//	private String response;
//	private final String[] rezColsName = { "TRAN DATE", "POLICY NUMBER", "CERT REF NUMBER", "INCEPTION", "EXPIRY",
//			"CUSTOMER NAME", "COVER TYPE", "VEHICLE REG", "MAKE", "MODEL" };
//	private final CustomTableHeaderRenderer THR = new CustomTableHeaderRenderer();
//	private final PolicyTableRenderer customTCR = new PolicyTableRenderer();
//	private static LoggingEngine logging;
//
//	PolFees polFees;
//	PolMasterRepository polMasterRepository;
//	PolFeesRepository polFeesRepository;
//	PolMtrVehRepository polMtrVehRepository;
//	PolRiskRepository polRiskRepository;
//	PolItemRepository polItemRepository;
//	PolItemBenRepository polItemBenRepository;
//	Xmm600Repository xmm600Repository;
//	Xmm106Repository xmm106Repository;
//	Xmm023Repository xmm023Repository;
//	DbCommandExecutor dbCommandExecutor;
//	EntityManager em;
//	EntityManagerFactory emf;
//	private String polNo = null;
//	String myDocuments = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
//	Integer maxEndtCnt = new Integer(0);
//	private boolean skipPolicy = true;
//	private boolean branchExists = true;
//
//	@SuppressWarnings("rawtypes")
//	Map mainMap = new LinkedHashMap();
//
//	public Main_Screen() {
//
//		logging = LoggingEngine.getInstance();
//		logging.setReady(Main_Screen.class.getName());
//		logging.changeLoggingLevel(Level.FINE);
//		logging.setConsoleLogging(false);
//
//		componentOrientation = new ChangeComponentOrientation();
//		componentOrientation.setThePanel(this);
//
//		setLayout(new BorderLayout(0, 0));
//
//		buttonPanel = new JPanel();
//		buttonPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
//		buttonPanel.setAutoscrolls(true);
//		buttonPanel.setPreferredSize(new Dimension(10, 65));
//		add(buttonPanel, BorderLayout.NORTH);
//
//		importBtn = new JButton("Import");
//		importBtn.setBounds(6, 12, 155, 45);
//		importBtn.setIcon(new ImageIcon(Main_Screen.class.getResource("/icons/main_new_rez.png")));
//		importBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
//		importBtn.setPreferredSize(new Dimension(150, 33));
//		importBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
//		importBtn.setFont(new Font("Arial", Font.BOLD, 12));
//		importBtn.setEnabled(false);
//		importBtn.setActionCommand("start");
//		importBtn.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(final ActionEvent e) {
//
//				try {
//
//					if (checkConnectionToPortal()) {
//						createJsonRecords();
//					} else {
//						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//
//					}
//
//				} catch (final AuthenticationException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (final ClientProtocolException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (final IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//
//			}
//
//		});
//
//		buttonPanel.setLayout(null);
//		buttonPanel.add(importBtn);
//
//		historyBtn = new JButton("Import History");
//		historyBtn.setBounds(1020, 12, 155, 45);
//		historyBtn.setIcon(new ImageIcon(Main_Screen.class.getResource("/icons/main_new_rez.png")));
//		historyBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
//		historyBtn.setPreferredSize(new Dimension(150, 33));
//		historyBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
//		historyBtn.setFont(new Font("Arial", Font.BOLD, 12));
//		historyBtn.setEnabled(true);
//		historyBtn.setActionCommand("start");
//		historyBtn.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(final ActionEvent e) {
//
//				try {
//
//					int selectedOption = JOptionPane.showConfirmDialog(null,
//							"Do you want to run historical data for January 2020?", "Choose",
//							JOptionPane.YES_NO_OPTION);
//					if (selectedOption == JOptionPane.NO_OPTION) {
//						System.exit(1);
//					}
//
//					if (checkConnectionToPortal()) {
//						createHistoryJsonRecords();
//					} else {
//						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//
//					}
//
//				} catch (final AuthenticationException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (final ClientProtocolException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (final IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//
//			}
//
//		});
//
//		buttonPanel.setLayout(null);
//		// buttonPanel.add(historyBtn);
//
//		validateBtn = new JButton("Validate Records");
//		// validateBtn.setBounds(810, 9, 400, 45);
//		// 690, 8, 114, 48
//		validateBtn.setBounds(810, 9, 200, 48);
//		validateBtn.setIcon(new ImageIcon(Main_Screen.class.getResource("/icons/main_new_rez.png")));
//		validateBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
//		validateBtn.setPreferredSize(new Dimension(150, 33));
//		validateBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
//		validateBtn.setFont(new Font("Arial", Font.BOLD, 12));
//		validateBtn.setEnabled(true);
//		validateBtn.setActionCommand("start");
//		validateBtn.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(final ActionEvent e) {
//
//				try {
//					validateJsonRecords();
//				} catch (AuthenticationException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (ClientProtocolException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//
//			}
//
//		});
//
//		buttonPanel.setLayout(null);
//		buttonPanel.add(validateBtn);
//
//		final JSeparator separator = new JSeparator();
//		separator.setBackground(Color.DARK_GRAY);
//		separator.setBounds(175, 12, 13, 45);
//		separator.setOrientation(SwingConstants.VERTICAL);
//		separator.setFocusable(true);
//		separator.setForeground(Color.DARK_GRAY);
//		separator.setAutoscrolls(true);
//		separator.setPreferredSize(new Dimension(10, 20));
//		buttonPanel.add(separator);
//
//		startdateLbl = new JLabel("Start date : ");
//		startdateLbl.setBounds(192, 8, 79, 26);
//		buttonPanel.add(startdateLbl);
//
//		startDatePicker = new JDateChooser();
//		startDatePicker.setDate(convertedDate);
//		startDatePicker.setDateFormatString("yyyy-MM-dd");
//		startDatePicker.setBounds(275, 8, 155, 26);
//		buttonPanel.add(startDatePicker);
//
//		endDateLbl = new JLabel("End date : ");
//		endDateLbl.setBounds(192, 35, 79, 26);
//		buttonPanel.add(endDateLbl);
//
//		endDatePicker = new JDateChooser();
//		endDatePicker.setDate(convertedDate);
//		endDatePicker.setDateFormatString("yyyy-MM-dd");
//		endDatePicker.setBounds(275, 35, 155, 26);
//		buttonPanel.add(endDatePicker);
//
//		polNoLbl = new JLabel("Policy Number : ");
//		polNoLbl.setBounds(442, 6, 94, 26);
//		buttonPanel.add(polNoLbl);
//
//		agencyRefLbl = new JLabel("Veh Reg No : ");
//		agencyRefLbl.setBounds(442, 33, 94, 26);
//		buttonPanel.add(agencyRefLbl);
//
//		findBtn = new JButton("Search");
//		findBtn.setIcon(new ImageIcon(Main_Screen.class.getResource("/icons/main_find.png")));
//		findBtn.setPreferredSize(new Dimension(150, 33));
//		findBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
//		findBtn.setFont(new Font("Arial", Font.BOLD, 12));
//		findBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
//		findBtn.setBounds(690, 8, 114, 48);
//		findBtn.setActionCommand("start");
//		findBtn.addActionListener(this);
//		buttonPanel.add(findBtn);
//
//		JPopupMenu menu = new JPopupMenu();
//		Action cut = new DefaultEditorKit.CutAction();
//		cut.putValue(Action.NAME, "Cut");
//		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
//		menu.add(cut);
//
//		Action copy = new DefaultEditorKit.CopyAction();
//		copy.putValue(Action.NAME, "Copy");
//		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
//		menu.add(copy);
//
//		Action paste = new DefaultEditorKit.PasteAction();
//		paste.putValue(Action.NAME, "Paste");
//		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
//		menu.add(paste);
//
//		Action selectAll = new SelectAll();
//		menu.add(selectAll);
//
//		polNoField = new JTextField();
//		polNoField.setBounds(535, 6, 143, 26);
//		polNoField.setFont(new Font("Arial", Font.BOLD, 13));
//		polNoField.setColumns(10);
//		buttonPanel.add(polNoField);
//		polNoField.setComponentPopupMenu(menu);
//
//		vehRegRefField = new JTextField();
//		vehRegRefField.setBounds(535, 33, 143, 26);
//		vehRegRefField.setFont(new Font("Arial", Font.BOLD, 13));
//		vehRegRefField.setColumns(10);
//		buttonPanel.add(vehRegRefField);
//		vehRegRefField.setComponentPopupMenu(menu);
//		model = new DefaultTableModel(rezColsName, 0);
//
//		customTCR.setHorizontalAlignment(SwingConstants.CENTER);
//		THR.setHorizontalAlignment(SwingConstants.CENTER);
//
//		table = new JTable(model);
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		table.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
//		table.getTableHeader().setDefaultRenderer(THR);
//		table.setDefaultRenderer(Object.class, customTCR);
//		table.setFont(new Font("Dialog", Font.PLAIN, 14));
//		table.setBackground(UIManager.getColor("InternalFrame.borderColor"));
//
//		scrollPane = new JScrollPane();
//		scrollPane.setViewportView(table);
//		add(scrollPane, BorderLayout.CENTER);
//
//	}
//
//	class Task extends SwingWorker<Void, Void> {
//		/*
//		 * Main task. Executed in background thread.
//		 */
//		@Override
//		public Void doInBackground() {
//			final Random random = new Random();
//			int progress = 0;
//			// Initialize progress property.
//			setProgress(0);
//			while (progress < 100) {
//				// Sleep for up to one second.
//				try {
//
//					Thread.sleep(random.nextInt(1000));
//				} catch (final InterruptedException ignore) {
//				}
//				// Make random progress.
//				progress += random.nextInt(10);
//				setProgress(Math.min(progress, 100));
//			}
//			return null;
//		}
//
//		/*
//		 * Executed in event dispatching thread
//		 */
//		@Override
//		public void done() {
//			Toolkit.getDefaultToolkit().beep();
//			findBtn.setEnabled(true);
//			setCursor(null); // turn off the wait cursor
//
//		}
//
//	}
//
//	@Override
//	public void actionPerformed(final ActionEvent evt) {
//
//		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//
//		task = new Task();
//		task.addPropertyChangeListener(this);
//		findRecord();
//		// task.execute();
//		findBtn.setEnabled(true);
//
//	}
//
//	@Override
//	public void propertyChange(final PropertyChangeEvent evt) {
//		if ("progress" == evt.getPropertyName()) {
//			final int progress = (Integer) evt.getNewValue();
//			progressBar.setValue(progress);
//
//		}
//	}
//
//	public List<PolMaster> selectionHistoryCriteria() {
//		List<PolMaster> polMasters = null;
//
//		final LocalDate startDate = LocalDate.parse("2020-01-01");
//		final LocalDate endDate = LocalDate.parse("2020-01-20");
//
//		prepareDataFlow();
//		return polMasters = dbCommandExecutor.executeCommand(() -> {
//
//			return polMasterRepository.findPolMasterRecordByTranDate(DateUtils.convertToDateViaInstant(startDate),
//					DateUtils.convertToDateViaInstant(endDate));
//		});
//
//	}
//
////	public List<PolMaster> getPolicyMasterRecordsByFilter() {
////		List<PolMaster> polMasters = null;
////
////		if (polNoField.getText().length() > 0) {
////			prepareDataFlow();
////			polMasters = dbCommandExecutor.executeCommand(() -> {
////
////				return polMasterRepository.findPolMasterRecordByPolNo(polNoField.getText());
////			});
////
////		} else if (vehRegRefField.getText().length() > 0) {
////			prepareDataFlow();
////			polNo = dbCommandExecutor.executeCommand(() -> {
////				return polMtrVehRepository.findPolNoByVehReg(vehRegRefField.getText());
////			});
////			if (polNo != null) {
////				polMasters = dbCommandExecutor.executeCommand(() -> {
////
////					return polMasterRepository.findPolMasterRecordByPolNo(polNo);
////				});
////			}
////		} else if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
////
////			// get dates from date pickers
////			final LocalDate startDate = startDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault())
////					.toLocalDate();
////			final LocalDate endDate = endDatePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
////
////			// compare if start date greater than end date
////			if (startDate.isAfter(endDate)) {
////				JOptionPane.showMessageDialog(null, "Start date is after end date!", JOptionPane.MESSAGE_PROPERTY,
////						JOptionPane.WARNING_MESSAGE);
////			} else {
////				prepareDataFlow();
////				polMasters = dbCommandExecutor.executeCommand(() -> {
////
////					return polMasterRepository.findPolMasterRecordByTranDate(
////							DateUtils.convertToDateViaInstant(startDate), DateUtils.convertToDateViaInstant(endDate));
////				});
////			}
////		} else {
////
////			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
////			findBtn.setEnabled(true);
////			JOptionPane.showMessageDialog(null, "Please Select Criteria to search on!", JOptionPane.MESSAGE_PROPERTY,
////					JOptionPane.WARNING_MESSAGE);
////		}
////
////		return polMasters;
////
////	}
//
//	public List<PolMaster> getPolicyMasterRecordsByFilter() {
//		List<PolMaster> polMasters = null;
//
//		try {
//			prepareDataFlow();
//
//			if (!polNoField.getText().isEmpty()) {
//				polMasters = executePolNoSearch(polNoField.getText());
//			} else if (!vehRegRefField.getText().isEmpty()) {
//				polMasters = executeVehRegSearch(vehRegRefField.getText());
//			} else if (startDatePicker.getDate() != null && endDatePicker.getDate() != null) {
//				polMasters = executeDateRangeSearch();
//			} else {
//				showMessage("Please Select Criteria to search on!", JOptionPane.WARNING_MESSAGE);
//			}
//		} finally {
//			resetUIState();
//		}
//
//		return polMasters;
//	}
//
//	private List<PolMaster> executePolNoSearch(String polNo) {
//		return dbCommandExecutor.executeCommand(() -> polMasterRepository.findPolMasterRecordByPolNo(polNo));
//	}
//
//	private List<PolMaster> executeVehRegSearch(String vehRegRef) {
//		String polNo = dbCommandExecutor.executeCommand(() -> polMtrVehRepository.findPolNoByVehReg(vehRegRef));
//		if (polNo != null) {
//			return executePolNoSearch(polNo);
//		} else {
//			return null;
//		}
//	}
//
//	private List<PolMaster> executeDateRangeSearch() {
//		LocalDate startDate = convertToLocalDate(startDatePicker.getDate());
//		LocalDate endDate = convertToLocalDate(endDatePicker.getDate());
//
//		if (startDate.isAfter(endDate)) {
//			showMessage("Start date is after end date!", JOptionPane.WARNING_MESSAGE);
//			return null;
//		}
//
//		return dbCommandExecutor.executeCommand(() -> polMasterRepository.findPolMasterRecordByTranDate(
//				DateUtils.convertToDateViaInstant(startDate),
//				DateUtils.convertToDateViaInstant(endDate)));
//	}
//
//	private LocalDate convertToLocalDate(Date date) {
//		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//	}
//
//	private void showMessage(String message, int messageType) {
//		JOptionPane.showMessageDialog(null, message, JOptionPane.MESSAGE_PROPERTY, messageType);
//	}
//
//	private void resetUIState() {
//		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//		findBtn.setEnabled(true);
//	}
//
//	@SuppressWarnings("unchecked")
//	public void createHistoryJsonRecords() throws AuthenticationException, ClientProtocolException, IOException {
//
//		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//
//		final List<PolMaster> polMasters = selectionHistoryCriteria();
//
//		model.setRowCount(0);
//
//		if (polMasters != null) {
//
//			for (final PolMaster polMaster : polMasters) {
//				skipPolicy = dbCommandExecutor.executeCommand(() -> {
//					return polMasterRepository.isSkipPolicy(polMaster.getPolNo(), polMaster.getRenCnt());
//				});
//
//				if (!skipPolicy) {
//					List<PolMtrVeh> polMtrVehs;
//
//					if (vehRegRefField.getText().length() > 0) {
//
//						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
//							return polMtrVehRepository.findPolMtrVehicleByVehRegNo(polMaster.getPolNo(),
//									polMaster.getRenCnt(), polMaster.getEndtCnt(), vehRegRefField.getText());
//						});
//					} else {
//						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
//							return polMtrVehRepository.findPolMtrVehicleList(polMaster.getPolNo(),
//									polMaster.getRenCnt(), polMaster.getEndtCnt());
//						});
//					}
//
//					for (final PolMtrVeh mtrVeh : polMtrVehs) {
//
//						final List<PolRisk> polRisks = dbCommandExecutor.executeCommand(() -> {
//							return polRiskRepository.findPolRiskRecord(polMaster.getPolNo(), polMaster.getRenCnt(),
//									polMaster.getEndtCnt(), mtrVeh.getRiskGrp(), mtrVeh.getRiskNo());
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
//
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
//								MapCreator mapCreator = new MapCreator();
//								mainMap = mapCreator.createPolicyMap(polMaster, polFees, polRisk, mtrVeh, polItem,
//										itemBens,
//										pctIncBaseAP);
//
//								result = JsonWriter.writeJsonPolicyRecord(mainMap, xmm600InterMediary, polMaster,
//										itemBens, xmm600ClientList);
//
//								importJsonRecords("policy", result, polMaster.getPolNo());
//
//								System.out.println(result);
//
//							}
//						}
//					}
//				}
//			}
//
//		}
//
//		;
//
//		importBtn.setEnabled(false);
//
//		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//
//		mainMap.clear();
//		// printWriter.close();
//		em.close();
//		emf.close();
//		JOptionPane.showMessageDialog(null, " History Import Process Completed !", "History Data Import",
//				JOptionPane.INFORMATION_MESSAGE);
//
//	}
//
//	@SuppressWarnings("unchecked")
//	public void createJsonRecords() throws AuthenticationException, ClientProtocolException, IOException {
//
//		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//
//		FileWriter file = new FileWriter(myDocuments + "\\results.json");
//		BufferedWriter bw = null;
//		PrintWriter pw = null;
//
//		importBtn.setEnabled(false);
//
//		// createBranches();
//
//		final List<PolMaster> polMasters = getPolicyMasterRecordsByFilter();
//		List<PolItemBen> itemBens;
//
//		model.setRowCount(0);
//
//		if (polMasters != null) {
//
//			for (final PolMaster polMaster : polMasters) {
//				skipPolicy = dbCommandExecutor.executeCommand(() -> {
//					return polMasterRepository.isSkipPolicy(polMaster.getPolNo(), polMaster.getRenCnt());
//				});
//
//				if (!skipPolicy) {
//					List<PolMtrVeh> polMtrVehs;
//
//					if (vehRegRefField.getText().length() > 0) {
//
//						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
//							return polMtrVehRepository.findPolMtrVehicleByVehRegNo(polMaster.getPolNo(),
//									polMaster.getRenCnt(), polMaster.getEndtCnt(), vehRegRefField.getText());
//						});
//					} else {
//						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
//							return polMtrVehRepository.findPolMtrVehicleList(polMaster.getPolNo(),
//									polMaster.getRenCnt(), polMaster.getEndtCnt());
//						});
//					}
//
//					for (final PolMtrVeh mtrVeh : polMtrVehs) {
//
//						final List<PolRisk> polRisks = dbCommandExecutor.executeCommand(() -> {
//							return polRiskRepository.findPolRiskRecord(polMaster.getPolNo(), polMaster.getRenCnt(),
//									polMaster.getEndtCnt(), mtrVeh.getRiskGrp(), mtrVeh.getRiskNo());
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
//							// check if policy is a non-premium endorsement
//							if (polRisk.getPremDue().compareTo(BigDecimal.ZERO) == 0) {
//
//								maxEndtCnt = dbCommandExecutor.executeCommand(() -> {
//									return polRiskRepository.getMaxEndtCount(polRisk);
//								});
//
//								itemBens = dbCommandExecutor.executeCommand(() -> {
//
//									return polItemBenRepository.findPolItemBenRecord(polMaster.getPolNo(),
//											polMaster.getRenCnt(), maxEndtCnt, mtrVeh.getRiskGrp(),
//											mtrVeh.getRiskNo(),
//											mtrVeh.getItemNo());
//								});
//							} else {
//
//								itemBens = dbCommandExecutor.executeCommand(() -> {
//									return polItemBenRepository.findPolItemBenRecord(polMaster.getPolNo(),
//											polMaster.getRenCnt(), polMaster.getEndtCnt(), mtrVeh.getRiskGrp(),
//											mtrVeh.getRiskNo(),
//											mtrVeh.getItemNo());
//								});
//
//							}
//
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
//
//								if (Validations.recordIsValidated(polMaster, polRisk, mtrVeh, itemBens,
//										xmm600ClientList,
//										xmm600IntermediaryList, logging)) {
//
//									System.out.println(polMaster.getPolNo());
//									MapCreator mapCreator = new MapCreator();
//									mainMap = new LinkedHashMap();
//									mainMap = mapCreator.createPolicyMap(polMaster, polFees, polRisk, mtrVeh, polItem,
//											itemBens,
//											pctIncBaseAP);
//
//									result = JsonWriter.writeJsonPolicyRecord(mainMap, xmm600InterMediary, polMaster,
//											itemBens, xmm600ClientList);
//
//									response = importJsonRecords("policy", result, polMaster.getPolNo());
//
//									System.out.println(result);
//
//									bw = new BufferedWriter(file);
//									pw = new PrintWriter(bw);
//									pw.println(polMaster.getPolNo() + " " + mtrVeh.getCertRef() + " " + response);
//									pw.flush();
//
//								} else {
//
//								}
//							}
//						}
//					}
//				}
//			}
//
//		}
//
//		;
//
//		importBtn.setEnabled(false);
//
//		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//
//		mainMap.clear();
//		// printWriter.close();
//		// Desktop.getDesktop().open(new File(myDocuments + "\\results.json"));
//		if (pw != null) {
//			pw.close();
//		}
//		if (bw != null) {
//			bw.close();
//		}
//		file.close();
//		em.close();
//		emf.close();
//		JOptionPane.showMessageDialog(null, "Import Process Completed !", "Data Import",
//				JOptionPane.INFORMATION_MESSAGE);
//
//	}
//
//	@SuppressWarnings({ "unchecked", "resource" })
//	public void validateJsonRecords() throws AuthenticationException, ClientProtocolException, IOException {
//		JsonObject jsonObject = new JsonObject();
//		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//
//		FileWriter file = new FileWriter(myDocuments + "\\validation.json");
//		BufferedWriter bw = null;
//		PrintWriter pw = null;
//
//		final List<PolMaster> polMasters = getPolicyMasterRecordsByFilter();
//
//		model.setRowCount(0);
//
//		if (polMasters != null) {
//
//			for (final PolMaster polMaster : polMasters) {
//				skipPolicy = dbCommandExecutor.executeCommand(() -> {
//					return polMasterRepository.isSkipPolicy(polMaster.getPolNo(), polMaster.getRenCnt());
//				});
//
//				if (!skipPolicy) {
//					List<PolMtrVeh> polMtrVehs;
//
//					if (vehRegRefField.getText().length() > 0) {
//
//						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
//							return polMtrVehRepository.findPolMtrVehicleByVehRegNo(polMaster.getPolNo(),
//									polMaster.getRenCnt(), polMaster.getEndtCnt(), vehRegRefField.getText());
//						});
//					} else {
//						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
//							return polMtrVehRepository.findPolMtrVehicleList(polMaster.getPolNo(),
//									polMaster.getRenCnt(), polMaster.getEndtCnt());
//						});
//					}
//
//					for (final PolMtrVeh mtrVeh : polMtrVehs) {
//
//						final List<PolRisk> polRisks = dbCommandExecutor.executeCommand(() -> {
//							return polRiskRepository.findPolRiskRecord(polMaster.getPolNo(), polMaster.getRenCnt(),
//									polMaster.getEndtCnt(), mtrVeh.getRiskGrp(), mtrVeh.getRiskNo());
//						});
//
//						for (final PolRisk polRisk : polRisks) {
//
//							pctIncBaseAP = dbCommandExecutor.executeCommand(() -> {
//								return xmm106Repository.findPctIncBaseAPForClass(polRisk.getBusinessClass(),
//										mtrVeh.getCoverType());
//							});
//
//							final List<PolItemBen> itemBens = dbCommandExecutor.executeCommand(() -> {
//								return polItemBenRepository.findPolItemBenRecord(polMaster.getPolNo(),
//										polMaster.getRenCnt(), polMaster.getEndtCnt(), mtrVeh.getRiskGrp(),
//										mtrVeh.getRiskNo(), mtrVeh.getItemNo());
//							});
//
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
//							for (final Xmm600 xmm600 : xmm600ClientList) {
//
//								jsonObject = (JsonObject) validateRecord(polMaster, polRisk, mtrVeh, xmm600);
//
//								if (jsonObject.isJsonNull() || jsonObject == null
//										|| jsonObject.toString().equals("{}")) {
//
//								} else {
//									bw = new BufferedWriter(file);
//									pw = new PrintWriter(bw);
//									pw.println(jsonObject.toString());
//									pw.flush();
//								}
//							}
//
//						}
//					}
//				}
//			}
//
//			Desktop.getDesktop().open(new File(myDocuments + "\\validation.json"));
//
//			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//
//			em.close();
//			emf.close();
//
//			pw.close();
//			bw.close();
//			file.close();
//			JOptionPane.showMessageDialog(null, "Validation Process Completed !", "Data Validation",
//					JOptionPane.INFORMATION_MESSAGE);
//		} else {
//			JOptionPane.showMessageDialog(null, "No records To be Validated !", "Data Validation",
//					JOptionPane.INFORMATION_MESSAGE);
//		}
//
//	}
//
//	private JsonElement validateRecord(final PolMaster polMaster, final PolRisk polRisk, final PolMtrVeh mtrVeh,
//			final Xmm600 xmm600) {
//		JsonObject jsonObject = new JsonObject();
//		String result = "";
//		boolean isValid = true;
//		boolean foundScheduleCode = false;
//
//		Properties propSchedule = new Properties();
//
//		// InputStream inputStream;
//		final InputStream inputStream = MapCreator.class.getResourceAsStream(System.getProperty("PropFile"));
//		// inputStream = MapCreator.class.getClassLoader().getResourceAsStream("primeLiveConfig.properties");
//		propSchedule = new Properties();
//
//		try {
//			propSchedule.load(inputStream);
//		} catch (IOException e1) {
//
//			e1.printStackTrace();
//		}
//
//		if (polRisk.getComDate() == null) {
//			jsonObject.addProperty("riskCommenceDateIdentification", polMaster.getPolNo());
//			jsonObject.addProperty("riskCommenceDateDescription", "Risk Commence Date is Null");
//			isValid = false;
//		}
//
//		if (polRisk.getExpiryDate() == null) {
//			jsonObject.addProperty("riskExpiryDateIdentification", polMaster.getPolNo());
//			jsonObject.addProperty("riskExpiryDateDescription", "Risk Expiry Date is Null");
//			isValid = false;
//		}
//
//		if (xmm600.getName1().isEmpty()) {
//			isValid = false;
//		}
//
//		if (xmm600.getBirthday() == null) {
//
//			jsonObject.addProperty("clientBirthdayIdentification",
//					xmm600.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm600.getName1());
//			jsonObject.addProperty("clientBirthDayDescription", "Birthday is Null");
//			isValid = false;
//		}
//
//		if (xmm600.getBirthday() != null) {
//			if (!GenericValidator.isDate(xmm600.getBirthday().toString().substring(0, 10), "yyyy-MM-dd", true)) {
//
//				jsonObject.addProperty("invalidBirthdayIdentification",
//						xmm600.getClientNo() + " " + polMaster.getPolNo() + "  " + xmm600.getName1().toString() + " "
//								+ xmm600.getBirthday().toString().substring(0, 10));
//				jsonObject.addProperty("Invalid BirthdayDescription", "Invalid Birthday");
//				isValid = false;
//			}
//		}
//
//		if (xmm600.getTelno7() == null || xmm600.getTelno7().isEmpty() || xmm600.getTelno7().length() < 10) {
//
//			jsonObject.addProperty("invalidClientTelephoneIdentification", xmm600.getClientNo() + " "
//					+ polMaster.getPolNo() + "  " + xmm600.getName1().toString() + " " + xmm600.getTelno7().toString());
//			jsonObject.addProperty("invalidClientTelephoneDescription", "Invalid Client Telephone");
//			isValid = false;
//		}
//
//		if (mtrVeh.getColour() == null || mtrVeh.getColour().isEmpty()) {
//			jsonObject.addProperty("motorColorIdentification",
//					polMaster.getPolNo() + " " + xmm600.getName1().toString() + " " + mtrVeh.getVehRegNo());
//			jsonObject.addProperty("motorColoreDescription", "No Motor Colour");
//			isValid = false;
//		}
//
//		if (mtrVeh.getNoSeats() == null || mtrVeh.getNoSeats() == 0) {
//			jsonObject.addProperty("numberOfSeatsIdentification",
//					polMaster.getPolNo() + " " + xmm600.getName1().toString() + " " + mtrVeh.getVehRegNo());
//			jsonObject.addProperty("numberOfSeatDescription", "Number of Seats should be greater than Zero!!");
//			isValid = false;
//		}
//
//		@SuppressWarnings("unchecked")
//		Enumeration<String> enums = (Enumeration<String>) propSchedule.propertyNames();
//
//		while (enums.hasMoreElements() && (!foundScheduleCode)) {
//			String key = enums.nextElement();
//
//			if (mtrVeh.getCertRef().replace(" ", "").replace("  ", "").replace("   ", "").trim()
//					.equalsIgnoreCase(key)) {
//				foundScheduleCode = true;
//
//			}
//
//		}
//
//		if (!foundScheduleCode) {
//			jsonObject.addProperty("certRefNotMappedIdentification",
//					polMaster.getPolNo() + " " + xmm600.getName1().toString() + " " + mtrVeh.getVehRegNo() + " "
//							+ mtrVeh.getCertRef());
//			jsonObject.addProperty("certRefNotMappedDescription", "Cert Ref Not Mapped!!");
//			isValid = false;
//
//		}
//
//		return jsonObject;
//	}
//
//	private void writeJsonRecords(final String polNo, final Integer renCnt, final Integer endtCnt) {
//
//		JsonObject jsonObject = new JsonObject();
//
//		jsonObject.addProperty("polNo", polNo);
//		jsonObject.addProperty("renCnt", renCnt);
//		jsonObject.addProperty("endtCnt", endtCnt);
//		jsonObject.addProperty("trandate", endtCnt);
//
//		String result = new Gson().toJson(jsonObject);
//
//		// System.out.println(result);
//
//	}
//
//	/*
//	 * private boolean recordIsValidated(final PolMaster polMaster, final PolRisk polRisk, final PolMtrVeh mtrVeh, final
//	 * List<PolItemBen> itemBens, final List<Xmm600> xmm600ClientList, final List<Xmm600> xmm600IntermediaryList) {
//	 * 
//	 * // return true;
//	 * 
//	 * JsonObject jsonObject = new JsonObject(); String result = ""; boolean isValid = true; InputStream inputStream;
//	 * Properties propSchedule = new Properties(); boolean foundScheduleCode = false;
//	 * 
//	 * inputStream = MapCreator.class.getClassLoader().getResourceAsStream("primeLiveConfig.properties"); propSchedule =
//	 * new Properties();
//	 * 
//	 * try { propSchedule.load(inputStream); } catch (IOException e1) {
//	 * 
//	 * e1.printStackTrace(); }
//	 * 
//	 * @SuppressWarnings("unchecked") Enumeration<String> enums = (Enumeration<String>) propSchedule.propertyNames();
//	 * 
//	 * while (enums.hasMoreElements() && (!foundScheduleCode)) { String key = enums.nextElement();
//	 * 
//	 * 
//	 * if (mtrVeh.getCertRef().replace(" ", "").replace("  ", "").replace("   ", "").trim() .equalsIgnoreCase(key)) {
//	 * foundScheduleCode = true;
//	 * 
//	 * }
//	 * 
//	 * }
//	 * 
//	 * if(!foundScheduleCode) { logging.setMessage(" Certificate reference Not mapped For :" + mtrVeh.getPolNo() +
//	 * " OV2 CERT REF : " + mtrVeh.getCertRef()); isValid = false; }
//	 * 
//	 * if (itemBens.isEmpty()) { jsonObject.addProperty("NoItemBenefitsIdentification", polMaster.getPolNo() + " " +
//	 * mtrVeh.getVehRegNo()); jsonObject.addProperty("NoItemBenefitsDescription", "Record Has No Item Benefits");
//	 * logging.setMessage(polMaster.getPolNo() + " " + mtrVeh.getVehRegNo() + " Record Has No Item Benefits "); isValid
//	 * = false; }
//	 * 
//	 * for (final Xmm600 xmm6002 : xmm600ClientList) {
//	 * 
//	 * if (polRisk.getComDate() == null) { jsonObject.addProperty("riskCommenceDateIdentification", polMaster.getPolNo()
//	 * + " " + mtrVeh.getVehRegNo()); jsonObject.addProperty("riskCommenceDateDescription",
//	 * "Risk Commence Date is Null"); logging.setMessage(polMaster.getPolNo() + " " + mtrVeh.getVehRegNo() +
//	 * " Risk Commence Date is Null "); isValid = false; }
//	 * 
//	 * if (polRisk.getExpiryDate() == null) { jsonObject.addProperty("riskExpiryDateIdentification",
//	 * polMaster.getPolNo() + " " + mtrVeh.getVehRegNo()); jsonObject.addProperty("riskExpiryDateDescription",
//	 * "Risk Expiry Date is Null"); logging.setMessage(polMaster.getPolNo() + " " + mtrVeh.getVehRegNo() +
//	 * " Risk Expiry Date is Null "); isValid = false; }
//	 * 
//	 * if (xmm6002.getName1().isEmpty()) { logging.setMessage( xmm6002.getName1().toString() + " " +
//	 * xmm6002.getTelno7().toString() + " Client name is Null "); return false;
//	 * 
//	 * }
//	 * 
//	 * if (xmm6002.getBirthday() == null) {
//	 * 
//	 * jsonObject.addProperty("clientBirthdayIdentification", xmm6002.getClientNo() + " " + polMaster.getPolNo() + "  "
//	 * + xmm6002.getName1()); jsonObject.addProperty("clientBirthDayDescription", "Birthday is Null");
//	 * logging.setMessage( xmm6002.getName1().toString() + "  " + xmm6002.getTelno7().toString() +
//	 * " Birthday is Null "); isValid = false; }
//	 * 
//	 * if (xmm6002.getBirthday() != null) { if (!GenericValidator.isDate(xmm6002.getBirthday().toString().substring(0,
//	 * 10), "yyyy-MM-dd", true)) {
//	 * 
//	 * jsonObject.addProperty("invalidBirthdayIdentification", xmm6002.getClientNo() + " " + polMaster.getPolNo() + "  "
//	 * + xmm6002.getName1().toString() + " " + xmm6002.getBirthday().toString().substring(0, 10));
//	 * jsonObject.addProperty("Invalid BirthdayDescription", "Invalid Birthday"); logging.setMessage(
//	 * xmm6002.getName1().toString() + "  " + xmm6002.getTelno7().toString() + " Invalid Birthday "); isValid = false; }
//	 * }
//	 * 
//	 * if (xmm6002.getTelno7() == null || xmm6002.getTelno7().isEmpty() || xmm6002.getTelno7().length() < 10) {
//	 * 
//	 * jsonObject.addProperty("invalidClientTelephoneIdentification", xmm6002.getClientNo() + " " + polMaster.getPolNo()
//	 * + "  " + xmm6002.getName1().toString() + " " + xmm6002.getTelno7().toString());
//	 * jsonObject.addProperty("invalidClientTelephoneDescription", "Invalid Client Telephone");
//	 * logging.setMessage(xmm6002.getClientNo().toString() + "  " + xmm6002.getName1().toString() + "  " +
//	 * xmm6002.getTelno7().toString() + " Invalid Client Telephone "); isValid = false; }
//	 * 
//	 * if (mtrVeh.getColour() == null || mtrVeh.getColour().isEmpty()) {
//	 * jsonObject.addProperty("motorColorIdentification", polMaster.getPolNo() + " " + xmm6002.getName1().toString() +
//	 * " " + mtrVeh.getVehRegNo()); jsonObject.addProperty("motorColoreDescription", "No Motor Colour");
//	 * logging.setMessage(polMaster.getPolNo() + "  " + mtrVeh.getVehRegNo() + " No Motor Colour "); isValid = false; }
//	 * 
//	 * if (mtrVeh.getNoSeats() == null || mtrVeh.getNoSeats() == 0) {
//	 * jsonObject.addProperty("numberOfSeatsIdentification", polMaster.getPolNo() + " " + xmm6002.getName1().toString()
//	 * + " " + mtrVeh.getVehRegNo()); jsonObject.addProperty("numberOfSeatDescription",
//	 * "Number of Seats should be greater than Zero!!"); logging.setMessage(polMaster.getPolNo() + "  " +
//	 * mtrVeh.getVehRegNo() + " Number of Seats should be greater than Zero!! "); isValid = false; } }
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
//	private void createBranches() {
//
//		List<Xmm023> xmm023s = null;
//		prepareDataFlow();
//		xmm023s = dbCommandExecutor.executeCommand(() -> {
//
//			return xmm023Repository.findXmm023();
//
//		});
//
//		for (final Xmm023 xmm023 : xmm023s) {
//
//			if (!branchExists) {
//				result = JsonWriter.writeJsonBranchRecord(xmm023);
//				try {
//					importJsonRecords("branch", result, "");
//				} catch (AuthenticationException | IOException e) {
//
//					e.printStackTrace();
//				}
//				branchExists = true;
//			}
//		}
//
//	}
//
//	protected String importJsonRecords(final String url, final String json, final String polNo)
//			throws ClientProtocolException, IOException, AuthenticationException {
//
//		final CloseableHttpResponse response = HttpAuthentication.getPostPolicyToPortalResponse("policy", json);
//
//		final HttpEntity body = response.getEntity();
//
//		final StatusLine statusLine = response.getStatusLine();
//		System.out.println(response.getStatusLine());
//		final String content = EntityUtils.toString(body);
//
//		if (statusLine.getStatusCode() == HttpCode.OK.getCode()) {
//
//			if (url.equals("policy")) {
//				// updatePolMtrVeh(content);
//
//			}
//			System.out.println(polNo.toString() + " " + HttpCode.OK.getCode());
//
//		} else {
//			logging.setMessage(polNo.toString() + " " + content.toString());
//			System.out.println(polNo.toString() + " " + content.toString());
//		}
//
//		return content;
//
//	}
//
//	protected void getMIDSchedules() throws AuthenticationException {
//
//		final String branches = HttpAuthentication.getMIDBranches();
//
//	}
//
//	protected JsonArray getMIDBranches() throws AuthenticationException {
//
//		final String branches = HttpAuthentication.getMIDBranches();
//
//		return JsonReader.readAsJsonArray(branches);
//
//	}
//
//	private void updatePolMtrVeh(final String content) {
//
//		final String polNo;
//
//		final Integer renCnt;
//
//		final Integer endtCnt;
//
//		final String vehRegNo;
//
//		final String referenceNumber;
//
//		String stickerNumber;
//
//		final String refExp = "$.motor_policy.reference";
//		final String stickerExp = "$.motor_policy.sticker_number";
//		final String vehRegNumberExp = "$.motor_policy.vehicle_registration";
//		referenceNumber = JsonPath.parse(content).read(refExp);
//		stickerNumber = JsonPath.parse(content).read(stickerExp);
//		if (!(stickerNumber == null)) {
//			final Matcher matcher = Pattern.compile("\\d+").matcher(stickerNumber);
//			matcher.find();
//			final Long i = Long.valueOf(matcher.group());
//			stickerNumber = i.toString();
//			polNo = referenceNumber.substring(0, 16);
//
//			renCnt = Integer.parseInt(referenceNumber.substring(16, 17));
//
//			endtCnt = Integer.parseInt(referenceNumber.substring(17, 18));
//
//			vehRegNo = JsonPath.parse(content).read(vehRegNumberExp);
//			;
//			getReadyForDataUpdate();
//			final PolMtrVeh polMtrVeh = dbCommandExecutor.executeCommand(() -> {
//				return polMtrVehRepository.findPolMtrVehicleRecordByCompositeKey(polNo, renCnt, endtCnt, vehRegNo);
//			});
//
//			if (polMtrVeh != null) {
//				polMtrVeh.setOwnName(stickerNumber);
//
//				dbCommandExecutor.executeCommand(() -> {
//
//					polMtrVehRepository.update(polMtrVeh);
//					return null;
//				});
//			}
//		}
//	}
//
//	public synchronized void prepareDataFlow() {
//
//		emf = Persistence.createEntityManagerFactory("midPU");
//		em = emf.createEntityManager();
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
//		xmm023Repository = new Xmm023Repository();
//		xmm023Repository.em = em;
//		xmm023Repository.setDbCommandExecutor(dbCommandExecutor);
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
//	public void findRecord() {
//
//		final List<PolMaster> polMasters = getPolicyMasterRecordsByFilter();
//		model.setRowCount(0);
//
//		if (polMasters != null) {
//
//			for (final PolMaster polMaster : polMasters) {
//
//				skipPolicy = dbCommandExecutor.executeCommand(() -> {
//					return polMasterRepository.isSkipPolicy(polMaster.getPolNo(), polMaster.getRenCnt());
//				});
//
//				if (!skipPolicy) {
//					List<PolMtrVeh> polMtrVehs;
//
//					if (vehRegRefField.getText().length() > 0) {
//
//						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
//							return polMtrVehRepository.findPolMtrVehicleByVehRegNo(polMaster.getPolNo(),
//									polMaster.getRenCnt(), polMaster.getEndtCnt(), vehRegRefField.getText());
//						});
//					} else {
//						polMtrVehs = dbCommandExecutor.executeCommand(() -> {
//							return polMtrVehRepository.findPolMtrVehicleList(polMaster.getPolNo(),
//									polMaster.getRenCnt(), polMaster.getEndtCnt());
//						});
//					}
//
//					for (final PolMtrVeh mtrVeh : polMtrVehs) {
//
//						final List<PolRisk> polRisks = dbCommandExecutor.executeCommand(() -> {
//							return polRiskRepository.findPolRiskRecord(polMaster.getPolNo(), polMaster.getRenCnt(),
//									polMaster.getEndtCnt(), mtrVeh.getRiskGrp(), mtrVeh.getRiskNo());
//						});
//
//						for (final PolRisk polRisk : polRisks) {
//
//							final Object[] colRowVect = new Object[] { polMaster.getTranDate().toString(),
//									polMaster.getPolNo(), mtrVeh.getCertRef(), polRisk.getComDate(),
//									polRisk.getExpiryDate(),
//									polMaster.getInsdName1() + polMaster.getInsdName2() + polMaster.getInsdName3(),
//									mtrVeh.getCoverType(), mtrVeh.getVehRegNo(), mtrVeh.getVehMake(),
//									mtrVeh.getModelDesc() };
//							model.addRow(colRowVect);
//
//						}
//
//					}
//
//					// for (final PolMtrVeh mtrVeh : polMtrVehs) {
//
//					// final Object[] colRowVect = new Object[] {
//					// polMaster.getTranDate().toString(),
//					// polMaster.getPolNo(), mtrVeh.getOwnName(), polMaster.getComDate(),
//					// polMaster.getExpiryDate(),
//					// polMaster.getInsdName1() + polMaster.getInsdName2() +
//					// polMaster.getInsdName3(),
//					// mtrVeh.getCoverType(), mtrVeh.getVehRegNo(), mtrVeh.getVehMake(),
//					// mtrVeh.getModelDesc() };
//					// model.addRow(colRowVect);
//					// }
//				}
//			}
//			em.close();
//			emf.close();
//			importBtn.setEnabled(true);
//
//			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//
//		}
//
//	}
//
//	static class SelectAll extends TextAction {
//		public SelectAll() {
//			super("Select All");
//			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
//		}
//
//		@Override
//		public void actionPerformed(final ActionEvent e) {
//			JTextComponent component = getFocusedComponent();
//			component.selectAll();
//			component.requestFocusInWindow();
//		}
//	}
//
//}
