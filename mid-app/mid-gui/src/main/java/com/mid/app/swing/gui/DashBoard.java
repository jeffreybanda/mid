package com.mid.app.swing.gui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.http.auth.AuthenticationException;

import com.mid.app.http.utils.ConnectionUtils;

public class DashBoard {

	public static void main(final String[] args) {

		try {
			System.setProperty("PropFile", "/milleniumLiveConfig.properties");
			System.setProperty("BenefitsFile", "/milleniumBenefitsStructure.properties");

			ConnectionUtils.checkConnectionToPortal();
		} catch (AuthenticationException e) {

			e.printStackTrace();
		}

		try {
			System.setProperty("PropFile", "/milleniumLiveConfig.properties");
			System.setProperty("BenefitsFile", "/milleniumBenefitsStructure.properties");

			System.out.println(System.getProperty("PropFile"));
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {

		}

		new MainFrame();
	}

}
