
package com.mid.app.swing.gui;

import static com.mid.app.quartz.job.QuartzSchedulerApp.*;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

import com.mid.app.ui.external.DialogFrame;
import com.mid.app.ui.external.ReadLogsWindow;
import com.mid.app.ui.extras.ApplicationThemeChanger;

public class Main_MenuBar {

	private final JMenuBar menuBar;
	private JFrame mainFrame;
	private String exitMessage = "";
	private String titleMessage = "";
	// private static LoggingEngine logging;
	private final Runtime run = Runtime.getRuntime();
	// private final LocaleBean bean = LocaleBean.getInstance();
	// private static SessionBean SESSION_BEAN;
	// private final ChangeComponentOrientation componentOrientation;
	private final static Desktop DESKTOP = Desktop.getDesktop();
	private final String command = System.getProperty("os.name");
	public final ApplicationThemeChanger themeChanger = new ApplicationThemeChanger();
	private final JMenu mnTools, usersMenu, mnAbout, utils;
	private final JMenuItem restart, menuInnerItemExit, systemLogs,
			defaultTheme, mnitmAero, mnitmBernstain,
			mnitmMint, mnitmMcwin, mnitmAcryl, mnitmNoire, mnitmLuna,
			aboutDeveloper, mnitmTexture,
			shareYourOpinion, jobProps, calculator;

	// getter method for getting the modified menubar from another class
	public JMenuBar getMenuBar() {
		return this.menuBar;
	}

	public void setJFrame(final JFrame frame) {
		this.mainFrame = frame;
	}

