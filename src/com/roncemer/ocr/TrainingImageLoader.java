// TrainingImageLoader.java
// Copyright (c) 2003-2009 Ronald B. Cemer
// All rights reserved.
/*
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, version 2 of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.roncemer.ocr;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.roncemer.ocr.tracker.MediaTrackerProxy;

/**
  * Utility class to load an image file, break it into individual characters,
  * and create and store multiple TrainingImage objects from the characters.
  * The source image must contain a range of characters specified in the
  * character range passed into the load() method.
  * @author Ronald B. Cemer
  */
public class TrainingImageLoader extends DocumentScannerListenerAdaptor {

	private static final Log LOG = LogFactory.getLog(TrainingImageLoader.class);

	private int charValue = 0;
	private Map dest;

	protected DocumentScanner documentScanner = new DocumentScanner();

	/**
      * @return The <code>DocumentScanner</code> instance that is used to load the training
      * images.  This is useful if the caller wants to adjust some of the scanner's parameters.
      */
	public DocumentScanner getDocumentScanner() {
		return documentScanner;
	}

	/**
     * Load an image containing training characters, break it up into
     * characters, and build a training set.
     * Each entry in the training set (a <code>Map</code>) has a key which
     * is a <code>Character</code> object whose value is the character code.
     * Each corresponding value in the <code>Map</code> is an
     * <code>ArrayList</code> of one or more <code>TrainingImage</code>
     * objects which contain images of the character represented in the key.
     * @param component A <code>Component</code> compatible with the image.
     * This is used to instantiate a <code>MediaTracker</code> to wait for the
     * image to load.
     * @param imageFileURL An {@link URL} object pointing to the image file.
     * @param charRange A <code>CharacterRange</code> object representing the
     * range of characters which is contained in this image.
     * @param dest A <code>Map</code> which gets loaded with the training
     * data.  Multiple calls to this method may be made with the same
     * <code>Map</code> to populate it with the data from several training
     * images.
     */
	public void load(Component component, URL imageFileURL, CharacterRange charRange, Map dest)
		throws IOException {

		Image image = Toolkit.getDefaultToolkit().createImage(imageFileURL);
		if (image == null) {
			throw new IOException("Cannot find training image file at " + imageFileURL.toString());
		}
		load(component, image, charRange, dest, imageFileURL.toString());
	}

	/**
      * Load an image containing training characters, break it up into
      * characters, and build a training set.
      * Each entry in the training set (a <code>Map</code>) has a key which
      * is a <code>Character</code> object whose value is the character code.
      * Each corresponding value in the <code>Map</code> is an
      * <code>ArrayList</code> of one or more <code>TrainingImage</code>
      * objects which contain images of the character represented in the key.
      * @param component A <code>Component</code> compatible with the image.
      * This is used to instantiate a <code>MediaTracker</code> to wait for the
      * image to load.
      * @param imageFilename The filename of the image to load.
      * @param charRange A <code>CharacterRange</code> object representing the
      * range of characters which is contained in this image.
      * @param dest A <code>Map</code> which gets loaded with the training
      * data.  Multiple calls to this method may be made with the same
      * <code>Map</code> to populate it with the data from several training
      * images.
      */
	public void load(Component component, String imageFilename, CharacterRange charRange, Map dest)
		throws IOException {

		File imageFile = new File(imageFilename);
		String imageFileUrlString = imageFile.getCanonicalPath();
		Image image = Toolkit.getDefaultToolkit().createImage(imageFileUrlString);
		if (image == null) {
			throw new IOException("Cannot find training image file at " + imageFileUrlString);
		}
		load(component, image, charRange, dest, imageFileUrlString);
	}

	public void load(
		Component component,
		Image image,
		CharacterRange charRange,
		Map dest,
		String imageFileUrlString)
		throws IOException {


		MediaTracker mt = new MediaTrackerProxy(component);
		mt.addImage(image, 0);
		try {
			mt.waitForAll();
		} catch(InterruptedException ex) {}

		PixelImage pixelImage = new PixelImage(image);
		pixelImage.toGrayScale(true);
		pixelImage.filter();
		charValue = charRange.min;
		this.dest = dest;
		documentScanner.scan(pixelImage, this, 0, 0, 0, 0, null);
		if (charValue != (charRange.max + 1)) {
			throw new IOException(
				"Expected to decode " +
				((charRange.max + 1) - charRange.min) +
				" characters but actually decoded " +
				(charValue - charRange.min) +
				" characters in training: " +
				imageFileUrlString);
		}
	}

	public void beginRow(PixelImage pixelImage, int y1, int y2) {
		if(LOG.isDebugEnabled())
			LOG.debug("TrainingImageLoader.beginRow " + y1 + "," + y2);
	}

	public void endRow(PixelImage pixelImage, int y1, int y2) {
		if(LOG.isDebugEnabled())
			LOG.debug("TrainingImageLoader.endRow " + y1 + "," + y2);
	}

	public void processChar
		(PixelImage pixelImage, int x1, int y1, int x2, int y2, int rowY1, int rowY2) {

		if(LOG.isDebugEnabled()) {
			LOG.debug(
				"TrainingImageLoader.processChar: \'" +
				(char) charValue + "\' " + x1 + "," + y1 + "-" + x2 + "," + y2);
		}
		int w = x2 - x1;
		int h = y2 - y1;
		int[] pixels = new int[w * h];
		for (int y = y1, destY = 0; y < y2; y++, destY++) {
			System.arraycopy(pixelImage.pixels, (y * pixelImage.width) + x1, pixels, destY * w, w);
		}
		if(LOG.isDebugEnabled()) {
			StringBuffer strBuf = new StringBuffer();
			for (int y = 0, idx = 0; y < h; y++) {
				for (int x = 0; x < w; x++, idx++) {
					strBuf.append((pixels[idx] > 0) ? ' ' : '*');
				}
				LOG.debug(strBuf.toString());
			}
			LOG.debug("-----------------------------------------------------");
		}
		Character chr = new Character((char) charValue);
		ArrayList al = (ArrayList) (dest.get(chr));
		if (al == null) {
			al = new ArrayList();
			dest.put(chr, al);
		}
		al.add(new TrainingImage(pixels, w, h, y1 - rowY1, rowY2 - y2));
		charValue++;
	}
}
