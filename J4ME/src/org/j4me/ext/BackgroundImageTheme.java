/**
 * 
 */
package org.j4me.ext;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.j4me.ui.Theme;

/**
 * This class implements a theme that can
 * paint a background image for the application
 * screens.
 * 
 * @author Giuseppe Miscione
 *
 */
public class BackgroundImageTheme extends Theme {

	public static final int REPEATX    = 128;
	public static final int REPEATY    = 256;
	public static final int FULLSCREEN = 512;
	
	protected Image image;
	protected Image resizedImage;
	protected int position;
	
	/**
	 * Creates a new instance of a {@link BackgroundImageTheme}.
	 */
	public BackgroundImageTheme() {
		this(null);
	}
	
	/**
	 * Creates a new instance of a {@link BackgroundImageTheme}.
	 * @param image The image that will be used as background.
	 */
	public BackgroundImageTheme(Image image) {
		this(image, 0);
	}

	/**
	 * Creates a new instance of a {@link BackgroundImageTheme}.
	 * @param image The image that will be used as background.
	 * @param position A combination of flags that indicates how
	 * to place and draw the background.
	 */
	public BackgroundImageTheme(Image image, int position) {
		setImage(image);
		setBackgroundPosition(position);
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.Theme#paintBackground(javax.microedition.lcdui.Graphics)
	 */
	public void paintBackground(Graphics g) {
		super.paintBackground(g);
		if(image != null) {
			int x = g.getClipX();
			int y = g.getClipY();
			int w = g.getClipWidth();
			int h = g.getClipHeight();
			
			if((position & FULLSCREEN) != 0) {
				if(resizedImage == null) {
					resizedImage = resizeImage(image, w, h);
				}
				g.drawImage(resizedImage, x, y, Graphics.TOP | Graphics.LEFT);
			}
			else if((position & Graphics.TOP) != 0 && (position & Graphics.LEFT) != 0) {
				g.drawImage(image, x, y, Graphics.TOP | Graphics.LEFT);

				if((position & REPEATX) != 0 && (position & REPEATY) == 0) {
					int offsetXrep = image.getWidth();
					while(offsetXrep < w) {
						g.drawImage(image, x + offsetXrep, y, Graphics.TOP | Graphics.LEFT);
						offsetXrep += image.getWidth();
					}
				}
				
				if((position & REPEATX) == 0 && (position & REPEATY) != 0) {
					int offsetYrep = image.getHeight();
					while(offsetYrep < h) {
						g.drawImage(image, x, y + offsetYrep, Graphics.TOP | Graphics.LEFT);
						offsetYrep += image.getHeight();					
					}
				}
				
				if((position & REPEATX) != 0 && (position & REPEATY) != 0) {
					int offsetYrep = 0;
					while(offsetYrep < h) {
						int offsetXrep = 0;
						while(offsetXrep < w) {
							if(offsetXrep != 0 || offsetYrep != 0)
								g.drawImage(image, x + offsetXrep, y + offsetYrep, Graphics.TOP | Graphics.LEFT);
							offsetXrep += image.getWidth();
						}
						offsetYrep += image.getHeight();					
					}
				}
			}
			else {
				int offsetX = 0;
				int offsetY = 0;
				
				if((position & Graphics.RIGHT) != 0) {
					offsetX = w - image.getWidth();
				}
				if((position & Graphics.HCENTER) != 0) {
					offsetX = (w - image.getWidth()) / 2;
				}
				if((position & Graphics.BOTTOM) != 0) {
					offsetY = h - image.getHeight();
				}
				if((position & Graphics.VCENTER) != 0) {
					offsetY = (h - image.getHeight()) / 2;
				}
				
				g.drawImage(image, x + offsetX, y + offsetY, Graphics.TOP | Graphics.LEFT);				
			}

		}
	}

	/**
	 * Returns the combination of flags used to draw the 
	 * background.
	 * 
	 * @return The combination of flags used to draw the 
	 * background.
	 */
	public int getBackgroundPosition() {
		return position;
	}

	/**
	 * Returns the image used as background.
	 * 
	 * @return The image used as background.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Sets the combination of flags that will be used to
	 * draw the background.
	 * @param position The combination of flags that will be used to
	 * draw the background.
	 */
	public void setBackgroundPosition(int position) {
		this.position = position;
	}

	/**
	 * Sets the image that will be used as
	 * background.
	 * @param image The image  that will be used as
	 * background.
	 */
	public void setImage(Image image) {
		this.image = image;
		this.resizedImage = null;
	}
	
	/**
	 * Resize the provided image to the specified size.
	 * 
	 * @param src The source image.
	 * @param targetWidth The target width.
	 * @param targetHeight The target height.
	 * @return The resized image.
	 */
	protected Image resizeImage(Image src, int targetWidth, int targetHeight) {
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		Image tmp = Image.createImage(targetWidth, srcHeight);
		Graphics g = tmp.getGraphics();
		int ratio = (srcWidth << 16) / targetWidth;
		int pos = ratio / 2;

		// Horizontal Resize

		for (int x = 0; x < targetWidth; x++) {
			g.setClip(x, 0, 1, srcHeight);
			g.drawImage(src, x - (pos >> 16), 0, Graphics.LEFT | Graphics.TOP);
			pos += ratio;
		}

		Image resizedImage = Image.createImage(targetWidth, targetHeight);
		g = resizedImage.getGraphics();
		ratio = (srcHeight << 16) / targetHeight;
		pos = ratio / 2;

		// Vertical resize

		for (int y = 0; y < targetHeight; y++) {
			g.setClip(0, y, targetWidth, 1);
			g.drawImage(tmp, 0, y - (pos >> 16), Graphics.LEFT | Graphics.TOP);
			pos += ratio;
		}
		return resizedImage;

	}

}
