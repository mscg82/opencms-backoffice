/**
 * 
 */
package org.j4me.ext;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import org.j4me.ui.Theme;
import org.j4me.ui.components.Component;
import org.j4me.ui.components.InvalidableComponent;

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
	private int rowHeights[];
	
	/**
	 * The size of the component.
	 */
	private int size[];
	
	/**
	 * A vector of {@link TableCell}s arrays
	 * with the rows of the table.
	 */
	private Vector rows;
	
	/**
	 * The default value of cell margins.
	 */
	private int cellMargin;
	
	/**
	 * The default value of cell paddings.
	 */
	private int cellPadding;
	
	/**
	 * A boolean switch that indicates if a bulk update
	 * is in progress.
	 */
	private boolean bulkUpdate;
	
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
		this.size = null;
		this.bulkUpdate = false;
		this.cellMargin = 0;
		this.cellPadding = 0;
	}
	
	/**
	 * Adds a component to the table.
	 * 
	 * @param row The row in which the component must be placed.
	 * @param column The column in which the component must be placed.
	 * @param component The {@link Component} that will be added.
	 * @return The {@link TableCell} holding the provided element.
	 * @throws IndexOutOfBoundsException If the row or column index
	 * fall outside the table actual size.
	 * @throws NullPointerException If component is <code>null</code>
	 */
	public TableCell addComponent(int row, int column, Component component)
			throws IndexOutOfBoundsException, NullPointerException {
		if(component == null)
			throw new NullPointerException("Component cannot be null");
		TableCell rowData[] = (TableCell[])rows.elementAt(row);
		TableCell cell = rowData[column];
		cell.setComponent(component);
		this.invalidate();
		return cell;
	}

	/**
	 * Adds a row in the table.
	 * 
	 * @return The index of the newly created row.
	 */
	public int addRow() {
		int ret = rows.size();
		TableCell rowData[] = new TableCell[colWidths.size()];
		for(int i = 0; i < rowData.length; i++) {
			rowData[i] = new TableCell();
		}
		rows.addElement(rowData);
		return ret;
	}

	/**
	 * Terminates a bulk update and invalidates the
	 * whole table so that it can be redrawn.
	 */
	public void endBulkUpdate() {
		bulkUpdate = false;
		invalidate();
	}

	/**
	 * Returns the default value of cell margin.
	 * 
	 * @return The default value of cell margin.
	 */
	public int getCellMargin() {
		return cellMargin;
	}

	/**
	 * Returns the default value of cell padding.
	 * 
	 * @return The default value of cell padding.
	 */
	public int getCellPadding() {
		return cellPadding;
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
	 * Returns a cell from the table.
	 * 
	 * @param row The row from which the component will be retrieved.
	 * @param column The column from which the component will be retrieved.
	 * @return The {@link TableCell} that was retrieved.
	 * @throws IndexOutOfBoundsException If the row or column index
	 * fall outside the table actual size.
	 */
	public TableCell getTableCell(int row, int column) throws IndexOutOfBoundsException {
		TableCell rowData[] = (TableCell[])rows.elementAt(row);
		TableCell ret = rowData[column];
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see org.j4me.ui.components.Component#getPreferredComponentSize(org.j4me.ui.Theme, int, int)
	 */
	protected int[] getPreferredComponentSize(Theme theme, int viewportWidth, int viewportHeight) {
		if(size == null || rowHeights == null) {
			size = new int[]{viewportWidth, 0};
			rowHeights = new int[rows.size()];
			int widths[] = new int[colWidths.size()];
			for(int i = 0, l = colWidths.size(); i < l; i++) {
				Double w = (Double)this.colWidths.elementAt(i);
				widths[i] = (int)Math.floor(w.doubleValue() * viewportWidth);
			}
			for(int i = 0, l = rows.size(); i < l; i++) {
				int rowHeight = 0;
				TableCell row[] = (TableCell[])rows.elementAt(i);
				for(int j = 0; j < row.length; j++) {
					int paddingTop    = (row[j].getPaddings()[0] >= 0 ? row[j].getPaddings()[0] : getCellPadding());
					int paddingRight  = (row[j].getPaddings()[1] >= 0 ? row[j].getPaddings()[1] : getCellPadding());
					int paddingBottom = (row[j].getPaddings()[2] >= 0 ? row[j].getPaddings()[2] : getCellPadding());
					int paddingLeft   = (row[j].getPaddings()[3] >= 0 ? row[j].getPaddings()[3] : getCellPadding());
					Component c = row[j].getComponent();
					if(c != null) {
						int tmp[] = c.getPreferredSize(theme, widths[j] - paddingLeft - paddingRight, viewportHeight);
						rowHeight = (int)Math.max(tmp[1] + paddingTop + paddingBottom, rowHeight);
					}
				}
				rowHeights[i] = rowHeight;
				size[1] += rowHeight;
			}
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
	
	/**
	 * In this implementation, the invalidation is not
	 * propagated if a bulk update in progress.
	 * Use {@link #startBulkUpdate()} and {@link #endBulkUpdate()}
	 * to optime the update of the table.
	 * 
	 * @see Component#invalidate()
	 */
	protected void invalidate() {
		if(!bulkUpdate) {
			rowHeights = null;
			size = null;
			// invalidate all child elements
			for(int i = 0, l = rows.size(); i < l; i++) {
				TableCell row[] = (TableCell[]) rows.elementAt(i);
				for(int j = 0; j < row.length; j++) {
					Component c = row[j].getComponent();
					if(c != null)
						new InvalidableComponent(c).invalidateComponent();
				}
			}
			super.invalidate();
		}
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
			TableCell row[] = (TableCell[])rows.elementAt(i);
			offsetX = 0;
			for(int j = 0; j < row.length; j++) {
				int paddingTop    = (row[j].getPaddings()[0] >= 0 ? row[j].getPaddings()[0] : getCellPadding());
				int paddingRight  = (row[j].getPaddings()[1] >= 0 ? row[j].getPaddings()[1] : getCellPadding());
				int paddingBottom = (row[j].getPaddings()[2] >= 0 ? row[j].getPaddings()[2] : getCellPadding());
				int paddingLeft   = (row[j].getPaddings()[3] >= 0 ? row[j].getPaddings()[3] : getCellPadding());
				Component c = row[j].getComponent();
				if(c != null) {
					if((offsetY + rowHeights[i] >= clipTop) && (offsetY <= clipBottom)){
						c.visible(true);
						c.paint(g, theme, getScreen(),
							offsetX + paddingLeft,
							offsetY + paddingTop,
							widths[j] - paddingLeft - paddingRight,
							rowHeights[i] - paddingTop - paddingBottom,
							selected);
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
		TableCell rowData[] = (TableCell[])rows.elementAt(row);
		TableCell cell = rowData[column];
		Component ret = cell.getComponent(); 
		cell.setComponent(null);
		return ret;
	}

	/**
	 * Removes the last row in the table and returns the components
	 * that where hold in it.
	 * 
	 * @return The array of {@link TableCell}s contained in
	 * the last row.
	 */
	public TableCell[] removeRow() {
		TableCell rowData[] = (TableCell[])rows.elementAt(rows.size() - 1);
		rows.setSize(rows.size() - 1);
		invalidate();
		return rowData;
	}

	/**
	 * Removes a complete row from the table
	 * and returns the components that where hold in it.
	 * 
	 * @param row The row that must be removed.
	 * @return The array of {@link Component}s contained in
	 * the removed row.
	 * 
	 * @throws IndexOutOfBoundsException If the row index
	 * falls outside the table actual size.
	 */
	public TableCell[] removeRow(int row) throws IndexOutOfBoundsException {
		TableCell rowData[] = (TableCell[])rows.elementAt(row);
		rows.removeElementAt(row);
		invalidate();
		return rowData;
	}
	
	/**
	 * Sets the default cell margin and invalidates the table
	 * so it will be redrawn.
	 * 
	 * @param cellMargin The new cell margin value. The value
	 * must be &gt;= 0.
	 */
	public void setCellMargin(int cellMargin) {
		if(cellMargin < 0)
			throw new IllegalArgumentException();
		this.cellMargin = cellMargin;
		invalidate();
	}

	/**
	 * Sets the default cell padding and invalidates the table
	 * so it will be redrawn.
	 * 
	 * @param cellMargin The new cell padding value. The value
	 * must be &gt;= 0.
	 */
	public void setCellPadding(int cellPadding) {
		if(cellPadding < 0)
			throw new IllegalArgumentException();
		this.cellPadding = cellPadding;
		invalidate();
	}
	
	/**
	 * Starts a bulk update and prevents invalidation
	 * of the table to be propagated to contained elements
	 * and/or to the screen. Use {@link #endBulkUpdate()}
	 * to terminate the bulk update and draw the new table.
	 */
	public void startBulkUpdate() {
		bulkUpdate = true;
	}

}
