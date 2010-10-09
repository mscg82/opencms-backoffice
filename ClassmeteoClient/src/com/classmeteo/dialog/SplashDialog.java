package com.classmeteo.dialog;

import java.io.InputStream;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.j4me.ui.Dialog;

import com.classmeteo.data.Settings;
import com.classmeteo.midlet.ClassmeteoMidlet;
import com.mscg.util.Properties;

public class SplashDialog extends Dialog {

	private Image splash;
	
	public SplashDialog() {
		try {
			splash = Image.createImage("/img/splash.png");
			setFullScreenMode(true);
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.Dialog#paint(javax.microedition.lcdui.Graphics)
	 */
	protected synchronized void paint(Graphics g) {
		g.drawImage(splash, 0, 0, Graphics.TOP | Graphics.LEFT);
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.DeviceScreen#showNotify()
	 */
	public void showNotify() {
		super.showNotify();
		new Thread() {

			/* (non-Javadoc)
			 * @see java.lang.Thread#run()
			 */
			public void run() {
				InputStream is = null;
				try {
					is = ClassmeteoMidlet.class.getResourceAsStream("/translation-it.properties");
					Settings.init(new Properties(is), "savedLocations");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch(Exception e){}
				}
				try {
					Thread.sleep(3000);
				} catch(Exception e){}
				
				Settings.getSelectCityDialog().show();
			}
			
		}.start();
	}
	
}
