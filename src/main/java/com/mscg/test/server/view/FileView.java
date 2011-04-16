package com.mscg.test.server.view;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.view.AbstractView;

public class FileView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			                               HttpServletRequest request,
			                               HttpServletResponse response) throws Exception {

		OutputStream os = response.getOutputStream();
		InputStream source = (InputStream)model.get("file-source");
		if(source == null)
			throw new FileNotFoundException("Cannot find file source in model property \"file-source\"");

		try {
			Integer sourceSize = (Integer)model.get("file-size");
			String contentType = (String)model.get("content-type");
			String fileName = (String)model.get("file-name");

			if(contentType != null && contentType.trim().length() != 0)
				response.setContentType(contentType);
			response.addHeader("Content-Disposition", "attachment" +
				(fileName != null && fileName.trim().length() != 0 ? "; filename=\"" + fileName + "\"" : ""));
			if(sourceSize != null)
				response.setContentLength(sourceSize);
			IOUtils.copy(source, os);
		} finally {
			IOUtils.closeQuietly(source);
			try {
				os.flush();
			} catch(Exception e){}
		}
	}

}
