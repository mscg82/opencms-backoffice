/**
 *
 */
package eni.virtualoffice.beans;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import eni.virtualoffice.config.ConfigLoader;
import eni.virtualoffice.util.Util;

/**
 * This class hold the lists of css and javascript
 * contained in the theme.
 *
 * @author Giuseppe Miscione
 *
 */
public class ThemeBean {

	private class CssFileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".css");
		}

	}

	private class JsFileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".js");
		}

	}

	/**
	 * The list of the css for the theme.
	 */
	private List<ThemeElementBean> css;

	/**
	 * The lost of javascripts for the theme.
	 */
	private List<ThemeElementBean> js;

	/**
	 * Loads the theme with the provided name.
	 * If the theme doesn't exists, it loads the
	 * default theme.
	 *
	 * @param themeName The name of the theme that
	 * will be loaded.
	 */
	public ThemeBean(String themeName) {
		css = new LinkedList<ThemeElementBean>();
		js = new LinkedList<ThemeElementBean>();

		String themeFolderName = (String)ConfigLoader.getInstance().get("general.theme.base-folder");
		ServletContext ctx = ServletActionContext.getServletContext();
		String fullpath = ctx.getRealPath(themeFolderName + ConfigLoader.webAppFileSep + themeName);
		File themeFolder = new File(fullpath);
		if(!themeFolder.exists() || !themeFolder.isDirectory()) {
			// load default theme
			themeName = (String)ConfigLoader.getInstance().get("general.theme.default");
			fullpath = ctx.getRealPath(themeFolderName + ConfigLoader.webAppFileSep + themeName);

			themeFolder = new File(fullpath);
		}

		// load css files
		String cssFolderName = fullpath + ConfigLoader.webAppFileSep + "style";
		File cssFolder = new File(cssFolderName);
		String themePath = ctx.getContextPath() + ConfigLoader.webAppFileSep +
			themeFolderName + ConfigLoader.webAppFileSep +
			themeName + ConfigLoader.webAppFileSep;
		if(cssFolder.exists() && cssFolder.isDirectory()) {
			// load pre css definitions
			File preCssFile = new File(cssFolder + ConfigLoader.webAppFileSep + "pre-css.txt");
			List<String> preCsses = Util.readFileLines(preCssFile);
			for(String preCss : preCsses) {
				ThemeElementBean element = new ThemeElementBean("css");
				element.setSourceFile(preCss.replace("${THEMEPATH}", themePath + "style"));
				css.add(element);
			}

			// load theme css
			List<ThemeElementBean> tmp = new LinkedList<ThemeElementBean>();
			File cssFiles[] = cssFolder.listFiles(new CssFileFilter());
			for(File cssFile : cssFiles) {
				ThemeElementBean element = new ThemeElementBean("css");
				element.setSourceFile(themePath + "style" + ConfigLoader.webAppFileSep + cssFile.getName());
				tmp.add(element);
			}
			Collections.sort(tmp);
			css.addAll(tmp);

			// load post css definitions
			File postCssFile = new File(cssFolder + ConfigLoader.webAppFileSep + "post-css.txt");
			List<String> postCsses = Util.readFileLines(postCssFile);
			for(String postCss : postCsses) {
				ThemeElementBean element = new ThemeElementBean("css");
				element.setSourceFile(postCss.replace("${THEMEPATH}", themePath + "style"));
				css.add(element);
			}
		}

		// load js files
		String jsFolderName = fullpath + ConfigLoader.webAppFileSep + "js";
		File jsFolder = new File(jsFolderName);
		if(jsFolder.exists() && jsFolder.isDirectory()) {
			// load pre js definitions
			File preJsFile = new File(jsFolder + ConfigLoader.webAppFileSep + "pre-js.txt");
			List<String> preJses = Util.readFileLines(preJsFile);
			for(String preJs : preJses) {
				ThemeElementBean element = new ThemeElementBean("js");
				element.setSourceFile(preJs.replace("${THEMEPATH}", themePath + "js"));
				js.add(element);
			}

			// load theme js
			List<ThemeElementBean> tmp = new LinkedList<ThemeElementBean>();
			File jsFiles[] = jsFolder.listFiles(new JsFileFilter());
			for(File jsFile : jsFiles) {
				ThemeElementBean element = new ThemeElementBean("js");
				element.setSourceFile(themePath + "js" + ConfigLoader.webAppFileSep + jsFile.getName());
				tmp.add(element);
			}
			Collections.sort(tmp);
			js.addAll(tmp);

			// load post js definitions
			File postJsFile = new File(jsFolder + ConfigLoader.webAppFileSep + "post-js.txt");
			List<String> postJses = Util.readFileLines(postJsFile);
			for(String postJs : postJses) {
				ThemeElementBean element = new ThemeElementBean("js");
				element.setSourceFile(postJs.replace("${THEMEPATH}", themePath + "js"));
				js.add(element);
			}
		}
	}

	/**
	 * Returns the list of css for the theme.
	 *
	 * @return The list of css for the theme.
	 */
	public List<ThemeElementBean> getCss() {
		return css;
	}

	/**
	 * Returns the list of javascript for the theme.
	 *
	 * @return The list of javascript for the theme.
	 */
	public List<ThemeElementBean> getJs() {
		return js;
	}
}
