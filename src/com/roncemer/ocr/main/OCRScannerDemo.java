// OCRScannerDemo.java
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

package com.roncemer.ocr.main;

import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.ScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.roncemer.ocr.CharacterRange;
import com.roncemer.ocr.OCRImageCanvas;
import com.roncemer.ocr.OCRScanner;
import com.roncemer.ocr.PixelImage;
import com.roncemer.ocr.TrainingImageLoader;
import com.roncemer.ocr.tracker.MediaTrackerProxy;

/**
  * Demo application to demonstrate OCR document scanning and decoding.
  * @author Ronald B. Cemer
  */
public class OCRScannerDemo extends Frame {

	private static final long serialVersionUID = -8030499184564680363L;

	private boolean debug = true;

	private Image image;
	private OCRImageCanvas imageCanvas;
	private OCRScanner scanner;

	public OCRScannerDemo() {
		super("OCR from a scanned image");
		setSize(1024, 768);
		ScrollPane scrollPane = new ScrollPane();
		imageCanvas = new OCRImageCanvas();
		scrollPane.add(imageCanvas);
		add(scrollPane);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				((Frame)(e.getSource())).hide();
				System.exit(0);
			}
		});
		scanner = new OCRScanner();
		show();
	}

	/**
      * Load demo training images.
      * @param trainingImageDir The directory from which to load the images.
      */
	public void loadTrainingImages(String trainingImageDir) {
		if (debug) System.err.println("loadTrainingImages(" + trainingImageDir + ")");
		if (!trainingImageDir.endsWith(File.separator)) {
			trainingImageDir += File.separator;
		}
		try {
			scanner.clearTrainingImages();
			TrainingImageLoader loader = new TrainingImageLoader();
			HashMap images = new HashMap();
			if (debug) System.err.println("ascii.png");
			loader.load(
				this,
				trainingImageDir + "ascii.png",
				new CharacterRange('!', '~'),
				images);
			if (debug) System.err.println("hpljPica.jpg");
			loader.load(
				this,
				trainingImageDir + "hpljPica.jpg",
				new CharacterRange('!', '~'),
				images);
			if (debug) System.err.println("digits.jpg");
			loader.load(
				this,
				trainingImageDir + "digits.jpg",
				new CharacterRange('0', '9'),
				images);
			if (debug) System.err.println("adding images");
			scanner.addTrainingImages(images);
			if (debug) System.err.println("loadTrainingImages() done");
		}
		catch(IOException ex) {
			ex.printStackTrace();
			System.exit(2);
		}
	}

	public void process(String imageFilename) {
		if (debug) System.err.println("process(" + imageFilename + ")");
		String imageFileUrlString = "";
		try {
			imageFileUrlString = new File(imageFilename).toURL().toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			ImageProducer imageProducer = (ImageProducer)
				(new URL(imageFileUrlString).getContent());
			image = createImage(imageProducer);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		if (image == null) {
			System.err.println("Cannot find image file at " + imageFileUrlString);
			return;
		}
		MediaTracker mt = new MediaTrackerProxy(this);
		mt.addImage(image, 0);
		try {
			mt.waitForAll();
		} catch(InterruptedException ex) {}
		if (debug) System.err.println("image loaded");

/*	int w = image.getWidth(null);
	int h = image.getHeight(null);
	if ( (w > 0) && (h > 0) ) {
	    float scaleFactor = 2048.0f/(float)Math.max(w, h);
	    if (scaleFactor < 1.0f) {
		image = image.getScaledInstance(
		    (int)((float)w*scaleFactor),
		    (int)((float)h*scaleFactor), Image.SCALE_SMOOTH);
		mt = new MediaTrackerProxy(this);
		mt.addImage(image, 0);
		try { mt.waitForAll(); } catch(InterruptedException ex) {}
	    }
	}*/

		imageCanvas.setSize(image.getWidth(null), image.getHeight(null));

		if (debug) System.err.println("constructing new PixelImage");
		PixelImage pixelImage = new PixelImage(image);
		if (debug) System.err.println("converting PixelImage to grayScale");
		pixelImage.toGrayScale(true);
		if (debug) System.err.println("filtering");
		pixelImage.filter();
		if (debug) System.err.println("setting image for display");
		imageCanvas.setImage(
			pixelImage.rgbToImage(
				pixelImage.grayScaleToRGB(pixelImage.pixels),
				pixelImage.width,
				pixelImage.height,
				imageCanvas));
		System.out.println(imageFilename + ":");
		String text = scanner.scan(image, 0, 0, 0, 0, null, imageCanvas.getGraphics());
		System.out.println("[" + text + "]");
	}

	public static void main(String[]args) {
		if (args.length < 1) {
			System.err.println("Please specify one or more image filenames.");
			System.exit(1);
		}
		String trainingImageDir = System.getProperty("TRAINING_IMAGE_DIR");
		if (trainingImageDir == null) {
			System.err.println
				("Please specify -DTRAINING_IMAGE_DIR=<dir> on " +
				 "the java command line.");
			return;
		}
		OCRScannerDemo demo = new OCRScannerDemo();
		demo.loadTrainingImages(trainingImageDir);
		for (int i = 0; i < args.length; i++)
			demo.process(args[i]);
		System.out.println("done.");
	}
}
