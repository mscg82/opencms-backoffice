/**
 * 
 */
package it.virgilio.guidatv.menu;

import it.virgilio.guidatv.programs.Channel;
import it.virgilio.guidatv.programs.Programs;
import it.virgilio.guidatv.theme.VirgilioTheme;
import it.virgilio.guidatv.util.Constants;
import it.virgilio.guidatv.util.ProgramXMLParser;
import it.virgilio.guidatv.util.Util;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Graphics;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.MenuOption;
import org.j4me.ui.components.ProgressBar;

import com.mscg.util.Iterator;
import com.mscg.util.List;
import com.mscg.util.connection.GetMethod;
import com.mscg.util.connection.HttpClient;
import com.mscg.util.connection.HttpMethod;
import com.mscg.util.connection.HttpMethodExecutionListener;
import com.mscg.util.io.InputStreamDataReadListener;
import com.mscg.util.io.PositionNotifierInputStream;

/**
 * @author Giuseppe Miscione
 *
 */
public class WeekDayMenu extends BaseMenu {
	
	private HttpClient httpClient;
	
	private String dayName;
	private int dayNumber;
	
	private ProgressBar spinner;
	
	private Programs programs;
	
	private int actualPage;
	private int elementsPerPage;
	private int totalPages;
	private List page;
	
	/**
	 * @param name
	 * @param previous
	 */
	public WeekDayMenu(String title, String dayName, int dayNumber, DeviceScreen previous) {
		super(title, previous);

		this.dayName = dayName;
		this.dayNumber = dayNumber;
		
		this.programs = null;
		httpClient = new HttpClient();
		
		actualPage = 0;
		totalPages = 0;
		elementsPerPage = 0;
	}

	public String getDayName() {
		return dayName;
	}

	public int getDayNumber() {
		return dayNumber;
	}

	public void showNotify() {
		super.showNotify();
		if(this.programs == null) {
			// Add an indefinate progress bar.
			spinner = new ProgressBar();
			spinner.setLabel(((VirgilioTheme)UIManager.getTheme()).getProgramsLoadingText());
			append(spinner);
			GetMethod get = new GetMethod(
				"palinsesto-" + dayName,
				Constants.palinsestoURLprefix + dayName + Constants.palinsestoURLsuffix);
			httpClient.removeListener();
			httpClient.setListener(new PalinsestoListener());
			httpClient.executeMethod(get);
		}
		else {
			updateInterface();
		}
	}
	
	protected void keyPressed(int key) {
		preventKeyPropagation = false;
		if(totalPages > 1) {
			int sel = getSelected();
			switch(key) {
			case DOWN:
				if(sel == page.size() - 1) {
					// The last element was selected and
					// down was pressed: change page and
					// update interface.
					actualPage = (actualPage + 1) % totalPages;
					updateInterface();
					// internally recalculates the layout
					hasVerticalScrollbar();
					setSelected(0);
					
					preventKeyPropagation = true;
				}
				break;
			case UP:
				if(sel == 0) {
					// The first element was selected and
					// up was pressed: change page and
					// update interface.
					actualPage = (actualPage + totalPages - 1) % totalPages;
					updateInterface();
					// internally recalculates the layout
					hasVerticalScrollbar();
					setSelected(page.size() - 1);
					
					preventKeyPropagation = true;
				}
				break;
			}
		}		
		super.keyPressed(key);
	}

	protected synchronized void paint(Graphics g) {
		if(totalPages > 1) {
			preventScrollbar = true;
			super.paint(g);
			int h = getHeight();
			int w = getTotalWidth();
			paintVerticalScrollbar(g, 0, 0, w, h, actualPage * h, totalPages * h);
		}
		else {
			preventScrollbar = false;
			super.paint(g);
		}
	}

	private void updateInterface() {
		if(visible) {
			if(spinner != null) {
				// stop the spinner timer
				spinner.setMaxValue(1);
				spinner = null;
			}
			deleteAll();
			if(programs.getChannels().size() != 0) {
				if(elementsPerPage == 0) {
					// calculate how many elements per page will fit the screen
					calculateElementsPerPage(); 
				}
				int firstIndex = actualPage * elementsPerPage;
				int endIndex = Math.min(firstIndex + elementsPerPage, programs.getChannels().size());
				page = programs.getChannels().subList(firstIndex, endIndex);
				for(Iterator it = page.iterator(); it.hasNext();) {
					Channel channel = (Channel) it.next();
					appendSubmenu(new ChannelMenu(channel, this));
				}
			}
			else {
				append(new Label(((VirgilioTheme)UIManager.getTheme()).getTextForEmptyPrograms()));
			}
			repaint();
		}
	}
	
	private void calculateElementsPerPage() {
		MenuOption tmp = new MenuOption(
			new ChannelMenu((Channel)programs.getChannels().get(0), this),
			true);
		int h = getHeight();
		int w = getWidth();
		int ls[] = tmp.getPreferredSize(UIManager.getTheme(), w, h);
		elementsPerPage = h / ls[1];
		totalPages = (int) Math.ceil(((double)programs.getChannels().size()) / elementsPerPage);
	}
	
	/**
	 * Private class that listens on HTTP events.
	 * 
	 * @author Giuseppe Miscione
	 *
	 */
	private class PalinsestoListener implements HttpMethodExecutionListener, InputStreamDataReadListener {

		private List elaboratingStrParts;
		
		public PalinsestoListener() {
			String el = ((VirgilioTheme) UIManager.getTheme()).getElaboratingDataText();
			elaboratingStrParts = Util.splitStringAsList(el, "${perc}");
		}
		
		private void displayError(Exception e) {
			WeekDayMenu.this.deleteAll();
			WeekDayMenu.this.append(
				new Label(((VirgilioTheme)UIManager.getTheme()).getUpdateErrorText()));
			e.printStackTrace();
		}
		
		public void onMethodExecuted(HttpMethod method) {
			 try {
				InputStream is = new PositionNotifierInputStream(
					method.getResponseBodyAsStream(),
					method.getResponseSize(),
					this);
				ProgramXMLParser parser = new ProgramXMLParser(is);
				if(parser.isValid()) {
					WeekDayMenu.this.programs = parser.getPrograms();
					WeekDayMenu.this.updateInterface();
				}
				else {
					displayError(new Exception("Invalid response"));
				}
			} catch (IOException e) {
				displayError(e);
			}
		}

		public void onMethodExecutionError(Exception e, HttpMethod method) {
			displayError(e);
		}

		public void onResponse(HttpMethod method) {

		}

		public void onSendingData(HttpMethod method) {

		}

		public void onDataRead(long actualPosition, long totalSize) {
			double percentage = (((double)actualPosition) / totalSize) * 100.0;
			int max = 100 * 100;
			int value = (int)(percentage * 100);
			double dValue = ((double)value / 100.0);
			spinner.setMaxValue(max);
			spinner.setValue(value);
			spinner.setLabel((String)elaboratingStrParts.get(0) + dValue + elaboratingStrParts.get(1));
			WeekDayMenu.this.repaint();
		}

		public void onStreamEnd() {
			int max = 100 * 100;
			spinner.setMaxValue(max);
			spinner.setValue(max);
			spinner.setLabel((String)elaboratingStrParts.get(0) + 100.0 + elaboratingStrParts.get(1));
			WeekDayMenu.this.repaint();
		}
		
	}

}
