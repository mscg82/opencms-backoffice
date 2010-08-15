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
		leftMenuItems = new Vector();
		rightMenuItems = new Vector();
		
		leftMenuOpened = false;
		rigthMenuOpened = false;
		
		menuItemSelected = 0;
	}
	
	protected void acceptNotify() {
		if(!leftMenuOpened && !rigthMenuOpened) {
			if(rightMenuItems.size() == 1) {
				// only one element in menu, execute it
				((MenuOption)rightMenuItems.elementAt(0)).select();
			}
			else {
				// show rigth button menu
				rigthMenuOpened = true;
				menuItemSelected = 0;
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
		leftMenuItems.addElement(item);
	}
	
	public void addRightItem(MenuOption item) {
		rightMenuItems.addElement(item);
	}

	protected void declineNotify() {
		if(!leftMenuOpened && !rigthMenuOpened) {
			if(leftMenuItems.size() == 1) {
				// only one element in menu, execute it
				((MenuOption)leftMenuItems.elementAt(0)).select();
			}
			else {
				// show left button menu
				leftMenuOpened = true;
				menuItemSelected = 0;
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
		int bgColor = PopupMenuTheme.DEFAULT_BACKGROUND_COLOR;
		int bgAlpha = PopupMenuTheme.DEFAULT_BACKGROUND_ALPHA;
		int selBgColor = PopupMenuTheme.DEFAULT_SELECTED_BACKGROUND_COLOR;
		int selFgColor = PopupMenuTheme.DEFAULT_SELECTED_FOREGROUND_COLOR;
		int margins[] = PopupMenuTheme.DEFAULT_MARGINS;
		int borders[] = PopupMenuTheme.DEFAULT_MENUBORDERS;
		int bordersColors[] = PopupMenuTheme.DEFAULT_BORDER_COLORS;
		if(theme instanceof PopupMenuTheme) {
			bgColor = ((PopupMenuTheme)theme).getMenuBackgroundColor();
			bgAlpha = ((PopupMenuTheme)theme).getMenuBackgroundAlpha();
			selBgColor = ((PopupMenuTheme)theme).getSelectionBackgroundColor();
			selFgColor = ((PopupMenuTheme)theme).getSelectionForegroundColor();
			margins = ((PopupMenuTheme)theme).getItemMargins();
			borders = ((PopupMenuTheme)theme).getMenuBorders();
			bordersColors = ((PopupMenuTheme)theme).getMenuBorderColors();
		}

		int menuTop = getScreenHeight() - theme.getMenuHeight() - size[1] - borders[0] - borders[2];

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

		for(int i = 0, l = menu.size(); i < l; i++) {
			MenuOption el = (MenuOption)menu.elementAt(i);
			int elY = menuTop + i * elHeight;
			if(i == menuItemSelected) {
				if(selBgColor > 0) {
					g.setColor(selBgColor);
					g.fillRect(left ? borders[0] : getScreenWidth() - borders[1] - size[0], elY, size[0], elHeight);
				}
				g.setColor(selFgColor);
			}
			else {
				g.setColor(theme.getFontColor());
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
						if(el.acceptsInput()) {
							menuItemSelected = i;
							break;
						}
					}
				}
				else {
					// select previous highlightable element
					for(int i = ((menuItemSelected + count - 1) % count); i != menuItemSelected; i = ((i + count - 1) % count)) {
						MenuOption el = (MenuOption)menu.elementAt(i);
						if(el.acceptsInput()) {
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
		super.keyRepeated(keyCode);
	}

	protected int[] getMenuSize(Vector menu) {
		int ret[] = {0, 0};
		Theme theme = UIManager.getTheme();
		int margins[] = PopupMenuTheme.DEFAULT_MARGINS;
		if(theme instanceof PopupMenuTheme) {
			margins = ((PopupMenuTheme)theme).getItemMargins();
		}
		int maxWidth = getMaxMenuItemLength(menu);
		ret[0] = margins[1] + maxWidth + margins[3];
		ret[1] = (margins[0] + theme.getFont().getHeight() + margins[2]) * menu.size();
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
			leftText = ((MenuOption)leftMenuItems.elementAt(0)).getLabel();
		} catch(Exception e){}
		try {
			rightText = ((MenuOption)rightMenuItems.elementAt(0)).getLabel();
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