	public Main_MenuBar() {

		menuBar = new JMenuBar();
		// logging = LoggingEngine.getInstance();
		menuBar.setPreferredSize(new Dimension(0, 30));
		menuBar.setFont(new Font("Dialog", Font.BOLD, 14));
		menuBar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menuBar.setBorder(new LineBorder(new Color(128, 128, 128)));
		menuBar.setAutoscrolls(true);

		// SESSION_BEAN = SessionBean.getSESSION_BEAN();
		// componentOrientation = new ChangeComponentOrientation();
		// componentOrientation.setTheMenuBar(menuBar);

		restart = new JMenuItem("Restart");
		restart.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 13));
		// restart.setIcon(new ImageIcon(getClass().getResource("/com/mid/app/icons/menuBar_restart.png")));

		menuInnerItemExit = new JMenuItem("Exit");
		menuInnerItemExit.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 13));
		// menuInnerItemExit.setIcon(new ImageIcon(getClass().getResource("/com/mid/app/icons/main_exit.png")));
		// add shortcut keys
		menuInnerItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, (InputEvent.SHIFT_MASK
				| Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		menuInnerItemExit.setMnemonic(KeyEvent.VK_Q + KeyEvent.VK_CONTROL);
		// add listener for exiting when CTRL+SHIFT+Q pressed
		menuInnerItemExit.addActionListener(ActionEvent -> {

			final int decision = JOptionPane.showConfirmDialog(null, exitMessage,
					titleMessage, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (decision == JOptionPane.YES_OPTION) {
				// new DataSourceFactory().shutDown();
				System.exit(0);
			} else {
				mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			}

		});

		mnTools = new JMenu("Tools");
		mnTools.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 13));
		menuBar.add(mnTools);

		// add calculator application section to menubar
		calculator = new JMenuItem("Calculator");
		calculator.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		calculator.setIcon(new ImageIcon(Main_MenuBar.class.getResource("/icons/menubar_calc.png")));
		calculator.addActionListener(ActionListener -> {

			final Thread openLocalApps = new Thread(() -> {
				if (command.contains("Windows")) {
					try {
						
						run.exec("C:/Windows/System32/calc.exe");

					} catch (final IOException e) {
						e.printStackTrace();
					}

				} else {

					try {
						run.exec("/usr/bin/open -a Calculator");

					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			});

			openLocalApps.start();
		});

		mnTools.add(calculator);

		systemLogs = new JMenuItem("System logs");
		systemLogs.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		systemLogs.setIcon(new ImageIcon(Main_MenuBar.class.getResource("/icons/logging.png")));
		systemLogs.addActionListener(ActionListener -> {
			SwingUtilities.invokeLater(ReadLogsWindow::new);
		});
		mnTools.add(systemLogs);

		/* Add all themes into themes section */
		defaultTheme = new JMenuItem("Nimbus");
		defaultTheme.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// defaultTheme.addActionListener(ActionListener -> {
		// themeChanger.ChangeTheme("Nimbus");
		// });
		mnitmAero = new JMenuItem("Aero");
		mnitmAero.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// mnitmAero.addActionListener(ActionListener -> {
		// themeChanger.ChangeTheme("Aero");
		// });
		mnitmAcryl = new JMenuItem("Acryl");
		mnitmAcryl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// mnitmAcryl.addActionListener(ActionListener -> {
		// themeChanger.ChangeTheme("Acryl");
		// });
		mnitmLuna = new JMenuItem("Luna");
		mnitmLuna.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// mnitmLuna.addActionListener(ActionListener -> {
		// themeChanger.ChangeTheme("Luna");
		// });
		mnitmBernstain = new JMenuItem("Bernstein");
		mnitmBernstain.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// mnitmBernstain.addActionListener(ActionListener -> {
		// themeChanger.ChangeTheme("bernstein");
		// });
		mnitmNoire = new JMenuItem("Noire");
		mnitmNoire.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// mnitmNoire.addActionListener(ActionListener -> {
		// themeChanger.ChangeTheme("Noire");
		// });
		mnitmMint = new JMenuItem("Mint");
		mnitmMint.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// mnitmMint.addActionListener(ActionListener -> {
		// themeChanger.ChangeTheme("Mint");
		// });
		mnitmMcwin = new JMenuItem("McWin");
		mnitmMcwin.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// mnitmMcwin.addActionListener(ActionListener -> {
		// themeChanger.ChangeTheme("McWin");
		// });
		mnitmTexture = new JMenuItem("Texture");
		mnitmTexture.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// mnitmTexture.addActionListener(ActionListener -> {
		// themeChanger.ChangeTheme("Texture");
		// });

		utils = new JMenu("Job Scheduling");
		utils.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 13));
		utils.setToolTipText("Creator permission required!");
		menuBar.add(utils);

		jobProps = new JMenuItem("Run Scheduled Job");
		jobProps.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		jobProps.setIcon(new ImageIcon(Main_MenuBar.class.getResource("/icons/login_database.png")));
		jobProps.addActionListener(doJobAction());
		utils.add(jobProps);

		usersMenu = new JMenu("Users");
		usersMenu.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 13));
		menuBar.add(usersMenu);

		mnAbout = new JMenu("Others");
		mnAbout.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 13));
		menuBar.add(mnAbout);

		// add about developer section to menubar
		aboutDeveloper = new JMenuItem("About developer");
		aboutDeveloper.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// aboutDeveloper.setIcon(new ImageIcon(getClass().getResource("/com/mid/app/icons/menubar_developer.png")));
		aboutDeveloper.addActionListener(ActionListener -> {
			if (Desktop.isDesktopSupported() && DESKTOP.isSupported(Desktop.Action.BROWSE)) {

				try {

					final URI uri = new URI("https://www.linkedin.com");
					DESKTOP.browse(uri);

				} catch (URISyntaxException | IOException e) {
					// logging.setMessage("Send to about developer command Error -> "+e.getLocalizedMessage()+"\n");
				}

			}
		});
		mnAbout.add(aboutDeveloper);

		// add feedback section to menubar
		shareYourOpinion = new JMenuItem("Feedback");
		shareYourOpinion.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 14));
		// shareYourOpinion.setIcon(new ImageIcon(getClass().getResource("/com/mid/app/icons/manubar_feedback.png")));
		shareYourOpinion.addActionListener(ActionListener -> {

			if (Desktop.isDesktopSupported() && DESKTOP.isSupported(Desktop.Action.MAIL)) {

				try {

					final URI uriMailTo = new URI(
							"mailto:hexa.jeffreybanda@gmail.com?subject=About%20Coder%20MID%20API%20System");
					DESKTOP.mail(uriMailTo);

				} catch (URISyntaxException | IOException e) {
					// logging.setMessage("Send email Error -> "+e.getLocalizedMessage()+"\n");
				}
			}
		});

	}

	private ActionListener doJobAction() {
		final ActionListener listener = (final ActionEvent e) -> {
			// here creating schema and importing tables.
			final DialogFrame frame = new DialogFrame();
			frame.setMessage("Attention !!\nYou are about to run a continuos job!");
			frame.btnYes.addActionListener(ActionListener -> {
				frame.dispose();
				try {

					runJob();
				} catch (final Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			});

			frame.btnNo.addActionListener(ActionListener -> {
				frame.dispose();
			});
			frame.setVisible(true);
		};
		return listener;
	}

}
