// OCRScanner.java
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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.roncemer.ocr.tracker.MediaTrackerProxy;

/**
  * OCR document scanner.
  * @author Ronald B. Cemer
  */
public class OCRScanner extends DocumentScannerListenerAdaptor {

	//private static final Log LOG = LogFactory.getLog(OCRScanner.class);

	private static final int BEST_MATCH_STORE_COUNT = 8;
	private StringBuffer decodeBuffer = new StringBuffer();
	private CharacterRange[] acceptableChars;
	private boolean beginningOfRow = false;
	private boolean firstRow = false;
	private String newline = System.getProperty("line.separator");
	private HashMap trainingImages = new HashMap();
	private Character[] bestChars = new Character[BEST_MATCH_STORE_COUNT];
	private double[] bestMSEs = new double[BEST_MATCH_STORE_COUNT];

	protected DocumentScanner documentScanner = new DocumentScanner();

	/**
      * Add training images to the training set.
      * @param images A <code>HashMap</code> using <code>Character</code>s for
      * the keys.  Each value is an <code>ArrayList</code> of
      * <code>TrainingImages</code> for the specified character.  The training
      * images are added to any that may already have been loaded.
      */
	public void addTrainingImages(HashMap images) {
		for (Iterator it = images.keySet().iterator(); it.hasNext();) {
			Character key = (Character) (it.next());
			ArrayList al = (ArrayList) (images.get(key));
			ArrayList oldAl = (ArrayList) (trainingImages.get(key));
			if (oldAl == null) {
				oldAl = new ArrayList();
				trainingImages.put(key, oldAl);
			}
			for (int i = 0; i < al.size(); i++) oldAl.add(al.get(i));
		}
	}

	public void beginRow(PixelImage pixelImage, int y1, int y2) {
		beginningOfRow = true;
		if (firstRow) {
			firstRow = false;
		} else {
			decodeBuffer.append(newline);
		}
	}

	/**
      * Remove all training images from the training set.
      */
	public void clearTrainingImages() {
		trainingImages.clear();
	}

	/**
      * @return The <code>DocumentScanner</code> instance that is used to scan the document(s).
      * This is useful if the caller wants to adjust some of the scanner's parameters.
      */
	public DocumentScanner getDocumentScanner() {
		return documentScanner;
	}

	private final boolean isTrainingImageACandidate(
		float aspectRatio,
		int w,
		int h,
		float
		topWhiteSpaceFraction,
		float
		bottomWhiteSpaceFraction,
		TrainingImage ti) {
		// The aspect ratios must be within tolerance.
		if (((aspectRatio / ti.aspectRatio) - 1.0f) > TrainingImage.ASPECT_RATIO_TOLERANCE) {
			return false;
		}
		if (((ti.aspectRatio / aspectRatio) - 1.0f) > TrainingImage.ASPECT_RATIO_TOLERANCE) {
			return false;
		}
		// The top whitespace fractions must be within tolerance.
		if (Math.abs(topWhiteSpaceFraction - ti.topWhiteSpaceFraction) >
			TrainingImage.TOP_WHITE_SPACE_FRACTION_TOLERANCE) {
			return false;
		}
		// The bottom whitespace fractions must be within tolerance.
		if (Math.abs(bottomWhiteSpaceFraction - ti.bottomWhiteSpaceFraction) >
			TrainingImage.BOTTOM_WHITE_SPACE_FRACTION_TOLERANCE) {
			return false;
		}
		// If the area being scanned is really small and we
		// are about to crunch down a training image by a huge
		// factor in order to compare to it, then don't do that.
		if ((w <= 4) && (ti.width >= (w * 10))) return false;
		if ((h <= 4) && (ti.height >= (h * 10))) return false;
		// If the area being scanned is really large and we
		// are about to expand a training image by a huge
		// factor in order to compare to it, then don't do that.
		if ((ti.width <= 4) && (w >= (ti.width * 10))) return false;
		if ((ti.height <= 4) && (h >= (ti.height * 10))) return false;
		return true;
	}

