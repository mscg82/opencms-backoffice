/**
 * 
 */
package it.virgilio.guidatv.util;

import it.virgilio.guidatv.programs.Channel;
import it.virgilio.guidatv.programs.Programs;
import it.virgilio.guidatv.programs.TVProgram;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mscg.util.List;

/**
 * @author Giuseppe Miscione
 *
 */
public class ProgramXMLParser {

	private Programs programs;
	
	public ProgramXMLParser(InputStream is) {
		programs = null;
        try {
        	programs = new Programs();
        	SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(is, new XMLHandler());
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			programs = null;
		} catch (SAXException e) {
			e.printStackTrace();
			programs = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isValid() {
		return programs != null;
	}

	public Programs getPrograms() {
		return programs;
	}
	
	private class XMLHandler extends DefaultHandler {
		
		private Channel channel;

		public void characters(char[] ch, int start, int length) throws SAXException {
			
		}

		public void endElement(String uri, String localName, String qName) throws SAXException {
			if("ch".equals(qName)) {
				channel = null;
			}
		}

		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if("programs".equals(qName)) {
				// Parse the date associated with the programs list
				String date = attributes.getValue("date");
				int year = 0;
				try {
					year = Integer.parseInt(date.substring(0, 4), 10);
				} catch(Exception e){}
				int month = 0;
				try {
					month = Integer.parseInt(date.substring(5, 7), 10);
				} catch(Exception e){}
				int day = 0;
				try {
					day = Integer.parseInt(date.substring(8, 10), 10);
				} catch(Exception e){}
				
				Calendar dateCal = Calendar.getInstance();
				dateCal.set(Calendar.YEAR, year);
				dateCal.set(Calendar.MONTH, month - 1);
				dateCal.set(Calendar.DAY_OF_MONTH, day);
				dateCal.set(Calendar.HOUR_OF_DAY, 0);
				dateCal.set(Calendar.MINUTE, 0);
				dateCal.set(Calendar.SECOND, 0);
				dateCal.set(Calendar.MILLISECOND, 0);
				programs.setDate(dateCal.getTime());
				
				// Parse the update date associated with the programs list
				String lastUpdate = attributes.getValue("last-update");
				year = 0;
				try {
					year = Integer.parseInt(lastUpdate.substring(0, 4), 10);
				} catch(Exception e){}
				month = 0;
				try {
					month = Integer.parseInt(lastUpdate.substring(5, 7), 10);
				} catch(Exception e){}
				day = 0;
				try {
					day = Integer.parseInt(lastUpdate.substring(8, 10), 10);
				} catch(Exception e){}
				int hour = 0;
				try {
					hour = Integer.parseInt(lastUpdate.substring(11, 13), 10);
				} catch(Exception e){}
				int minute = 0;
				try {
					minute = Integer.parseInt(lastUpdate.substring(14, 16), 10);
				} catch(Exception e){}
				int second = 0;
				try {
					second = Integer.parseInt(lastUpdate.substring(17, 19), 10);
				} catch(Exception e){}
				
				dateCal = Calendar.getInstance();
				dateCal.set(Calendar.YEAR, year);
				dateCal.set(Calendar.MONTH, month - 1);
				dateCal.set(Calendar.DAY_OF_MONTH, day);
				dateCal.set(Calendar.HOUR_OF_DAY, hour);
				dateCal.set(Calendar.MINUTE, minute);
				dateCal.set(Calendar.SECOND, second);
				dateCal.set(Calendar.MILLISECOND, 0);
				programs.setLastUpdate(dateCal.getTime());
			}
			else if("ch".equals(qName)) {
				String typesStr = attributes.getValue("t");
				List types = Util.splitStringAsList(typesStr, "|");
				channel = new Channel(attributes.getValue("id"), attributes.getValue("n"), types);
				programs.addChannel(channel);
			}
			else if("pr".equals(qName)) {
				TVProgram tvpr = new TVProgram(
					attributes.getValue("id"),
					attributes.getValue("n"),
					attributes.getValue("st"),
					attributes.getValue("et"),
					attributes.getValue("c"));
				channel.addTVProgram(tvpr);
			}
		}
		
	}
}
