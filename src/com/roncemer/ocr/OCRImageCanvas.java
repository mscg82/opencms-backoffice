/**
 *
 */
package com.roncemer.ocr;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

public class OCRImageCanvas extends Canvas {
	private static final long serialVersionUID = 1132718684689362892L;

	private Image image;

	public OCRImageCanvas() {
		super();
	}

	public void setImage(Image image) {
		this.image = image;
		repaint();
	}

	public void paint(Graphics g) {
		if (image != null)
			g.drawImage(image, 0, 0, null);
	}
}