	public void processChar(
		PixelImage pixelImage,
		int x1,
		int y1,
		int x2,
		int y2,
		int rowY1,
		int rowY2) {

		int[] pixels = pixelImage.pixels;
		int w = pixelImage.width;
		int h = pixelImage.height;
		int areaW = x2 - x1, areaH = y2 - y1;
		float aspectRatio = ((float) areaW) / ((float) areaH);
		int rowHeight = rowY2 - rowY1;
		float topWhiteSpaceFraction = (float)(y1-rowY1) / (float)rowHeight;
		float bottomWhiteSpaceFraction = (float)(rowY2-y2) / (float)rowHeight;
		Iterator it;
		if (acceptableChars != null) {
			ArrayList al = new ArrayList();
			for (int cs = 0; cs < acceptableChars.length; cs++) {
				CharacterRange cr = acceptableChars[cs];
				for (int c = cr.min; c <= cr.max; c++) {
					Character ch = new Character((char) c);
					if (al.indexOf(ch) < 0)
						al.add(ch);
				}
			}
			it = al.iterator();
		} else {
			it = trainingImages.keySet().iterator();
		}
		int bestCount = 0;
		while (it.hasNext()) {
			Character ch = (Character) (it.next());
			ArrayList al = (ArrayList) (trainingImages.get(ch));
			int nimg = al.size();
			if (nimg > 0) {
				double mse = 0.0;
				boolean gotAny = false;
				for (int i = 0; i < nimg; i++) {
					TrainingImage ti = (TrainingImage) (al.get(i));
					if (isTrainingImageACandidate(
						aspectRatio,
						areaW,
						areaH,
						topWhiteSpaceFraction,
						bottomWhiteSpaceFraction,
						ti)) {
						double thisMSE = ti.calcMSE(pixels, w, h, x1, y1, x2, y2);
						if ((!gotAny) || (thisMSE < mse)) {
							gotAny = true;
							mse = thisMSE;
						}
					}
				}
/// Maybe mse should be required to be below a certain threshold before we store it.
/// That would help us to handle things like welded characters, and characters that get improperly
/// split into two or more characters.
				if (gotAny) {
					boolean inserted = false;
					for (int i = 0; i < bestCount; i++) {
						if (mse < bestMSEs[i]) {
							for (int j = Math.min(bestCount, BEST_MATCH_STORE_COUNT-1); j>i; j--) {
								int k = j-1;
								bestChars[j] = bestChars[k];
								bestMSEs[j] = bestMSEs[k];
							}
							bestChars[i] = ch;
							bestMSEs[i] = mse;
							if (bestCount < BEST_MATCH_STORE_COUNT) bestCount++;
							inserted = true;
							break;
						}
					}
					if ( (!inserted) && (bestCount < BEST_MATCH_STORE_COUNT) ) {
						bestChars[bestCount] = ch;
						bestMSEs[bestCount] = mse;
						bestCount++;
					}
				}
			}
		}
///
///for (int i = 0; i < bestCount; i++) {
///    if (i > 0) System.out.print(' ');
///    System.out.print(bestChars[i]);
///    System.out.print('=');
///    System.out.print(bestMSEs[i]);
///}
///System.out.println();

/// We could also put some aspect ratio range checking into the page scanning logic (but only when
/// decoding; not when loading training images) so that the aspect ratio of a non-empty character
/// block is limited to within the min and max of the aspect ratios in the training set.
		if (bestCount > 0) {
///
///System.out.print(bestChars[0].charValue());
///System.out.flush();
			decodeBuffer.append(bestChars[0].charValue());
/*
for (int i = 0; i < bestCount; i++) {
    if (i > 0) System.out.print(" ");
    System.out.print("\'"+bestChars[i]+"\':"+bestMSEs[i]);
}
System.out.println();
*/
		}
	}

	public void processSpace(PixelImage pixelImage, int x1, int y1, int x2, int y2) {
///
///System.out.print(' ');
///System.out.flush();
		decodeBuffer.append(' ');
	}

