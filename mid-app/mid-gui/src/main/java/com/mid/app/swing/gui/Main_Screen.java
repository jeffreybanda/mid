package com.mid.app.swing.gui;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mid.app.politem.model.PolItem;
import com.mid.app.politemben.model.PolItemBen;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

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
    private static final String[] COLUMN_NAMES = {
        "TRAN DATE", "POLICY NUMBER", "STICKER NUMBER", "INCEPTION", "EXPIRY",
        "CUSTOMER NAME", "COVER TYPE", "VEHICLE REG", "MAKE", "MODEL"
    };
    
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
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
            
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
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
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
        gbc.gridx = 0; gbc.gridy = 0;
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
        gbc.gridx = 0; gbc.gridy = 1;
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
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        
        // Add focus listener for better UX
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    new EmptyBorder(7, 9, 7, 9)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(8, 10, 8, 10)
                ));
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
    	    button.setBorder(BorderFactory.createCompoundBorder(
    	        BorderFactory.createLineBorder(color.darker(), 1),
    	        new EmptyBorder(10, 20, 10, 20)
    	    ));
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
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(0, 0, 0, 0)
        ));
        
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
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE),
                    new EmptyBorder(10, 5, 10, 5)
                ));
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
        progressBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(2, 2, 2, 2)
        ));
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
    
    // ========== Business Logic Methods (Unchanged from previous version) ==========
    
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
                        boolean skipPolicy = dataService.shouldSkipPolicy(em, polMaster.getPolNo(), polMaster.getRenCnt());
                        
                        if (!skipPolicy) {
                            List<PolMtrVeh> vehicles = dataService.findVehicles(em, polMaster, criteria.getVehicleRegNo());
                            
                            for (PolMtrVeh vehicle : vehicles) {
                                List<PolRisk> risks = dataService.findRisks(em, polMaster, vehicle);
                                
                                for (PolRisk risk : risks) {
                                    Object[] row = new Object[] {
                                        polMaster.getTranDate(),
                                        polMaster.getPolNo(),
                                        vehicle.getOwnName(),
                                        risk.getComDate(),
                                        risk.getExpiryDate(),
                                        polMaster.getInsdName1() + " " + polMaster.getInsdName2(),
                                        vehicle.getCoverType(),
                                        vehicle.getVehRegNo(),
                                        vehicle.getVehMake(),
                                        vehicle.getModelDesc()
                                    };
                                    
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
                        JOptionPane.showMessageDialog(Main_Screen.this,
                            "No policies found for the given criteria",
                            "Search Result",
                            JOptionPane.INFORMATION_MESSAGE);
                    });
                }
            } catch (Exception e) {
                if (logging != null) {
                    logging.setMessage("Search failed: " + e.getMessage());
                    
                }
                
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Search failed");
                    JOptionPane.showMessageDialog(Main_Screen.this,
                        "Search failed: " + e.getMessage(),
                        "Error",
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
                            String.format("Import completed successfully!\n\n" +
                                        "Total processed: %d\n" +
                                        "Successful: %d\n" +
                                        "Failed: %d",
                                result.getTotalProcessed(), result.getSuccessCount(), result.getFailureCount()),
                            "Import Complete",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(Main_Screen.this,
                            "Import failed: " + result.getMessage(),
                            "Import Failed",
                            JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(Main_Screen.this,
                        "Import failed: " + e.getMessage(),
                        "Error",
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
                        boolean skipPolicy = dataService.shouldSkipPolicy(em, polMaster.getPolNo(), polMaster.getRenCnt());
                        
                        if (!skipPolicy) {
                            List<PolMtrVeh> vehicles = dataService.findVehicles(em, polMaster, criteria.getVehicleRegNo());
                            
                            for (PolMtrVeh vehicle : vehicles) {
                                List<PolRisk> risks = dataService.findRisks(em, polMaster, vehicle);
                                
                                for (PolRisk risk : risks) {
                                    List<Xmm600> clients = dataService.findClients(em, polMaster);
                                    
                                    for (Xmm600 client : clients) {
                                        JsonObject validationJson = validationService.createValidationJson(
                                            polMaster, risk, vehicle, client);
                                        
                                        if (!validationJson.isJsonNull() || validationJson !=null) {
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
                                "Validation file created but could not open: " + e.getMessage(),
                                "Warning",
                                JOptionPane.WARNING_MESSAGE);
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(Main_Screen.this,
                            "No policies found for validation",
                            "Validation Result",
                            JOptionPane.INFORMATION_MESSAGE);
                    });
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(Main_Screen.this,
                        "Validation failed: " + e.getMessage(),
                        "Error",
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