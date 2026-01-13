package com.mid.app.swing.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mid.app.polmaster.model.PolMaster;
import com.mid.app.polmtrveh.model.PolMtrVeh;
import com.mid.app.polrisk.model.PolRisk;
import com.mid.app.swing.model.ImportCriteria;
import com.mid.app.swing.model.ImportResult;
import com.mid.app.swing.service.MotorPolicyJsonBuilder;
import com.mid.app.swing.service.PolicyDataService;
import com.mid.app.swing.service.PolicyImportService;
import com.mid.app.swing.service.PolicyValidationService;
import com.mid.app.utils.LoggingEngine;
import com.mid.app.xmm600.model.Xmm600;
import com.toedter.calendar.JDateChooser;

public class Main_Screen extends JPanel implements ActionListener, PropertyChangeListener {

	// UI Components
	private JTable table;
	private DefaultTableModel model;
	private JButton importBtn, findBtn, validateBtn;
	private JTextField polNoField, vehRegRefField;
	private JDateChooser startDatePicker, endDatePicker;
	private JProgressBar progressBar;
	private JLabel statusLabel;

	// Modern UI colors - softer, more professional palette
	private static final Color PRIMARY_COLOR = new Color(0, 112, 192); // Professional blue
	private static final Color SECONDARY_COLOR = new Color(0, 153, 204); // Light blue
	private static final Color ACCENT_COLOR = new Color(76, 175, 80); // Green
	private static final Color BACKGROUND_COLOR = new Color(248, 248, 248); // Very light gray
	private static final Color PANEL_BACKGROUND = Color.WHITE;
	private static final Color TABLE_HEADER_COLOR = new Color(51, 51, 51); // Dark gray
	private static final Color TABLE_SELECTION_COLOR = new Color(229, 243, 255); // Light blue selection
	private static final Color TABLE_GRID_COLOR = new Color(230, 230, 230);
	private static final Color BORDER_COLOR = new Color(204, 204, 204);

