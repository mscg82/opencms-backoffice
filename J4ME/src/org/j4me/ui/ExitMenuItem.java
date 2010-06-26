/**
 * 
 */
package org.j4me.ui;

import javax.microedition.midlet.MIDlet;

/**
 * This class implements the &quot;Exit&quot;
 * voice in application menu. This item will
 * make the application exit.
 * 
 * @author Giuseppe Miscione
 *
 */
public class ExitMenuItem implements MenuItem {
	
	private MIDlet midlet;
	private String text;
	
	public ExitMenuItem() {
		init(null, null);
	}
	
	public ExitMenuItem(MIDlet midlet) {
		init(midlet, null);
	}
	
	public ExitMenuItem(MIDlet midlet, String text) {
		init(midlet, text);
	}
	
	public ExitMenuItem(String text) {
		init(null, text);
	}
	
	public String getText() {
		return text;
	}

	private void init(MIDlet midlet, String text) {
		if(midlet != null)
			this.midlet = midlet;
		else
			this.midlet = UIManager.getMidlet();
		
		if(text != null)
			this.setText(text);
		else
			this.setText("Exit");
	}
	
	public void onSelection() {
		midlet.notifyDestroyed();
	}

	public void setText(String text) {
		this.text = text;
	}

}
