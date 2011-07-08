package com.mscg.emule.net.handler;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CompactXmlSerializer;
import org.htmlcleaner.XmlSerializer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mscg.emule.R;
import com.mscg.emule.util.Constants;
import com.mscg.io.InputStreamDataReadListener;
import com.mscg.net.handler.HtmlCleanerHandler;

public abstract class GenericNetHandler extends HtmlCleanerHandler {

	protected Handler handler;
	protected XmlSerializer xmlSerializer;
	protected DocumentBuilder documentBuilder;
	protected XPath xpath;

	public GenericNetHandler(Handler handler) {
		this(handler, new CleanerProperties(), false, null);
	}

	public GenericNetHandler(Handler handler, boolean localCache) {
		this(handler, new CleanerProperties(), localCache, null);
	}

	public GenericNetHandler(Handler handler, CleanerProperties props, boolean localCache, InputStreamDataReadListener dataListener) {
		super(props, localCache, dataListener);
		this.handler = handler;
		xmlSerializer = new CompactXmlSerializer(props);

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setNamespaceAware(true);
		try {
			documentBuilder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			Log.e(this.getClass().getCanonicalName(), "Cannot get a document builder", e);
		}

		xpath = XPathFactory.newInstance().newXPath();
	}

	public GenericNetHandler(Handler handler, CleanerProperties props) {
		this(handler, props, false, null);
	}

	@Override
	public void afterCaching() {

	}

	@Override
	public void afterCleaning() {

	}

	@Override
	public boolean beforeCaching(HttpResponse response) {
		return false;
	}

	@Override
	public boolean beforeCleaning(HttpResponse response) {
		Message m = handler.obtainMessage(Constants.Messages.ANALYZING_RESPONSE);
		handler.sendMessage(m);
		return true;
	}

	@Override
	public void onBadResponse(HttpResponse response) throws IOException {
		Log.w(this.getClass().getCanonicalName(),
			"Bad response from server: " + response.getStatusLine().toString());
		Message m = handler.obtainMessage(
				Constants.Messages.ERROR,
				Constants.Messages.ARG1_MESSAGE_CODE,
				R.string.server_bad_response, null);
		handler.sendMessage(m);
	}

	@Override
	public void handleException(ClientProtocolException e) {
		Log.e(this.getClass().getCanonicalName(), "ClientProtocolException", e);
		Message m = handler.obtainMessage(
				Constants.Messages.ERROR,
				Constants.Messages.ARG1_MESSAGE_STRING,
				-1, e.getMessage());
		handler.sendMessage(m);
	}

	@Override
	public void handleException(IOException e) {
		Log.e(this.getClass().getCanonicalName(), "IOException", e);
		Message m = null;
		if(e instanceof UnknownHostException) {
			m = handler.obtainMessage(
					Constants.Messages.ERROR,
					Constants.Messages.ARG1_MESSAGE_CODE,
					R.string.unknown_host, null);
		}
		else {
			m = handler.obtainMessage(
					Constants.Messages.ERROR,
					Constants.Messages.ARG1_MESSAGE_CODE,
					R.string.load_failed, null);
		}
		handler.sendMessage(m);
	}

	@Override
	public void handleException(Exception e) {
		Log.e(this.getClass().getCanonicalName(), "Exception", e);
		Message m = handler.obtainMessage(
				Constants.Messages.ERROR,
				Constants.Messages.ARG1_MESSAGE_STRING,
				-1, e.getMessage());
		handler.sendMessage(m);
	}

	@Override
	public void startDownload() {
		Message m = handler.obtainMessage(Constants.Messages.START_DOWNLOAD);
		handler.sendMessage(m);
	}

}
