package com.mscg.actions.models;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestModel {

	private String filename;

	private Integer status;

	public String getContentType() {
		if(filename.endsWith(".pdf"))
			return "application/pdf";
		else if(filename.endsWith("png"))
			return "image/png";
		else
			return "plain/txt";
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	public Map<String, String> getHeaders() {
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put("test-header", "test-value");
		return headers;
	}

	public String getInputName() {
		return "fileStream";
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return (status != null ? status : 200);
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
}
