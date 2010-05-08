/**
 *
 */
package com.mscg.httpinterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.util.Util;
import com.mscg.util.passwordreader.PasswordReader;
import com.mscg.util.passwordreader.PlainPasswordReader;
import com.roncemer.ocr.CharacterRange;
import com.roncemer.ocr.OCRScanner;

/**
 * @author Giuseppe Miscione
 *
 */
public class EatjTesterInterface extends AbstractHttpInterface implements TesterInterface {

	private static Logger LOG = Logger.getLogger(EatjTesterInterface.class);

	private int thinkTime;

	private String testUrl;
	private String userAgent;

	private String mainUrl;
	private String captchaUrl;
	private String formActionUrl;
	private String confirmUrl;
	private String accountUrl;
	private String restartUrl;
	private String logoutUrl;

	private String usernameParam;
	private String passwordParam;
	private String captchaParam;
	private String uuidParam;

	private Pattern uuidPattern;

	private String username;
	private String password;
	private PasswordReader pwdReader;

	private String captchaFileName;
	private OCRScanner scanner;

	public EatjTesterInterface() throws ConfigurationException, ClassCastException, IOException {
		super();

		thinkTime = 1000;
		try{
			thinkTime = Integer.parseInt((String) config.get(ConfigLoader.EATJ_THINKTIME));
		} catch(NumberFormatException e){}

		userAgent = (String) config.get(ConfigLoader.EATJ_USERAGENT);
		testUrl   = (String) config.get(ConfigLoader.EATJ_TESTURL);

		String prefix = (String) config.get(ConfigLoader.EATJ_URL_PREFIX);
		mainUrl       = prefix + (String) config.get(ConfigLoader.EATJ_URL_MAIN);
		captchaUrl    = prefix + (String) config.get(ConfigLoader.EATJ_URL_CAPTCHA);
		formActionUrl = prefix + (String) config.get(ConfigLoader.EATJ_URL_FORM);
		confirmUrl    = prefix + (String) config.get(ConfigLoader.EATJ_URL_CONFIRM);
		accountUrl    = prefix + (String) config.get(ConfigLoader.EATJ_URL_ACCOUNT);
		restartUrl    = prefix + (String) config.get(ConfigLoader.EATJ_URL_RESTART);
		logoutUrl     = prefix + (String) config.get(ConfigLoader.EATJ_URL_LOGOUT);

		usernameParam = (String) config.get(ConfigLoader.EATJ_PARAM_USERNAME);
		passwordParam = (String) config.get(ConfigLoader.EATJ_PARAM_PASSWORD);
		captchaParam  = (String) config.get(ConfigLoader.EATJ_PARAM_CAPTCHA);
		uuidParam     = (String) config.get(ConfigLoader.EATJ_PARAM_UUID);
		String regExp = "<input.*id=\"" + uuidParam + "\".*name=\"" + uuidParam + "\".*value=\"(.*)\".*/>";
		uuidPattern = Pattern.compile(regExp);

		username = (String) config.get(ConfigLoader.EATJ_VALUES_USERNAME);
		password = (String) config.get(ConfigLoader.EATJ_VALUES_PASSWORD);

		String pwdReaderClassName = (String) config.get(ConfigLoader.EATJ_VALUES_PWDREADER);
		pwdReader = (PasswordReader) Util.loadClass(pwdReaderClassName, this.getClass().getClassLoader());
		if(pwdReader == null){
			// default password reader
			pwdReader = new PlainPasswordReader();
		}

		captchaFileName = (String) config.get(ConfigLoader.CAPTCHA_TMP_FILE);
		scanner = new OCRScanner();
		trainScanner();
	}

	private void trainScanner() throws ClassCastException, IOException {
		Map<Object, CharacterRange> trainingset = new LinkedHashMap<Object, CharacterRange>();

		List<String> trainingSets = Util.readConfigurationList(ConfigLoader.CAPTCHA_TRAINING_SETS);
		for(String trainingSet : trainingSets) {
			String trainingSetParam = ConfigLoader.CAPTCHA_PARAMS + "." + trainingSet;
			List<String> elements = Util.readConfigurationList(trainingSetParam + ".elements");
			for(String element : elements) {
				String elementParam = trainingSetParam + "." + element;

				String chars = Util.readConfigurationString(elementParam + ".chars");
				if(chars.length() == 2){
					CharacterRange cr = new CharacterRange(chars.charAt(0), chars.charAt(1));

					String filePath = Util.readConfigurationString(elementParam + ".url");
					URL fileUrl = (filePath == null ? null : Util.readResource(filePath, this.getClass().getClassLoader()));
					if(fileUrl != null){
						// read the image from resource URL
						trainingset.put(fileUrl, cr);
					}
					else{
						// read the image from file
						filePath = Util.readConfigurationString(elementParam + ".file");
						if(filePath != null){
							File imgFile = new File(filePath);
							if(imgFile.exists())
								trainingset.put(imgFile.getCanonicalPath(), cr);
						}
					}
				}
			}
		}

		scanner.train(null, trainingset, true);
	}

