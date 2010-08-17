/**
 * 
 */
package org.j4me.ext;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import org.j4me.ui.Theme;
import org.j4me.ui.components.Component;

/**
 * @author Giuseppe Miscione
 *
 */
public class TableLayout extends Component {
	
	/**
	 * A vector with the widths of all the columns
	 * of the table. The number of columns is fixed
	 * when the layout is created.
	 */
	private Vector colWidths;
	
	/**
	 * An array with the row heights.
	 */
	private int[] rowHeights;
	
	/**
	 * A vector of {@link Component}s arrays
	 * with the rows of the table.
	 */
	private Vector rows;
	
	/**
	 * Builds a new TableLayout, specifing the number
	 * of columns and their width. The columns information
	 * is immutable.
	 * 
	 * @param colWidths A {@link Vector} with the columns
	 * widths. The number of elements in the vector
	 * indicates the number of columns in the table.
	 * <p>
	 * Each element in the vector must be a {@link Double} object.
	 * The sum of all the widths will be used as the 100%
	 * and the proportion of each element against the sum
	 * will be the actual percentage width of the column.
	 * </p>
	 * <p>
	 * In example, {0.2, 0.3, 0.5} will build a table with
	 * three columns, the first 20% wide, the second 30%
	 * wide and the last one 50% wide.
	 * </p>
	 */
	public TableLayout(Vector colWidths) {
		this.colWidths = colWidths;
		
		double total = 0.0;
		for(int i = 0, l = this.colWidths.size(); i < l; i++) {
			Double w = (Double)this.colWidths.elementAt(i);
			total += w.doubleValue();
		}
		for(int i = 0, l = this.colWidths.size(); i < l; i++) {
			Double w = (Double)this.colWidths.elementAt(i);
			this.colWidths.setElementAt(
				new Double(w.doubleValue() / total),
				i);
		}
		
		this.rows = new Vector(1, 1);
		
		this.rowHeights = null;
	}
	
	/**
	 * Adds a component to the table.
	 * 
	 * @param row The row in which the component must be placed.
	 * @param column The column in which the component must be placed.
	 * @param component The {@link Component} that will be added.
	 * @throws IndexOutOfBoundsException If the row or column index
	 * fall outside the table actual size.
	 * @throws NullPointerException If component is <code>null</code>
	 */
	public void addComponent(int row, int column, Component component)
			throws IndexOutOfBoundsException, NullPointerException {
		if(component == null)
			throw new NullPointerException("Component cannot be null");
		Component rowData[] = (Component[])rows.elementAt(row);
		rowData[column] = component;
		this.invalidate();
	}
	
	/**
	 * Adds a row in the table.
	 * 
	 * @return The index of the newly created row.
	 */
	public int addRow() {
		int ret = rows.size();
		rows.addElement(new Component[colWidths.size()]);
		return ret;
	}
	
	/**
	 * Returns the number of columns of the table.
	 * 
	 * @return The number of columns of the table.
	 */
	public int getColumnsCount() {
		return colWidths.size();
	}
	
	/**
	 * Returns a component from the table.
	 * 
	 * @param row The row from which the component will be retrieved.
	 * @param column The column from which the component will be retrieved.
	 * @return The {@link Component} that was retrieved.
	 * @throws IndexOutOfBoundsException If the row or column index
	 * fall outside the table actual size.
	 */
	public Component getComponent(int row, int column) throws IndexOutOfBoundsException {
		Component rowData[] = (Component[])rows.elementAt(row);
		Component ret = rowData[column];
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see org.j4me.ui.components.Component#getPreferredComponentSize(org.j4me.ui.Theme, int, int)
	 */
	protected int[] getPreferredComponentSize(Theme theme, int viewportWidth, int viewportHeight) {
		int size[] = {viewportWidth, 0};
		int widths[] = new int[colWidths.size()];
		for(int i = 0, l = colWidths.size(); i < l; i++) {
			Double w = (Double)this.colWidths.elementAt(i);
			widths[i] = (int)Math.floor(w.doubleValue() * viewportWidth);
		}
		rowHeights = new int[rows.size()];
		for(int i = 0, l = rows.size(); i < l; i++) {
			int rowHeight = 0;
			Component row[] = (Component[])rows.elementAt(i);
			for(int j = 0; j < row.length; j++) {
				Component c = row[j];
				if(c != null) {
					int tmp[] = c.getPreferredSize(theme, widths[j], viewportHeight);
					rowHeight = (int)Math.max(tmp[1], rowHeight);
				}
			}
			rowHeights[i] = rowHeight;
			size[1] += rowHeight;
		}
		return size;
	}
	
	/**
	 * Returns the actual rows count in the table.
	 * 
	 * @return The actual rows count in the table.
	 */
	public int getRowsCount() {
		return rows.size();
	}

	protected void invalidate() {
		rowHeights = null;
		super.invalidate();
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.components.Component#paintComponent(javax.microedition.lcdui.Graphics, org.j4me.ui.Theme, int, int, boolean)
	 */
	protected void paintComponent(Graphics g, Theme theme, int width, int height, boolean selected) {
		int widths[] = new int[colWidths.size()];
		for(int i = 0, l = colWidths.size(); i < l; i++) {
			Double w = (Double)this.colWidths.elementAt(i);
			widths[i] = (int)Math.floor(w.doubleValue() * width);
		}
		int offsetX = 0;
		int offsetY = 0;
		
		// Get the top and bottom of current graphics clip.
		int clipTop = g.getClipY();
		int clipBottom = clipTop + g.getClipHeight();
		
		for(int i = 0, l = rows.size(); i < l; i++) {
			Component row[] = (Component[])rows.elementAt(i);
			offsetX = 0;
			for(int j = 0; j < row.length; j++) {
				Component c = row[j];
				if(c != null) {
					if((offsetY + rowHeights[i] >= clipTop) && (offsetY <= clipBottom)){
						c.visible(true);
						c.paint(g, theme, getScreen(), offsetX, offsetY, widths[j], height, selected);
					}
					else {
						c.visible(false);
					}
				}
				offsetX += widths[j];
			}
			offsetY += rowHeights[i];
		}
	}

	/**
	 * Removes a component from the table and returns it.
	 * 
	 * @param row The row from which the component will be removed.
	 * @param column The column from which the component will be removed.
	 * @return The {@link Component} that was removed.
	 * @throws IndexOutOfBoundsException If the row or column index
	 * fall outside the table actual size.
	 */
	public Component removeComponent(int row, int column) throws IndexOutOfBoundsException {
		Component rowData[] = (Component[])rows.elementAt(row);
		Component ret = rowData[column];
		rowData[column] = null;
		return ret;
	}

}
