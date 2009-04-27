/**
 * 
 */
package test.util;

import org.apache.xmlbeans.XmlOptions;

/**
 * @author Giuseppe Miscione
 *
 */
public class Util {
	/**
	 * Returns the standard option to print xmlbeans in the right way.
	 * 
	 * @return The {@link org.apache.xmlbeans.XmlOptions} with the standard flags setted.
	 */
	public static XmlOptions getStandardOptions(){
		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		opts.setUseDefaultNamespace();
		opts.setSaveOuter();
		opts.setSaveCDataLengthThreshold(0);
		opts.setSaveCDataEntityCountThreshold(0);
		opts.setCharacterEncoding("UTF-8");
		return opts;
	}
}
