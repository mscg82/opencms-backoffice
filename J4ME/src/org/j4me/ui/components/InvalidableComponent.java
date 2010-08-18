/**
 * 
 */
package org.j4me.ui.components;

import javax.microedition.lcdui.Graphics;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.Theme;

/**
 * This class is a {@link Component} decorator
 * and allows users to freely invalidate the
 * underlying component. 
 * 
 * @author Giuseppe Miscione
 *
 */
public class InvalidableComponent extends Component {
	
	private Component component;

	public InvalidableComponent(Component component) {
		super();
		this.component = component;
	}

	public boolean acceptsInput() {
		return component.acceptsInput();
	}

	public boolean equals(Object obj) {
		return component.equals(obj);
	}

	public Component getComponent() {
		return component;
	}

	public int getHeight() {
		return component.getHeight();
	}

	public int getHorizontalAlignment() {
		return component.getHorizontalAlignment();
	}

	protected int[] getPreferredComponentSize(Theme theme, int viewportWidth, int viewportHeight) {
		return component.getPreferredComponentSize(theme, viewportWidth, viewportHeight);
	}

	public DeviceScreen getScreen() {
		return component.getScreen();
	}

	public int getWidth() {
		return component.getWidth();
	}

	public int getX() {
		return component.getX();
	}

	public int getY() {
		return component.getY();
	}

	public int hashCode() {
		return component.hashCode();
	}

	protected void hideNotify() {
		component.hideNotify();
	}

	protected void invalidate() {
		component.invalidate();
	}

	public void invalidateComponent() {
		component.invalidate();
	}

	public boolean isShown() {
		return component.isShown();
	}

	public void keyPressed(int keyCode) {
		component.keyPressed(keyCode);
	}

	public void keyReleased(int keyCode) {
		component.keyReleased(keyCode);
	}

	public void keyRepeated(int keyCode) {
		component.keyRepeated(keyCode);
	}

	protected void paintComponent(Graphics g, Theme theme, int width, int height, boolean selected) {
		component.paintComponent(g, theme, width, height, selected);
	}

	public void pointerDragged(int x, int y) {
		component.pointerDragged(x, y);
	}

	public void pointerPressed(int x, int y) {
		component.pointerPressed(x, y);
	}

	public void pointerReleased(int x, int y) {
		component.pointerReleased(x, y);
	}

	public void repaint() {
		component.repaint();
	}

	public void setHorizontalAlignment(int alignment) {
		component.setHorizontalAlignment(alignment);
	}

	protected void showNotify() {
		component.showNotify();
	}

	public String toString() {
		return component.toString();
	}
	
	public void visible(boolean visible) {
		component.visible(visible);
	}

}
