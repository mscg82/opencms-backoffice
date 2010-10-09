package com.classmeteo.dialog;

import org.j4me.ext.ChainedMenuItem;
import org.j4me.ext.ExitApplicationMenuOption;
import org.j4me.ext.OpenMenuMenuOption;
import org.j4me.ext.UpdatablePopupMenuDialog;
import org.j4me.ui.DeviceScreen;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.Component;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.MenuOption;
import org.j4me.ui.components.TextBox;

import com.classmeteo.data.Settings;
import com.classmeteo.items.RemoveCityMenuItem;
import com.classmeteo.items.RepaintScreenMenuItem;
import com.classmeteo.items.SavedCityMenuItem;
import com.classmeteo.theme.ClassMeteoAppTheme;
import com.mscg.util.Iterator;
import com.mscg.util.Map;
import com.mscg.util.Properties;
import com.mscg.util.RecordStoreMap.RecordStoreMapValue;

public class SelectCityDialog extends UpdatablePopupMenuDialog {

	private DeviceScreen previous;
	private TextBox citySelector;
	
	private MenuOption searchCity;
	private MenuOption gotoCurrent;
	
	public SelectCityDialog(DeviceScreen previous) {
		Properties tr = Settings.getTranslation();
		setPrevious(previous);

		setDefaultMenuText();
		
		setTitle(tr.getProperty("cityselector.title"));
		
		citySelector = new TextBox();
		citySelector.setForAnyText();
		citySelector.setLabel(tr.getProperty("cityselector.textbox.title"));
		
		searchCity = new OpenMenuMenuOption(tr.getProperty("menu.search"), Settings.getSearchCityDialog());
		gotoCurrent = new OpenMenuMenuOption(tr.getProperty("menu.goto.current"), Settings.getCurrentCondDialog());
	}

	/**
	 * @return the previous
	 */
	public DeviceScreen getPrevious() {
		return previous;
	}

	public String getSelectedCityName() {
		return citySelector.getString();
	}
	
	/* (non-Javadoc)
	 * @see org.j4me.ext.PopupMenuDialog#keyPressed(int)
	 */
	protected void keyPressed(int keyCode) {
		super.keyPressed(keyCode);
		updateMenu();
	}

	/**
	 * @param previous the previous to set
	 */
	public void setPrevious(DeviceScreen previous) {
		Properties tr = Settings.getTranslation();
		this.previous = previous;
		
		if(this.previous == null) {
			addLeftItem(new ExitApplicationMenuOption(tr.getProperty("menu.exit")));
		}
		else {
			addLeftItem(new OpenMenuMenuOption(tr.getProperty("menu.exit"), previous));
		}
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.DeviceScreen#showNotify()
	 */
	public void showNotify() {
		super.showNotify();
		
		updateComponents();
		setSelected(citySelector);
		updateMenu();
	}
	
	public void updateComponents() {
		deleteAll();
		//append(Settings.getWhiteSpace());
		append(citySelector);
		Map savedLocations = Settings.getSavedLocations();
		if(savedLocations.size() != 0) {
			Label l = new Label(Settings.getTranslation().getProperty("cityselector.list.title"));
			l.setFont(((ClassMeteoAppTheme)UIManager.getTheme()).getBoldFont());
			append(l);
			for(Iterator it = savedLocations.entrySet().iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry)it.next();
				RecordStoreMapValue value = (RecordStoreMapValue)entry.getValue();
				String values[] = value.getStrings();
				append(new MenuOption(new SavedCityMenuItem(values[1], values[0])));
			}
		}
		super.updateComponents();
	}

	private void updateMenu() {
		Component sel = get(getSelected());
		if(sel.acceptsInput()) {
			getRightMenuItems().removeAllElements();
			if(sel == citySelector) {
				if(citySelector.getString().length() >= 3) {
					Settings.getSearchCityDialog().setPrevious(this);
					addRightItem(searchCity);
					setDefaultMenuText();
				}
			}
			else {
				MenuOption mo = (MenuOption)sel;
				Properties tr = Settings.getTranslation();
				addRightItem(gotoCurrent);
				addRightItem(new MenuOption(
					new ChainedMenuItem(
						new RemoveCityMenuItem(tr.getProperty("menu.remove"), ((SavedCityMenuItem)mo.getMenuItem()).getCityId()),
						new RepaintScreenMenuItem("", this))));
				setMenuText(getLeftMenuText(), tr.getProperty("menu.actionbutton"));
			}
		}
		invalidate();
	}
	
}
