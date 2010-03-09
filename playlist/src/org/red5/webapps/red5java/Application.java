package org.red5.webapps.red5java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.so.ISharedObject;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/*.
 *
 * NB: ApplicationAdapter is a ready base class for all your Red5 Applications.
 * @author Joseph Wamicha (jwamicha@yahoo.co.uk)
 * 01:01 AM, February 9th, 2007.
 *
 */

public class Application extends ApplicationAdapter {

	private IScope appScope;
	protected static Logger log = Red5LoggerFactory.getLogger(Application.class, "playlist");
	private Map<String, Map> playListContainer = new HashMap<String, Map>();

	/** {@inheritDoc} */
	@Override
	public boolean appConnect(IConnection conn, Object[] params) {
		// -----------this below remain here...
		// create or increment Persistent Shared Object value everytime a new
		// flash client connects...
		this.incrementPersistentSharedObject();

		log.debug("Client with id \"" + conn.getClient().getId() + "\" and session id \"" + conn.getSessionId() + "\" connected.");

		log.debug("Client parameters:\n" + conn.getConnectParams());

		log.debug("Client \"" + conn.getClient().getId() + "\" permissions: " + conn.getClient().getPermissions(conn));
		log.debug("Client \"" + conn.getClient().getId() + "\" attributes (before): " + conn.getClient().getAttributes());

		conn.getClient().setAttribute("param1", "value1");
		conn.getClient().setAttribute("param2", "value2");

		log.debug("Client \"" + conn.getClient().getId() + "\" attributes (after): " + conn.getClient().getAttributes());

		return super.appConnect(conn, params);
	}

	/** {@inheritDoc} */
	@Override
	public void appDisconnect(IConnection conn) {
		// -----------this below remain here...
		// create or decrement Persistent Shared Object value everytime a new
		// flash client connects...
		this.decrementPersistentSharedObject();

		super.appDisconnect(conn);
	}

	/** {@inheritDoc} */
	@Override
	public void appStop(IScope app) {
		log.info("Application with context \"" + appScope.getContextPath() + "\" is stopping.");

		// destroy the transient shared objects...
		this.clearSharedObjects(this.appScope, "playListSO");
		// you can do "message*" to delete both messageTSO and messagePSO in one
		// shot.
		this.clearSharedObjects(this.appScope, "messageTSO");
		this.clearSharedObjects(this.appScope, "messagePSO");

		this.clearSharedObjects(this.appScope, "counterSO");
		// remove the scheduled job...
		String id = (String) this.appScope.getAttribute("ReloadPlayListJobId");
		removeScheduledJob(id);

		log.info("Application  with context \"" + appScope.getContextPath() + "\" stopped successfully.");

		super.appStop(app);
	}

	/** {@inheritDoc} */
	@Override
	public void roomStop(IScope room) {
		log.info("Application room with context \"" + appScope.getContextPath() + "\" is stopping.");

		// destroy the transient shared objects...
		this.clearSharedObjects(this.appScope, "playListSO");
		// you can do "message*" to delete both messageTSO and messagePSO in one
		// shot.
		this.clearSharedObjects(this.appScope, "messageTSO");
		this.clearSharedObjects(this.appScope, "messagePSO");

		this.clearSharedObjects(this.appScope, "counterSO");
		// remove the scheduled job...
		String id = (String) this.appScope.getAttribute("ReloadPlayListJobId");
		removeScheduledJob(id);

		log.info("Application room with context \"" + appScope.getContextPath() + "\" stopped successfully.");
		super.roomStop(room);
	}

	/** {@inheritDoc} */
	@Override
	public boolean appStart(IScope app) {
		appScope = app;

		log.info("Application with context \"" + appScope.getContextPath() + "\" is starting.");

		// Read in the playlist from the playlist.xml file in playlist folder...

		// String playListName;
		// String playListLength;

		playListContainer = this.loadPlayList("playlistConf/playlist.xml");

		this.registerSharedObjectSecurity(new ApplSecurityHandler());

		if (log.isDebugEnabled()) {

			log.debug("-----------------");
			log.debug("> Verifying playListContainer structure...");

			for (Iterator iter = playListContainer.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<String, Object> container = (Map.Entry<String, Object>) iter.next();

				String key = container.getKey();
				Object value = container.getValue();
				log.debug("Key is:" + key + " and Value is:" + value);

				Map<String, Object> playListInfo = new HashMap<String, Object>();
				playListInfo = (Map<String, Object>) value;

				log.debug("Final playListInfo:" + playListInfo.get("Name") + " and Value is:" + playListInfo.get("Length"));
			}

			log.debug("> PlayListContainer ok...");
			log.debug("-----------------");
		}

		// create Transient SharedObject using the just loaded TSO...
		this.initTransientSharedObject(playListContainer);

		// initialize a TSO (Transient Shared Object) with Listener...
		this.initTSOwithListener();

		// initialize a PSO (Persistent Shared Object) with Listener...
		this.initPSOwithListener();

		// initialize reloading of playlist...
		this.initJobSchedule(600);// Perform a scheduled job every 10 minutes...

		super.appStart(app);

		log.info("Application with context \"" + appScope.getContextPath() + "\" started successfully.");

		return true;
	}

