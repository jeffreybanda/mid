

package com.mid.app.swing.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Optional;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


import com.mid.app.swing.utils.ChangeComponentOrientation;
import com.mid.app.ui.external.VehicleDetailWindow;
import com.mid.app.ui.extras.CustomTableHeaderRenderer;
import com.mid.app.ui.extras.VehiclesTableRenderer;

public class Main_VehiclesFrame extends JPanel {

	/**
	 *
	 */

	private JTable vehicleTable;

	private JLabel lblTableFilter;
	private JScrollPane scrollPane;

	private JTextField searchFilterField;

	private JPanel searchPanel = new JPanel();
	private static final long serialVersionUID = 1L;
	private ChangeComponentOrientation componentOrientation;
	private final String[] colsName = { "VEHREGNO", "CHASSISNO", "MAKE ", "MODEL",
			"COLOR" };
	private DefaultTableModel model = new DefaultTableModel(colsName, 0);
	final static VehicleDetailWindow custWindow = new VehicleDetailWindow();
	private final CustomTableHeaderRenderer THR = new CustomTableHeaderRenderer();
	private final VehiclesTableRenderer renderer = new VehiclesTableRenderer();

//private CustomerDaoImpl customerDaoImpl;
	

	public Main_VehiclesFrame() {

		this.setAutoscrolls(true);
		this.setMinimumSize(new Dimension(800, 600));
		/* make it default size of frame maximized */
		this.setMaximumSize(new Dimension(1000, 900));
		this.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		setLayout(new BorderLayout(0, 0));

		componentOrientation = new ChangeComponentOrientation();
		componentOrientation.setThePanel(this);

		searchPanel.setDoubleBuffered(false);
		searchPanel.setAutoscrolls(true);
		add(searchPanel, BorderLayout.NORTH);
		searchPanel.setPreferredSize(new Dimension(10, 30));

		lblTableFilter = new JLabel("Type to search : ");
		lblTableFilter.setForeground(new Color(178, 34, 34));
		lblTableFilter.setSize(new Dimension(130, 25));
		lblTableFilter.setPreferredSize(new Dimension(130, 22));
		lblTableFilter.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTableFilter.setAutoscrolls(true);
		lblTableFilter.setHorizontalAlignment(SwingConstants.LEFT);
		lblTableFilter.setFont(new Font("Dialog", Font.BOLD, 15));

		searchFilterField = new JTextField();
		searchFilterField.setDragEnabled(true);
		searchFilterField.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, new Color(0, 191, 255), null, null, null));
		searchFilterField.setPreferredSize(new Dimension(200, 22));
		searchFilterField.setIgnoreRepaint(true);
		searchFilterField.setColumns(10);
		searchFilterField.setFont(new Font("Dialog", Font.BOLD, 13));
		searchFilterField.setHorizontalAlignment(SwingConstants.LEFT);
		final GroupLayout gl_searchPanel = new GroupLayout(searchPanel);
		gl_searchPanel.setHorizontalGroup(
				gl_searchPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_searchPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblTableFilter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(searchFilterField, GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
								.addGap(118)));
		gl_searchPanel.setVerticalGroup(
				gl_searchPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_searchPanel.createSequentialGroup()
								.addGap(5)
								.addGroup(gl_searchPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblTableFilter, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(searchFilterField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addContainerGap()));
		searchPanel.setLayout(gl_searchPanel);

		searchFilterField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(final KeyEvent e) {

				final String searchedWord = searchFilterField.getText();
				filter(searchedWord);

			}
		});

		scrollPane = new JScrollPane();
		add(scrollPane);

		// populate main table model with custom method
		//populateMainTable(model);

		vehicleTable = new JTable(model);
		vehicleTable.setFillsViewportHeight(true);
		vehicleTable.setRowSelectionAllowed(true);

		THR.setHorizontalAlignment(SwingConstants.CENTER);

		vehicleTable.getTableHeader().setDefaultRenderer(THR);
		vehicleTable.setFont(new Font("Dialog", Font.PLAIN, 14));
		vehicleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		vehicleTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		vehicleTable.setBackground(new Color(245, 245, 245));
		vehicleTable.addMouseListener(openCustomerListener());
		scrollPane.setViewportView(vehicleTable);

		this.setVisible(true);
	}

	private void filter(final String query) {
		final String modifiedQuery = "(?i)" + query;
		final TableRowSorter<TableModel> tr = new TableRowSorter<TableModel>(model);
		vehicleTable.setRowSorter(tr);
		tr.setRowFilter(RowFilter.regexFilter(modifiedQuery));
	}

	

	private MouseListener openCustomerListener() {
		final MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {

				if (e.getClickCount() == 2) {

					final int rowIndex = vehicleTable.getSelectedRow();
					final String name = vehicleTable.getValueAt(rowIndex, 2).toString();
					final String lastname = vehicleTable.getValueAt(rowIndex, 3).toString();

				}

				super.mousePressed(e);
			}
		};
		return adapter;
	}

	// save all changed properties in customer table
	private ActionListener saveChanges() {
		final ActionListener listener = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

			}
		};
		// refresh table to update datails.
		
		return listener;
	}

}
