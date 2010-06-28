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
		"${day} / ${month} Lunedì",
		"${day} / ${month} Martedì",
		"${day} / ${month} Mercoledì",
		"${day} / ${month} Giovedì",
		"${day} / ${month} Venerdì",
		"${day} / ${month} Sabato",
		"${day} / ${month} Domenica"
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
	
	public static final long MILLIS_PER_SECOND = 1000L;
	public static final long MILLIS_PER_MINUTE = 1000L * 60;
	public static final long MILLIS_PER_HOUR   = 1000L * 60 * 60;
	public static final long MILLIS_PER_DAY    = 1000L * 60 * 60 * 24;
	
	private Constants() {
		
	}
}
