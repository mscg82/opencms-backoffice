package com.classmeteo.data;

/**
 * This class extends the Thread class adding a property
 * to notified the thread that it's action has been stopped.
 * 
 * @author Giuseppe Miscione
 *
 */
public class InterruptibleThread extends Thread {

	private boolean interrupted;

	/**
	 * @return the interrupted
	 */
	public synchronized boolean isInterrupted() {
		return interrupted;
	}

	/**
	 * @param interrupted the interrupted to set
	 */
	public synchronized void setInterrupted(boolean interrupted) {
		this.interrupted = interrupted;
	}
}
