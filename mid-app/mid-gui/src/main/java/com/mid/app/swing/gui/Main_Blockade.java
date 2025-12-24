/**
 * @author Coder ACJHP
 * @Email hexa.octabin@gmail.com
 * @Date 15/07/2017
 */
package com.mid.app.swing.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

public final class Main_Blockade extends JPanel implements ActionListener {

	/**
	 *
	 */
	private JPanel panel;

	private JLabel lblSearch;
	private ResourceBundle bundle;
	private JTextField searchField;
	private String reservIdFromRow;

	private List<Long> rezervationIdList;

	private String today = "";
	private final Calendar masterDate = Calendar.getInstance();
	private String[] weekDates = new String[10];

	private JDateChooser dateChooser;
	private JPanel leftSidePanel, buttonPanel;
	private JButton previousBtn, nextBtn, btnShowRes;
	private static final long serialVersionUID = 1L;
	private TableRowSorter<DefaultTableModel> tableRowShorter;

	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final JTable blokajTable, blokajRoomsTable, blokajCustomerTable, table;
	private final JSplitPane mainVerticalSplitter;
	private final JSplitPane leftCenterSplitter, centerRightSplitter;
	private final JScrollPane blokajScrollPane, roomScrollPane, customerScrollPane, generalScrollPane;

	private final String[] bottomTableHeader = new String[10];
	private final DefaultTableModel model = new DefaultTableModel(bottomTableHeader, 0);

	private final String[] blokajColsName = { "REZERV. NO", "GROUP", "AGENCY", "CHECK/IN", "CHECK/OUT", "EARLY PAY" };
	private final DefaultTableModel blokajModel = new DefaultTableModel(blokajColsName, 0);

	private final String[] blokajRoomsColsName = { "ROOM", "TYPE", "PERSON COUNT" };
	private final DefaultTableModel blokajRoomsModel = new DefaultTableModel(blokajRoomsColsName, 0);

	private final String[] blokajCustomerColsName = { "FIRSTNAME", "LASTNAME" };
	private final DefaultTableModel blokajCustomerModel = new DefaultTableModel(blokajCustomerColsName, 0);

