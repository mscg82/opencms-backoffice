/**
 *
 */
package eni.virtualoffice.actions;

import java.util.Map;

import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import eni.virtualoffice.beans.ThemeBean;
import eni.virtualoffice.config.ConfigLoader;
import eni.virtualoffice.util.Constants;
import eni.virtualoffice.util.Util;

/**
 * This class is the superclass of all backend actions.
 *
 * @author Giuseppe Miscione
 *
 */
public class GenericAction extends ActionSupport implements ApplicationAware, SessionAware{

	private static final long serialVersionUID = 2762044250396432648L;

	protected Map<String, Object> application;
	protected Map<String, Object> session;

	protected ThemeBean themeInfo;

	/**
	 * Returns the locale stored in the session.
	 *
	 * @return The locale stored in the session. The default
	 * language is loaded from the configuration file.
	 */
	public String getPageLocale() {
		return (session.containsKey(Constants.pageLocaleParam) ?
			(String)session.get(Constants.pageLocaleParam) :
			(String)ConfigLoader.getInstance().get("general.lang.default"));
	}

	/**
	 * Loads the theme that is set for this action.
	 *
	 * @return The {@link ThemeBean} object with the
	 * informations of the theme.
	 */
	public ThemeBean getThemeInfo() {
		if(themeInfo == null) {
			themeInfo = new ThemeBean(getThemeName());
		}
		return themeInfo;
	}

	/**
	 * Returns the theme name stored in the session.
	 *
	 * @return The theme name stored in the session. The default
	 * theme is loaded from the configuration file.
	 */
	protected String getThemeName() {
		return (session.containsKey(Constants.themeParam) ?
				(String)session.get(Constants.themeParam) :
				(String)ConfigLoader.getInstance().get("general.theme.default"));
	}

	public String getTitle() {
		return Util.getConfigurationParamAsString("general.title");
	}

	@Override
	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

	/**
	 * Sets the specified locale in session.
	 *
	 * @param locale The locale to set.
	 */
	public void setPageLocale(String pageLocale) {
		session.put(Constants.pageLocaleParam, pageLocale);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	/**
	 * Sets the theme name that will be used to render pages.
	 *
	 * @param themeName The name of theme to set.
	 */
	protected void setThemeName(String themeName) {
		session.put(Constants.themeParam, themeName);
	}

}
