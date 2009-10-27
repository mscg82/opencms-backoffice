/**
 *
 */
package com.roncemer.ocr.tracker;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Giuseppe Miscione
 *
 */
public class MediaTrackerProxy extends MediaTracker {

	private static final long serialVersionUID = -3436899525138680606L;

	Component component;
	Map<Integer, Image> images;
	int maxTries;

	public MediaTrackerProxy(Component comp) {
		super(comp);
		component = comp;
		images = new LinkedHashMap<Integer, Image>();
		setMaxTries(100);
	}

	public int getMaxTries() {
		return maxTries;
	}

	public void setMaxTries(int maxTries) {
		this.maxTries = maxTries;
	}

	@Override
	public void addImage(Image image, int id) {
		super.addImage(image, id);
		images.put(id, image);
	}

	@Override
	public synchronized void addImage(Image image, int id, int w, int h) {
		super.addImage(image, id, w, h);
		images.put(id, image);
	}

	@Override
	public synchronized void removeImage(Image image) {
		super.removeImage(image);
		for(Map.Entry<Integer, Image> entry : images.entrySet()){
			if(entry.getValue().equals(image)){
				images.remove(entry.getKey());
				break;
			}
		}
	}

	@Override
	public synchronized void removeImage(Image image, int id) {
		super.removeImage(image, id);
		images.remove(id);
	}

	@Override
	public synchronized void removeImage(Image image, int id, int width, int height) {
		super.removeImage(image, id, width, height);
		images.remove(id);
	}

	@Override
	public void waitForAll() throws InterruptedException {
		if(component != null)
			super.waitForAll();
		else{
			for(Map.Entry<Integer, Image> entry : images.entrySet()){
				Image image = entry.getValue();
				int tries = 0;
				while(tries < maxTries && (image.getWidth(null) == -1 || image.getHeight(null) == -1)){
					tries++;
					Thread.sleep(20);
				}
			}
		}
	}

	@Override
	public synchronized boolean waitForAll(long ms) throws InterruptedException {
		if(component != null)
			return super.waitForAll(ms);
		else {
			waitForAll();
			return true;
		}
	}

}