	// Modern fonts
	private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
	private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
	private static final Font BUTTON_FONT = new Font("Segoe UI Semibold", Font.PLAIN, 12);
	private static final Font TEXT_FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 12);
	private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 11);
	private static final Font TABLE_HEADER_FONT = new Font("Segoe UI Semibold", Font.BOLD, 12);
	private static final Font TITLE_FONT = new Font("Segoe UI Light", Font.BOLD, 28);

	// Services
	private final PolicyImportService importService;
	private final PolicyValidationService validationService;
	private final MotorPolicyJsonBuilder jsonBuilder;
	private final PolicyDataService dataService;
	private final LoggingEngine logging;

	// Constants
	private static final String[] COLUMN_NAMES = { "TRAN DATE", "POLICY NUMBER", "STICKER NUMBER", "INCEPTION",
			"EXPIRY", "CUSTOMER NAME", "COVER TYPE", "VEHICLE REG", "MAKE", "MODEL" };
	
	

	public Main_Screen() {
		// Initialize services
		this.logging = LoggingEngine.getInstance();
		this.dataService = new PolicyDataService();
		this.validationService = new PolicyValidationService();
		this.jsonBuilder = new MotorPolicyJsonBuilder();
		this.importService = new PolicyImportService(jsonBuilder, validationService, dataService);

		// Set modern look and feel
		setModernLookAndFeel();

		initializeUI();
	}

	private void setModernLookAndFeel() {
		try {
			// Use system look and feel for native appearance
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

			// Custom UI defaults
			UIManager.put("Button.background", PRIMARY_COLOR);
			UIManager.put("Button.foreground", Color.WHITE);
			UIManager.put("Button.font", BUTTON_FONT);
			UIManager.put("Button.border", BorderFactory.createEmptyBorder(8, 16, 8, 16));

			UIManager.put("TextField.font", TEXT_FIELD_FONT);
			UIManager.put("TextField.background", Color.WHITE);
			UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(BORDER_COLOR, 1), BorderFactory.createEmptyBorder(6, 8, 6, 8)));

			UIManager.put("Label.font", LABEL_FONT);
			UIManager.put("Table.font", TABLE_FONT);
			UIManager.put("TableHeader.font", TABLE_HEADER_FONT);
			UIManager.put("TableHeader.background", TABLE_HEADER_COLOR);
			UIManager.put("TableHeader.foreground", Color.WHITE);
			UIManager.put("Table.selectionBackground", TABLE_SELECTION_COLOR);
			UIManager.put("Table.selectionForeground", Color.BLACK);
			UIManager.put("Table.gridColor", TABLE_GRID_COLOR);

			UIManager.put("ProgressBar.foreground", ACCENT_COLOR);

		} catch (Exception e) {
			System.err.println("Could not set modern look and feel: " + e.getMessage());
		}
	}

	private void initializeUI() {
		setLayout(new BorderLayout(0, 0));
		setBackground(BACKGROUND_COLOR);

		// Create header panel
		JPanel headerPanel = createHeaderPanel();
		add(headerPanel, BorderLayout.NORTH);

		// Create main content panel
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(BACKGROUND_COLOR);
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

		// Create control panel
		JPanel controlPanel = createControlPanel();
		contentPanel.add(controlPanel, BorderLayout.NORTH);

		// Create table panel
		JPanel tablePanel = createTablePanel();
		contentPanel.add(tablePanel, BorderLayout.CENTER);

		// Create status panel
		JPanel statusPanel = createStatusPanel();
		contentPanel.add(statusPanel, BorderLayout.SOUTH);

		add(contentPanel, BorderLayout.CENTER);
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(PRIMARY_COLOR);
		headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
		headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

		// Title
		JLabel titleLabel = new JLabel("Policy Management System");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(Color.WHITE);

		// Subtitle
		JLabel subtitleLabel = new JLabel("Import • Validate • Manage");
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subtitleLabel.setForeground(new Color(240, 240, 240));

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setOpaque(false);
		titlePanel.add(titleLabel);
		titlePanel.add(Box.createVerticalStrut(5));
		titlePanel.add(subtitleLabel);

		headerPanel.add(titlePanel, BorderLayout.WEST);

		// Add a subtle separator on the right
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setForeground(new Color(255, 255, 255, 100));
		separator.setPreferredSize(new Dimension(1, 40));

		JPanel separatorPanel = new JPanel();
		separatorPanel.setOpaque(false);
		separatorPanel.add(separator);

		headerPanel.add(separatorPanel, BorderLayout.EAST);

		return headerPanel;
	}

	private JPanel createControlPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(PANEL_BACKGROUND);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1),
				new EmptyBorder(20, 20, 20, 20)));

		// Section title with icon
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		titlePanel.setBackground(PANEL_BACKGROUND);

		JLabel sectionTitle = new JLabel("Search Criteria");
		sectionTitle.setFont(HEADER_FONT);
		sectionTitle.setForeground(TABLE_HEADER_COLOR);
		sectionTitle.setIcon(createIcon("🔍", 16)); // Search icon
		sectionTitle.setIconTextGap(8);

		titlePanel.add(sectionTitle);
		panel.add(titlePanel);
		panel.add(Box.createVerticalStrut(15));

		// Create form panel
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(PANEL_BACKGROUND);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(8, 8, 8, 8);

		// Date controls row
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(createFormLabel("Start Date:"), gbc);

		gbc.gridx = 1;
		startDatePicker = createStyledDateChooser();
		formPanel.add(startDatePicker, gbc);

		gbc.gridx = 2;
		formPanel.add(createFormLabel("End Date:"), gbc);

		gbc.gridx = 3;
		endDatePicker = createStyledDateChooser();
		formPanel.add(endDatePicker, gbc);

		// Search fields row
		gbc.gridx = 0;
		gbc.gridy = 1;
		formPanel.add(createFormLabel("Policy Number:"), gbc);

		gbc.gridx = 1;
		polNoField = createStyledTextField();
		formPanel.add(polNoField, gbc);

		gbc.gridx = 2;
		formPanel.add(createFormLabel("Vehicle Reg:"), gbc);

		gbc.gridx = 3;
		vehRegRefField = createStyledTextField();
		formPanel.add(vehRegRefField, gbc);

		panel.add(formPanel);
		panel.add(Box.createVerticalStrut(20));

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		buttonPanel.setBackground(PANEL_BACKGROUND);

		findBtn = createStyledButton("Search", PRIMARY_COLOR, "🔍");
		findBtn.addActionListener(this);
		buttonPanel.add(findBtn);

		importBtn = createStyledButton("Import", ACCENT_COLOR, "📤");
		importBtn.setEnabled(false);
		importBtn.addActionListener(e -> startImport());
		buttonPanel.add(importBtn);

		// Add a button to your control panel to open scheduler control
		JButton schedulerBtn = new JButton("Scheduler");
		schedulerBtn.addActionListener(e -> showSchedulerControl());
		buttonPanel.add(schedulerBtn);

		validateBtn = createStyledButton("Validate", SECONDARY_COLOR, "✓");
		validateBtn.addActionListener(e -> startValidation());
		buttonPanel.add(validateBtn);

		panel.add(buttonPanel);

		return panel;
	}

	private JLabel createFormLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(LABEL_FONT);
		label.setForeground(new Color(60, 60, 60));
		return label;
	}

	private Icon createIcon(String emoji, int size) {
		return new Icon() {
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, size));
				FontMetrics fm = g2.getFontMetrics();
				g2.drawString(emoji, x, y + fm.getAscent());
				g2.dispose();
			}

			@Override
			public int getIconWidth() {
				return size;
			}

			@Override
			public int getIconHeight() {
				return size;
			}
		};
	}

	private JDateChooser createStyledDateChooser() {
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setPreferredSize(new Dimension(150, 35));

		// Try to style the calendar button
		try {
			// Get the calendar button using reflection since it's protected
			java.lang.reflect.Field field = dateChooser.getClass().getDeclaredField("calendarButton");
			field.setAccessible(true);
			JButton calendarButton = (JButton) field.get(dateChooser);

			if (calendarButton != null) {
				calendarButton.setBackground(PRIMARY_COLOR);
				calendarButton.setForeground(Color.WHITE);
				calendarButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
				calendarButton.setFocusPainted(false);
				calendarButton.setText("📅"); // Calendar emoji
				calendarButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
			}
		} catch (Exception e) {
			// If reflection fails, just use default styling
			System.err.println("Could not style calendar button: " + e.getMessage());
		}

		return dateChooser;
	}

	private JTextField createStyledTextField() {
		JTextField textField = new JTextField(15);
		textField.setFont(TEXT_FIELD_FONT);
		textField.setPreferredSize(new Dimension(150, 35));

		// Modern border with rounded corners effect
		textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1),
				new EmptyBorder(8, 10, 8, 10)));

		// Add focus listener for better UX
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
						new EmptyBorder(7, 9, 7, 9)));
			}

			@Override
			public void focusLost(FocusEvent e) {
				textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1),
						new EmptyBorder(8, 10, 8, 10)));
			}
		});

		// Add context menu
		JPopupMenu contextMenu = createContextMenu();
		textField.setComponentPopupMenu(contextMenu);

		return textField;
	}

	private JButton createStyledButton(String text, Color color, String iconEmoji) {
		JButton button = new JButton(text) {
			// Keep the background painting but ensure text is visible
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Paint background
				Color bgColor = getBackground();
				if (!isEnabled()) {
					bgColor = bgColor.darker();
				} else if (getModel().isPressed()) {
					bgColor = bgColor.darker();
				} else if (getModel().isRollover()) {
					bgColor = bgColor.brighter();
				}

				g2.setColor(bgColor);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
				g2.dispose();

				// Let super paint the text and icon
				super.paintComponent(g);
			}

			@Override
			public void setBackground(Color bg) {
				super.setBackground(bg);
			}
		};

		button.setFont(BUTTON_FONT);
		button.setForeground(Color.WHITE);
		button.setBackground(color);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color.darker(), 1),
				new EmptyBorder(10, 20, 10, 20)));
		button.setFocusPainted(false);
		button.setContentAreaFilled(false); // Important: Let our custom paintComponent handle filling
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Add icon if provided
		if (iconEmoji != null && !iconEmoji.isEmpty()) {
			button.setIcon(createIcon(iconEmoji, 14));
			button.setHorizontalTextPosition(SwingConstants.RIGHT);
			button.setIconTextGap(8);
		}

		// Hover effect
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(color.brighter());
				button.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(color);
				button.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				button.setBackground(color.darker());
				button.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				button.setBackground(color.brighter());
				button.repaint();
			}
		});

		return button;
	}

	private JPanel createTablePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(PANEL_BACKGROUND);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1),
				new EmptyBorder(0, 0, 0, 0)));

		// Table header
		JPanel tableHeader = new JPanel(new BorderLayout());
		tableHeader.setBackground(PANEL_BACKGROUND);
		tableHeader.setBorder(new EmptyBorder(15, 15, 10, 15));

		JLabel tableTitle = new JLabel("Policy Records");
		tableTitle.setFont(HEADER_FONT);
		tableTitle.setForeground(TABLE_HEADER_COLOR);
		tableTitle.setIcon(createIcon("📋", 16)); // Clipboard icon
		tableTitle.setIconTextGap(8);

		JLabel recordCount = new JLabel("0 records");
		recordCount.setFont(LABEL_FONT);
		recordCount.setForeground(new Color(100, 100, 100));

		tableHeader.add(tableTitle, BorderLayout.WEST);
		tableHeader.add(recordCount, BorderLayout.EAST);
		panel.add(tableHeader, BorderLayout.NORTH);

		// Create table model
		model = new DefaultTableModel(COLUMN_NAMES, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Create table with custom renderer
		table = new JTable(model) {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				// Alternate row colors
				if (!isRowSelected(row)) {
					c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 250));
				}

				// Highlight empty cells
				Object value = getValueAt(row, column);
				if (value == null || value.toString().trim().isEmpty()) {
					c.setBackground(new Color(255, 243, 205)); // Light yellow
				}

				// Set font
				c.setFont(TABLE_FONT);

				return c;
			}
		};

		// Configure table appearance
		table.setRowHeight(32);
		table.setShowGrid(true);
		table.setGridColor(TABLE_GRID_COLOR);
		table.setSelectionBackground(TABLE_SELECTION_COLOR);
		table.setSelectionForeground(Color.BLACK);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setFillsViewportHeight(true);

		// Configure table header
		JTableHeader header = table.getTableHeader();
		header.setFont(TABLE_HEADER_FONT);
		header.setBackground(TABLE_HEADER_COLOR);
		header.setForeground(Color.WHITE);
		header.setReorderingAllowed(false);

		// Center align header text
		DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(SwingConstants.CENTER);
				setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE),
						new EmptyBorder(10, 5, 10, 5)));
				return this;
			}
		};

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
		}

		// Center align all columns
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		// Create scroll pane
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
		scrollPane.getViewport().setBackground(Color.WHITE);

		// Add scroll pane to panel
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createStatusPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(PANEL_BACKGROUND);
		panel.setBorder(new EmptyBorder(10, 0, 0, 0));

		// Separator
		JSeparator separator = new JSeparator();
		separator.setForeground(BORDER_COLOR);
		panel.add(separator, BorderLayout.NORTH);

		// Status components
		JPanel statusContainer = new JPanel(new BorderLayout());
		statusContainer.setBackground(PANEL_BACKGROUND);
		statusContainer.setBorder(new EmptyBorder(10, 0, 10, 0));

		// Progress bar
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setFont(LABEL_FONT);
		progressBar.setForeground(ACCENT_COLOR);
		progressBar.setBackground(new Color(240, 240, 240));
		progressBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1),
				new EmptyBorder(2, 2, 2, 2)));
		progressBar.setVisible(false);

		// Status label
		statusLabel = new JLabel("Ready");
		statusLabel.setFont(LABEL_FONT);
		statusLabel.setForeground(new Color(100, 100, 100));
		statusLabel.setBorder(new EmptyBorder(0, 10, 0, 10));

		statusContainer.add(progressBar, BorderLayout.CENTER);
		statusContainer.add(statusLabel, BorderLayout.EAST);

		panel.add(statusContainer, BorderLayout.CENTER);

		return panel;
	}

	private JPopupMenu createContextMenu() {
		JPopupMenu menu = new JPopupMenu();
		menu.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

		// Cut action
		Action cut = new DefaultEditorKit.CutAction();
		cut.putValue(Action.NAME, "Cut");
		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		cut.putValue(Action.SMALL_ICON, createIcon("✂", 14));
		menu.add(cut);

		// Copy action
		Action copy = new DefaultEditorKit.CopyAction();
		copy.putValue(Action.NAME, "Copy");
		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		copy.putValue(Action.SMALL_ICON, createIcon("📋", 14));
		menu.add(copy);

		// Paste action
		Action paste = new DefaultEditorKit.PasteAction();
		paste.putValue(Action.NAME, "Paste");
		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		paste.putValue(Action.SMALL_ICON, createIcon("📝", 14));
		menu.add(paste);

		menu.addSeparator();

		// Select All action
		Action selectAll = new SelectAllAction();
		selectAll.putValue(Action.SMALL_ICON, createIcon("📄", 14));
		menu.add(selectAll);

		return menu;
	}

	private static class SelectAllAction extends TextAction {
		public SelectAllAction() {
			super("Select All");
			putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent component = getFocusedComponent();
			if (component != null) {
				component.selectAll();
				component.requestFocusInWindow();
			}
		}
	}

	// ========== Business Logic Methods (Unchanged from previous version)
	// ==========

	private void startImport() {
		ImportWorker worker = new ImportWorker();
		worker.execute();
	}

	private void startValidation() {
		ValidationWorker worker = new ValidationWorker();
		worker.execute();
	}

	private ImportCriteria createImportCriteria() {
		ImportCriteria criteria = new ImportCriteria();

		criteria.setPolicyNumber(polNoField.getText().trim());
		criteria.setVehicleRegNo(vehRegRefField.getText().trim());
		criteria.setStartDate(startDatePicker.getDate());
		criteria.setEndDate(endDatePicker.getDate());

		return criteria;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == findBtn) {
			SearchWorker worker = new SearchWorker();
			worker.execute();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			progressBar.setString(String.format("Processing... %d%%", progress));
		} else if ("state".equals(evt.getPropertyName())) {
			if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
				progressBar.setVisible(false);
				setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	// In your Main_Screen.java, add a method to create a scheduler control window
	private void showSchedulerControl() {
		JFrame schedulerFrame = new JFrame("Scheduler Control");
		schedulerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		schedulerFrame.setSize(400, 300);
		schedulerFrame.setLocationRelativeTo(this);

		SchedulerPanel schedulerPanel = new SchedulerPanel();
		schedulerFrame.add(schedulerPanel);
		schedulerFrame.setVisible(true);
	}

	// ========== Worker Classes ==========

	private class SearchWorker extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() throws Exception {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			SwingUtilities.invokeLater(() -> {
				statusLabel.setText("Searching...");
				model.setRowCount(0);
				importBtn.setEnabled(false);
			});

			ImportCriteria criteria = createImportCriteria();

			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				emf = Persistence.createEntityManagerFactory("midPU");
				em = emf.createEntityManager();

				List<PolMaster> polMasters = dataService.findPolMasters(em, criteria);

				if (polMasters != null && !polMasters.isEmpty()) {
					for (PolMaster polMaster : polMasters) {
						boolean skipPolicy = dataService.shouldSkipPolicy(em, polMaster.getPolNo(),
								polMaster.getRenCnt());

						if (!skipPolicy) {
							List<PolMtrVeh> vehicles = dataService.findVehicles(em, polMaster,
									criteria.getVehicleRegNo());

							for (PolMtrVeh vehicle : vehicles) {
								List<PolRisk> risks = dataService.findRisks(em, polMaster, vehicle);

								for (PolRisk risk : risks) {
									Object[] row = new Object[] { polMaster.getTranDate(), polMaster.getPolNo(),
											vehicle.getOwnName(), risk.getComDate(), risk.getExpiryDate(),
											polMaster.getInsdName1() + " " + polMaster.getInsdName2(),
											vehicle.getCoverType(), vehicle.getVehRegNo(), vehicle.getVehMake(),
											vehicle.getModelDesc() };

									SwingUtilities.invokeLater(() -> model.addRow(row));
								}
							}
						}
					}

					final int count = polMasters.size();
					SwingUtilities.invokeLater(() -> {
						importBtn.setEnabled(true);
						statusLabel.setText(String.format("Found %d policies", count));
					});
				} else {
					SwingUtilities.invokeLater(() -> {
						statusLabel.setText("No policies found");
						JOptionPane.showMessageDialog(Main_Screen.this, "No policies found for the given criteria",
								"Search Result", JOptionPane.INFORMATION_MESSAGE);
					});
				}
			} catch (Exception e) {
				if (logging != null) {
					logging.setMessage("Search failed: " + e.getMessage());

				}

				SwingUtilities.invokeLater(() -> {
					statusLabel.setText("Search failed");
					JOptionPane.showMessageDialog(Main_Screen.this, "Search failed: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				});
			} finally {
				if (em != null && em.isOpen()) {
					em.close();
				}
				if (emf != null && emf.isOpen()) {
					emf.close();
				}
			}

			return null;
		}

		@Override
		protected void done() {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	private class ImportWorker extends SwingWorker<ImportResult, Void> {
		@Override
		protected ImportResult doInBackground() {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			SwingUtilities.invokeLater(() -> {
				progressBar.setVisible(true);
				progressBar.setIndeterminate(true);
				statusLabel.setText("Importing policies...");
				progressBar.setString("Starting import...");

				importBtn.setEnabled(false);
				findBtn.setEnabled(false);
				validateBtn.setEnabled(false);
			});

			ImportCriteria criteria = createImportCriteria();
			return importService.importPolicies(criteria);
		}

		@Override
		protected void done() {
			try {
				ImportResult result = get();

				SwingUtilities.invokeLater(() -> {
					if (result.isSuccess()) {
						JOptionPane.showMessageDialog(Main_Screen.this,
								String.format(
										"Import completed successfully\n\n" + "Total processed: %d\n"
												+ "Successful: %d\n" + "Failed: %d",
										result.getTotalProcessed(), result.getSuccessCount(), result.getFailureCount()),
								"Import Complete", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(Main_Screen.this, "Import failed: " + result.getMessage(),
								"Import Failed", JOptionPane.ERROR_MESSAGE);
					}
					
					if (!result.getFailedPolicies().isEmpty()) {
					    if (isAuthorizedForRetryOperation()) {
					        showFailedPoliciesRetryDialog(result.getFailedPolicies());
					    } else {
					    	
					    	JOptionPane.showMessageDialog(Main_Screen.this, "Unauthorized retry attempt blocked",
									"Import Failed", JOptionPane.ERROR_MESSAGE);
					        logging.setMessage("Unauthorized retry attempt blocked");
					    }
					}

//					// Check if there are failed policies to retry
//					if (!result.getFailedPolicies().isEmpty()) {
//						showFailedPoliciesRetryDialog(result.getFailedPolicies());
//					}
				});

			} catch (Exception e) {
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(Main_Screen.this, "Import failed: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				});
			} finally {
				SwingUtilities.invokeLater(() -> {
					importBtn.setEnabled(true);
					findBtn.setEnabled(true);
					validateBtn.setEnabled(true);
					progressBar.setVisible(false);
					statusLabel.setText("Ready");
					setCursor(Cursor.getDefaultCursor());
				});
			}
		}
	}
	
	
	private boolean isAuthorizedForRetryOperation() {
	    // You can store the password in a secure way (not plain text)
	    // Consider using environment variables or encrypted configuration
	    String expectedPassword = System.getenv("RETRY_OPERATION_PASSWORD");
	    if (expectedPassword == null) {
	        expectedPassword = "defaultAdminPass"; // Fallback, but not recommended for production
	    }
	    
	    JPasswordField pf = new JPasswordField();
	    int okCxl = JOptionPane.showConfirmDialog(
	        Main_Screen.this, 
	        pf, 
	        "Enter Retry Operation Password", 
	        JOptionPane.OK_CANCEL_OPTION, 
	        JOptionPane.PLAIN_MESSAGE
	    );

	    if (okCxl == JOptionPane.OK_OPTION) {
	        String password = new String(pf.getPassword());
	        return password.equals(expectedPassword);
	    }
	    return false;
	}

	private void showFailedPoliciesRetryDialog(List<ImportResult.FailedPolicy> failedPolicies) {
	    JDialog failedDialog = new JDialog();
	    failedDialog.setTitle("Failed Policies - Review & Retry");
	    failedDialog.setModal(true);
	    failedDialog.setSize(600, 400);
	    failedDialog.setLocationRelativeTo(null); // Center on screen
	    
	    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
	    mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
	    
	    // Title
	    JLabel titleLabel = new JLabel(failedPolicies.size() + " policies failed:");
	    titleLabel.setFont(HEADER_FONT);
	    mainPanel.add(titleLabel, BorderLayout.NORTH);
	    
	    // Create list
	    DefaultListModel<String> listModel = new DefaultListModel<>();
	    for (ImportResult.FailedPolicy policy : failedPolicies) {
	        listModel.addElement("Policy: " + policy.getPolicyNumber() + 
	                           " - Error: " + 
	                           (policy.getError().length() > 60 ? 
	                            policy.getError().substring(0, 60) + "..." : 
	                            policy.getError()));
	    }
	    
	    JList<String> policyList = new JList<>(listModel);
	    policyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    policyList.setFont(LABEL_FONT);
	    
	    JScrollPane listScrollPane = new JScrollPane(policyList);
	    mainPanel.add(listScrollPane, BorderLayout.CENTER);
	    
	    // Button panel
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
	    
	    JButton editBtn = createStyledButton("Edit Selected", SECONDARY_COLOR, "✏️");
	    JButton closeBtn = createStyledButton("Close", new Color(120, 120, 120), "✕");
	    
	    editBtn.addActionListener(e -> {
	        int selectedIndex = policyList.getSelectedIndex();
	        if (selectedIndex >= 0) {
	            ImportResult.FailedPolicy failedPolicy = failedPolicies.get(selectedIndex);
	            failedDialog.dispose();
	            showJsonEditorDialog(failedPolicy);
	        } else {
	            JOptionPane.showMessageDialog(failedDialog,
	                "Please select a policy to edit",
	                "No Selection",
	                JOptionPane.WARNING_MESSAGE);
	        }
	    });
	    
	    closeBtn.addActionListener(e -> failedDialog.dispose());
	    
	    buttonPanel.add(editBtn);
	    buttonPanel.add(closeBtn);
	    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
	    
	    failedDialog.add(mainPanel);
	    failedDialog.setVisible(true);
	}


	void showJsonEditorDialog(ImportResult.FailedPolicy failedPolicy) {
	    // Create dialog
	    JDialog editorDialog = new JDialog();
	    editorDialog.setTitle("Edit JSON - Policy: " + failedPolicy.getPolicyNumber());
	    editorDialog.setModal(true);
	    editorDialog.setSize(700, 550);
	    editorDialog.setLocationRelativeTo(this);
	    
	    // JSON text area
	    JTextArea jsonArea = new JTextArea(formatJson(failedPolicy.getJson()));
	    jsonArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
	    jsonArea.setTabSize(2);
	    
	    JScrollPane scrollPane = new JScrollPane(jsonArea);
	    
	    // Control panel
	    JPanel controlPanel = new JPanel(new BorderLayout());
	    
	    // Error label
	    JLabel errorLabel = new JLabel("Error: " + failedPolicy.getError());
	    errorLabel.setForeground(Color.RED);
	    errorLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
	    
	    // Button panel
	    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
	    
	    JButton validateBtn = new JButton("Validate JSON");
	    JButton retryBtn = new JButton("Retry Submit");
	    JButton cancelBtn = new JButton("Cancel");
	    
	    validateBtn.addActionListener(e -> {
	        if (isValidJson(jsonArea.getText())) {
	            JOptionPane.showMessageDialog(editorDialog,
	                "JSON is valid",
	                "Validation",
	                JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(editorDialog,
	                "Invalid JSON format",
	                "Validation Error",
	                JOptionPane.ERROR_MESSAGE);
	        }
	    });
	    
	    retryBtn.addActionListener(e -> {
	        String editedJson = jsonArea.getText().trim();
	        
	        if (!isValidJson(editedJson)) {
	            JOptionPane.showMessageDialog(editorDialog,
	                "Cannot submit: Invalid JSON format",
	                "Validation Error",
	                JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        
	        // Show progress
	        int confirm = JOptionPane.showConfirmDialog(editorDialog,
	            "Submit edited JSON for policy " + failedPolicy.getPolicyNumber() + "?",
	            "Confirm Resubmit",
	            JOptionPane.YES_NO_OPTION);
	        
	        if (confirm == JOptionPane.YES_OPTION) {
	            // Disable buttons during submission
	            retryBtn.setEnabled(false);
	            validateBtn.setEnabled(false);
	            
	            // Execute in background to keep UI responsive
	            SwingWorker<Boolean, Void> retryWorker = new SwingWorker<Boolean, Void>() {
	                @Override
	                protected Boolean doInBackground() throws Exception {
	                    // importService is accessible here from Main_Screen
	                    return importService.retryPolicySubmission(
	                        failedPolicy.getPolicyNumber(), editedJson);
	                }
	                
	                @Override
	                protected void done() {
	                    try {
	                        boolean success = get();
	                        if (success) {
	                            JOptionPane.showMessageDialog(editorDialog,
	                                "Policy " + failedPolicy.getPolicyNumber() + " submitted successfully!",
	                                "Success",
	                                JOptionPane.INFORMATION_MESSAGE);
	                            editorDialog.dispose();
	                        } else {
	                            JOptionPane.showMessageDialog(editorDialog,
	                                "Resubmit failed. You can continue editing.",
	                                "Retry Failed",
	                                JOptionPane.WARNING_MESSAGE);
	                        }
	                    } catch (Exception ex) {
	                        JOptionPane.showMessageDialog(editorDialog,
	                            "Error during resubmit: " + ex.getMessage(),
	                            "Error",
	                            JOptionPane.ERROR_MESSAGE);
	                    } finally {
	                        retryBtn.setEnabled(true);
	                        validateBtn.setEnabled(true);
	                    }
	                }
	            };
	            retryWorker.execute();
	        }
	    });
	    
	    cancelBtn.addActionListener(e -> editorDialog.dispose());
	    
	    buttonPanel.add(validateBtn);
	    buttonPanel.add(retryBtn);
	    buttonPanel.add(cancelBtn);
	    
	    controlPanel.add(errorLabel, BorderLayout.NORTH);
	    controlPanel.add(buttonPanel, BorderLayout.SOUTH);
	    
	    editorDialog.setLayout(new BorderLayout());
	    editorDialog.add(scrollPane, BorderLayout.CENTER);
	    editorDialog.add(controlPanel, BorderLayout.SOUTH);
	    
	    editorDialog.setVisible(true);
	}

	private boolean isValidJson(String json) {

		if (json == null || json.trim().isEmpty()) {
			return false;
		}

		try {
			// Using Gson library (already imported in your code)
			com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
			parser.parse(json);
			return true;
		} catch (com.google.gson.JsonSyntaxException e) {
			return false;
		} catch (Exception e) {
			// Catch any other unexpected exceptions
			return false;
		}
	
	}

	private String formatJson(String json) {
	    if (json == null || json.trim().isEmpty()) {
	        return "";
	    }
	    
	    try {
	        // Most common approach that works across versions
	        com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
	            .setPrettyPrinting()
	            .serializeNulls()
	            .create();
	        
	        // Parse JSON string into JsonElement
	        com.google.gson.JsonElement jsonElement = gson.fromJson(json, com.google.gson.JsonElement.class);
	        
	        // Convert back to pretty formatted string
	        return gson.toJson(jsonElement);
	        
	    } catch (Exception e) {
	        System.err.println("JSON formatting error: " + e.getMessage());
	        return json; // Return original if formatting fails
	    }
	}

	private class ValidationWorker extends SwingWorker<Void, Void> {
		@Override
		protected Void doInBackground() throws Exception {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			SwingUtilities.invokeLater(() -> {
				progressBar.setVisible(true);
				progressBar.setIndeterminate(true);
				statusLabel.setText("Validating policies...");
				progressBar.setString("Validating...");

				importBtn.setEnabled(false);
				findBtn.setEnabled(false);
				validateBtn.setEnabled(false);
			});

			String validationFile = System.getProperty("user.home") + File.separator + "validation.json";
			FileWriter file = null;
			PrintWriter writer = null;
			EntityManagerFactory emf = null;
			EntityManager em = null;

			try {
				file = new FileWriter(validationFile);
				writer = new PrintWriter(file);

				ImportCriteria criteria = createImportCriteria();

				emf = Persistence.createEntityManagerFactory("midPU");
				em = emf.createEntityManager();

				List<PolMaster> polMasters = dataService.findPolMasters(em, criteria);

				if (polMasters != null && !polMasters.isEmpty()) {
					Gson gson = new Gson();

					for (PolMaster polMaster : polMasters) {
						boolean skipPolicy = dataService.shouldSkipPolicy(em, polMaster.getPolNo(),
								polMaster.getRenCnt());

						if (!skipPolicy) {
							List<PolMtrVeh> vehicles = dataService.findVehicles(em, polMaster,
									criteria.getVehicleRegNo());

							for (PolMtrVeh vehicle : vehicles) {
								List<PolRisk> risks = dataService.findRisks(em, polMaster, vehicle);

								for (PolRisk risk : risks) {
									List<Xmm600> clients = dataService.findClients(em, polMaster);

									for (Xmm600 client : clients) {
										JsonObject validationJson = validationService.createValidationJson(polMaster,
												risk, vehicle, client);

										if (!validationJson.isJsonNull() || validationJson != null) {
											writer.println(gson.toJson(validationJson));
										}
									}
								}
							}
						}
					}

					writer.flush();

					SwingUtilities.invokeLater(() -> {
						try {
							Desktop.getDesktop().open(new File(validationFile));
							statusLabel.setText("Validation complete");
						} catch (IOException e) {
							JOptionPane.showMessageDialog(Main_Screen.this,
									"Validation file created but could not open: " + e.getMessage(), "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					});
				} else {
					SwingUtilities.invokeLater(() -> {
						JOptionPane.showMessageDialog(Main_Screen.this, "No policies found for validation",
								"Validation Result", JOptionPane.INFORMATION_MESSAGE);
					});
				}
			} catch (Exception e) {
				SwingUtilities.invokeLater(() -> {
					JOptionPane.showMessageDialog(Main_Screen.this, "Validation failed: " + e.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				});
			} finally {
				if (writer != null) {
					writer.close();
				}
				if (file != null) {
					try {
						file.close();
					} catch (IOException e) {
						// Ignore
					}
				}
				if (em != null && em.isOpen()) {
					em.close();
				}
				if (emf != null && emf.isOpen()) {
					emf.close();
				}
			}

			return null;
		}

		@Override
		protected void done() {
			SwingUtilities.invokeLater(() -> {
				importBtn.setEnabled(true);
				findBtn.setEnabled(true);
				validateBtn.setEnabled(true);
				progressBar.setVisible(false);
				statusLabel.setText("Ready");
				setCursor(Cursor.getDefaultCursor());
			});
		}
	}
	
	
	
	
}

//Custom cell renderer for the action button
class ButtonRenderer extends JButton implements TableCellRenderer {
	public ButtonRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setText((value == null) ? "" : value.toString());
		return this;
	}
}

//Update the ButtonEditor constructor to receive parent component
class ButtonEditor extends DefaultCellEditor {
	private JButton button;
	private String label;
	private boolean isPushed;
	private List<ImportResult.FailedPolicy> failedPolicies;
	private Component parentComponent;
	private Main_Screen parentScreen; // Change type to Main_Screen

	public ButtonEditor(JCheckBox checkBox, List<ImportResult.FailedPolicy> failedPolicies, Main_Screen parentScreen) { // Accept
																														// Main_Screen
		super(checkBox);
		this.failedPolicies = failedPolicies;
		this.parentScreen = parentScreen; // Store reference
		button = new JButton();
		button.setOpaque(true);
		button.addActionListener(e -> fireEditingStopped());
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		label = (value == null) ? "" : value.toString();
		button.setText(label);
		isPushed = true;
		return button;
	}

	@Override
	public Object getCellEditorValue() {
		if (isPushed) {
			SwingUtilities.invokeLater(() -> {
				// Get the current table
				Component editorComponent = button.getParent();
				while (editorComponent != null && !(editorComponent instanceof JTable)) {
					editorComponent = editorComponent.getParent();
				}

				if (editorComponent instanceof JTable) {
					JTable table = (JTable) editorComponent;
					int row = table.getSelectedRow();
					if (row >= 0 && row < failedPolicies.size()) {
						ImportResult.FailedPolicy failedPolicy = failedPolicies.get(row);
						// Call method on parentScreen instead of this
						parentScreen.showJsonEditorDialog(failedPolicy);
					}
				}
			});
		}
		isPushed = false;
		return label;
	}



	private String formatJson(String json) {

	    if (json == null || json.trim().isEmpty()) {
	        return "";
	    }
	    
	    try {
	        // Most common approach that works across versions
	        com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
	            .setPrettyPrinting()
	            .create();
	        
	        // Parse JSON string into JsonElement
	        com.google.gson.JsonElement jsonElement = gson.fromJson(json, com.google.gson.JsonElement.class);
	        
	        // Convert back to pretty formatted string
	        return gson.toJson(jsonElement);
	        
	    } catch (Exception e) {
	        System.err.println("JSON formatting error: " + e.getMessage());
	        return json; // Return original if formatting fails
	    }
	
	}

	private boolean isValidJson(String json) {
		if (json == null || json.trim().isEmpty()) {
			return false;
		}

		try {
			// Using Gson library (already imported in your code)
			com.google.gson.JsonParser parser = new com.google.gson.JsonParser();
			parser.parse(json);
			return true;
		} catch (com.google.gson.JsonSyntaxException e) {
			return false;
		} catch (Exception e) {
			// Catch any other unexpected exceptions
			return false;
		}
	}

	@Override
	public boolean stopCellEditing() {
		isPushed = false;
		return super.stopCellEditing();
	}
}
