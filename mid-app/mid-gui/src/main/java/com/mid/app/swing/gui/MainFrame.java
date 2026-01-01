
package com.mid.app.swing.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.mid.app.utils.LoggingEngine;

public class MainFrame extends JFrame {

	/**
	 *
	 */
	private final JPanel mainPanel;
	// private static LocaleBean bean;
	private String exitMessage = "";
	private String titleMessage = "";
	private Main_MenuBar customMenuBar;
	private static LoggingEngine logging;
	// private static SessionBean sessionBean;
	// private GetLiveCurrencyRates currency = null;
	private static final long serialVersionUID = 1L;
	// private ChangeComponentOrientation componentOrientation;
	/* get the external toolbar and initialize it */
	private final Main_UpperToolbar customUperToolbar;
//	private final Main_BottomToolbar customBottomToolbar;

	private final String LOGOPATH = "/icons/main_logo(128X12).png";

	// Set basic properties for main frame.
	public MainFrame() {

		logging = LoggingEngine.getInstance();
		logging.setReady(MainFrame.class.getName());
		logging.changeLoggingLevel(Level.FINE);
		logging.setConsoleLogging(false);

		// get operation system name to add icon (if windows to taskbar else for dock)
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.decode("#066d95"));

		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(LOGOPATH)));

		this.setTitle("Motor Insurance Database - [API]" + System.getProperty("PropFile"));

		this.setMinimumSize(new Dimension(800, 600));
		/* make it default size of frame maximized */
		this.setExtendedState(Frame.MAXIMIZED_BOTH);

		/* get the external menubar and initialize it */
		customMenuBar = new Main_MenuBar();
		customMenuBar.setJFrame(this);
		customMenuBar.themeChanger.setFrame(this);
		/* add it to our frame */
		this.setJMenuBar(customMenuBar.getMenuBar());

		// currency = new GetLiveCurrencyRates();
		customUperToolbar = new Main_UpperToolbar(mainPanel);
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);// add it this frame after injection

		/* add it to our frame */
		getContentPane().add(customUperToolbar, BorderLayout.NORTH);
		// change language as locale.

		// change component orientation with locale.

		/* set exiting from the application when clicking on X button */
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				final int decision = JOptionPane.showConfirmDialog(null, exitMessage,
						titleMessage, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (decision == JOptionPane.YES_OPTION) {

					System.exit(0);
				} else {
					setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}

				super.windowClosing(e);
			}
		});
		
		this.setVisible(true);

	}

}
