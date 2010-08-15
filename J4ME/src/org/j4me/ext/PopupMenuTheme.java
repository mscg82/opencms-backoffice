package org.j4me.ext;

import org.j4me.ui.Theme;

public interface PopupMenuTheme {

	public static final int[] DEFAULT_MARGINS = {0, 0, 0, 0};
	public static final int[] DEFAULT_MENUBORDERS = {1, 1, 1, 1};
	public static final int DEFAULT_BACKGROUND_COLOR = Theme.BLACK;
	public static final int[] DEFAULT_BORDER_COLORS = {Theme.BLACK, Theme.BLACK, Theme.BLACK, Theme.BLACK};
	public static final int DEFAULT_BACKGROUND_ALPHA = 170;
	public static final int DEFAULT_SELECTED_BACKGROUND_COLOR = Theme.BLUE;
	public static final int DEFAULT_SELECTED_FOREGROUND_COLOR = Theme.WHITE;
	
	/**
	 * Returns the margins, in pixels,
	 * that must be used to draw the popup
	 * menu items.
	 * 
	 * @return An array of integers indicating
	 * the top, right, bottom and left margins.
	 */
	public int[] getItemMargins();
	
	/**
	 * Returns the alpha value of the menu background.
	 * 0 stands for completely transparent, 255 for completely
	 * opaque.
	 * 
	 * @return The alpha value of the menu background.
	 */
	public int getMenuBackgroundAlpha();
	
	/**
	 * Returns the color of the menu background.
	 * 
	 * @return The color of the menu background.
	 */
	public int getMenuBackgroundColor();
	
	/**
	 * Returns an array with the colors of the menu borders.
	 * 
	 * @return An array of integers indicating
	 * the top, right, bottom and left borders colors.
	 */
	public int[] getMenuBorderColors();
	
	/**
	 * Returns the menu borders, in pixels,
	 * that must be used to draw the popup
	 * menu.
	 * 
	 * @return An array of integers indicating
	 * the top, right, bottom and left borders widths.
	 */
	public int[] getMenuBorders();
	
	/**
	 * Returns the color of the background of
	 * the selected menu item. A negative value
	 * stands for transparent.
	 * 
	 * @return The color of the background of
	 * the selected menu item
	 */
	public int getSelectionBackgroundColor();

	/**
	 * Returns the color of the foreground of
	 * the selected menu item.
	 * 
	 * @return The color of the foreground of
	 * the selected menu item
	 */
	public int getSelectionForegroundColor();
}
