package com.mid.app.swing.utils;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

public class JFormattedDateTextField extends JFormattedTextField {
	Format format = new SimpleDateFormat("MM/dd/yyyy");

	public JFormattedDateTextField() {
		super();
		MaskFormatter maskFormatter = null;
		try {
			maskFormatter = new MaskFormatter("##/##/####");
		} catch (final ParseException e) {
			e.printStackTrace();
		}

		maskFormatter.setPlaceholderCharacter('_');
		setFormatterFactory(new DefaultFormatterFactory(maskFormatter));
		this.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				if (getFocusLostBehavior() == JFormattedTextField.PERSIST)
					setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
			}

			@Override
			public void focusLost(final FocusEvent e) {
				try {
					final Date date = (Date) format.parseObject(getText());
					setValue(format.format(date));
				} catch (final ParseException pe) {
					setFocusLostBehavior(JFormattedTextField.PERSIST);
					setText("");
					setValue(null);
				}
			}
		});
	}

	public void setValue(final Date date) {
		super.setValue(toString(date));
	}

	private Date toDate(final String sDate) {
		Date date = null;
		if (sDate == null)
			return null;
		try {
			date = (Date) format.parseObject(sDate);
		} catch (final ParseException pe) {
			// ignore
		}

		return date;
	}

	private String toString(final Date date) {
		try {
			return format.format(date);
		} catch (final Exception e) {
			return "";
		}
	}
}