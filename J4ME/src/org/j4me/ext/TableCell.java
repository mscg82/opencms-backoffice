package org.j4me.ext;

import java.util.Vector;

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
	 * The default values for cell borders
	 */
	public static final int DEFAULT_BORDERS[] = {-1, -1, -1, -1};
	
	/**
	 * The components hold in the cell.
	 */
	private Vector components;
	
	/**
	 * The heights of the components hold in the cell.
	 */
	private Vector componentsHeights;
	
	/**
	 * The margins of the cell.
	 */
	private int margins[];
	
	/**
	 * The paddings of the cell.
	 */
	private int paddings[];
	
	/**
	 * The borders of the cell
	 */
	private int borders[];
	
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
		components = new Vector(1);
		componentsHeights = new Vector(1);
		addComponent(component);
		setMargins(DEFAULT_MARGINS);
		setPaddings(DEFAULT_PADDIGS);
		setBorders(DEFAULT_BORDERS);
	}

	/**
	 * Adds the component in the cell.
	 * 
	 * @param component The {@link Component} that
	 * will be set in the cell.
	 */
	public void addComponent(Component component) {
		if(component != null) {
			this.components.addElement(component);
			this.componentsHeights.addElement(new Integer(0));
		}
	}

	/**
	 * Removes all the elements from this cell.
	 */
	public void clearComponents() {
		this.components = new Vector();
	}

	/**
	 * Returns the border colors of the cell. If a
	 * border value is negative, this border won't be painted.
	 * 
	 * @return Returns an array with the top, right,
	 * bottom and left border colors of the cell.
	 */
	public int[] getBorders() {
		return borders;
	}

	/**
	 * Returns the elements at the specified index
	 * stored in this cell.
	 * 
	 * @param index The index of the element.
	 * @return The elements at the specified index
	 * stored in this cell.
	 * @throws IndexOutOfBoundsException If the index falls
	 * outside the vector size.
	 */
	public Component getComponent(int index) throws IndexOutOfBoundsException {
		return (Component)components.elementAt(index);
	}

	/**
	 * Returns the element height at the specified index.
	 * @param index The index of the element whose height will be
	 * retrieved.
	 * @return The element height at the specified index.
	 * @throws IndexOutOfBoundsException If the idnex falls outside
	 * the size of the vector.
	 */
	public int getComponentHeight(int index) throws IndexOutOfBoundsException {
		return ((Integer)this.componentsHeights.elementAt(index)).intValue();
	}

	/**
	 * Returns the components hold in the cell.
	 * 
	 * @return The components hold in the cell
	 * or a void vector if the cell is empty.
	 */
	public Vector getComponents() {
		return components;
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
	 * Sets the border colors value for this cell.
	 * A negative value of a border color makes this
	 * border transparent..
	 * 
	 * @param margins An array with the top, right,
	 * bottom and left border colors.
	 * 
	 * @return this cell object, so that multiple
	 * layout setting calls can be chianed.
	 */
	public TableCell setBorders(int[] borders) {
		this.borders = borders;
		return this;
	}
	
	/**
	 * Sets the element height at the specified index.
	 * 
	 * @param index The index of the component.
	 * @param height The height of the component.
	 * 
	 * @return this cell object, so that multiple
	 * layout setting calls can be chianed.
	 */
	public TableCell setComponentsHeight(int index, int height) {
		this.componentsHeights.setElementAt(new Integer(height), index);
		return this;
	}
	
	/**
	 * Sets the margins value for this cell.
	 * To draw the table-default value of a margin,
	 * specify a negative value for that particular
	 * margin.
	 * 
	 * @param margins An array with the top, right,
	 * bottom and left margins.
	 * 
	 * @return this cell object, so that multiple
	 * layout setting calls can be chianed.
	 */
	public TableCell setMargins(int[] margins) {
		this.margins = margins;
		return this;
	}
	
	/**
	 * Sets the paddings value for this cell.
	 * To draw the table-default value of a padding,
	 * specify a negative value for that particular
	 * padding.
	 * 
	 * @param margins An array with the top, right,
	 * bottom and left paddings.
	 * 
	 * @return this cell object, so that multiple
	 * layout setting calls can be chianed.
	 */
	public TableCell setPaddings(int[] paddings) {
		this.paddings = paddings;
		return this;
	}
}
