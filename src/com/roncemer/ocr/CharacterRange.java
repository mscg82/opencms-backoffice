// CharacterRange.java
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
  * Class to represent a range of character codes.
  * @author Ronald B. Cemer
  */
public class CharacterRange {
	/**
      * The minimum character value in this range.
      */
	public final int min;
	/**
      * The maximum character value in this range.
      */
	public final int max;

	/**
      * Construct a new <code>CharacterRange</code> object for a range of
      * character codes.
      * @param min The minimum character value in this range.
      * @param max The maximum character value in this range.
      */
	public CharacterRange(int min, int max) {
		if (min > max)
			throw new InternalError("max must be >= min");
		 this.min = min;
		 this.max = max;
	}

	/**
      * Construct a new <code>CharacterRange</code> object for a single
      * character code.
      * @param c The character code for this range.  This code will be both
      * the minimum and maximum for this range.
      */ public CharacterRange(int c) {
		this(c, c);
	}
}
