package com.mid.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mid.app.ui.external.InformationFrame;

public class PropertiesReader {

	private String userName;
	private String password;
	private String companyName;
	final InputStream inputStream;
	// private static LoggingEngine logging;

	final static String FILE_PATH = "/src/main/resources/Credentials.properties";

	public PropertiesReader() {

		// logging = LoggingEngine.getInstance();

		final Properties properties = new Properties();

		inputStream = PropertiesReader.class.getResourceAsStream(FILE_PATH);
		try {

			properties.load(inputStream);

		} catch (final IOException ex) {

			// logging.setMessage("PropertiesReader : " + ex.getLocalizedMessage());
			final InformationFrame frame = new InformationFrame();
			frame.setMessage(ex.getLocalizedMessage());
			frame.setVisible(true);
		}

		userName = properties.getProperty("system.username");
		password = properties.getProperty("system.password");
		companyName = properties.getProperty("system.hotelname");
	}

	public boolean checkIsAdministrator(final String inputName, final String inputPwd) {
		// logging.setMessage("PropertiesReader -> checking System moderator credentials...");
		return true;
	}

	public String getCompanyName() {
		return companyName;
	}

}
