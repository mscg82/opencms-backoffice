package it.virgilio.guidatv.util;

import java.util.Calendar;
import java.util.Date;

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
	 * Adds the specified amount of time to the provided {@link Calendar}.
	 * 
	 * @param orig The original {@link Calendar} object.
	 * @param time The amount of time that must be added.
	 * @param type This field indicates what kind of time must me added (days, hours, etc.).
	 * Supported values are {@link Calendar#DAY_OF_MONTH}, {@link Calendar#HOUR_OF_DAY},
	 * {@link Calendar#MINUTE}, {@link Calendar#SECOND}, {@link Calendar#MILLISECOND}.
	 * @return a new {@link Calendar} object that is the original date
	 * added with the provided number of days.
	 */
	public static Calendar addTimeToDate(Calendar orig, long time, int type) {
		long origMillis = orig.getTime().getTime();
		Calendar ret = Calendar.getInstance();
		
		long factor = 0;
		switch(type) {
		case Calendar.DAY_OF_MONTH:
			factor = Constants.MILLIS_PER_DAY;
			break;
		case Calendar.HOUR_OF_DAY:
			factor = Constants.MILLIS_PER_HOUR;
			break;
		case Calendar.MINUTE:
			factor = Constants.MILLIS_PER_MINUTE;
			break;
		case Calendar.SECOND:
			factor = Constants.MILLIS_PER_SECOND;
			break;
		case Calendar.MILLISECOND:
			factor = 1;
			break;
		default:
			throw new IllegalArgumentException("Invalid time type");
		}
		
		ret.setTime(new Date(origMillis + time * factor));
		return ret;
	}
	
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
		if(str.endsWith(separator)) {
			list.add("");
		}
		return list;
	}
	
	private Util() {
		
	}
}
