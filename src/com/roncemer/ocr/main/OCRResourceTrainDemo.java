/**
 *
 */
package com.roncemer.ocr.main;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.roncemer.ocr.CharacterRange;
import com.roncemer.ocr.OCRScanner;

/**
 * @author Giuseppe Miscione
 *
 */
public class OCRResourceTrainDemo {

	private static Log LOG = LogFactory.getLog(OCRResourceTrainDemo.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			OCRScanner scanner = new OCRScanner();

			Map<Object, CharacterRange> trainingset = new LinkedHashMap<Object, CharacterRange>();
			URL img = OCRResourceTrainDemo.class.getClassLoader().getResource("eatj/eatj_letter.jpg");
			if(img != null) {
				LOG.info("Loading training image: " + img.toString());
				trainingset.put(img, new CharacterRange('a', 'f'));
			}
			else{
				LOG.info("Cannot find eatj/eatj_letter.jpg with class loader");
			}

			img = OCRResourceTrainDemo.class.getClassLoader().getResource("eatj/eatj_number.jpg");
			if(img != null) {
				LOG.info("Loading training image: " + img.toString());
				trainingset.put(img, new CharacterRange('0', '9'));
			}
			else{
				LOG.info("Cannot find eatj/eatj_number.jpg with class loader");
			}
			scanner.train(null, trainingset, true);

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
				String text = scanner.scan(file, 0, 0, 0, 0, null, null);
				LOG.info("File \"" + file.getName() + "\", text \"" + text + "\"");
			}

		} catch(Exception e){
			LOG.error("Error found.", e);
		}
	}

}