	/* (non-Javadoc)
	 * @see com.mscg.httpinterface.TesterInterface#testIfServerIsRunning()
	 */
	public boolean testIfServerIsRunning() throws HttpException, IOException{
		boolean ret = false;
		strUrl = prepareUrl(testUrl);
		LOG.debug("Testign connection on url: " + strUrl);
		httpGet = null;
		try{
			httpGet = new GetMethod(strUrl);
			if(userAgent != null && userAgent.trim().length() != 0)
				httpGet.setRequestHeader("User-Agent", userAgent);
			client.executeMethod(httpGet);
			// server is running if the http status code is 200/OK and no redirect has been made
			if (httpGet.getStatusCode() == HttpStatus.SC_OK && httpGet.getPath().endsWith(testUrl)) {
				ret = true;
			}
		} finally{
			if(httpGet != null){
				httpGet.releaseConnection();
				httpGet = null;
			}
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see com.mscg.httpinterface.TesterInterface#startServer()
	 */
	public boolean startServer() throws HttpException, IOException{
		boolean ret = false;

		httpGet = new GetMethod();
		httpPost = new PostMethod();

		if(userAgent != null && userAgent.trim().length() != 0){
			httpGet.setRequestHeader("User-Agent", userAgent);
			httpPost.setRequestHeader("User-Agent", userAgent);
		}


		File captchaFile = null;
		try{
			// call the main page to obtain a new session on eatj
			LOG.trace("Calling " + mainUrl);
			httpGet.setURI(new URI(mainUrl, true));
			client.executeMethod(httpGet);
			if(httpGet.getStatusCode() != HttpStatus.SC_OK)
				return ret;
			String responseBody = httpGet.getResponseBodyAsString();
			String uuid = null;
			Matcher uuidMatcher = uuidPattern.matcher(responseBody);
			if(uuidMatcher.find()){
				uuid = uuidMatcher.group(1);
			}
			LOG.trace("Called " + mainUrl);

			// Thinking...
			try {
				Thread.sleep(thinkTime);
			} catch (InterruptedException e) { }

			// call the captcha url and save the image in a temp file
			LOG.trace("Calling " + captchaUrl);
			httpGet.setURI(new URI(captchaUrl, true));
			client.executeMethod(httpGet);
			if(httpGet.getStatusCode() != HttpStatus.SC_OK)
				return ret;
			captchaFile = new File(captchaFileName);
			OutputStream os = null;
			InputStream is = null;
			try{
				os = new FileOutputStream(captchaFile);
				is = httpGet.getResponseBodyAsStream();
				Util.copyData(is, os, 10240);
			} finally{
				Util.safeFlushAndClose(os);
				Util.safeFlushAndClose(is);
			}
			LOG.trace("Called " + captchaUrl);

			// decode captcha
			String captchaText = scanner.scan(captchaFile, 0, 0, 0, 0, null, null);
			LOG.trace("Decoded captcha: " + captchaText);

			// Thinking...
			try {
				Thread.sleep(thinkTime);
			} catch (InterruptedException e) { }

			// send login form
			LOG.trace("Posting " + formActionUrl);
			httpPost.setURI(new URI(formActionUrl, true));
			httpPost.setParameter(usernameParam, username);
			httpPost.setParameter(passwordParam, pwdReader.readPassword(password));
			httpPost.setParameter(captchaParam, captchaText);
			if(uuid != null)
				httpPost.setParameter(uuidParam, uuid);
			client.executeMethod(httpPost);
			String resultLocation = null;
			Header locationHeader = httpPost.getResponseHeader("Location");
			if(locationHeader != null)
				resultLocation = locationHeader.getValue();
			if(resultLocation == null || !resultLocation.contains(confirmUrl))
				return ret;
			LOG.trace("Posted " + formActionUrl);

			// Thinking...
			try {
				Thread.sleep(thinkTime);
			} catch (InterruptedException e) { }


			// load the wait login page
			LOG.trace("Calling " + resultLocation);
			httpGet.setURI(new URI(resultLocation, true));
			client.executeMethod(httpGet);
			if(httpGet.getStatusCode() != HttpStatus.SC_OK)
				return ret;
			responseBody = httpGet.getResponseBodyAsString();
			LOG.trace("Called " + resultLocation);

			// Thinking...
			try {
				Thread.sleep(thinkTime);
			} catch (InterruptedException e) { }

			// load the account page
			LOG.trace("Calling " + accountUrl);
			httpGet.setURI(new URI(accountUrl, true));
			client.executeMethod(httpGet);
			if(httpGet.getStatusCode() != HttpStatus.SC_OK)
				return ret;
			responseBody = httpGet.getResponseBodyAsString();
			LOG.trace("Called " + accountUrl);

			// Thinking...
			try {
				Thread.sleep(thinkTime);
			} catch (InterruptedException e) { }

			// restart the server
			LOG.trace("Calling " + restartUrl);
			httpGet.setURI(new URI(restartUrl, true));
			client.executeMethod(httpGet);
			if(httpGet.getStatusCode() != HttpStatus.SC_OK)
				return ret;
			responseBody = httpGet.getResponseBodyAsString();
			LOG.trace("Called " + restartUrl);

			// Thinking...
			try {
				Thread.sleep(thinkTime);
			} catch (InterruptedException e) { }

			// logout
			LOG.trace("Calling " + logoutUrl);
			httpGet.setURI(new URI(logoutUrl, true));
			client.executeMethod(httpGet);
			if(httpGet.getStatusCode() != HttpStatus.SC_OK)
				return ret;
			responseBody = httpGet.getResponseBodyAsString();
			LOG.trace("Called " + logoutUrl);

			// Thinking...
			try {
				Thread.sleep(thinkTime);
			} catch (InterruptedException e) { }

			// create new client
			renewClient();

			// all ok, return positive feedback
			ret = true;

		} finally{
			if(captchaFile != null){
				try{
					captchaFile.delete();
				} catch(Exception e){}
			}

			if(httpGet != null){
				httpGet.releaseConnection();
				httpGet = null;
			}

			if(httpPost != null){
				httpPost.releaseConnection();
				httpPost = null;
			}
		}

		return ret;
	}

}
