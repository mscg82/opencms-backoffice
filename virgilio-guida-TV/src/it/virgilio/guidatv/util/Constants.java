/**
 * 
 */
package it.virgilio.guidatv.util;

import java.util.Calendar;


/**
 * A utility class that holds all constants.
 * 
 * @author Giuseppe Miscione
 *
 */
public class Constants {

	public static final String[] daysNames = {
		"Lunedì",
		"Martedì",
		"Mercoledì",
		"Giovedì",
		"Venerdì",
		"Sabato",
		"Domenica"
	};
	
	public static final String[] dayFileNames = {
		"lunedi",
		"martedi",
		"mercoledi",
		"giovedi",
		"venerdi",
		"sabato",
		"domenica"
	};
	
	public static final int[] dayNumbers = {
		Calendar.MONDAY,
		Calendar.TUESDAY,
		Calendar.WEDNESDAY,
		Calendar.THURSDAY,
		Calendar.FRIDAY,
		Calendar.SATURDAY,
		Calendar.SUNDAY
	};
	
	public static final String palinsestoURLprefix =
		"http://cinema-tv.virgilio.it/services/guida-tv/flash/palinsesto-";
	public static final String palinsestoURLsuffix =".xml";
	
	private Constants() {
		
	}
}
