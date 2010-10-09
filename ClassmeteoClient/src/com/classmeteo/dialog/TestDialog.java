/**
 * 
 */
package com.classmeteo.dialog;

import java.rmi.RemoteException;

import org.j4me.ext.EmptyMenuOption;
import org.j4me.ext.ExitApplicationMenuOption;
import org.j4me.ext.PopupMenuDialog;
import org.j4me.ui.MenuItem;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.MenuOption;
import org.j4me.ui.components.Picture;

import com.classmeteo.ws.ClassMeteoFluxesWS;
import com.classmeteo.ws.ClassMeteoFluxesWS_Stub;
import com.classmeteo.ws.CurrentCondWS;
import com.classmeteo.ws.MasterLocWS;

/**
 * @author Giuseppe Miscione
 *
 */
public class TestDialog extends PopupMenuDialog {
	
	private static final String SERVER_NAME = "classmeteo.weather.com";//"weather08-dev.mashfrog.com";
	
	private ClassMeteoFluxesWS client;
	
	private class ReadWSItem implements MenuItem {

		public String getText() {
			return "Read current cond";
		}

		public void onSelection() {
			TestDialog.this.deleteAll();
			Label l = new Label();
			l.setLabel("Calling web service...");
			TestDialog.this.append(l);
			new Thread() {

				public void run() {
					try {
						MasterLocWS ml = client.getMasterLocByLocId("66015");
						CurrentCondWS curr = client.getCurrentByLocId("66015");
						
						TestDialog.this.deleteAll();
						
						StringBuffer sb = new StringBuffer();
						sb.append(ml.getPrsntNm() + ", " +
							(ml.getStCd().length() != 0 ? ml.getStCd() + ", " : "") + ml.getCntryCd());
						
						Label l = new Label();
						l.setLabel(sb.toString());
						TestDialog.this.append(l);
						
						Picture pic = new Picture();
						try {
							pic.setImage(TestDialog.class.getResourceAsStream("/52x52/" + curr.getSky() + ".png"));
							TestDialog.this.append(pic);
						} catch(Exception e) {
							e.printStackTrace();
						}
						
						sb = new StringBuffer();
						sb.append(curr.getWx() + "\n");
						sb.append("Temp.: " + curr.getTmpC() + "°C\n");
						sb.append("Humi.: " + curr.getRH() + "%\n");
						sb.append("Wind: \"" + curr.getWDirAsc() + "\" at " + curr.getWSpdK() + "km/h");
						l = new Label();
						l.setLabel(sb.toString());
						TestDialog.this.append(l);
						
						TestDialog.this.invalidate();
						TestDialog.this.repaint();
						
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				
			}.start();
		}
		
	}

	public TestDialog() {
		super();
		client = new ClassMeteoFluxesWS_Stub(SERVER_NAME);
		
		this.setTitle("Classmeteo application");

		Label l = new Label("Choose an option from the menu");
		this.append(l);
		
		this.addLeftItem(new MenuOption(new ReadWSItem()));
		this.addLeftItem(new EmptyMenuOption());
		this.addLeftItem(new ExitApplicationMenuOption("Exit"));
		
		this.setMenuText("Actions", null);
	}

}
