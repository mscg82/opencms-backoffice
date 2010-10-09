package com.classmeteo.theme;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import org.j4me.ext.BackgroundImageTheme;
import org.j4me.ext.PopupMenuTheme;
import org.j4me.ui.Theme;

public class ClassMeteoAppTheme extends BackgroundImageTheme implements PopupMenuTheme {
	
	private static final int[] margins = {2, 3, 2, 3};
//	private static final int[] borderColors = {
//		PopupMenuTheme.DEFAULT_BORDER_COLOR,
//		PopupMenuTheme.DEFAULT_BORDER_COLOR,
//		PopupMenuTheme.DEFAULT_BORDER_COLOR,
//		PopupMenuTheme.DEFAULT_BORDER_COLOR};
//	private static final int[] borderColors = {
//		Theme.RED,
//		Theme.BLUE,
//		Theme.GREEN,
//		Theme.YELLOW};
	
	private Font font;
	private Font bold;
	
	/**
	 * Creates a new instance of a {@link ClassMeteoAppTheme}.
	 */
	public ClassMeteoAppTheme() {
		super(null);
		Font base = Font.getDefaultFont();
		int face = base.getFace();
		
		font = Font.getFont( face, Font.STYLE_PLAIN, Font.SIZE_SMALL );
		bold = Font.getFont( font.getFace(), Font.STYLE_BOLD, font.getSize() );
		
//		try {
//			Image bkg = Image.createImage("/img/bkg_with_logo.png");
//			setImage(bkg);
//			setBackgroundPosition(BackgroundImageTheme.FULLSCREEN);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * Creates a new instance of a {@link ClassMeteoAppTheme}.
	 * @param image The image that will be used as background.
	 */
	public ClassMeteoAppTheme(Image image) {
		super(image, 0);
	}

	/**
	 * Creates a new instance of a {@link ClassMeteoAppTheme}.
	 * @param image The image that will be used as background.
	 * @param position A combination of flags that indicates how
	 * to place and draw the background.
	 */
	public ClassMeteoAppTheme(Image image, int position) {
		super(image, position);
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * @return the bold font
	 */
	public Font getBoldFont() {
		return bold;
	}

	public int[] getItemMargins() {
		return margins;
	}

	public int getMenuBackgroundAlpha() {
		return PopupMenuTheme.DEFAULT_BACKGROUND_ALPHA;
	}

	public int getMenuBackgroundColor() {
		return Theme.BLACK;
	}

	public int[] getMenuBorderColors() {
		return PopupMenuTheme.DEFAULT_BORDER_COLORS;
	}

	public int[] getMenuBorders() {
		return PopupMenuTheme.DEFAULT_MENUBORDERS;
	}

	public String getMinWidth() {
		return "auto";
	}

	public int getPopupMenuFontColor() {
		return Theme.LIGHT_BLUE;
	}

	public int getSelectionBackgroundColor() {
		return PopupMenuTheme.DEFAULT_SELECTED_BACKGROUND_COLOR;
	}

	public int getSelectionForegroundColor() {
		return PopupMenuTheme.DEFAULT_SELECTED_FOREGROUND_COLOR;
	}

//	public int getFontColor() {
//		return Theme.WHITE;
//	}
	
	public int getTextBoxColor() {
		return Theme.BLACK;
	}

//	public int getHighlightColor() {
//		return Theme.BLACK;
//	}

}