	/**
     * Scan an image and return the decoded text.
     * @param imageFile The {@link File} containing the image to be scanned.
     * @param x1 The leftmost pixel position of the area to be scanned, or
     * <code>0</code> to start scanning at the left boundary of the image.
     * @param y1 The topmost pixel position of the area to be scanned, or
     * <code>0</code> to start scanning at the top boundary of the image.
     * @param x2 The rightmost pixel position of the area to be scanned, or
     * <code>0</code> to stop scanning at the right boundary of the image.
     * @param y2 The bottommost pixel position of the area to be scanned, or
     * <code>0</code> to stop scanning at the bottom boundary of the image.
     * @param acceptableChars An array of <code>CharacterRange</code> objects
     * representing the ranges of characters which are allowed to be decoded,
     * or <code>null</code> to not limit which characters can be decoded.
     * @param g An optional <code>Graphics</code> object onto which border lines
     * will be drawn to denote the borders of rows of text and character cells.
     * This is used for debugging and can be <code>null</code>.
     * @return The decoded text.
	 * @throws IOException If an error occurs while reading the input file.
     */
	public String scan(
		File imageFile,
		int x1,
		int y1,
		int x2,
		int y2,
		CharacterRange[]acceptableChars, Graphics g) throws IOException {

		Image image = Toolkit.getDefaultToolkit().createImage(imageFile.getCanonicalPath());
		MediaTracker mt = new MediaTrackerProxy(null);
		mt.addImage(image, 0);
		try {
			mt.waitForAll();
		} catch(InterruptedException ex) {}

		return scan(image, x1, y1, x2, y2, acceptableChars, g);
	}

	/**
      * Scan an image and return the decoded text.
      * @param image The {@link Image} to be scanned.
      * @param x1 The leftmost pixel position of the area to be scanned, or
      * <code>0</code> to start scanning at the left boundary of the image.
      * @param y1 The topmost pixel position of the area to be scanned, or
      * <code>0</code> to start scanning at the top boundary of the image.
      * @param x2 The rightmost pixel position of the area to be scanned, or
      * <code>0</code> to stop scanning at the right boundary of the image.
      * @param y2 The bottommost pixel position of the area to be scanned, or
      * <code>0</code> to stop scanning at the bottom boundary of the image.
      * @param acceptableChars An array of <code>CharacterRange</code> objects
      * representing the ranges of characters which are allowed to be decoded,
      * or <code>null</code> to not limit which characters can be decoded.
      * @param g An optional {@link Graphics} object onto which border lines
      * will be drawn to denote the borders of rows of text and character cells.
      * This is used for debugging and can be <code>null</code>.
      * @return The decoded text.
      */
	public String scan(
		Image image,
		int x1,
		int y1,
		int x2,
		int y2,
		CharacterRange[]acceptableChars, Graphics g) {

		this.acceptableChars = acceptableChars;
		PixelImage pixelImage = new PixelImage(image);
		pixelImage.toGrayScale(true);
		pixelImage.filter();
		decodeBuffer.setLength(0);
		firstRow = true;
		documentScanner.scan(pixelImage, this, x1, y1, x2, y2, g);
		String result = decodeBuffer.toString();
		decodeBuffer.setLength(0);
		return result;
	}

	/**
	 * Trains this scanner with the provided images. Each image is associated to a character range
	 * in order to tell the scanner how to interpret images.
	 *
	 * @param component A {@link Component} that will be used to load images. If <code>null</code>
	 * an alternative way to load images will be used.
	 * @param trainingSet A <code>{@link Map}&lt;String, {@link CharacterRange}&gt;</code> that
	 * maps file names pointing to images that will be used to train the scanner to the corresponding
	 * character range represented in the image.
	 * @param cleanBeforeTrain A boolean switch to force to clean the old training set of this scanner.
	 * @throws IOException If an error occurs while reading training files.
	 */
	public void train(Component component, Map<String, CharacterRange> trainingSet, boolean cleanBeforeTrain) throws IOException {
		if(cleanBeforeTrain)
			clearTrainingImages();

		HashMap images = new HashMap();
		TrainingImageLoader loader = new TrainingImageLoader();

		for(Map.Entry<String, CharacterRange> entry : trainingSet.entrySet()) {
			loader.load(component, entry.getKey(), entry.getValue(), images);
		}

		addTrainingImages(images);
	}
}
