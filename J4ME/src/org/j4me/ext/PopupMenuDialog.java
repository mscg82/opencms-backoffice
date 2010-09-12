/**
 * 
 */
package org.j4me.ext;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.j4me.ui.Dialog;
import org.j4me.ui.Theme;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.Component;
import org.j4me.ui.components.MenuOption;

/**
 * This class implements a dialog with
 * popup menus shown when the action buttons
 * are pressed.
 * 
 * @author Giuseppe Miscione
 *
 */
public class PopupMenuDialog extends Dialog {
	
	private static Image createRGBImage(int rgbPixel, int width, int height, int alpha) {
        int[] rgb = new int[width*height];
        int color = alpha * 0x01000000 + rgbPixel;
        for (int i = 0; i < width * height; ++i)
            rgb[i] = color;
        return Image.createRGBImage(rgb, width, height, true);
    }
	
	protected Vector leftMenuItems;
	protected Vector rightMenuItems;
	
	protected boolean leftMenuOpened;
	protected boolean rigthMenuOpened;
	
	protected int menuItemSelected;

	/**
	 * Create a new dialog with popup menus
	 */
	public PopupMenuDialog() {
		this(null);
	}
	
	/**
	 * Create a new dialog with popup menus
	 * 
	 * @param name is the title for this menu, for example "Main Menu".  It
	 *  appears at the top of the screen in the title area.
	 */
	public PopupMenuDialog(String name) {
		setTitle(name);
		
		leftMenuItems = new Vector();
		rightMenuItems = new Vector();
		
		leftMenuOpened = false;
		rigthMenuOpened = false;
		
		menuItemSelected = 0;
	}

	protected void acceptNotify() {
		if(getRightMenuText() == null)
			return;
		if(!leftMenuOpened && !rigthMenuOpened) {
			if(rightMenuItems.size() == 0) {
				Component selected = get(getSelected());
				if(selected.acceptsInput()) {
					selected.keyPressed(FIRE);
				}
			}
			else if(rightMenuItems.size() == 1) {
				// only one element in menu, execute it
				((MenuOption)rightMenuItems.elementAt(0)).select();
			}
			else {
				// show rigth button menu
				rigthMenuOpened = true;
				selectFirst(rightMenuItems);
			}
		}
		else {
			leftMenuOpened = false;
			rigthMenuOpened = false;
		}
		invalidate();
		repaint();
	}
	
	public void addLeftItem(MenuOption item) {
		item.visible(true);
		leftMenuItems.addElement(item);
	}
	
	public void insertLeftItem(MenuOption item, int position) {
		item.visible(true);
		leftMenuItems.insertElementAt(item, position);
	}
	
	public void addRightItem(MenuOption item) {
		item.visible(true);
		rightMenuItems.addElement(item);
	}
	
	public void insertRightItem(MenuOption item, int position) {
		item.visible(true);
		rightMenuItems.insertElementAt(item, position);
	}

	protected void declineNotify() {
		if(getLeftMenuText() == null)
			return;
		if(!leftMenuOpened && !rigthMenuOpened) {
			if(leftMenuItems.size() == 1) {
				// only one element in menu, execute it
				((MenuOption)leftMenuItems.elementAt(0)).select();
			}
			else {
				// show left button menu
				leftMenuOpened = true;
				selectFirst(leftMenuItems);
			}
		}
		else {
			leftMenuOpened = false;
			rigthMenuOpened = false;
		}
		invalidate();
		repaint();
	}

