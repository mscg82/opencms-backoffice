package com.mscg.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.mscg.io.BufferedReader;

public class Properties {

	Map properties;
	
	/**
	 * Builds a new instance of a {@link Properties} object
	 * reading the data from the provided stream.
	 * 
	 * @param source the {@link InputStream} to which the data are read.
	 * @throws IOException If an error occurs.
	 */
	public Properties(InputStream source) throws IOException {	
		properties = new LinkedHashMap();
		Reader reader = new InputStreamReader(source);
		BufferedReader lineReader = new BufferedReader(reader);
		String line = null;
		while((line = lineReader.readLine()) != null) {
			line = line.trim();
			if(line.length() != 0) {
				int index = line.indexOf('#');
				if(index > 0)
					line = line.substring(0, index);
				index = line.indexOf('=');
				if(index == 0)
					throw new IOException("Invalid syntax");
				String name = line.substring(0, index).trim();
				String value = line.substring(Math.min(index + 1, line.length())).trim();
				properties.put(name, value);
			}
		}
	}
	
	/**
	 * Returns the property with the provided name.
	 * 
	 * @param name The name of the property to retrieve.
	 * @return The property with the provided name or <code>null</code>
	 * if the property is not found.
	 */
	public String getProperty(String name) {
		return getProperty(name, null);
	}
	
	/**
	 * Returns the property with the provided name.
	 * 
	 * @param name The name of the property to retrieve.
	 * @param defaultValue The default value to return.
	 * @return The property with the provided name or the default value
	 * if the property is not found.
	 */
	public String getProperty(String name, String defaultValue) {
		if(!properties.containsKey(name))
			return defaultValue;
		else
			return (String)properties.get(name);
	}
	
}
