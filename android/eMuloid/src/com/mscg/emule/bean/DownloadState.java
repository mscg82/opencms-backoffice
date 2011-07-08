/**
 * 
 */
package com.mscg.emule.bean;

public enum DownloadState {
	WAITING("waiting"),
	STOPPED("stopped"),
	PAUSED("paused"),
	DOWNLOADING("downloading"),
	COMPLETE("complete"),
	COMPLETING("completing");

	private String stateStr;

	private DownloadState(String stateStr) {
		this.stateStr = stateStr;
	}

	public static DownloadState fromString(String stateString) {
		for(DownloadState state : DownloadState.values()) {
			if(state.stateStr.contains(stateString))
				return state;
		}
		return null;
	}
}