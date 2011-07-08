package com.mscg.net.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.net.ParseException;

import com.mscg.io.InputStreamDataReadListener;
import com.mscg.io.PositionNotifierInputStream;

public abstract class HtmlCleanerHandler implements AsynchResponseHandler<TagNode> {

	protected HtmlCleaner cleaner;
	protected CleanerProperties props;
	protected boolean localCache;
	protected InputStreamDataReadListener dataListener;
	//protected Pattern contentTypePattern = Pattern.compile("(.*);\\s*charset=(.*)");
	protected String encoding;
	protected boolean forceEncode;

	public HtmlCleanerHandler() {
		this(null, false, null);
	}

	public HtmlCleanerHandler(boolean localCache) {
		this(null, localCache, null);
	}

	public HtmlCleanerHandler(CleanerProperties props) {
		this(props, false, null);
	}

	public HtmlCleanerHandler(CleanerProperties props, boolean localCache, InputStreamDataReadListener dataListener) {
		super();
		setProps(props);
		setLocalCache(localCache);
		setDataListener(dataListener);
	}

	public abstract void afterCaching();

	public abstract void afterCleaning();

	public abstract boolean beforeCaching(HttpResponse response);

	public abstract boolean beforeCleaning(HttpResponse response);

	public InputStreamDataReadListener getDataListener() {
		return dataListener;
	}

	public CleanerProperties getProps() {
		return props;
	}

	public abstract void onBadResponse(HttpResponse response) throws IOException;

	@Override
	public TagNode handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		TagNode ret = null;

		HttpEntity entity = null;
		try {
			if(200 != response.getStatusLine().getStatusCode())
				onBadResponse(response);
			else {
				entity = response.getEntity();

				long responseSize = entity.getContentLength();
				if(responseSize < 0) {
					Header contentLength = response.getLastHeader(HTTP.CONTENT_LEN);
					try {
						responseSize = Long.parseLong(contentLength.getValue());
					} catch(Exception e){}
				}

				forceEncode = false;
//				Header contentType = response.getLastHeader(HTTP.CONTENT_TYPE);
//				encoding = HTTP.UTF_8;
//				if(contentType != null) {
//					Matcher contentTypeMatcher = contentTypePattern.matcher(contentType.getValue());
//					try {
//						contentTypeMatcher.find();
//						encoding = contentTypeMatcher.group(2);
//					} catch(Exception e){}
//				}

				encoding = null;
				try {
					encoding = EntityUtils.getContentCharSet(entity);
				} catch(ParseException e){}
				if(encoding == null)
					encoding = HTTP.UTF_8;

				InputStream is = entity.getContent();
				String contentEncoding = null;
				try {
					contentEncoding = response.getLastHeader(HTTP.CONTENT_ENCODING).getValue().toLowerCase();
				} catch(Exception e){}
				if("gzip".equals(contentEncoding)) {
					is = new GZIPInputStream(is);
				}

				if(beforeCaching(response) || localCache) {
					byte buffer[] = null;
					ByteArrayOutputStream bos = null;
					if(responseSize > 0)
						bos = new ByteArrayOutputStream((int)responseSize);
					else
						bos = new ByteArrayOutputStream();
					if(forceEncode) {
						IOUtils.copy(new InputStreamReader(is, encoding), new OutputStreamWriter(bos, encoding));
						String tmp = new String(bos.toByteArray(), encoding);
//						String tmp = EntityUtils.toString(entity, encoding);
						encoding = HTTP.UTF_8;
						buffer = tmp.getBytes(encoding);
					}
					else {
						if(dataListener != null)
							is = new PositionNotifierInputStream(is, entity.getContentLength(), dataListener);

						IOUtils.copy(is, bos);
						buffer = bos.toByteArray();
					}
					is = new ByteArrayInputStream(buffer);
					if(dataListener != null)
						is = new PositionNotifierInputStream(is, buffer.length, dataListener);

					afterCaching();
				}
				else {
					if(dataListener != null)
						is = new PositionNotifierInputStream(is, responseSize, dataListener);
				}

				if(beforeCleaning(response)) {
					// do parsing
					ret = cleaner.clean(is, encoding);
					afterCleaning();
				}
			}
		} finally {
			try {
				entity.consumeContent();
			} catch(Exception e){}
		}

		return ret;
	}

	@Override
	public final void handleResponseObject(TagNode response) throws Exception{
		// handle the cleaned document
		handleTag(response);
	}

	public abstract void handleTag(TagNode tagNode) throws Exception;

	public boolean isLocalCache() {
		return localCache;
	}

	public void setDataListener(InputStreamDataReadListener dataListener) {
		this.dataListener = dataListener;
	}

	public void setLocalCache(boolean localCache) {
		this.localCache = localCache;
	}

	public void setProps(CleanerProperties props) {
		this.props = props;
		cleaner = new HtmlCleaner(this.props);
	}

}
