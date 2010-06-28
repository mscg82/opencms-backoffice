package it.virgilio.guidatv.util;

import com.mscg.util.Iterator;
import com.mscg.util.LinkedList;
import com.mscg.util.List;

/**
 * Utility class that holds some utility methods.
 * 
 * @author Giuseppe Miscione
 *
 */
public class Util {

	/**
	 * Replaces all the occurrences of <code>orig</code> in <code>str</code>
	 * with the string <code>replace</code>.
	 * 
	 * @param str The source string.
	 * @param orig The string that will be replaced.
	 * @param replace The string that will replace <code>orig</code>
	 * @return A new <code>String</code> where all the occurrences of <code>orig</code>
	 * in <code>str</code> are replaced with the string <code>replace</code>.
	 */
	public static String replace(String str, String orig, String replace) {
		List list = splitStringAsList(str, orig);
		StringBuffer ret = new StringBuffer();
		boolean first = true;
		for(Iterator it = list.iterator(); it.hasNext();) {
			String s = (String) it.next();
			if(first) {
				first = false;
			}
			else {
				ret.append(replace);
			}
			ret.append(s);
		}
		return ret.toString();
	}
	
	/**
	 * Splits a string using the second string as separator.
	 * 
	 * @param str The string that will be split.
	 * @param separator The string used as separator.
	 * @return A {@link List} with the parts of the provided string.
	 */
	public static List splitStringAsList(String str, String separator) {
		List list = new LinkedList();
		int fromIndex = 0;
		int l = str.length();
		while(fromIndex < l) {
			int index = str.indexOf(separator, fromIndex);
			if(index != -1) {
				list.add(str.substring(fromIndex, index));
				fromIndex = index + separator.length();
			}
			else {
				list.add(str.substring(fromIndex));
				break;
			}
		}
		return list;
	}
	
	private Util() {
		
	}
}
