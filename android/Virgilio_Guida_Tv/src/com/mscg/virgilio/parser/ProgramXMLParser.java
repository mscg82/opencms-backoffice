/**
 *
 */
package com.mscg.virgilio.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.Programs;
import com.mscg.virgilio.programs.TVProgram;
import com.mscg.virgilio.util.Util;

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
            Log.e(ProgramXMLParser.class.getCanonicalName(), "Cannot create parser", e);
            programs = null;
        } catch (SAXException e) {
            Log.e(ProgramXMLParser.class.getCanonicalName(), "Cannot parse document", e);
            programs = null;
        } catch (IOException e) {
            Log.e(ProgramXMLParser.class.getCanonicalName(), "Cannot read document source", e);
            programs = null;
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

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("ch".equals(qName)) {
                channel = null;
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("programs".equals(qName)) {
                // Parse the date associated with the programs list
                String dateStr = attributes.getValue("date");
                Date date = null;
                try {
                    date = Util.programsDateFormat.parse(dateStr);
                } catch (ParseException e) {
                }
                programs.setDate(date);

                // Parse the update date associated with the programs list
                String lastUpdateStr = attributes.getValue("last-update");
                Date lastUpdate = null;
                try {
                    lastUpdate = Util.programsLastUpdateFormat.parse(lastUpdateStr);
                } catch (ParseException e) {
                }
                programs.setLastUpdate(lastUpdate);
            } else if ("ch".equals(qName)) {
                String typesStr = attributes.getValue("t");
                List<String> types = Arrays.asList(typesStr.split("\\|"));
                channel = new Channel(attributes.getValue("id"), attributes.getValue("n"), types);
                programs.addChannel(channel);
            } else if ("pr".equals(qName)) {
                Date startTime = null;
                try {
                    startTime = Util.programTimeFormat.parse(attributes.getValue("st"));
                } catch (ParseException e) {
                }
                Date endTime = null;
                try {
                    endTime = Util.programTimeFormat.parse(attributes.getValue("et"));
                } catch (ParseException e) {
                }
                TVProgram tvpr = new TVProgram(attributes.getValue("id"), attributes.getValue("n"), startTime, endTime,
                                               attributes.getValue("c"));
                channel.addTVProgram(tvpr);
            }
        }

    }
}
