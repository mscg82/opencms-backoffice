package com.classmeteo.data;

public class Util {

	/**
	 * Converts a string with the altimeter value to
	 * the corresponding value in millibars.
	 * 
	 * @param alt The altimeter value.
	 * @return the value in millibars as a double
	 */
	public static double convertToMb(String alt) {
		double altD = Double.parseDouble(alt);
		return altD * 33.86;
	}
	
	/**
	 * Converts a string with an inches measure
	 * into an integer with the corresponding
	 * millimeters.
	 * 
	 * @param inches The string with the measure in inches.
	 * @return an integer with the equivalent millimeters.
	 */
	public static int convertToMM(String inches) {
		if(inches.startsWith("."))
			inches = "0" + inches;
		double inc = 0.0;
		try {
			inc = Double.parseDouble(inches);
		} catch(Exception e) {}
		return (int)Math.floor((inc * 25.4) + 0.5);
	}
	
	/**
	 * Formats a double value into a string using the
	 * specified number of fractional digits.
	 * 
	 * @param value The double value.
	 * @param fractionalDigits The number of fractional digits.
	 * @return A string where the double value is formatted.
	 */
	public static String formatDouble(double value, int fractionalDigits) {
		int factor = 1;
		for(int i = 0; i < fractionalDigits; i++) {
			factor *= 10;
		}
		int tmp = (int)(value * factor);
		value = (double)tmp / factor;
		return Double.toString(value);
	}
	
}