	/**
	 * Create the frame.
	 */
	public Main_Blockade() {

		setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));

		this.setAutoscrolls(true);
		this.setMinimumSize(new Dimension(800, 600));
		/* make it default size of frame maximized */
		this.setMaximumSize(new Dimension(1000, 900));
		this.setLayout(new BorderLayout());

		mainVerticalSplitter = new JSplitPane();
		mainVerticalSplitter.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		mainVerticalSplitter.setOneTouchExpandable(true);
		mainVerticalSplitter.setIgnoreRepaint(true);
		mainVerticalSplitter.setInheritsPopupMenu(true);
		mainVerticalSplitter.setAutoscrolls(true);
		mainVerticalSplitter.setDividerLocation(200);
		mainVerticalSplitter.setAlignmentY(Component.CENTER_ALIGNMENT);
		mainVerticalSplitter.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainVerticalSplitter.setContinuousLayout(true);
		mainVerticalSplitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainVerticalSplitter.resetToPreferredSizes();
		setLayout(new BorderLayout(0, 0));
		add(mainVerticalSplitter, BorderLayout.CENTER);

		leftSidePanel = new JPanel();
		leftSidePanel.setAutoscrolls(true);
		leftSidePanel.setPreferredSize(new Dimension(10, 300));
		leftSidePanel.setBounds(0, 0, 10, 10);
		leftSidePanel.setLayout(new BorderLayout(0, 0));
		mainVerticalSplitter.setLeftComponent(leftSidePanel);

		leftCenterSplitter = new JSplitPane();
		leftCenterSplitter.setIgnoreRepaint(true);
		leftCenterSplitter.setInheritsPopupMenu(true);
		leftCenterSplitter.setOneTouchExpandable(true);
		leftCenterSplitter.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftCenterSplitter.setContinuousLayout(true);
		leftCenterSplitter.setAutoscrolls(true);
		leftCenterSplitter.setDividerLocation(430);
		leftCenterSplitter.resetToPreferredSizes();
		leftSidePanel.add(leftCenterSplitter);

		blokajTable = new JTable(blokajModel);
		blokajTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		blokajTable.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
		blokajTable.setRowSelectionAllowed(true);

		blokajTable.addMouseListener(blokajMouseListener());
		blokajTable.setRowHeight(20);
		blokajTable.setBackground(UIManager.getColor("InternalFrame.borderColor"));

		blokajScrollPane = new JScrollPane();
		blokajScrollPane.setViewportView(blokajTable);
		leftCenterSplitter.setLeftComponent(blokajScrollPane);

		centerRightSplitter = new JSplitPane();
		centerRightSplitter.setOneTouchExpandable(true);
		centerRightSplitter.setInheritsPopupMenu(true);
		centerRightSplitter.setIgnoreRepaint(true);
		centerRightSplitter.setContinuousLayout(true);
		centerRightSplitter.setAutoscrolls(true);
		centerRightSplitter.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerRightSplitter.resetToPreferredSizes();
		centerRightSplitter.setDividerLocation(430);
		leftCenterSplitter.setRightComponent(centerRightSplitter);

		blokajRoomsTable = new JTable(blokajRoomsModel);
		blokajRoomsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		blokajRoomsTable.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
		blokajRoomsTable.setColumnSelectionAllowed(false);
		blokajRoomsTable.setCellSelectionEnabled(false);
		blokajRoomsTable.setRowSelectionAllowed(true);

		blokajRoomsTable.setRowHeight(20);
		blokajRoomsTable.setBackground(UIManager.getColor("InternalFrame.borderColor"));

		roomScrollPane = new JScrollPane();
		roomScrollPane.setViewportView(blokajRoomsTable);
		centerRightSplitter.setLeftComponent(roomScrollPane);

		blokajCustomerTable = new JTable(blokajCustomerModel);
		blokajCustomerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		blokajCustomerTable.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
		blokajCustomerTable.setCellSelectionEnabled(false);
		blokajCustomerTable.setColumnSelectionAllowed(false);

		blokajCustomerTable.setRowHeight(20);
		blokajCustomerTable.setBackground(UIManager.getColor("InternalFrame.borderColor"));

		customerScrollPane = new JScrollPane();
		customerScrollPane.setViewportView(blokajCustomerTable);
		centerRightSplitter.setRightComponent(customerScrollPane);

		tableRowShorter = new TableRowSorter<>(model);

		table = new JTable(model);

		table.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(20);
		table.setFont(new Font("Dialog", Font.PLAIN, 14));
		table.setRowSorter(tableRowShorter);
		table.setBackground(UIManager.getColor("InternalFrame.borderColor"));

		// populate table headers from this method.
		populateTableHeaders();

		generalScrollPane = new JScrollPane();
		generalScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		generalScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		generalScrollPane.setViewportView(table);

		mainVerticalSplitter.setRightComponent(generalScrollPane);

		final JPanel upperPanel = new JPanel();
		upperPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		upperPanel.setAutoscrolls(true);
		upperPanel.setPreferredSize(new Dimension(300, 45));
		upperPanel.setBackground(Color.decode("#066d95"));
		upperPanel.setLayout(new BorderLayout());
		add(upperPanel, BorderLayout.NORTH);

		// make a panel to add date chooser and buttons with null layout.
		buttonPanel = new JPanel();
		buttonPanel.setBorder(null);
		buttonPanel.setAutoscrolls(true);
		buttonPanel.setPreferredSize(new Dimension(440, 40));
		buttonPanel.setBackground(Color.decode("#066d95"));
		buttonPanel.setLayout(null);
		upperPanel.add(buttonPanel, BorderLayout.WEST);

		dateChooser = new JDateChooser();

		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(55, 6, 164, 26);
		dateChooser.addPropertyChangeListener(customPropListener());
		buttonPanel.add(dateChooser);

		previousBtn = new JButton("");
		previousBtn.setMaximumSize(new Dimension(400, 29));
		previousBtn.setMinimumSize(new Dimension(400, 29));
		previousBtn.setAutoscrolls(true);
		previousBtn.addActionListener(this);
		previousBtn
				.setIcon(new ImageIcon(Main_Blockade.class.getResource("/com/mid/app/icons/blockade_previous.png")));
		previousBtn.setBounds(6, 6, 49, 26);
		buttonPanel.add(previousBtn);

		nextBtn = new JButton("");
		nextBtn.addActionListener(this);
		nextBtn.setIcon(new ImageIcon(Main_Blockade.class.getResource("/com/mid/app/icons/blockade_next.png.png")));
		nextBtn.setBounds(219, 6, 49, 26);
		buttonPanel.add(nextBtn);

		btnShowRes = new JButton("Show reservation");
		btnShowRes.setToolTipText("<html>Select a reservation from the table with <br>"
				+ "single click and press this button to show it.</html>");
		btnShowRes.addActionListener(this);

		btnShowRes.setFont(new Font("Dialog", Font.PLAIN, 14));
		btnShowRes.setAutoscrolls(true);
		btnShowRes.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnShowRes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnShowRes.setBounds(280, 1, 154, 37);
		buttonPanel.add(btnShowRes);

		// add this label to upperPanel(main) to be centered.
		final JLabel lblBlockade = new JLabel("BLOCKADE");
		lblBlockade.setAutoscrolls(true);
		lblBlockade.setMinimumSize(new Dimension(70, 16));
		lblBlockade.setPreferredSize(new Dimension(70, 16));
		lblBlockade.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblBlockade.setLabelFor(buttonPanel);
		lblBlockade.setLocation(571, 6);
		lblBlockade.setSize(159, 30);
		lblBlockade.setForeground(UIManager.getColor("Button.highlight"));
		lblBlockade.setHorizontalTextPosition(SwingConstants.CENTER);
		lblBlockade.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlockade.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 25));
		upperPanel.add(lblBlockade, BorderLayout.CENTER);

		panel = new JPanel();
		panel.setAutoscrolls(true);
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(300, 40));
		upperPanel.add(panel, BorderLayout.EAST);
		panel.setLayout(null);

		lblSearch = new JLabel("Search : ");
		lblSearch.setHorizontalTextPosition(SwingConstants.RIGHT);
		lblSearch.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSearch.setForeground(new Color(255, 255, 51));
		lblSearch.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblSearch.setBounds(6, 6, 93, 22);
		panel.add(lblSearch);

		searchField = new JTextField();
		searchField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		searchField.setSelectionColor(new Color(102, 153, 255));
		searchField.setPreferredSize(new Dimension(13, 26));
		searchField.setIgnoreRepaint(true);
		searchField.setBounds(111, 5, 183, 26);
		searchField.setColumns(10);
		searchField.addKeyListener(customKeyListener());
		panel.add(searchField);

		this.setVisible(true);

		populateBlokajTable(blokajModel);
		populateMainTable(model);

	}

	public void populateTableHeaders() {

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		final Calendar c = Calendar.getInstance();
		c.setTime(masterDate.getTime());

		final JTableHeader tableHeader = table.getTableHeader();
		final TableColumnModel tableColumnModel = tableHeader.getColumnModel();
		TableColumn tableColumn;

		tableColumn = tableColumnModel.getColumn(0);
		tableColumn.setHeaderValue("ROOM");
		tableColumn = tableColumnModel.getColumn(1);
		tableColumn.setHeaderValue("TYPE");
		tableColumn = tableColumnModel.getColumn(2);
		tableColumn.setHeaderValue("STATUS");

		// start the date from minus 1 to get today date.
		c.add(Calendar.DATE, -1);

		// start the loop from 3 because first 3 columns already
		// populated up and the loop on 10 to get one week
		for (int i = 3; i < 10; i++) {
			c.add(Calendar.DATE, 1);
			today = simpleDateFormat.format(c.getTime());
			tableColumn = tableColumnModel.getColumn(i);
			tableColumn.setHeaderValue(today);

			// store dates in special array to use it in bottom
			weekDates[i] = today;
		}

		tableHeader.revalidate();
		tableHeader.repaint();

	}

	// Bottom main table that include all reservations
	public void populateMainTable(final DefaultTableModel model) {

		model.setRowCount(0);
		/* Simple object POJO class (entity) */

	}

	// Upper first at left tabel including blockade reservations
	public void populateBlokajTable(final DefaultTableModel blokajModel) {

	}

	public void populateBlokajRoomsModel(final DefaultTableModel blokajRoomsModel, final String reservId) {

	}

	public void populateBlokajCustomerModel(final DefaultTableModel blokajCustomerModel, final String reservId) {

	}

	private MouseListener blokajMouseListener() {
		final MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				final int selectedIndex = blokajTable.getSelectedRow();

				if (selectedIndex < 0) {
					blokajRoomsTable.revalidate();
					blokajRoomsTable.repaint();
					blokajCustomerTable.revalidate();
					blokajCustomerTable.repaint();
				}

				reservIdFromRow = blokajTable.getValueAt(selectedIndex, 0).toString();
				blokajRoomsModel.setRowCount(0);
				populateBlokajRoomsModel(blokajRoomsModel, reservIdFromRow);
				blokajCustomerModel.setRowCount(0);
				populateBlokajCustomerModel(blokajCustomerModel, reservIdFromRow);
				super.mousePressed(e);
			}
		};
		return adapter;
	}

	// this listener for changing table headers when date chosen from two rows.
	@Override
	public void actionPerformed(final ActionEvent e) {

	}

	// this listener for changing table headers when date chosen from date component.
	private PropertyChangeListener customPropListener() {
		final PropertyChangeListener propListener = (final PropertyChangeEvent evt) -> {
			if ("date".equals(evt.getPropertyName())) {
				masterDate.setTime((Date) evt.getNewValue());
				populateTableHeaders();
				populateBlokajTable(blokajModel);
				populateMainTable(model);
			}
		};
		return propListener;
	}

	private KeyListener customKeyListener() {
		final KeyAdapter adapter = new KeyAdapter() {

			@Override
			public void keyTyped(final KeyEvent e) {

				final String modifiedQuery = "(?i)" + searchField.getText();
				tableRowShorter.setRowFilter(RowFilter.regexFilter(modifiedQuery));

				super.keyTyped(e);
			}

		};
		return adapter;
	}
}