	/**
	 * This method returns the the string value within an Element's tag
	 *
	 * @param ele
	 *            A DOM <code>Element</code>
	 * @param tagName
	 *            The name of the DOM Element of type <code>String</code>
	 * @return The value contained within the DOM Element. Return value is of
	 *         type <code>String</code>.
	 *
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);

		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	/**
	 * This method increments the value of a persistent/non-transient
	 * appConnection connection counter
	 *
	 * @param None
	 *
	 * @return void.
	 *
	 */
	private void incrementPersistentSharedObject() {
		changePersistentSharedObject(1);
	}

	/**
	 * This method decrements the value of a persistent/non-transient
	 * appConnection connection counter
	 *
	 * @param None
	 *
	 * @return void.
	 *
	 */
	private void decrementPersistentSharedObject() {
		changePersistentSharedObject(-1);
	}

	/**
	 * This method changes the value of a persistent/non-transient
	 * appConnection connection counter
	 *
	 * @param delta The integer value to be added to the connection
	 * counter.
	 *
	 */
	private void changePersistentSharedObject(int delta) {
		Integer counter = 0;

		// createSharedObject internally checks to see if SO is already created.
		// If it is not...
		// ...it is created, else nothing happens...
		this.createSharedObject(this.appScope, "counterSO", true);
		// set to true to indicate that it is a persistent Shared Object...
		ISharedObject counterSO = this.getSharedObject(scope, "counterSO", true);
		counter = (Integer) counterSO.getAttribute("counterValue", 0) + delta;
		counterSO.setAttribute("counterValue", counter);
		log.debug("The new CONNECTION counter value is: " + counter);
	}

	/**
	 * This method creates a job schedule
	 *
	 * @param time
	 *            . <code>long</code> time in seconds.
	 * @return void.
	 *
	 */
	private void initJobSchedule(int timeInSeconds) {
		// Schedule invocation of job every 10 seconds.
		String id = this.addScheduledJob(timeInSeconds * 1000, new ScheduledJob());
		this.appScope.setAttribute("ReloadPlayListJobId", id);
	}

	/**
	 * This method only creates a Persistent Object with a Listener attached to
	 * it. When this Server-side Persistent object is attached to a Server-side
	 * Listener, if the value of the Persistent Object is changed by the flash
	 * client or the value is changed directly from the server ( after
	 * setAttribute(s), close(), clear, etc... ), the Server-Side Listener is
	 * triggered to begin running the methods specified in the
	 * ISharedObjectListener interface... This allows you to react to this
	 * "Persistent object change" triggered by the client/server, at the server.
	 *
	 * @param None
	 *            .
	 *
	 * @return void.
	 *
	 *         initPSOwithListener- init Persistent Shared Object With Listener
	 */
	private void initPSOwithListener() {
		// createSharedObject internally handles checking whether sharedobject
		// exists or not...
		// and if it does not exist, it will create it, otherwise not...
		this.createSharedObject(this.appScope, "messagePSO", true);
		// set to true to indicate that is a persistent Shared Object
		ISharedObject PSO = this.getSharedObject(scope, "messagePSO", true);
		// add a listener to the PSO...
		PSO.addSharedObjectListener(new SOEventListener());
		// register a handler for the PSO...
		PSO.registerServiceHandler("PSOHandler", new SOHandler());
		// set the initial attribute value of the PSO
		PSO.setAttribute("messagePSO", "Hello Everyone from sample PSO!");
	}

	/**
	 * This method initializes a non-persistent/transient object.
	 *
	 * @param playListContainer A <code>Map&lt;String, Object&gt;</code>
	 * which contains the playlist details ie name and length.
	 *
	 * @return void.
	 *
	 */
	private void initTransientSharedObject(Map<String, Map> playListContainer) {
		// set to false to indicate that is a non-persistent Shared Object
		this.createSharedObject(this.appScope, "playListSO", false);

		ISharedObject so = this.getSharedObject(this.appScope, "playListSO", false);

		// let's now feed this Shared Object with the playListContainer Map...
		Map<String, Object> playListInfo = new HashMap<String, Object>();
		for (String name : playListContainer.keySet()) {
			playListInfo.put(name, playListContainer.get(name));
		}
		// verify playListInfo...
		/*
		 * for (String name : playListInfo.keySet()) {
		 * log.debug("Name: "+name+", Value: "+playListInfo.get(name)+""); }
		 */

		so.setAttributes(playListInfo);
	}

