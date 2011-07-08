package com.mscg.emule.bean;

import java.util.List;

import android.util.Log;

import com.mscg.emule.util.Util;

public class DownloadBean {

	private DownloadState state;
	private String title;
	private Integer typeResource;
	private Integer commentStatusResource;
	private List<String> downloadCompleteInfo;
	private String size;
	private String completed;
	private String speed;
	private String sources;
	private String priority;
	private String category;

	public DownloadBean() {
		this(null, null, null, null, null, null, null, null, null, null, null);
	}

	public DownloadBean(DownloadState state, String title,
						Integer typeResource, Integer commentStatusResource,
						List<String> downloadCompleteInfo, String size, String completed,
						String speed, String sources, String priority, String category) {
		this.state = state;
		this.title = title;
		this.typeResource = typeResource;
		this.commentStatusResource = commentStatusResource;
		this.downloadCompleteInfo = downloadCompleteInfo;
		this.size = size;
		this.completed = completed;
		this.speed = speed;
		this.sources = sources;
		this.priority = priority;
		this.category = category;
	}

	public DownloadState getState() {
		return state;
	}

	public void setState(DownloadState state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getTypeResource() {
		return typeResource;
	}

	public void setTypeResource(Integer typeResource) {
		this.typeResource = typeResource;
	}

	public Integer getCommentStatusResource() {
		return commentStatusResource;
	}

	public void setCommentStatusResource(Integer commentStatusResource) {
		this.commentStatusResource = commentStatusResource;
	}

	public void setTypeResourceFromName(String typeResourceName) {
		try {
			setTypeResource(Util.getDrawableIDByName(typeResourceName));
		} catch (Exception e) {
			Log.e(this.getClass().getCanonicalName(), "Cannot get resource value", e);
		}
	}

	public List<String> getDownloadCompleteInfo() {
		return downloadCompleteInfo;
	}

	public void setDownloadCompleteInfo(List<String> downloadCompleteInfo) {
		this.downloadCompleteInfo = downloadCompleteInfo;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getSources() {
		return sources;
	}

	public void setSources(String sources) {
		this.sources = sources;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