	protected void drawMenu(Graphics g, Vector menu, boolean left) {
		int size[] = getMenuSize(menu);
		
		Theme theme = UIManager.getTheme();
		int fontColor = theme.getFontColor();
		int bgColor = PopupMenuTheme.DEFAULT_BACKGROUND_COLOR;
		int bgAlpha = PopupMenuTheme.DEFAULT_BACKGROUND_ALPHA;
		int selBgColor = PopupMenuTheme.DEFAULT_SELECTED_BACKGROUND_COLOR;
		int selFgColor = PopupMenuTheme.DEFAULT_SELECTED_FOREGROUND_COLOR;
		int margins[] = PopupMenuTheme.DEFAULT_MARGINS;
		int borders[] = PopupMenuTheme.DEFAULT_MENUBORDERS;
		int bordersColors[] = PopupMenuTheme.DEFAULT_BORDER_COLORS;
		if(theme instanceof PopupMenuTheme) {
			fontColor = ((PopupMenuTheme)theme).getPopupMenuFontColor();
			bgColor = ((PopupMenuTheme)theme).getMenuBackgroundColor();
			bgAlpha = ((PopupMenuTheme)theme).getMenuBackgroundAlpha();
			selBgColor = ((PopupMenuTheme)theme).getSelectionBackgroundColor();
			selFgColor = ((PopupMenuTheme)theme).getSelectionForegroundColor();
			margins = ((PopupMenuTheme)theme).getItemMargins();
			borders = ((PopupMenuTheme)theme).getMenuBorders();
			bordersColors = ((PopupMenuTheme)theme).getMenuBorderColors();
		}

		int menuTop = getScreenHeight() - theme.getMenuHeight() - size[1] - borders[0] - borders[2];
		if(this.getTitle() != null && this.getTitle().length() > 0) {
			menuTop -= theme.getTitleHeight();
		}

		// draw the borders
		g.setColor(bgColor);
		int offset = 0;
		if(!left) {
			offset = getScreenWidth() - borders[1] - size[0] - borders[3];
		}
		g.setColor(bordersColors[0]);
		g.fillRect(offset, menuTop, borders[3] + size[0] + borders[1], borders[0]); // top border
		g.setColor(bordersColors[1]);
		g.fillRect(offset + borders[3] + size[0], menuTop, borders[1], borders[0] + size[1] + borders[2]); // right border
		g.setColor(bordersColors[2]);
		g.fillRect(offset, menuTop + borders[0] + size[1], borders[3] + size[0] + borders[1], borders[2]); // bottom border
		g.setColor(bordersColors[3]);
		g.fillRect(offset, menuTop, borders[3], borders[0] + size[1] + borders[2]); // left border			
		g.setColor(bgColor);
		
		menuTop += borders[0];
		// draw the background
		Image background = createRGBImage(bgColor, size[0], size[1], bgAlpha);
		if(left)
			g.drawImage(background, borders[3], menuTop, Graphics.TOP | Graphics.LEFT);
		else
			g.drawImage(background, offset + borders[3], menuTop, Graphics.TOP | Graphics.LEFT);
		
		// Draw the items
		int elHeight = margins[0] + theme.getFont().getHeight() + margins[2];

		int yOffset = 0;
		for(int i = 0, l = menu.size(); i < l; i++) {
			MenuOption el = (MenuOption)menu.elementAt(i);
			if(!el.isShown())
				continue;
			int elY = menuTop + yOffset;
			yOffset += elHeight;
			if(i == menuItemSelected) {
				if(selBgColor > 0) {
					g.setColor(selBgColor);
					g.fillRect(left ? borders[0] : getScreenWidth() - borders[1] - size[0], elY, size[0], elHeight);
				}
				g.setColor(selFgColor);
			}
			else {
				g.setColor(fontColor);
			}
			if(left)
				g.drawString(el.getLabel(), borders[0] + margins[3], elY + margins[0], Graphics.TOP | Graphics.LEFT);
			else
				g.drawString(el.getLabel(), getScreenWidth() - borders[1] - margins[1], elY + margins[0], Graphics.TOP | Graphics.RIGHT);
		}
		
	}

	public Vector getLeftMenuItems() {
		return leftMenuItems;
	}
	
	protected int getMaxMenuItemLength(Vector menu) {
		int ret = 0;
		Theme theme = UIManager.getTheme();
		for(int i = 0, l = menu.size(); i < l; i++) {
			MenuOption item = (MenuOption)menu.elementAt(i);
			int size = theme.getFont().stringWidth(item.getLabel());
			if(ret < size) {
				ret = size;
			}
		}
		return ret;
	}
	
	protected void selectFirst(Vector menu) {
		menuItemSelected = getFirstSelectable(menu);
	}
	
	protected int getFirstSelectable(Vector menu) {
		for(int i = 0, l = menu.size(); i < l; i++) {
			MenuOption el = (MenuOption)menu.elementAt(i);
			if(el.isShown() && el.acceptsInput()) {
				return i;
			}
		}
		return -1;
	}
	
