package com.mscg.virgilio.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.mscg.virgilio.programs.ProgramDetails;

public class ProgramDetailsXMLParser {

    private static final SimpleDateFormat programsLastUpdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ProgramDetails programDetails;

    public ProgramDetailsXMLParser(InputStream is) {
        try {
            programDetails = new ProgramDetails();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(is, new XMLHandler());

        } catch (ParserConfigurationException e) {
            Log.e(ProgramXMLParser.class.getCanonicalName(), "Cannot create parser", e);
            programDetails = null;
        } catch (SAXException e) {
            Log.e(ProgramXMLParser.class.getCanonicalName(), "Cannot parse document", e);
            programDetails = null;
        } catch (IOException e) {
            Log.e(ProgramXMLParser.class.getCanonicalName(), "Cannot read document source", e);
            programDetails = null;
        }
    }

    public boolean isValid() {
        return programDetails != null;
    }

    public ProgramDetails getProgramDetails() {
        return programDetails;
    }

    private class XMLHandler extends DefaultHandler {

        private StringBuffer buffer;

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (buffer != null)
                buffer.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("pr".equals(qName)) {
                programDetails.setDescription(buffer.toString());
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            buffer = null;
            if ("program-detail".equals(qName)) {
                programDetails.setStrId(attributes.getValue("id"));
                try {
                    programDetails.setLastUpdate(programsLastUpdateFormat.parse(attributes.getValue("last-update")));
                } catch (Exception e) {
                }
            } else if ("pr".equals(qName)) {
                buffer = new StringBuffer();
                programDetails.setTitle(attributes.getValue("n"));
                try {
                    programDetails.setLevel(Integer.parseInt(attributes.getValue("pc")));
                } catch (Exception e) {
                    programDetails.setLevel(0);
                }
                programDetails.setUrl(attributes.getValue("url"));
            }
        }

    }

}
