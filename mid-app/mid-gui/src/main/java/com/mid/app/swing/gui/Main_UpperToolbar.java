
package com.mid.app.swing.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.mid.app.swing.gui.Main_VehiclesFrame;
import com.mid.app.swing.utils.ChangeComponentOrientation;

public class Main_UpperToolbar extends JPanel {

	private Main_Screen policyFrame;
	private Main_VehiclesFrame customersFrame;
	private static final long serialVersionUID = 1L;
	private final ChangeComponentOrientation componentOrientation;
	private JButton policiesBtn,vehiclesBtn,
			refreshBtn;

	public Main_UpperToolbar(final JPanel mainFramePanel) {
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setMaximumSize(new Dimension(32767, 55));
		setBounds(new Rectangle(0, 0, 1224, 55));

		setAutoscrolls(true);
		setSize(new Dimension(1224, 55));
		setPreferredSize(new Dimension(1224, 55));
		setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setBackground(SystemColor.activeCaption);
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		componentOrientation = new ChangeComponentOrientation();

		policiesBtn = new JButton("Policy List");
		policiesBtn.setMinimumSize(new Dimension(137, 40));
		policiesBtn.setMaximumSize(new Dimension(137, 40));
		policiesBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		policiesBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		policiesBtn.setIcon(new ImageIcon(Main_UpperToolbar.class.getResource("/icons/main_rezerv.png")));
		policiesBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
		policiesBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 11));
		policiesBtn.setPreferredSize(new Dimension(137, 40));
		policiesBtn.addActionListener(UpperToolbarActionListener(mainFramePanel));
		this.add(policiesBtn);
		
		//vehiclesBtn = new JButton("Vehicles");
       // vehiclesBtn.setMinimumSize(new Dimension(137, 40));
       // vehiclesBtn.setMaximumSize(new Dimension(137, 40));
      //  vehiclesBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
      //  vehiclesBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
     //   vehiclesBtn.setIcon(new ImageIcon(MainFrame.class.getResource("/icons/main_guests.png")));
      //  vehiclesBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
      //  vehiclesBtn.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 11));
      //  vehiclesBtn.setPreferredSize(new Dimension(137, 40));
     //   vehiclesBtn.addActionListener(UpperToolbarActionListener(mainFramePanel));
     //   this.add(vehiclesBtn);

		final JSeparator separator = new JSeparator();
		separator.setBackground(Color.DARK_GRAY);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setFocusable(true);
		separator.setForeground(Color.DARK_GRAY);
		separator.setAutoscrolls(true);
		separator.setPreferredSize(new Dimension(10, 40));
		this.add(separator);

		final JSeparator secondSeparator = new JSeparator();
		secondSeparator.setPreferredSize(new Dimension(10, 40));
		secondSeparator.setOrientation(SwingConstants.VERTICAL);
		secondSeparator.setForeground(Color.DARK_GRAY);
		secondSeparator.setFocusable(true);
		secondSeparator.setBackground(Color.DARK_GRAY);
		secondSeparator.setAutoscrolls(true);
		this.add(secondSeparator);

		refreshBtn = new JButton("");
		refreshBtn.setPreferredSize(new Dimension(75, 40));
		refreshBtn.setMinimumSize(new Dimension(75, 40));
		refreshBtn.setMaximumSize(new Dimension(75, 40));
		refreshBtn.setToolTipText("Refresh the application main window.");
		refreshBtn.setMnemonic(KeyEvent.VK_F5);
		refreshBtn.setHorizontalTextPosition(SwingConstants.CENTER);
		refreshBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		refreshBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		refreshBtn.setIcon(new ImageIcon(
				Main_UpperToolbar.class.getResource("/icons/menubar_exchange_calculate.png")));
		refreshBtn.setActionCommand("Refresh");
		refreshBtn.addActionListener(UpperToolbarActionListener(mainFramePanel));
		this.add(refreshBtn);

		componentOrientation.setThePanel(this);

	}

	public ActionListener UpperToolbarActionListener(final JPanel mainPanel) {

		final ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {

				if (e.getSource() == policiesBtn) {

					policyFrame = new Main_Screen();
					mainPanel.removeAll();
					// policyFrame.populateMainTable();
					mainPanel.add(policyFrame, BorderLayout.CENTER);
					mainPanel.revalidate();
					mainPanel.repaint();
				} else if (e.getSource() == vehiclesBtn) {

                    customersFrame = new Main_VehiclesFrame();
                    mainPanel.removeAll();
                    mainPanel.add(customersFrame, BorderLayout.CENTER);
                    mainPanel.revalidate();
                    mainPanel.repaint();

				} else if (e.getSource() == refreshBtn) {
					mainPanel.removeAll();
					mainPanel.revalidate();
					mainPanel.repaint();
				}
			}
		};
		return actionListener;
	}
}
