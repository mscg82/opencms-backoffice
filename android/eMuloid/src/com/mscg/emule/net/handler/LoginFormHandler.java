package com.mscg.emule.net.handler;

import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mscg.emule.util.Constants;
import com.mscg.io.InputStreamDataReadListener;

public class LoginFormHandler extends GenericNetHandler {

	private static final Pattern sessionIDPattern = Pattern.compile("^.*ses=(.*?)(&.*)?$");

	public LoginFormHandler(Handler handler, boolean localCache) {
		super(handler, localCache);
	}

	public LoginFormHandler(Handler handler, CleanerProperties props, boolean localCache, InputStreamDataReadListener dataListener) {
		super(handler, props, localCache, dataListener);
	}

	public LoginFormHandler(Handler handler, CleanerProperties props) {
		super(handler, props);
	}

	public LoginFormHandler(Handler handler) {
		super(handler);
	}

	@Override
	public void handleTag(TagNode tagNode) throws Exception {
		String resp = xmlSerializer.getAsString(tagNode);
		InputSource source = new InputSource(new StringReader(resp));
		Document document = documentBuilder.parse(source);

		// check if there is a link in the response that leads to the dowload list
		NodeList downloadLink = (NodeList)xpath.evaluate(Constants.LOGGED_CHECK_XPATH, document, XPathConstants.NODESET);
		Message m = null;
		if(downloadLink.getLength() == 0) {
			// we are again in the login page => wrong password
			m = handler.obtainMessage(Constants.Messages.Login.BAD_PASSWORD);
		}
		else {
			// logged in. Get session id and change activity.
			Node node = downloadLink.item(0);
			String href = node.getAttributes().getNamedItem("href").getTextContent();
			Matcher matcher = sessionIDPattern.matcher(href);
			try {
				m = handler.obtainMessage(Constants.Messages.Login.LOGGED_IN);
				if(matcher.find())
					m.arg1 = Integer.parseInt(matcher.group(1));
				else
					throw new Exception("Cannot get session ID.");
			} catch(Exception e) {
				Log.e(this.getClass().getCanonicalName(), "Cannot get session ID", e);
				m = handler.obtainMessage(
						Constants.Messages.ERROR,
						Constants.Messages.ARG1_MESSAGE_STRING,
						-1, e.getMessage());
			}
		}
		handler.sendMessage(m);
	}
}
