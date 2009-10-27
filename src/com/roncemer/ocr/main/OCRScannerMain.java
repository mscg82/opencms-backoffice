/**
 *
 */
package com.roncemer.ocr.main;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.roncemer.ocr.CharacterRange;
import com.roncemer.ocr.OCRImageCanvas;
import com.roncemer.ocr.OCRScanner;
import com.roncemer.ocr.PixelImage;
import com.roncemer.ocr.TrainingImageLoader;
import com.roncemer.ocr.tracker.MediaTrackerProxy;

/**
 * @author Giuseppe Miscione
 *
 */
public class OCRScannerMain {

	private static Log LOG = LogFactory.getLog(OCRScannerMain.class);

	/**
     * Load demo training images.
     * @param trainingImageDir The directory from which to load the images.
     * @param scanner The OCRScanner that will be trained.
     */
	public static void loadTrainingImages(String trainingImageDir, OCRScanner scanner) {
		LOG.warn("loadTrainingImages(" + trainingImageDir + ")");
		if (!trainingImageDir.endsWith(File.separator)) {
			trainingImageDir += File.separator;
		}
		try {
			scanner.clearTrainingImages();
			TrainingImageLoader loader = new TrainingImageLoader();
			HashMap images = new HashMap();
			LOG.info("ascii.png");
			loader.load(
				null,
				trainingImageDir + "ascii.png",
				new CharacterRange('!', '~'),
				images);
			LOG.info("hpljPica.jpg");
			loader.load(
				null,
				trainingImageDir + "hpljPica.jpg",
				new CharacterRange('!', '~'),
				images);
			LOG.info("digits.jpg");
			loader.load(
				null,
				trainingImageDir + "digits.jpg",
				new CharacterRange('0', '9'),
				images);
			LOG.info("eatj_letter.jpg");
			loader.load(
				null,
				trainingImageDir + "eatj_letter.jpg",
				new CharacterRange('a', 'f'),
				images);
			LOG.info("eatj_number.jpg");
			loader.load(
					null,
					trainingImageDir + "eatj_number.jpg",
					new CharacterRange('0', '9'),
					images);
			LOG.info("adding images");
			scanner.addTrainingImages(images);
			LOG.warn("loadTrainingImages() done");
		}
		catch(IOException ex) {
			LOG.error("Error found while training scanner.", ex);
			System.exit(2);
		}
	}

	public static void process(String imageFilename, OCRScanner scanner) {
		LOG.warn("");
		LOG.warn("process(" + imageFilename + ")");
		String imageFileUrlString = "";
		Image image = null;

		File imageFile = new File(imageFilename);
		try{
			imageFileUrlString = imageFile.getCanonicalPath();

			if(!imageFile.exists())
				throw new FileNotFoundException("Cannot find image file at " + imageFileUrlString);

			image = Toolkit.getDefaultToolkit().createImage(imageFileUrlString);

			if (image == null)
				throw new FileNotFoundException("Cannot find image file at " + imageFileUrlString);

			MediaTracker mt = new MediaTrackerProxy(null);
			mt.addImage(image, 0);
			try {
				mt.waitForAll();
			} catch(InterruptedException ex) {}
			LOG.info("image loaded");

			OCRImageCanvas imageCanvas = new OCRImageCanvas();
			imageCanvas.setSize(image.getWidth(null), image.getHeight(null));

			LOG.info("constructing new PixelImage");
			PixelImage pixelImage = new PixelImage(image);
			LOG.info("converting PixelImage to grayScale");
			pixelImage.toGrayScale(true);
			LOG.info("filtering");
			pixelImage.filter();
			LOG.info("setting image for display");
			imageCanvas.setImage(
				pixelImage.rgbToImage(
					pixelImage.grayScaleToRGB(pixelImage.pixels),
					pixelImage.width,
					pixelImage.height,
					imageCanvas));
			String text = scanner.scan(image, 0, 0, 0, 0, null, imageCanvas.getGraphics());
			LOG.warn(imageFilename + ":[" + text + "]");
		} catch(Exception e){
			LOG.error("Error found while processing image.", e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Please specify one or more image filenames.");
			return;
		}
		String trainingImageDir = System.getProperty("TRAINING_IMAGE_DIR");
		if (trainingImageDir == null) {
			System.err.println
				("Please specify -DTRAINING_IMAGE_DIR=<dir> on " +
				 "the java command line.");
			return;
		}

		OCRScanner scanner = new OCRScanner();
		loadTrainingImages(trainingImageDir, scanner);
		List<File> files = new LinkedList<File>();
		for (int i = 0; i < args.length; i++){
			File file = new File(args[i]);
			if(file.exists()){
				if(file.isDirectory()){
					files.addAll(Arrays.asList(file.listFiles(
						new FileFilter(){
							public boolean accept(File pathname) {
								return pathname.isFile();
							}}))
					);
				}
				else{
					files.add(file);
				}
			}
		}
		for(File file : files){
			try {
				process(file.getCanonicalPath(), scanner);
			} catch (IOException e) {
				LOG.error("Error found while getting file path.", e);
			}
		}
		LOG.info("done.");
	}

}