	/**
	 * This method only creates a Transient Object with a Listener attached to
	 * it. When this Server-side Transient object is attached to a Server-side
	 * Listener, if the value of the Transient Object is changed by the flash
	 * client or the value is changed directly from the server ( after
	 * setAttribute(s), close(), clear, etc... ), the Server-Side Listener is
	 * triggered to begin running the methods specified in the
	 * ISharedObjectListener interface... This allows you to react to this
	 * "Transient object change" triggered by the client/server, at the server.
	 */
	private void initTSOwithListener() {
		// set to false to indicate that this is a non-persistent Shared Object
		this.createSharedObject(this.appScope, "messageTSO", false);
		// get the TSO just created...
		ISharedObject TSO = this.getSharedObject(this.appScope, "messageTSO", false);
		// add a listener to the TSO...
		TSO.addSharedObjectListener(new SOEventListener());
		// register a handler for the TSO...
		TSO.registerServiceHandler("TSOHandler", new SOHandler());
		// set the initial attribute value of the TSO
		TSO.setAttribute("message", "Hello Everyone from Sample TSO!");
	}

	/**
	 * This method reads the XML file from the classpath and loads it into a
	 * Map<String, Map> object.
	 *
	 * @param fileName String representing the XML file to open. Uses ? classpath.
	 *
	 * @return a <code>Map&lt;String, Map&gt;</code> object of the PlayList
	 * items defined within the XML file.
	 *
	 */
	public Map<String, Map> loadPlayList(String fileName) {
		// Overall Map to store all playlist items...
		Map<String, Map> playListContainer = new HashMap<String, Map>();

		try {
			log.debug(">Filename: " + fileName + " in scope <red5_server>/webapps/WEB-INF/playlist");

			Resource playlistXML = this.appScope.getResource(fileName);

			// log.debug(playlistXML.getFile().isFile());

			// read in xml file...
			// get the inputstream...
			InputStream xmlinStream = playlistXML.getInputStream();
			// convert the inputstream into a datainputstream...
			BufferedReader xmldataStream = new BufferedReader(new InputStreamReader(xmlinStream));
			// store xmlstring
			StringBuffer xmlStringBuf = new StringBuffer();

			String inputLine;

			while ((inputLine = xmldataStream.readLine()) != null) {

				// if(log.isDebugEnabled())
				// log.debug(inputLine);

				xmlStringBuf.append(inputLine);
			}

			// close BufferedReader...
			xmldataStream.close();

			// create a DOM object out of it...
			Document dom = null;
			try {
				dom = this.stringToDoc(xmlStringBuf.toString());
			} catch (IOException ioex) {
				log.error("IOException converting xml to dom", ioex);
			}

			// enables access to the document element of the document...
			Element docElement = dom.getDocumentElement();

			// get a nodelist of <playlist-item> elements
			NodeList nl_level1 = docElement.getElementsByTagName("playlist-item");
			if (nl_level1 != null && nl_level1.getLength() > 0) {
				String playListName;
				String playListLength;

				log.debug("-----------------");
				log.debug(">Read xml file...");
				// looping through each of the <playlist-item> tags...
				for (int i = 0; i < nl_level1.getLength(); i++) {
					// get the playlist-item element
					Element playlistItem_nl_level1 = (Element) nl_level1.item(i);

					//
					// Get the values of the <name> and <length> tags within
					// each <playlist-item>
					// and put them into the Map<String, Object> Object...
					//
					playListName = getTextValue(playlistItem_nl_level1, "name");
					playListLength = getTextValue(playlistItem_nl_level1, "length");

					log.debug("Item no:" + i + ", Name:" + playListName + ", Length:" + playListLength);

					// Map to store individual playlist item...
					Map<String, Object> playListInfo = new HashMap<String, Object>();
					playListInfo.put("Name", playListName);
					playListInfo.put("Length", playListLength);

					playListContainer.put(Integer.toString(i), playListInfo);
				}
				log.debug("End xml read...");
				log.debug("-----------------");
			}
		} catch (IOException ioe) {
			log.debug("An error occurred.", ioe);
		}

		// return the playListContainer Map object that will be loaded into our
		// Transient Shared Object in initTransientSharedObject() method

		return playListContainer;

	}

	/**
	 * This method takes in an XML string and returns a DOM...
	 *
	 * @param element
	 *            <code>String</code> name of the file
	 * @return A DOM object created by the SAX parser
	 *
	 */
	public Document stringToDoc(String str) throws IOException {
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return db.parse(new InputSource(new StringReader(str)));
		} catch (Exception ex) {
			log.debug("Error in stringToDoc() converting from xml sting to xml doc " + ex.toString());
			return null;
		}
	}
}