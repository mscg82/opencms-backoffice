/**
 * 
 */
package main;

import java.util.Random;

import javax.microedition.lcdui.Font;

import radui.Callback;
import radui.CallbackChainer;
import radui.MenuTheme;
import radui.MenuView;
import radui.ScreenCanvas;
import sh.CommandPoints;

/**
 * @author Giuseppe Miscione
 *
 */
public class CommandPointsView extends MenuView {

	private ScreenCanvas sc_;
	private CommandPoints cp_;
	private Random r_;
    
	public CommandPointsView(ScreenCanvas sc, MenuTheme mt, CommandPoints cp,
			Random r, Callback back) {
		
		super(mt);
		sc_ = sc;
		cp_ = cp;
		r_ = r;
		
		Font labelFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
		Font menuFont = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
		
		String message = "You will have " + cp.get();
		add(message, null, labelFont);
		message = "command points.";
		add(message, null, labelFont);
		message = "Do you want to re-roll";
		add(message, null, labelFont);
		message = "or accept points?";
		add(message, null, labelFont);
		
		add(null, null, labelFont);
		
		add("Re-roll", new CallbackChainer(new RerollPointsCallback(), back), menuFont);
		add("Accept", back, menuFont);
		
	}
	
	private class RerollPointsCallback implements Callback {

		public void perform() {
			cp_.reset(r_);
			sc_.repaint();
		}
		
	}

}
