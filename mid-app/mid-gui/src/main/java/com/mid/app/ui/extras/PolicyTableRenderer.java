/**
	 * @author Coder ACJHP
	 * @Email hexa.octabin@gmail.com
	 * @Date 15/07/2017
	 */
package com.mid.app.ui.extras;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PolicyTableRenderer extends DefaultTableCellRenderer {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
			final boolean hasFocus,
			final int row, final int column) {

		final Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);

		final Object colrowVal = table.getModel().getValueAt(row, column);

		if (isSelected || hasFocus) {
			cellComponent.setBackground(Color.decode("#10d6d1"));
		}

		else if (column == 0) {
			cellComponent.setBackground(Color.decode("#effbad"));
		}

		else if (column == 2) {
			// final String trimmed = colrowVal.toString().substring(0, colrowVal.toString().length() - 1);
			// float val = Float.parseFloat(trimmed);
			// if(val > 75f) {
			// cellComponent.setBackground(Color.decode("#ffcdd5"));
			// }

		}

		else if (column == 3) {
			// final String trimmed = colrowVal.toString().substring(0, colrowVal.toString().length() - 1);
			// float val = Float.parseFloat(trimmed);
			// if(val > 25f) {
			// cellComponent.setBackground(Color.decode("#baffc6"));
			// }

		}

		else if (column == 5) {
			// if (Integer.parseInt(colrowVal.toString()) > 0) {
			// cellComponent.setBackground(Color.decode("#dc143c"));
			// }

		}

		else {
			cellComponent.setBackground(table.getBackground());
		}

		return cellComponent;
	}

}