	protected void keyPressed(int keyCode) {
		if(keyCode == Dialog.DOWN || keyCode == Dialog.UP || keyCode == Dialog.FIRE) {
			if(!leftMenuOpened && !rigthMenuOpened) {
				super.keyPressed(keyCode);
			}
			else {
				Vector menu = (leftMenuOpened ? leftMenuItems : rightMenuItems);
				int count = menu.size();
				if(keyCode == Dialog.FIRE) {
					MenuOption el = (MenuOption)menu.elementAt(menuItemSelected);
					el.select();
					leftMenuOpened = false;
					rigthMenuOpened = false;
				}
				else if(keyCode == Dialog.DOWN) {
					// select next highlightable element
					for(int i = ((menuItemSelected + 1) % count); i != menuItemSelected; i = ((i + 1) % count)) {
						MenuOption el = (MenuOption)menu.elementAt(i);
						if(el.isShown() && el.acceptsInput()) {
							menuItemSelected = i;
							break;
						}
					}
				}
				else {
					// select previous highlightable element
					for(int i = ((menuItemSelected + count - 1) % count); i != menuItemSelected; i = ((i + count - 1) % count)) {
						MenuOption el = (MenuOption)menu.elementAt(i);
						if(el.isShown() && el.acceptsInput()) {
							menuItemSelected = i;
							break;
						}
					}
				}
				
				invalidate();
				repaint();
			}
		}
		else
			super.keyPressed(keyCode);
	}

	protected void keyRepeated(int keyCode) {
		//keyPressed(keyCode);
		super.keyRepeated(keyCode);
	}
	
	protected int[] getMenuSize(Vector menu) {
		int ret[] = {0, 0};
		Theme theme = UIManager.getTheme();
		int margins[] = PopupMenuTheme.DEFAULT_MARGINS;
		String minWidth = PopupMenuTheme.DEFAULT_MIN_WIDTH;
		if(theme instanceof PopupMenuTheme) {
			margins = ((PopupMenuTheme)theme).getItemMargins();
			minWidth = ((PopupMenuTheme)theme).getMinWidth();
		}
		
		int menuSize = 0;
		for(int i = 0, l = menu.size(); i < l; i++) {
			MenuOption el = (MenuOption)menu.elementAt(i);
			if(el.isShown()) menuSize++;
		}
		
		int maxWidth = getMaxMenuItemLength(menu);
		ret[0] = margins[1] + maxWidth + margins[3];
		int targetWidth = 0;
		if(minWidth.equals(PopupMenuTheme.DEFAULT_MIN_WIDTH)) {
			// auto width, do nothing
		}
		else if(minWidth.endsWith("%")) {
			double widthPerc = Double.parseDouble(minWidth.substring(0, minWidth.length() - 1));
			targetWidth = (int)Math.floor((getWidth() * widthPerc) / 100);
		}
		else if(minWidth.endsWith("px")) {
			targetWidth = Integer.parseInt(minWidth.substring(0, minWidth.length() - 2));
		}
		else {
			throw new IllegalArgumentException("Illegal value for menu minimum width");
		}
		if(ret[0] < targetWidth)
			ret[0] = targetWidth;
		ret[1] = (margins[0] + theme.getFont().getHeight() + margins[2]) * menuSize;
		return ret;
	}
	
	public Vector getRightMenuItems() {
		return rightMenuItems;
	}

	protected synchronized void paint(Graphics g) {
		super.paint(g);
		if(leftMenuOpened) {
			drawMenu(g, leftMenuItems, true);
		}
		if(rigthMenuOpened) {
			drawMenu(g, rightMenuItems, false);
		}
	}

	public void setDefaultMenuText() {
		String leftText = null;
		String rightText = null;
		try {
			leftText = ((MenuOption)leftMenuItems.elementAt(getFirstSelectable(leftMenuItems))).getLabel();
		} catch(Exception e){}
		try {
			rightText = ((MenuOption)rightMenuItems.elementAt(getFirstSelectable(rightMenuItems))).getLabel();
		} catch(Exception e){}
		setMenuText(leftText, rightText);
	}

	public void setLeftMenuItems(Vector leftMenuItems) {
		this.leftMenuItems = leftMenuItems;
	}

	public void setRightMenuItems(Vector rightMenuItems) {
		this.rightMenuItems = rightMenuItems;
	}

}
