package com.mscg.emule.net.handler;

import java.io.StringReader;

import javax.xml.xpath.XPathConstants;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Handler;
import android.os.Message;

import com.mscg.emule.R;
import com.mscg.emule.bean.SpeedBean;
import com.mscg.emule.util.Constants;
import com.mscg.emule.util.Util;
import com.mscg.io.InputStreamDataReadListener;

public abstract class GenericSpeedInfoNetHandler extends GenericNetHandler {

	public GenericSpeedInfoNetHandler(Handler handler, boolean localCache) {
		super(handler, localCache);
	}

	public GenericSpeedInfoNetHandler(Handler handler, CleanerProperties props, boolean localCache, InputStreamDataReadListener dataListener) {
		super(handler, props, localCache, dataListener);
	}

	public GenericSpeedInfoNetHandler(Handler handler, CleanerProperties props) {
		super(handler, props);
	}

	public GenericSpeedInfoNetHandler(Handler handler) {
		super(handler);
	}

	@Override
	public void handleTag(TagNode tagNode) throws Exception {
		String resp = xmlSerializer.getAsString(tagNode);
		InputSource source = new InputSource(new StringReader(resp));
		Document document = documentBuilder.parse(source);

		// check if we are logged in
		NodeList downloadLink = (NodeList)xpath.evaluate(Constants.LOGGED_CHECK_XPATH, document, XPathConstants.NODESET);
		if(downloadLink.getLength() == 0) {
			// no more logged in, go to login screen
			Message m = handler.obtainMessage(Constants.Messages.NOT_LOGGED);
			handler.sendMessage(m);
		}
		else {
			// get server name and status
			NodeList serverInfoTmp = (NodeList)xpath.evaluate("//td[@background='main_topbar.gif']/table[position()=1]//td[position()=1]", document, XPathConstants.NODESET);
			Node serverInfo = serverInfoTmp.item(0);
			String serverStatus = (String)xpath.evaluate("img/@src", serverInfo, XPathConstants.STRING);
			NodeList serverText = (NodeList)xpath.evaluate("text()", serverInfo, XPathConstants.NODESET);
			String serverName = serverText.item(0).getTextContent().trim();
			if(serverName.endsWith("("))
				serverName = serverName.substring(0, serverName.length() - 1).trim();
			Message m = handler.obtainMessage(Constants.Messages.SpeedBox.UPDATE_SERVER, -1, -1, serverName);
			if("high.gif".equals(serverStatus))
				m.arg1 = R.drawable.server_high;
			else if("low.gif".equals(serverStatus))
				m.arg1 = R.drawable.server_low;
			if(m.arg1 >= 0)
				handler.sendMessage(m);

			// get kad status
			String kadStatus = (String)xpath.evaluate("//td[@background='main_topbar.gif']/table[position()=1]//td[position()=2]/a[position()=1]/@href", document, XPathConstants.STRING);
			m = handler.obtainMessage(Constants.Messages.SpeedBox.UPDATE_KAD, R.string.server_disconnected, -1, null);
			if(kadStatus.indexOf("disconnect") >= 0)
				m.arg1 = R.string.server_connected;
			else if(kadStatus.indexOf("rcfirewall") >= 0)
				m.arg1 = R.string.server_firewalled;
			handler.sendMessage(m);

			// update download/upload speed
			NodeList speedsBoxTmp = (NodeList)xpath.evaluate("//td[@background='main_topbar.gif']/table[position()=2]/tbody/tr", document, XPathConstants.NODESET);
			Node speedsBox = speedsBoxTmp.item(0);

			NodeList speedBoxTmp = (NodeList)xpath.evaluate("td[position()=1]", speedsBox, XPathConstants.NODESET);
			Node speedBox = speedBoxTmp.item(0);
			String downloadSpeed = Util.getNodesText((NodeList)xpath.evaluate("text()", speedBox, XPathConstants.NODESET));
			downloadSpeed = downloadSpeed.substring(downloadSpeed.indexOf(":") + 1).trim();
			String downloadPercStr = (String)xpath.evaluate("table//img/@width", speedBox, XPathConstants.STRING);
			downloadPercStr = downloadPercStr.trim();
			if(downloadPercStr.endsWith("%"))
				downloadPercStr = downloadPercStr.substring(0, downloadPercStr.length() - 1);
			double downloadPerc = 0.0;
			try {
				downloadPerc = Double.parseDouble(downloadPercStr);
			} catch(NumberFormatException e) {
				downloadPerc = Double.parseDouble(downloadPercStr.replace(".", ","));
			}
			if(downloadPerc > 100.0)
				downloadPerc = 100.0;
			m = handler.obtainMessage(Constants.Messages.SpeedBox.UPDATE_DOWNLOAD_SPEED, -1, -1,
				new SpeedBean(downloadSpeed, downloadPerc));
			handler.sendMessage(m);

			speedBoxTmp = (NodeList)xpath.evaluate("td[position()=2]", speedsBox, XPathConstants.NODESET);
			speedBox = speedBoxTmp.item(0);
			String uploadSpeed = Util.getNodesText((NodeList)xpath.evaluate("text()", speedBox, XPathConstants.NODESET));
			uploadSpeed = uploadSpeed.substring(uploadSpeed.indexOf(":") + 1).trim();
			String uploadPercStr = (String)xpath.evaluate("table//img/@width", speedBox, XPathConstants.STRING);
			uploadPercStr = uploadPercStr.trim();
			if(uploadPercStr.endsWith("%"))
				uploadPercStr = uploadPercStr.substring(0, uploadPercStr.length() - 1);
			double uploadPerc = 0.0;
			try {
				uploadPerc = Double.parseDouble(uploadPercStr);
			} catch(NumberFormatException e) {
				uploadPerc = Double.parseDouble(uploadPercStr.replace(".", ","));
			}
			if(uploadPerc > 100.0)
				uploadPerc = 100.0;
			m = handler.obtainMessage(Constants.Messages.SpeedBox.UPDATE_UPLOAD_SPEED, -1, -1,
					new SpeedBean(uploadSpeed, uploadPerc));
			handler.sendMessage(m);

			handleDocument(document);
		}
	}

	public abstract void handleDocument(Document document) throws Exception;

}
