/**
 *
 */
package com.mscg.dyndns.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author Giuseppe Miscione
 *
 */
public class DynamicJspGenerationServlet extends HttpServlet {

	private static final long serialVersionUID = -5332653736779391398L;

	private static Logger log = Logger.getLogger(DynamicJspGenerationServlet.class);

	private String applicationContext;
	private String repositoryBase;
	private String workBase;

	private static List<String> includeRegExp;

	static{
		includeRegExp = new LinkedList<String>();
		includeRegExp.add("<jsp:include.*page=\\\"(.*)\\\".*/>");
		includeRegExp.add("<%@include.*file=\\\"(.*)\\\".*%>");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String completeUrl = req.getRequestURL().toString();
		String servletPath = req.getServletPath();
		int startIndex = completeUrl.indexOf(servletPath);
		String requestedRes = completeUrl.substring(startIndex + servletPath.length());
		log.debug("The requested resource is. " + requestedRes);

		try{
			exportJsp(requestedRes);

			RequestDispatcher rd = req.getRequestDispatcher(workBase + requestedRes);
			rd.forward(req, resp);
		} catch(FileNotFoundException e){
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void exportJsp(String requestedRes) throws FileNotFoundException, IOException{
		if(!requestedRes.startsWith("/"))
			requestedRes = "/" + requestedRes;
		File repositoryFile = new File(applicationContext + repositoryBase + requestedRes);
		if(!repositoryFile.exists()){
			throw new FileNotFoundException("Cannot find file \"" + repositoryFile + "\"");
		}

		File workFile = new File(applicationContext + workBase + requestedRes);
		File workFileDir = workFile.getParentFile();

		if(!workFileDir.exists()){
			workFileDir.mkdirs();
			log.debug("Build directory tree to \"" + workFileDir.getAbsolutePath() + "\".");
		}

		if(!workFile.exists()){
			workFile.createNewFile();
			log.debug("Created file \"" + workFile.getAbsolutePath() + "\".");
		}

		//copy binary data from repository to work directory
		copyData(repositoryFile, workFile);

		for(String includedJsp : getIncludedJsps(repositoryFile)){
			exportJsp(includedJsp);
		}
	}

	private Set<String> getIncludedJsps(File originalJsp) throws IOException{
		Set<String> includedJsp = new HashSet<String>();

		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;

		try{
			fis = new FileInputStream(originalJsp);
			baos = new ByteArrayOutputStream();
			copyData(fis, baos);
			String textContent = new String(baos.toByteArray(), "UTF-8");

			for(String regExpStr : includeRegExp){
				Pattern regExp = Pattern.compile(regExpStr);
				Matcher matcher = regExp.matcher(textContent);
				while(matcher.find()){
					String name = matcher.group(1);
					includedJsp.add(name);
				}
			}

		} finally{
			if(fis != null){
				try{
					fis.close();
				} catch(IOException e){}
			}

			if(baos != null){
				try{
					baos.flush();
				} catch(IOException e){}
			}

			if(baos != null){
				try{
					baos.close();
				} catch(IOException e){}
			}
		}

		return includedJsp;
	}

	private void copyData(InputStream input, OutputStream output) throws IOException{
		int bufferSize = 1024;
		byte buffer[] = new byte[bufferSize];
		int byteRead = 0;
		while(byteRead >= 0){
			byteRead = input.read(buffer, 0, bufferSize);
			if(byteRead > 0){
				output.write(buffer, 0, byteRead);
			}
		}

		output.flush();
	}

	private void copyData(File input, File output) throws IOException{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try{
			fis = new FileInputStream(input);
			fos = new FileOutputStream(output);

			copyData(fis, fos);

		} finally{
			if(fis != null){
				try{
					fis.close();
				} catch(IOException e){}
			}

			if(fos != null){
				try{
					fos.flush();
				} catch(IOException e){}
			}

			if(fos != null){
				try{
					fos.close();
				} catch(IOException e){}
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		applicationContext = config.getServletContext().getRealPath("/");
		if(applicationContext.endsWith("/"))
			applicationContext = applicationContext.substring(0, applicationContext.length() - 1);
		log.debug("Application context folder is \"" + applicationContext + "\".");

		repositoryBase = config.getInitParameter("repositoryBase");
		log.debug("Using \"" + repositoryBase + "\" as base folder for repository.");

		workBase = config.getInitParameter("workBase");
		log.debug("Using \"" + workBase + "\" as base work folder.");
	}
}
