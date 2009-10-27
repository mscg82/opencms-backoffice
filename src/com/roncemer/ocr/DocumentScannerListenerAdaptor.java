// DocumentScannerListenerAdapter.java
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

/**
  * Empty implementation of DocumentScannerListener interface which can be
  * subclassed and only have the needed methods overridden.  This prevents
  * implementing classes from being forced to implement all methods in the
  * interface.
  * @author Ronald B. Cemer
  */
public class DocumentScannerListenerAdaptor
	implements DocumentScannerListener {

	public void beginDocument(PixelImage pixelImage) {
	}

	public void beginRow(PixelImage pixelImage, int y1, int y2) {
	}

	public void processChar
		(PixelImage pixelImage, int x1, int y1, int x2, int y2, int rowY1, int rowY2) {
	}

	public void processSpace(PixelImage pixelImage, int x1, int y1, int x2, int y2) {
	}

	public void endRow(PixelImage pixelImage, int y1, int y2) {
	}

	public void endDocument(PixelImage pixelImage) {
	}
}
