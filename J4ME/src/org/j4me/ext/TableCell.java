package org.j4me.ext;

import org.j4me.ui.components.Component;

/**
 * A cell contained in a {@link TableLayout},
 * holding a {@link Component} and the cell
 * layout properties. 
 * 
 * @author Giuseppe Miscione
 *
 */
public class TableCell {
	
	/**
	 * The default value for margins.
	 */
	public static final int DEFAULT_MARGINS[] = {-1, -1, -1, -1};
	
	/**
	 * The default value for paddings.
	 */
	public static final int DEFAULT_PADDIGS[] = {-1, -1, -1, -1};

	/**
	 * The component hold in the cell.
	 */
	private Component component;
	
	/**
	 * The margins of the cell.
	 */
	private int margins[];
	
	/**
	 * The paddings of the cell.
	 */
	private int paddings[];
	
	/**
	 * Builds an empty cell.
	 */
	public TableCell() {
		this(null);
	}
	
	/**
	 * Builds a cell holding the provided component.
	 * 
	 * @param component The {@link Component} object
	 * hold in the cell.
	 */
	public TableCell(Component component) {
		setComponent(component);
		setMargins(DEFAULT_MARGINS);
		setPaddings(DEFAULT_PADDIGS);
	}

	/**
	 * Returns the component hold in the cell.
	 * 
	 * @return The component hold in the cell
	 * or <code>null</code> if the cell is empty.
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * Returns the margins of the cell. If a
	 * margin value is negative, the table-default
	 * value should be used for painting.
	 * 
	 * @return Returns an array with the top, right,
	 * bottom and left margins of the cell.
	 */
	public int[] getMargins() {
		return margins;
	}

	/**
	 * Returns the paddings of the cell. If a
	 * padding value is negative, the table-default
	 * value should be used for painting.
	 * 
	 * @return Returns an array with the top, right,
	 * bottom and left paddings of the cell.
	 */
	public int[] getPaddings() {
		return paddings;
	}

	/**
	 * Sets the component in the cell.
	 * 
	 * @param component The {@link Component} that
	 * will be set in the cell or <code>null</code>
	 * to empty the cell.
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

	/**
	 * Sets the margins value for this cell.
	 * To draw the table-default value of a margin,
	 * specify a negative value for that particular
	 * margin.
	 * 
	 * @param margins An array with the top, right,
	 * bottom and left margins.
	 */
	public void setMargins(int[] margins) {
		this.margins = margins;
	}

	/**
	 * Sets the paddings value for this cell.
	 * To draw the table-default value of a padding,
	 * specify a negative value for that particular
	 * padding.
	 * 
	 * @param margins An array with the top, right,
	 * bottom and left paddings.
	 */
	public void setPaddings(int[] paddings) {
		this.paddings = paddings;
	}
}
