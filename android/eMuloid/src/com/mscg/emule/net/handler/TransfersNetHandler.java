package com.mscg.emule.net.handler;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathConstants;

import org.htmlcleaner.CleanerProperties;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mscg.emule.bean.CategoryBean;
import com.mscg.emule.bean.DownloadBean;
import com.mscg.emule.bean.DownloadState;
import com.mscg.emule.util.Constants;
import com.mscg.emule.util.Util;
import com.mscg.io.InputStreamDataReadListener;

public class TransfersNetHandler extends GenericSpeedInfoNetHandler {

	private Pattern categoryIDPattern = Pattern.compile(".*cat=(-?\\d+).*");

	public TransfersNetHandler(Handler handler, boolean localCache) {
		super(handler, localCache);
	}

	public TransfersNetHandler(Handler handler, CleanerProperties props, boolean localCache, InputStreamDataReadListener dataListener) {
		super(handler, props, localCache, dataListener);
	}

	public TransfersNetHandler(Handler handler, CleanerProperties props) {
		super(handler, props);
	}

	public TransfersNetHandler(Handler handler) {
		super(handler);
	}

	@Override
	public void handleDocument(Document document) throws Exception {
		Message m = null;
		// read the available categories
		NodeList downloadBoxTmp = (NodeList)xpath.evaluate("//table[./tbody/tr/td[@class='smallheader']][position()=1]/tbody", document, XPathConstants.NODESET);
		Node downloadBox = downloadBoxTmp.item(0);
		NodeList menuItems = (NodeList)xpath.evaluate("tr[position()=1]/td[position()=1]//div[@class='menuitems']//a", downloadBox, XPathConstants.NODESET);
		NodeList catLinks = (NodeList)xpath.evaluate("tr[position()=1]/td[position()=1]//div[@class='menuitems']//a/@href", downloadBox, XPathConstants.NODESET);
		NodeList catSelStatuses = (NodeList)xpath.evaluate("tr[position()=1]/td[position()=1]//div[@class='menuitems']//a/img/@src", downloadBox, XPathConstants.NODESET);
		List<CategoryBean> categories = new LinkedList<CategoryBean>();
		for(int i = 0, l = menuItems.getLength(); i < l; i++) {
			Node menuItem = menuItems.item(i);
			String catLink = catLinks.item(i).getTextContent();
			Matcher catIDMatcher = categoryIDPattern.matcher(catLink);
			if(catIDMatcher.find()) {
				try {
					Integer catID = Integer.parseInt(catIDMatcher.group(1));
					String catName = menuItem.getTextContent().trim();//Util.getNodesText((NodeList)xpath.evaluate("text()", menuItem, XPathConstants.NODESET)).trim();
					String catSelTxt = catSelStatuses.item(i).getTextContent();

					categories.add(new CategoryBean(
						catName, catID, catSelTxt.contains("checked.gif")));
				} catch(Exception e) {
					Log.w(this.getClass().getCanonicalName(), "Cannot create category bean.", e);
				}
			}
		}
		m = handler.obtainMessage(Constants.Messages.Transfers.UPDATE_CATEGORIES);
		m.obj = categories;
		handler.sendMessage(m);

		// read the downloads lines
		List<DownloadBean> downloads = new LinkedList<DownloadBean>();
		NodeList downloadStatuses = (NodeList)xpath.evaluate(
			"tr[./td[starts-with(./@class, 'down-line')]]/td[position()=1]/@class", downloadBox, XPathConstants.NODESET);
		NodeList downloadResTypes = (NodeList)xpath.evaluate(
			"tr[./td[starts-with(./@class, 'down-line')]]/td[position()=1]/table/tbody/tr/td[position()=3]/@background", downloadBox, XPathConstants.NODESET);
		NodeList downloadCommentStatuses = (NodeList)xpath.evaluate(
			"tr[./td[starts-with(./@class, 'down-line')]]/td[position()=1]/table/tbody/tr/td[position()=3]/img/@src", downloadBox, XPathConstants.NODESET);
		NodeList downloadInfoContainer = (NodeList)xpath.evaluate(
				"tr[./td[starts-with(./@class, 'down-line')]]/td[position()=1]/table/tbody/tr/td[position()=5]", downloadBox, XPathConstants.NODESET);
		NodeList downloadInfoSet = (NodeList)xpath.evaluate(
				"tr[./td[starts-with(./@class, 'down-line')]]/td[position()=1]/table/tbody/tr/td[position()=5]/div", downloadBox, XPathConstants.NODESET);
		NodeList downloadSizes = (NodeList)xpath.evaluate("tr[./td[starts-with(./@class, 'down-line')]]/td[position()=2]/text()", downloadBox, XPathConstants.NODESET);
		NodeList downloadCompletes = (NodeList)xpath.evaluate("tr[./td[starts-with(./@class, 'down-line')]]/td[position()=3]/text()", downloadBox, XPathConstants.NODESET);
		NodeList downloadSpeeds = (NodeList)xpath.evaluate("tr[./td[starts-with(./@class, 'down-line')]]/td[position()=5]/text()", downloadBox, XPathConstants.NODESET);
		NodeList downloadSources = (NodeList)xpath.evaluate("tr[./td[starts-with(./@class, 'down-line')]]/td[position()=6]/text()", downloadBox, XPathConstants.NODESET);
		NodeList downloadPriorities = (NodeList)xpath.evaluate("tr[./td[starts-with(./@class, 'down-line')]]/td[position()=7]/a/text()", downloadBox, XPathConstants.NODESET);
		NodeList downloadCategories = (NodeList)xpath.evaluate(
			"tr[./td[starts-with(./@class, 'down-line')]]/td[position()=8]/div[@class='menuitems']/a[./img[@src='checked.gif']]/text()", downloadBox, XPathConstants.NODESET);


		for(int i = 0, l = downloadStatuses.getLength(); i < l; i++) {
			String status = downloadStatuses.item(i).getTextContent();
			status = status.substring("down-line-".length());
			int index = status.lastIndexOf("-");
			if(index > 0)
				status = status.substring(0, index);

			String resType = downloadResTypes.item(i).getTextContent();
			resType = resType.substring(0, resType.lastIndexOf("."));
			Integer resTypeID = Util.getDrawableIDByName(resType);

			String commentStatus = downloadCommentStatuses.item(i).getTextContent();
			commentStatus = commentStatus.substring(0, commentStatus.lastIndexOf("."));
			Integer commentStatusID = Util.getDrawableIDByName(commentStatus);

			String title = Util.getNodesText((NodeList)xpath.evaluate("text()", downloadInfoContainer.item(i), XPathConstants.NODESET));

			List<String> downloadInfoStr = new LinkedList<String>();
			NodeList downloadInfos = (NodeList)xpath.evaluate("text()", downloadInfoSet.item(i), XPathConstants.NODESET);
			for(int j = 0, l2 = downloadInfos.getLength(); j < l2; j++) {
				downloadInfoStr.add(downloadInfos.item(j).getTextContent());
			}

			String size = downloadSizes.item(i).getTextContent().trim();
			String completed = downloadCompletes.item(i).getTextContent().trim();
			String speed = downloadSpeeds.item(i).getTextContent().trim();
			String sources = downloadSources.item(i).getTextContent().trim();
			String priority = downloadPriorities.item(i).getTextContent().trim();
			String category = downloadCategories.item(i).getTextContent().trim();

			downloads.add(new DownloadBean(
				DownloadState.fromString(status), title, resTypeID, commentStatusID,
				downloadInfoStr, size, completed, speed, sources, priority, category
			));
		}
		m = handler.obtainMessage(Constants.Messages.Transfers.UPDATE_DOWNLOADS, 0, 0, downloads);
		handler.sendMessage(m);

		m = handler.obtainMessage(Constants.Messages.UPDATE_TERMINATED);
		handler.sendMessage(m);
	}

}
