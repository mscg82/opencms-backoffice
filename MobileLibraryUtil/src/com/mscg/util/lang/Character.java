/*
 * @(#)Character.java	1.31 04/09/14
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.mscg.util.lang;

/**
 * The <code>Character</code> class wraps a value of the primitive
 * type <code>char</code> in an object. An object of type
 * <code>Character</code> contains a single field whose type is
 * <code>char</code>.
 * <p>
 * In addition, this class provides several methods for determining
 * a character's category (lowercase letter, digit, etc.) and for converting
 * characters from uppercase to lowercase and vice versa.
 * <p>
 * Character information is based on the Unicode Standard, version 4.0.
 * <p>
 * The methods and data of class <code>Character</code> are defined by
 * the information in the <i>UnicodeData</i> file that is part of the
 * Unicode Character Database maintained by the Unicode
 * Consortium. This file specifies various properties including name
 * and general category for every defined Unicode code point or
 * character range.
 * <p>
 * The file and its description are available from the Unicode Consortium at:
 * <ul>
 * <li><a href="http://www.unicode.org">http://www.unicode.org</a>
 * </ul>
 *
 * <h4><a name="unicode">Unicode Character Representations</a></h4>
 *
 * <p>The <code>char</code> data type (and therefore the value that a
 * <code>Character</code> object encapsulates) are based on the
 * original Unicode specification, which defined characters as
 * fixed-width 16-bit entities. The Unicode standard has since been
 * changed to allow for characters whose representation requires more
 * than 16 bits.  The range of legal <em>code point</em>s is now
 * U+0000 to U+10FFFF, known as <em>Unicode scalar value</em>.
 * (Refer to the <a
 * href="http://www.unicode.org/reports/tr27/#notation"><i>
 * definition</i></a> of the U+<i>n</i> notation in the Unicode
 * standard.)
 *
 * <p>The set of characters from U+0000 to U+FFFF is sometimes
 * referred to as the <em>Basic Multilingual Plane (BMP)</em>. <a
 * name="supplementary">Characters</a> whose code points are greater
 * than U+FFFF are called <em>supplementary character</em>s.  The Java
 * 2 platform uses the UTF-16 representation in <code>char</code>
 * arrays and in the <code>String</code> and <code>StringBuffer</code>
 * classes. In this representation, supplementary characters are
 * represented as a pair of <code>char</code> values, the first from
 * the <em>high-surrogates</em> range, (&#92;uD800-&#92;uDBFF), the
 * second from the <em>low-surrogates</em> range
 * (&#92;uDC00-&#92;uDFFF).
 *
 * <p>A <code>char</code> value, therefore, represents Basic
 * Multilingual Plane (BMP) code points, including the surrogate
 * code points, or code units of the UTF-16 encoding. An
 * <code>int</code> value represents all Unicode code points,
 * including supplementary code points. The lower (least significant)
 * 21 bits of <code>int</code> are used to represent Unicode code
 * points and the upper (most significant) 11 bits must be zero.
 * Unless otherwise specified, the behavior with respect to
 * supplementary characters and surrogate <code>char</code> values is
 * as follows:
 *
 * <ul>
 * <li>The methods that only accept a <code>char</code> value cannot support
 * supplementary characters. They treat <code>char</code> values from the
 * surrogate ranges as undefined characters. For example,
 * <code>Character.isLetter('&#92;uD840')</code> returns <code>false</code>, even though
 * this specific value if followed by any low-surrogate value in a string
 * would represent a letter.
 *
 * <li>The methods that accept an <code>int</code> value support all
 * Unicode characters, including supplementary characters. For
 * example, <code>Character.isLetter(0x2F81A)</code> returns
 * <code>true</code> because the code point value represents a letter
 * (a CJK ideograph).
 * </ul>
 *
 * <p>In the J2SE API documentation, <em>Unicode code point</em> is
 * used for character values in the range between U+0000 and U+10FFFF,
 * and <em>Unicode code unit</em> is used for 16-bit
 * <code>char</code> values that are code units of the <em>UTF-16</em>
 * encoding. For more information on Unicode terminology, refer to the
 * <a href="http://www.unicode.org/glossary/">Unicode Glossary</a>.
 *
 * @author  Lee Boynton
 * @author  Guy Steele
 * @author  Akira Tanaka
 * @since   1.0
 */
public final
class Character extends Object {
    /**
     * The minimum radix available for conversion to and from strings.
     * The constant value of this field is the smallest value permitted
     * for the radix argument in radix-conversion methods such as the
     * <code>digit</code> method, the <code>forDigit</code>
     * method, and the <code>toString</code> method of class
     * <code>Integer</code>.
     *
     * @see     java.lang.Character#digit(char, int)
     * @see     java.lang.Character#forDigit(int, int)
     * @see     java.lang.Integer#toString(int, int)
     * @see     java.lang.Integer#valueOf(java.lang.String)
     */
    public static final int MIN_RADIX = 2;

    /**
     * The maximum radix available for conversion to and from strings.
     * The constant value of this field is the largest value permitted
     * for the radix argument in radix-conversion methods such as the
     * <code>digit</code> method, the <code>forDigit</code>
     * method, and the <code>toString</code> method of class
     * <code>Integer</code>.
     *
     * @see     java.lang.Character#digit(char, int)
     * @see     java.lang.Character#forDigit(int, int)
     * @see     java.lang.Integer#toString(int, int)
     * @see     java.lang.Integer#valueOf(java.lang.String)
     */
    public static final int MAX_RADIX = 36;

    /**
     * The constant value of this field is the smallest value of type
     * <code>char</code>, <code>'&#92;u0000'</code>.
     *
     * @since   1.0.2
     */
    public static final char   MIN_VALUE = '\u0000';

    /**
     * The constant value of this field is the largest value of type
     * <code>char</code>, <code>'&#92;uFFFF'</code>.
     *
     * @since   1.0.2
     */
    public static final char   MAX_VALUE = '\uffff';

   /*
    * Normative general types
    */

   /*
    * General character types
    */

   /**
    * General category "Cn" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        UNASSIGNED                  = 0;

   /**
    * General category "Lu" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        UPPERCASE_LETTER            = 1;

   /**
    * General category "Ll" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        LOWERCASE_LETTER            = 2;

   /**
    * General category "Lt" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        TITLECASE_LETTER            = 3;

   /**
    * General category "Lm" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        MODIFIER_LETTER             = 4;

   /**
    * General category "Lo" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        OTHER_LETTER                = 5;

   /**
    * General category "Mn" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        NON_SPACING_MARK            = 6;

   /**
    * General category "Me" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        ENCLOSING_MARK              = 7;

   /**
    * General category "Mc" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        COMBINING_SPACING_MARK      = 8;

   /**
    * General category "Nd" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        DECIMAL_DIGIT_NUMBER        = 9;

   /**
    * General category "Nl" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        LETTER_NUMBER               = 10;

   /**
    * General category "No" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        OTHER_NUMBER                = 11;

   /**
    * General category "Zs" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        SPACE_SEPARATOR             = 12;

   /**
    * General category "Zl" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        LINE_SEPARATOR              = 13;

   /**
    * General category "Zp" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        PARAGRAPH_SEPARATOR         = 14;

   /**
    * General category "Cc" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        CONTROL                     = 15;

   /**
    * General category "Cf" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        FORMAT                      = 16;

   /**
    * General category "Co" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        PRIVATE_USE                 = 18;

   /**
    * General category "Cs" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        SURROGATE                   = 19;

   /**
    * General category "Pd" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        DASH_PUNCTUATION            = 20;

   /**
    * General category "Ps" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        START_PUNCTUATION           = 21;

   /**
    * General category "Pe" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        END_PUNCTUATION             = 22;

   /**
    * General category "Pc" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        CONNECTOR_PUNCTUATION       = 23;

   /**
    * General category "Po" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        OTHER_PUNCTUATION           = 24;

   /**
    * General category "Sm" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        MATH_SYMBOL                 = 25;

   /**
    * General category "Sc" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        CURRENCY_SYMBOL             = 26;

   /**
    * General category "Sk" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        MODIFIER_SYMBOL             = 27;

   /**
    * General category "So" in the Unicode specification.
    * @since   1.1
    */
    public static final byte
        OTHER_SYMBOL                = 28;

   /**
    * General category "Pi" in the Unicode specification.
    * @since   1.4
    */
    public static final byte
        INITIAL_QUOTE_PUNCTUATION   = 29;

   /**
    * General category "Pf" in the Unicode specification.
    * @since   1.4
    */
    public static final byte
        FINAL_QUOTE_PUNCTUATION     = 30;

    /**
     * Error flag. Use int (code point) to avoid confusion with U+FFFF.
     */
     static final int ERROR = 0xFFFFFFFF;


    /**
     * Undefined bidirectional character type. Undefined <code>char</code>
     * values have undefined directionality in the Unicode specification.
     * @since 1.4
     */
     public static final byte DIRECTIONALITY_UNDEFINED = -1;

    /**
     * Strong bidirectional character type "L" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT = 0;

    /**
     * Strong bidirectional character type "R" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT = 1;

    /**
    * Strong bidirectional character type "AL" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC = 2;

    /**
     * Weak bidirectional character type "EN" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER = 3;

    /**
     * Weak bidirectional character type "ES" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR = 4;

    /**
     * Weak bidirectional character type "ET" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR = 5;

    /**
     * Weak bidirectional character type "AN" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_ARABIC_NUMBER = 6;

    /**
     * Weak bidirectional character type "CS" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_COMMON_NUMBER_SEPARATOR = 7;

    /**
     * Weak bidirectional character type "NSM" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_NONSPACING_MARK = 8;

    /**
     * Weak bidirectional character type "BN" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_BOUNDARY_NEUTRAL = 9;

    /**
     * Neutral bidirectional character type "B" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_PARAGRAPH_SEPARATOR = 10;

    /**
     * Neutral bidirectional character type "S" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_SEGMENT_SEPARATOR = 11;

    /**
     * Neutral bidirectional character type "WS" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_WHITESPACE = 12;

    /**
     * Neutral bidirectional character type "ON" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_OTHER_NEUTRALS = 13;

    /**
     * Strong bidirectional character type "LRE" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING = 14;

    /**
     * Strong bidirectional character type "LRO" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE = 15;

    /**
     * Strong bidirectional character type "RLE" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING = 16;

    /**
     * Strong bidirectional character type "RLO" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE = 17;

    /**
     * Weak bidirectional character type "PDF" in the Unicode specification.
     * @since 1.4
     */
    public static final byte DIRECTIONALITY_POP_DIRECTIONAL_FORMAT = 18;

    /**
     * The minimum value of a Unicode high-surrogate code unit in the
     * UTF-16 encoding. A high-surrogate is also known as a
     * <i>leading-surrogate</i>.
     *
     * @since 1.5
     */
    public static final char MIN_HIGH_SURROGATE = '\uD800';

    /**
     * The maximum value of a Unicode high-surrogate code unit in the
     * UTF-16 encoding. A high-surrogate is also known as a
     * <i>leading-surrogate</i>.
     *
     * @since 1.5
     */
    public static final char MAX_HIGH_SURROGATE = '\uDBFF';

    /**
     * The minimum value of a Unicode low-surrogate code unit in the
     * UTF-16 encoding. A low-surrogate is also known as a
     * <i>trailing-surrogate</i>.
     *
     * @since 1.5
     */
    public static final char MIN_LOW_SURROGATE  = '\uDC00';

    /**
     * The maximum value of a Unicode low-surrogate code unit in the
     * UTF-16 encoding. A low-surrogate is also known as a
     * <i>trailing-surrogate</i>.
     *
     * @since 1.5
     */
    public static final char MAX_LOW_SURROGATE  = '\uDFFF';

    /**
     * The minimum value of a Unicode surrogate code unit in the UTF-16 encoding.
     *
     * @since 1.5
     */
    public static final char MIN_SURROGATE = MIN_HIGH_SURROGATE;

    /**
     * The maximum value of a Unicode surrogate code unit in the UTF-16 encoding.
     *
     * @since 1.5
     */
    public static final char MAX_SURROGATE = MAX_LOW_SURROGATE;

    /**
     * The minimum value of a supplementary code point.
     *
     * @since 1.5
     */
    public static final int MIN_SUPPLEMENTARY_CODE_POINT = 0x010000;

    /**
     * The minimum value of a Unicode code point.
     * 
     * @since 1.5
     */
    public static final int MIN_CODE_POINT = 0x000000;

    /**
     * The maximum value of a Unicode code point.
     *
     * @since 1.5
     */
    public static final int MAX_CODE_POINT = 0x10ffff;


    /**
     * Instances of this class represent particular subsets of the Unicode
     * character set.  The only family of subsets defined in the
     * <code>Character</code> class is <code>{@link Character.UnicodeBlock
     * UnicodeBlock}</code>.  Other portions of the Java API may define other
     * subsets for their own purposes.
     *
     * @since 1.2
     */
    public static class Subset  {

        private String name;

        /**
         * Constructs a new <code>Subset</code> instance.
         *
         * @exception NullPointerException if name is <code>null</code>
         * @param  name  The name of this subset
         */
        protected Subset(String name) {
            if (name == null) {
                throw new NullPointerException("name");
            }
            this.name = name;
        }

        /**
         * Compares two <code>Subset</code> objects for equality.
         * This method returns <code>true</code> if and only if
         * <code>this</code> and the argument refer to the same
         * object; since this method is <code>final</code>, this
         * guarantee holds for all subclasses.
         */
        public final boolean equals(Object obj) {
            return (this == obj);
        }

        /**
         * Returns the standard hash code as defined by the
         * <code>{@link Object#hashCode}</code> method.  This method
         * is <code>final</code> in order to ensure that the
         * <code>equals</code> and <code>hashCode</code> methods will
         * be consistent in all subclasses.
         */
        public final int hashCode() {
            return super.hashCode();
        }

        /**
         * Returns the name of this subset.
         */
        public final String toString() {
            return name;
        }
    }

    /**
     * The value of the <code>Character</code>.
     *
     * @serial
     */
    private final char value;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = 3786198910865385080L;

    /**
     * Constructs a newly allocated <code>Character</code> object that
     * represents the specified <code>char</code> value.
     *
     * @param  value   the value to be represented by the 
     *                  <code>Character</code> object.
     */
    public Character(char value) {
        this.value = value;
    }

    private static class CharacterCache {
	private CharacterCache(){}

	static final Character cache[] = new Character[127 + 1];

	static {
	    for(int i = 0; i < cache.length; i++)
		cache[i] = new Character((char)i);
	}
    }

    /**
     * Returns a <tt>Character</tt> instance representing the specified
     * <tt>char</tt> value.
     * If a new <tt>Character</tt> instance is not required, this method
     * should generally be used in preference to the constructor
     * {@link #Character(char)}, as this method is likely to yield
     * significantly better space and time performance by caching
     * frequently requested values.
     *
     * @param  c a char value.
     * @return a <tt>Character</tt> instance representing <tt>c</tt>.
     * @since  1.5
     */
    public static Character valueOf(char c) {
	if(c <= 127) { // must cache
	    return CharacterCache.cache[(int)c];
	}
        return new Character(c);
    }

    /**
     * Returns the value of this <code>Character</code> object.
     * @return  the primitive <code>char</code> value represented by
     *          this object.
     */
    public char charValue() {
        return value;
    }

    /**
     * Returns a hash code for this <code>Character</code>.
     * @return  a hash code value for this object.
     */
    public int hashCode() {
        return (int)value;
    }

    /**
     * Compares this object against the specified object.
     * The result is <code>true</code> if and only if the argument is not
     * <code>null</code> and is a <code>Character</code> object that
     * represents the same <code>char</code> value as this object.
     *
     * @param   obj   the object to compare with.
     * @return  <code>true</code> if the objects are the same;
     *          <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Character) {
            return value == ((Character)obj).charValue();
        }
        return false;
    }

    /**
     * Returns a <code>String</code> object representing this
     * <code>Character</code>'s value.  The result is a string of
     * length 1 whose sole component is the primitive
     * <code>char</code> value represented by this
     * <code>Character</code> object.
     *
     * @return  a string representation of this object.
     */
    public String toString() {
        char buf[] = {value};
        return String.valueOf(buf);
    }

    /**
     * Returns a <code>String</code> object representing the
     * specified <code>char</code>.  The result is a string of length
     * 1 consisting solely of the specified <code>char</code>.
     *
     * @param c the <code>char</code> to be converted
     * @return the string representation of the specified <code>char</code>
     * @since 1.4
     */
    public static String toString(char c) {
        return String.valueOf(c);
    }

    // Maximum character handled by internal fast-path code which
    // avoids initializing large tables.
    // Note: performance of this "fast-path" code may be sub-optimal
    // in negative cases for some accessors due to complicated ranges.
    // Should revisit after optimization of table initialization.

    private static final int FAST_PATH_MAX = 255;

    /**
     * Provide the character plane to which this codepoint belongs.
     * 
     * @param ch the codepoint
     * @return the plane of the codepoint argument
     * @since 1.5
     */
    private static int getPlane(int ch) {
        return (ch >>> 16);
    }

    /**
     * Determines whether the specified code point is a valid Unicode
     * code point value in the range of <code>0x0000</code> to
     * <code>0x10FFFF</code> inclusive. This method is equivalent to
     * the expression:
     *
     * <blockquote><pre>
     * codePoint >= 0x0000 && codePoint <= 0x10FFFF
     * </pre></blockquote>
     *
     * @param  codePoint the Unicode code point to be tested
     * @return <code>true</code> if the specified code point value
     * is a valid code point value;
     * <code>false</code> otherwise.
     * @since  1.5
     */
    public static boolean isValidCodePoint(int codePoint) {
        return codePoint >= MIN_CODE_POINT && codePoint <= MAX_CODE_POINT;
    }

    /**
     * Determines whether the specified character (Unicode code point)
     * is in the supplementary character range. The method call is
     * equivalent to the expression:
     * <blockquote><pre>
     * codePoint >= 0x10000 && codePoint <= 0x10ffff
     * </pre></blockquote>
     *
     * @param  codePoint the character (Unicode code point) to be tested
     * @return <code>true</code> if the specified character is in the Unicode
     *         supplementary character range; <code>false</code> otherwise.
     * @since  1.5
     */
    public static boolean isSupplementaryCodePoint(int codePoint) {
        return codePoint >= MIN_SUPPLEMENTARY_CODE_POINT
            && codePoint <= MAX_CODE_POINT;
    }

    /**
     * Determines if the given <code>char</code> value is a
     * high-surrogate code unit (also known as <i>leading-surrogate
     * code unit</i>). Such values do not represent characters by
     * themselves, but are used in the representation of <a
     * href="#supplementary">supplementary characters</a> in the
     * UTF-16 encoding.
     *
     * <p>This method returns <code>true</code> if and only if
     * <blockquote><pre>ch >= '&#92;uD800' && ch <= '&#92;uDBFF'
     * </pre></blockquote>
     * is <code>true</code>.
     *
     * @param   ch   the <code>char</code> value to be tested.
     * @return  <code>true</code> if the <code>char</code> value
     *          is between '&#92;uD800' and '&#92;uDBFF' inclusive;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isLowSurrogate(char)
     * @see     Character.UnicodeBlock#of(int)
     * @since   1.5
     */
    public static boolean isHighSurrogate(char ch) {
        return ch >= MIN_HIGH_SURROGATE && ch <= MAX_HIGH_SURROGATE;
    }
    
    /**
     * Determines if the given <code>char</code> value is a
     * low-surrogate code unit (also known as <i>trailing-surrogate code
     * unit</i>). Such values do not represent characters by themselves,
     * but are used in the representation of <a
     * href="#supplementary">supplementary characters</a> in the UTF-16 encoding.
     *
     * <p> This method returns <code>true</code> if and only if
     * <blockquote><pre>ch >= '&#92;uDC00' && ch <= '&#92;uDFFF'
     * </pre></blockquote> is <code>true</code>.
     *
     * @param   ch   the <code>char</code> value to be tested.
     * @return  <code>true</code> if the <code>char</code> value
     *          is between '&#92;uDC00' and '&#92;uDFFF' inclusive;
     *          <code>false</code> otherwise.
     * @see java.lang.Character#isHighSurrogate(char)
     * @since   1.5
     */
    public static boolean isLowSurrogate(char ch) {
        return ch >= MIN_LOW_SURROGATE && ch <= MAX_LOW_SURROGATE;
    }

    /**
     * Determines whether the specified pair of <code>char</code>
     * values is a valid surrogate pair. This method is equivalent to
     * the expression:
     * <blockquote><pre>
     * isHighSurrogate(high) && isLowSurrogate(low)
     * </pre></blockquote>
     *
     * @param  high the high-surrogate code value to be tested
     * @param  low the low-surrogate code value to be tested
     * @return <code>true</code> if the specified high and
     * low-surrogate code values represent a valid surrogate pair;
     * <code>false</code> otherwise.
     * @since  1.5
     */
    public static boolean isSurrogatePair(char high, char low) {
        return isHighSurrogate(high) && isLowSurrogate(low);
    }

    /**
     * Determines the number of <code>char</code> values needed to
     * represent the specified character (Unicode code point). If the
     * specified character is equal to or greater than 0x10000, then
     * the method returns 2. Otherwise, the method returns 1.
     *
     * <p>This method doesn't validate the specified character to be a
     * valid Unicode code point. The caller must validate the
     * character value using {@link #isValidCodePoint(int) isValidCodePoint}
     * if necessary.
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  2 if the character is a valid supplementary character; 1 otherwise.
     * @see     #isSupplementaryCodePoint(int)
     * @since   1.5
     */
    public static int charCount(int codePoint) {
        return codePoint >= MIN_SUPPLEMENTARY_CODE_POINT? 2 : 1;
    }

    /**
     * Converts the specified surrogate pair to its supplementary code
     * point value. This method does not validate the specified
     * surrogate pair. The caller must validate it using {@link
     * #isSurrogatePair(char, char) isSurrogatePair} if necessary.
     *
     * @param  high the high-surrogate code unit
     * @param  low the low-surrogate code unit
     * @return the supplementary code point composed from the
     *         specified surrogate pair.
     * @since  1.5
     */
    public static int toCodePoint(char high, char low) {
        return ((high - MIN_HIGH_SURROGATE) << 10)
            + (low - MIN_LOW_SURROGATE) + MIN_SUPPLEMENTARY_CODE_POINT;
    }

    /**
     * Returns the code point at the given index of the
     * <code>CharSequence</code>. If the <code>char</code> value at
     * the given index in the <code>CharSequence</code> is in the
     * high-surrogate range, the following index is less than the
     * length of the <code>CharSequence</code>, and the
     * <code>char</code> value at the following index is in the
     * low-surrogate range, then the supplementary code point
     * corresponding to this surrogate pair is returned. Otherwise,
     * the <code>char</code> value at the given index is returned.
     *
     * @param seq a sequence of <code>char</code> values (Unicode code
     * units)
     * @param index the index to the <code>char</code> values (Unicode
     * code units) in <code>seq</code> to be converted
     * @return the Unicode code point at the given index
     * @exception NullPointerException if <code>seq</code> is null.
     * @exception IndexOutOfBoundsException if the value
     * <code>index</code> is negative or not less than
     * {@link CharSequence#length() seq.length()}.
     * @since  1.5
     */
    public static int codePointAt(CharSequence seq, int index) {
        char c1 = seq.charAt(index++);
        if (isHighSurrogate(c1)) {
            if (index < seq.length()) {
                char c2 = seq.charAt(index);
                if (isLowSurrogate(c2)) {
                    return toCodePoint(c1, c2);
                }
            }
        }
        return c1;
    }

    /**
     * Returns the code point at the given index of the
     * <code>char</code> array. If the <code>char</code> value at
     * the given index in the <code>char</code> array is in the
     * high-surrogate range, the following index is less than the
     * length of the <code>char</code> array, and the
     * <code>char</code> value at the following index is in the
     * low-surrogate range, then the supplementary code point
     * corresponding to this surrogate pair is returned. Otherwise,
     * the <code>char</code> value at the given index is returned.
     *
     * @param a the <code>char</code> array
     * @param index the index to the <code>char</code> values (Unicode
     * code units) in the <code>char</code> array to be converted
     * @return the Unicode code point at the given index
     * @exception NullPointerException if <code>a</code> is null.
     * @exception IndexOutOfBoundsException if the value
     * <code>index</code> is negative or not less than
     * the length of the <code>char</code> array.
     * @since  1.5
     */
    public static int codePointAt(char[] a, int index) {
	return codePointAtImpl(a, index, a.length);
    }

    /**
     * Returns the code point at the given index of the
     * <code>char</code> array, where only array elements with
     * <code>index</code> less than <code>limit</code> can be used. If
     * the <code>char</code> value at the given index in the
     * <code>char</code> array is in the high-surrogate range, the
     * following index is less than the <code>limit</code>, and the
     * <code>char</code> value at the following index is in the
     * low-surrogate range, then the supplementary code point
     * corresponding to this surrogate pair is returned. Otherwise,
     * the <code>char</code> value at the given index is returned.
     *
     * @param a the <code>char</code> array
     * @param index the index to the <code>char</code> values (Unicode
     * code units) in the <code>char</code> array to be converted
     * @param limit the index after the last array element that can be used in the
     * <code>char</code> array
     * @return the Unicode code point at the given index
     * @exception NullPointerException if <code>a</code> is null.
     * @exception IndexOutOfBoundsException if the <code>index</code>
     * argument is negative or not less than the <code>limit</code>
     * argument, or if the <code>limit</code> argument is negative or
     * greater than the length of the <code>char</code> array.
     * @since  1.5
     */
    public static int codePointAt(char[] a, int index, int limit) {
	if (index >= limit || limit < 0 || limit > a.length) {
	    throw new IndexOutOfBoundsException();
	}
	return codePointAtImpl(a, index, limit);
    }

    static int codePointAtImpl(char[] a, int index, int limit) {
        char c1 = a[index++];
        if (isHighSurrogate(c1)) {
            if (index < limit) {
                char c2 = a[index];
                if (isLowSurrogate(c2)) {
                    return toCodePoint(c1, c2);
                }
            }
        }
        return c1;
    }

    /**
     * Returns the code point preceding the given index of the
     * <code>CharSequence</code>. If the <code>char</code> value at
     * <code>(index - 1)</code> in the <code>CharSequence</code> is in
     * the low-surrogate range, <code>(index - 2)</code> is not
     * negative, and the <code>char</code> value at <code>(index -
     * 2)</code> in the <code>CharSequence</code> is in the
     * high-surrogate range, then the supplementary code point
     * corresponding to this surrogate pair is returned. Otherwise,
     * the <code>char</code> value at <code>(index - 1)</code> is
     * returned.
     *
     * @param seq the <code>CharSequence</code> instance
     * @param index the index following the code point that should be returned
     * @return the Unicode code point value before the given index.
     * @exception NullPointerException if <code>seq</code> is null.
     * @exception IndexOutOfBoundsException if the <code>index</code>
     * argument is less than 1 or greater than {@link
     * CharSequence#length() seq.length()}.
     * @since  1.5
     */
    public static int codePointBefore(CharSequence seq, int index) {
        char c2 = seq.charAt(--index);
        if (isLowSurrogate(c2)) {
            if (index > 0) {
                char c1 = seq.charAt(--index);
                if (isHighSurrogate(c1)) {
                    return toCodePoint(c1, c2);
                }
            }
        }
        return c2;
    }

    /**
     * Returns the code point preceding the given index of the
     * <code>char</code> array. If the <code>char</code> value at
     * <code>(index - 1)</code> in the <code>char</code> array is in
     * the low-surrogate range, <code>(index - 2)</code> is not
     * negative, and the <code>char</code> value at <code>(index -
     * 2)</code> in the <code>char</code> array is in the
     * high-surrogate range, then the supplementary code point
     * corresponding to this surrogate pair is returned. Otherwise,
     * the <code>char</code> value at <code>(index - 1)</code> is
     * returned.
     *
     * @param a the <code>char</code> array
     * @param index the index following the code point that should be returned
     * @return the Unicode code point value before the given index.
     * @exception NullPointerException if <code>a</code> is null.
     * @exception IndexOutOfBoundsException if the <code>index</code>
     * argument is less than 1 or greater than the length of the
     * <code>char</code> array
     * @since  1.5
     */
    public static int codePointBefore(char[] a, int index) {
        return codePointBeforeImpl(a, index, 0);
    }

    /**
     * Returns the code point preceding the given index of the
     * <code>char</code> array, where only array elements with
     * <code>index</code> greater than or equal to <code>start</code>
     * can be used. If the <code>char</code> value at <code>(index -
     * 1)</code> in the <code>char</code> array is in the
     * low-surrogate range, <code>(index - 2)</code> is not less than
     * <code>start</code>, and the <code>char</code> value at
     * <code>(index - 2)</code> in the <code>char</code> array is in
     * the high-surrogate range, then the supplementary code point
     * corresponding to this surrogate pair is returned. Otherwise,
     * the <code>char</code> value at <code>(index - 1)</code> is
     * returned.
     *
     * @param a the <code>char</code> array
     * @param index the index following the code point that should be returned
     * @param start the index of the first array element in the
     * <code>char</code> array
     * @return the Unicode code point value before the given index.
     * @exception NullPointerException if <code>a</code> is null.
     * @exception IndexOutOfBoundsException if the <code>index</code>
     * argument is not greater than the <code>start</code> argument or
     * is greater than the length of the <code>char</code> array, or
     * if the <code>start</code> argument is negative or not less than
     * the length of the <code>char</code> array.
     * @since  1.5
     */
    public static int codePointBefore(char[] a, int index, int start) {
	if (index <= start || start < 0 || start >= a.length) {
	    throw new IndexOutOfBoundsException();
	}
	return codePointBeforeImpl(a, index, start);
    }

    static int codePointBeforeImpl(char[] a, int index, int start) {
        char c2 = a[--index];
        if (isLowSurrogate(c2)) {
            if (index > start) {
                char c1 = a[--index];
                if (isHighSurrogate(c1)) {
                    return toCodePoint(c1, c2);
                }
            }
        }
        return c2;
    }

    /**
     * Converts the specified character (Unicode code point) to its
     * UTF-16 representation. If the specified code point is a BMP
     * (Basic Multilingual Plane or Plane 0) value, the same value is
     * stored in <code>dst[dstIndex]</code>, and 1 is returned. If the
     * specified code point is a supplementary character, its
     * surrogate values are stored in <code>dst[dstIndex]</code>
     * (high-surrogate) and <code>dst[dstIndex+1]</code>
     * (low-surrogate), and 2 is returned.
     *
     * @param  codePoint the character (Unicode code point) to be converted.
     * @param  dst an array of <code>char</code> in which the
     * <code>codePoint</code>'s UTF-16 value is stored.
     * @param dstIndex the start index into the <code>dst</code>
     * array where the converted value is stored.
     * @return 1 if the code point is a BMP code point, 2 if the
     * code point is a supplementary code point.
     * @exception IllegalArgumentException if the specified
     * <code>codePoint</code> is not a valid Unicode code point.
     * @exception NullPointerException if the specified <code>dst</code> is null.
     * @exception IndexOutOfBoundsException if <code>dstIndex</code>
     * is negative or not less than <code>dst.length</code>, or if
     * <code>dst</code> at <code>dstIndex</code> doesn't have enough
     * array element(s) to store the resulting <code>char</code>
     * value(s). (If <code>dstIndex</code> is equal to
     * <code>dst.length-1</code> and the specified
     * <code>codePoint</code> is a supplementary character, the
     * high-surrogate value is not stored in
     * <code>dst[dstIndex]</code>.)
     * @since  1.5
     */
    public static int toChars(int codePoint, char[] dst, int dstIndex) {
        if (codePoint < 0 || codePoint > MAX_CODE_POINT) {
            throw new IllegalArgumentException();
        }
        if (codePoint < MIN_SUPPLEMENTARY_CODE_POINT) {
            dst[dstIndex] = (char) codePoint;
            return 1;
        }
        toSurrogates(codePoint, dst, dstIndex);
        return 2;
    }

    /**
     * Converts the specified character (Unicode code point) to its
     * UTF-16 representation stored in a <code>char</code> array. If
     * the specified code point is a BMP (Basic Multilingual Plane or
     * Plane 0) value, the resulting <code>char</code> array has
     * the same value as <code>codePoint</code>. If the specified code
     * point is a supplementary code point, the resulting
     * <code>char</code> array has the corresponding surrogate pair.
     *
     * @param  codePoint a Unicode code point
     * @return a <code>char</code> array having
     *         <code>codePoint</code>'s UTF-16 representation.
     * @exception IllegalArgumentException if the specified
     * <code>codePoint</code> is not a valid Unicode code point.
     * @since  1.5
     */
    public static char[] toChars(int codePoint) {
        if (codePoint < 0 || codePoint > MAX_CODE_POINT) {
            throw new IllegalArgumentException();
        }
        if (codePoint < MIN_SUPPLEMENTARY_CODE_POINT) {
                return new char[] { (char) codePoint };
        }
        char[] result = new char[2];
        toSurrogates(codePoint, result, 0);
        return result;
    }

    static void toSurrogates(int codePoint, char[] dst, int index) {
        int offset = codePoint - MIN_SUPPLEMENTARY_CODE_POINT;
        dst[index+1] = (char)((offset & 0x3ff) + MIN_LOW_SURROGATE);
        dst[index] = (char)((offset >>> 10) + MIN_HIGH_SURROGATE);
    }

    /**
     * Returns the number of Unicode code points in the text range of
     * the specified char sequence. The text range begins at the
     * specified <code>beginIndex</code> and extends to the
     * <code>char</code> at index <code>endIndex - 1</code>. Thus the
     * length (in <code>char</code>s) of the text range is
     * <code>endIndex-beginIndex</code>. Unpaired surrogates within
     * the text range count as one code point each.
     *
     * @param seq the char sequence
     * @param beginIndex the index to the first <code>char</code> of
     * the text range.
     * @param endIndex the index after the last <code>char</code> of
     * the text range.
     * @return the number of Unicode code points in the specified text
     * range
     * @exception NullPointerException if <code>seq</code> is null.
     * @exception IndexOutOfBoundsException if the
     * <code>beginIndex</code> is negative, or <code>endIndex</code>
     * is larger than the length of the given sequence, or
     * <code>beginIndex</code> is larger than <code>endIndex</code>.
     * @since  1.5
     */
    public static int codePointCount(CharSequence seq, int beginIndex, int endIndex) {
	int length = seq.length();
	if (beginIndex < 0 || endIndex > length || beginIndex > endIndex) {
	    throw new IndexOutOfBoundsException();
	}
	int n = 0;
	for (int i = beginIndex; i < endIndex; ) {
	    n++;
	    if (isHighSurrogate(seq.charAt(i++))) {
		if (i < endIndex && isLowSurrogate(seq.charAt(i))) {
		    i++;
		}
	    }
	}
	return n;
    }

    /**
     * Returns the number of Unicode code points in a subarray of the
     * <code>char</code> array argument. The <code>offset</code>
     * argument is the index of the first <code>char</code> of the
     * subarray and the <code>count</code> argument specifies the
     * length of the subarray in <code>char</code>s. Unpaired
     * surrogates within the subarray count as one code point each.
     *
     * @param a the <code>char</code> array
     * @param offset the index of the first <code>char</code> in the
     * given <code>char</code> array
     * @param count the length of the subarray in <code>char</code>s
     * @return the number of Unicode code points in the specified subarray
     * @exception NullPointerException if <code>a</code> is null.
     * @exception IndexOutOfBoundsException if <code>offset</code> or
     * <code>count</code> is negative, or if <code>offset +
     * count</code> is larger than the length of the given array.
     * @since  1.5
     */
    public static int codePointCount(char[] a, int offset, int count) {
	if (count > a.length - offset || offset < 0 || count < 0) {
	    throw new IndexOutOfBoundsException();
	}
	return codePointCountImpl(a, offset, count);
    }

    static int codePointCountImpl(char[] a, int offset, int count) {
	int endIndex = offset + count;
	int n = 0;
	for (int i = offset; i < endIndex; ) {
	    n++;
	    if (isHighSurrogate(a[i++])) {
		if (i < endIndex && isLowSurrogate(a[i])) {
		    i++;
		}
	    }
	}
	return n;
    }

    /**
     * Returns the index within the given char sequence that is offset
     * from the given <code>index</code> by <code>codePointOffset</code>
     * code points. Unpaired surrogates within the text range given by
     * <code>index</code> and <code>codePointOffset</code> count as
     * one code point each.
     *
     * @param seq the char sequence
     * @param index the index to be offset
     * @param codePointOffset the offset in code points
     * @return the index within the char sequence
     * @exception NullPointerException if <code>seq</code> is null.
     * @exception IndexOutOfBoundsException if <code>index</code>
     *   is negative or larger then the length of the char sequence,
     *   or if <code>codePointOffset</code> is positive and the
     *   subsequence starting with <code>index</code> has fewer than
     *   <code>codePointOffset</code> code points, or if
     *   <code>codePointOffset</code> is negative and the subsequence
     *   before <code>index</code> has fewer than the absolute value
     *   of <code>codePointOffset</code> code points.
     * @since 1.5
     */
    public static int offsetByCodePoints(CharSequence seq, int index,
					 int codePointOffset) {
	int length = seq.length();
	if (index < 0 || index > length) {
	    throw new IndexOutOfBoundsException();
	}

	int x = index;
	if (codePointOffset >= 0) {
	    int i;
	    for (i = 0; x < length && i < codePointOffset; i++) {
		if (isHighSurrogate(seq.charAt(x++))) {
		    if (x < length && isLowSurrogate(seq.charAt(x))) {
			x++;
		    }
		}
	    }
	    if (i < codePointOffset) {
		throw new IndexOutOfBoundsException();
	    }
	} else {
	    int i;
	    for (i = codePointOffset; x > 0 && i < 0; i++) {
		if (isLowSurrogate(seq.charAt(--x))) {
		    if (x > 0 && isHighSurrogate(seq.charAt(x-1))) {
			x--;
		    }
		}
	    }
	    if (i < 0) {
		throw new IndexOutOfBoundsException();
	    }
	}
	return x;
    }

    /**
     * Returns the index within the given <code>char</code> subarray
     * that is offset from the given <code>index</code> by
     * <code>codePointOffset</code> code points. The
     * <code>start</code> and <code>count</code> arguments specify a
     * subarray of the <code>char</code> array. Unpaired surrogates
     * within the text range given by <code>index</code> and
     * <code>codePointOffset</code> count as one code point each.
     *
     * @param a the <code>char</code> array
     * @param start the index of the first <code>char</code> of the
     * subarray
     * @param count the length of the subarray in <code>char</code>s
     * @param index the index to be offset
     * @param codePointOffset the offset in code points
     * @return the index within the subarray
     * @exception NullPointerException if <code>a</code> is null.
     * @exception IndexOutOfBoundsException 
     *   if <code>start</code> or <code>count</code> is negative,
     *   or if <code>start + count</code> is larger than the length of
     *   the given array,
     *   or if <code>index</code> is less than <code>start</code> or
     *   larger then <code>start + count</code>,
     *   or if <code>codePointOffset</code> is positive and the text range
     *   starting with <code>index</code> and ending with <code>start
     *   + count - 1</code> has fewer than <code>codePointOffset</code> code
     *   points,
     *   or if <code>codePointOffset</code> is negative and the text range
     *   starting with <code>start</code> and ending with <code>index
     *   - 1</code> has fewer than the absolute value of
     *   <code>codePointOffset</code> code points.
     * @since 1.5
     */
    public static int offsetByCodePoints(char[] a, int start, int count,
					 int index, int codePointOffset) {
	if (count > a.length-start || start < 0 || count < 0
	    || index < start || index > start+count) {
	    throw new IndexOutOfBoundsException();
	}
	return offsetByCodePointsImpl(a, start, count, index, codePointOffset);
    }

    static int offsetByCodePointsImpl(char[]a, int start, int count,
				      int index, int codePointOffset) {
	int x = index;
	if (codePointOffset >= 0) {
	    int limit = start + count;
	    int i;
	    for (i = 0; x < limit && i < codePointOffset; i++) {
		if (isHighSurrogate(a[x++])) {
		    if (x < limit && isLowSurrogate(a[x])) {
			x++;
		    }
		}
	    }
	    if (i < codePointOffset) {
		throw new IndexOutOfBoundsException();
	    }
	} else {
	    int i;
	    for (i = codePointOffset; x > start && i < 0; i++) {
		if (isLowSurrogate(a[--x])) {
		    if (x > start && isHighSurrogate(a[x-1])) {
			x--;
		    }
		}
	    } 
	    if (i < 0) {
		throw new IndexOutOfBoundsException();
	    }
	}
	return x;
    }

   /**
     * Determines if the specified character is a lowercase character.
     * <p>
     * A character is lowercase if its general category type, provided
     * by <code>Character.getType(ch)</code>, is
     * <code>LOWERCASE_LETTER</code>.
     * <p>
     * The following are examples of lowercase characters:
     * <p><blockquote><pre>
     * a b c d e f g h i j k l m n o p q r s t u v w x y z
     * '&#92;u00DF' '&#92;u00E0' '&#92;u00E1' '&#92;u00E2' '&#92;u00E3' '&#92;u00E4' '&#92;u00E5' '&#92;u00E6' 
     * '&#92;u00E7' '&#92;u00E8' '&#92;u00E9' '&#92;u00EA' '&#92;u00EB' '&#92;u00EC' '&#92;u00ED' '&#92;u00EE'
     * '&#92;u00EF' '&#92;u00F0' '&#92;u00F1' '&#92;u00F2' '&#92;u00F3' '&#92;u00F4' '&#92;u00F5' '&#92;u00F6'
     * '&#92;u00F8' '&#92;u00F9' '&#92;u00FA' '&#92;u00FB' '&#92;u00FC' '&#92;u00FD' '&#92;u00FE' '&#92;u00FF'
     * </pre></blockquote>
     * <p> Many other Unicode characters are lowercase too.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isLowerCase(int)} method.
     *
     * @param   ch   the character to be tested.
     * @return  <code>true</code> if the character is lowercase;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isLowerCase(char)
     * @see     java.lang.Character#isTitleCase(char)
     * @see     java.lang.Character#toLowerCase(char)
     * @see     java.lang.Character#getType(char)
     */
    public static boolean isLowerCase(char ch) {
        return isLowerCase((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) is a
     * lowercase character.
     * <p>
     * A character is lowercase if its general category type, provided
     * by {@link Character#getType getType(codePoint)}, is
     * <code>LOWERCASE_LETTER</code>.
     * <p>
     * The following are examples of lowercase characters:
     * <p><blockquote><pre>
     * a b c d e f g h i j k l m n o p q r s t u v w x y z
     * '&#92;u00DF' '&#92;u00E0' '&#92;u00E1' '&#92;u00E2' '&#92;u00E3' '&#92;u00E4' '&#92;u00E5' '&#92;u00E6' 
     * '&#92;u00E7' '&#92;u00E8' '&#92;u00E9' '&#92;u00EA' '&#92;u00EB' '&#92;u00EC' '&#92;u00ED' '&#92;u00EE'
     * '&#92;u00EF' '&#92;u00F0' '&#92;u00F1' '&#92;u00F2' '&#92;u00F3' '&#92;u00F4' '&#92;u00F5' '&#92;u00F6'
     * '&#92;u00F8' '&#92;u00F9' '&#92;u00FA' '&#92;u00FB' '&#92;u00FC' '&#92;u00FD' '&#92;u00FE' '&#92;u00FF'
     * </pre></blockquote>
     * <p> Many other Unicode characters are lowercase too.
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is lowercase;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isLowerCase(int)
     * @see     java.lang.Character#isTitleCase(int)
     * @see     java.lang.Character#toLowerCase(int)
     * @see     java.lang.Character#getType(int)
     * @since   1.5
     */
    public static boolean isLowerCase(int codePoint) {
        boolean bLowerCase = false;

        // codePoint must be in the valid range of codepoints
        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bLowerCase = CharacterDataLatin1.isLowerCase(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bLowerCase = CharacterData00.isLowerCase(codePoint);
                break;
            case(1):
                bLowerCase = CharacterData01.isLowerCase(codePoint);
                break;
            case(2):
                bLowerCase = CharacterData02.isLowerCase(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bLowerCase = CharacterDataUndefined.isLowerCase(codePoint);
                break;
            case(14): 
                bLowerCase = CharacterData0E.isLowerCase(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bLowerCase = CharacterDataPrivateUse.isLowerCase(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bLowerCase remains false
                break;
            }
        }
        return bLowerCase;
    }

   /**
     * Determines if the specified character is an uppercase character.
     * <p>
     * A character is uppercase if its general category type, provided by
     * <code>Character.getType(ch)</code>, is <code>UPPERCASE_LETTER</code>.
     * <p>
     * The following are examples of uppercase characters:
     * <p><blockquote><pre>
     * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
     * '&#92;u00C0' '&#92;u00C1' '&#92;u00C2' '&#92;u00C3' '&#92;u00C4' '&#92;u00C5' '&#92;u00C6' '&#92;u00C7'
     * '&#92;u00C8' '&#92;u00C9' '&#92;u00CA' '&#92;u00CB' '&#92;u00CC' '&#92;u00CD' '&#92;u00CE' '&#92;u00CF'
     * '&#92;u00D0' '&#92;u00D1' '&#92;u00D2' '&#92;u00D3' '&#92;u00D4' '&#92;u00D5' '&#92;u00D6' '&#92;u00D8'
     * '&#92;u00D9' '&#92;u00DA' '&#92;u00DB' '&#92;u00DC' '&#92;u00DD' '&#92;u00DE'
     * </pre></blockquote>
     * <p> Many other Unicode characters are uppercase too.<p>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isUpperCase(int)} method.
     *
     * @param   ch   the character to be tested.
     * @return  <code>true</code> if the character is uppercase;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isLowerCase(char)
     * @see     java.lang.Character#isTitleCase(char)
     * @see     java.lang.Character#toUpperCase(char)
     * @see     java.lang.Character#getType(char)
     * @since   1.0
     */
    public static boolean isUpperCase(char ch) {
        return isUpperCase((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) is an uppercase character.
     * <p>
     * A character is uppercase if its general category type, provided by
     * {@link Character#getType(int) getType(codePoint)}, is <code>UPPERCASE_LETTER</code>.
     * <p>
     * The following are examples of uppercase characters:
     * <p><blockquote><pre>
     * A B C D E F G H I J K L M N O P Q R S T U V W X Y Z
     * '&#92;u00C0' '&#92;u00C1' '&#92;u00C2' '&#92;u00C3' '&#92;u00C4' '&#92;u00C5' '&#92;u00C6' '&#92;u00C7'
     * '&#92;u00C8' '&#92;u00C9' '&#92;u00CA' '&#92;u00CB' '&#92;u00CC' '&#92;u00CD' '&#92;u00CE' '&#92;u00CF'
     * '&#92;u00D0' '&#92;u00D1' '&#92;u00D2' '&#92;u00D3' '&#92;u00D4' '&#92;u00D5' '&#92;u00D6' '&#92;u00D8'
     * '&#92;u00D9' '&#92;u00DA' '&#92;u00DB' '&#92;u00DC' '&#92;u00DD' '&#92;u00DE'
     * </pre></blockquote>
     * <p> Many other Unicode characters are uppercase too.<p>
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is uppercase;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isLowerCase(int)
     * @see     java.lang.Character#isTitleCase(int)
     * @see     java.lang.Character#toUpperCase(int)
     * @see     java.lang.Character#getType(int)
     * @since   1.5
     */
    public static boolean isUpperCase(int codePoint) {
        boolean bUpperCase = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bUpperCase = CharacterDataLatin1.isUpperCase(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bUpperCase = CharacterData00.isUpperCase(codePoint);
                break;
            case(1):
                bUpperCase = CharacterData01.isUpperCase(codePoint);
                break;
            case(2):
                bUpperCase = CharacterData02.isUpperCase(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bUpperCase = CharacterDataUndefined.isUpperCase(codePoint);
                break;
            case(14):
                bUpperCase = CharacterData0E.isUpperCase(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bUpperCase = CharacterDataPrivateUse.isUpperCase(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bUpperCase remains false;
                break;
            }
        }
        return bUpperCase;
    }

    /**
     * Determines if the specified character is a titlecase character.
     * <p> 
     * A character is a titlecase character if its general
     * category type, provided by <code>Character.getType(ch)</code>,
     * is <code>TITLECASE_LETTER</code>.
     * <p>
     * Some characters look like pairs of Latin letters. For example, there
     * is an uppercase letter that looks like "LJ" and has a corresponding
     * lowercase letter that looks like "lj". A third form, which looks like "Lj",
     * is the appropriate form to use when rendering a word in lowercase
     * with initial capitals, as for a book title.
     * <p>
     * These are some of the Unicode characters for which this method returns
     * <code>true</code>:
     * <ul>
     * <li><code>LATIN CAPITAL LETTER D WITH SMALL LETTER Z WITH CARON</code>
     * <li><code>LATIN CAPITAL LETTER L WITH SMALL LETTER J</code>
     * <li><code>LATIN CAPITAL LETTER N WITH SMALL LETTER J</code>
     * <li><code>LATIN CAPITAL LETTER D WITH SMALL LETTER Z</code>
     * </ul>
     * <p> Many other Unicode characters are titlecase too.<p>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isTitleCase(int)} method.
     *
     * @param   ch   the character to be tested.
     * @return  <code>true</code> if the character is titlecase;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isLowerCase(char)
     * @see     java.lang.Character#isUpperCase(char)
     * @see     java.lang.Character#toTitleCase(char)
     * @see     java.lang.Character#getType(char)
     * @since   1.0.2
     */
    public static boolean isTitleCase(char ch) {
        return isTitleCase((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) is a titlecase character.
     * <p> 
     * A character is a titlecase character if its general
     * category type, provided by {@link Character#getType(int) getType(codePoint)},
     * is <code>TITLECASE_LETTER</code>.
     * <p>
     * Some characters look like pairs of Latin letters. For example, there
     * is an uppercase letter that looks like "LJ" and has a corresponding
     * lowercase letter that looks like "lj". A third form, which looks like "Lj",
     * is the appropriate form to use when rendering a word in lowercase
     * with initial capitals, as for a book title.
     * <p>
     * These are some of the Unicode characters for which this method returns
     * <code>true</code>:
     * <ul>
     * <li><code>LATIN CAPITAL LETTER D WITH SMALL LETTER Z WITH CARON</code>
     * <li><code>LATIN CAPITAL LETTER L WITH SMALL LETTER J</code>
     * <li><code>LATIN CAPITAL LETTER N WITH SMALL LETTER J</code>
     * <li><code>LATIN CAPITAL LETTER D WITH SMALL LETTER Z</code>
     * </ul>
     * <p> Many other Unicode characters are titlecase too.<p>
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is titlecase;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isLowerCase(int)
     * @see     java.lang.Character#isUpperCase(int)
     * @see     java.lang.Character#toTitleCase(int)
     * @see     java.lang.Character#getType(int)
     * @since   1.5
     */
    public static boolean isTitleCase(int codePoint) {
        boolean bTitleCase = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bTitleCase = CharacterDataLatin1.isTitleCase(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bTitleCase = CharacterData00.isTitleCase(codePoint);
                break;
            case(1):
                bTitleCase = CharacterData01.isTitleCase(codePoint);
                break;
            case(2):
                bTitleCase = CharacterData02.isTitleCase(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bTitleCase = CharacterDataUndefined.isTitleCase(codePoint);
                break;
            case(14): 
                bTitleCase = CharacterData0E.isTitleCase(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bTitleCase = CharacterDataPrivateUse.isTitleCase(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bTitleCase remains false;
                break;
            }
        }
        return bTitleCase;
    }

    /**
     * Determines if the specified character is a digit.
     * <p>
     * A character is a digit if its general category type, provided
     * by <code>Character.getType(ch)</code>, is
     * <code>DECIMAL_DIGIT_NUMBER</code>.
     * <p>
     * Some Unicode character ranges that contain digits:
     * <ul>
     * <li><code>'&#92;u0030'</code> through <code>'&#92;u0039'</code>, 
     *     ISO-LATIN-1 digits (<code>'0'</code> through <code>'9'</code>)
     * <li><code>'&#92;u0660'</code> through <code>'&#92;u0669'</code>,
     *     Arabic-Indic digits
     * <li><code>'&#92;u06F0'</code> through <code>'&#92;u06F9'</code>,
     *     Extended Arabic-Indic digits
     * <li><code>'&#92;u0966'</code> through <code>'&#92;u096F'</code>,
     *     Devanagari digits
     * <li><code>'&#92;uFF10'</code> through <code>'&#92;uFF19'</code>,
     *     Fullwidth digits
     * </ul>
     *
     * Many other character ranges contain digits as well.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isDigit(int)} method.
     *
     * @param   ch   the character to be tested.
     * @return  <code>true</code> if the character is a digit;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#digit(char, int)
     * @see     java.lang.Character#forDigit(int, int)
     * @see     java.lang.Character#getType(char)
     */
    public static boolean isDigit(char ch) {
        return isDigit((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) is a digit.
     * <p>
     * A character is a digit if its general category type, provided
     * by {@link Character#getType(int) getType(codePoint)}, is
     * <code>DECIMAL_DIGIT_NUMBER</code>.
     * <p>
     * Some Unicode character ranges that contain digits:
     * <ul>
     * <li><code>'&#92;u0030'</code> through <code>'&#92;u0039'</code>, 
     *     ISO-LATIN-1 digits (<code>'0'</code> through <code>'9'</code>)
     * <li><code>'&#92;u0660'</code> through <code>'&#92;u0669'</code>,
     *     Arabic-Indic digits
     * <li><code>'&#92;u06F0'</code> through <code>'&#92;u06F9'</code>,
     *     Extended Arabic-Indic digits
     * <li><code>'&#92;u0966'</code> through <code>'&#92;u096F'</code>,
     *     Devanagari digits
     * <li><code>'&#92;uFF10'</code> through <code>'&#92;uFF19'</code>,
     *     Fullwidth digits
     * </ul>
     *
     * Many other character ranges contain digits as well.
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is a digit;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#forDigit(int, int)
     * @see     java.lang.Character#getType(int)
     * @since   1.5
     */
    public static boolean isDigit(int codePoint) {
        boolean bDigit = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bDigit = CharacterDataLatin1.isDigit(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bDigit = CharacterData00.isDigit(codePoint);
                break;
            case(1):
                bDigit = CharacterData01.isDigit(codePoint);
                break;
            case(2):
                bDigit = CharacterData02.isDigit(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bDigit = CharacterDataUndefined.isDigit(codePoint);
                break;
            case(14):
                bDigit = CharacterData0E.isDigit(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bDigit = CharacterDataPrivateUse.isDigit(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bDigit remains false;
                break;                          
            }
        }
        return bDigit;
    }

    /**
     * Determines if a character is defined in Unicode.
     * <p>
     * A character is defined if at least one of the following is true:
     * <ul>
     * <li>It has an entry in the UnicodeData file.
     * <li>It has a value in a range defined by the UnicodeData file.
     * </ul>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isDefined(int)} method.
     *
     * @param   ch   the character to be tested
     * @return  <code>true</code> if the character has a defined meaning
     *          in Unicode; <code>false</code> otherwise.
     * @see     java.lang.Character#isDigit(char)
     * @see     java.lang.Character#isLetter(char)
     * @see     java.lang.Character#isLetterOrDigit(char)
     * @see     java.lang.Character#isLowerCase(char)
     * @see     java.lang.Character#isTitleCase(char)
     * @see     java.lang.Character#isUpperCase(char)
     * @since   1.0.2
     */
    public static boolean isDefined(char ch) {
        return isDefined((int)ch);
    }

    /**
     * Determines if a character (Unicode code point) is defined in Unicode.
     * <p>
     * A character is defined if at least one of the following is true:
     * <ul>
     * <li>It has an entry in the UnicodeData file.
     * <li>It has a value in a range defined by the UnicodeData file.
     * </ul>
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character has a defined meaning
     *          in Unicode; <code>false</code> otherwise.
     * @see     java.lang.Character#isDigit(int)
     * @see     java.lang.Character#isLetter(int)
     * @see     java.lang.Character#isLetterOrDigit(int)
     * @see     java.lang.Character#isLowerCase(int)
     * @see     java.lang.Character#isTitleCase(int)
     * @see     java.lang.Character#isUpperCase(int)
     * @since   1.5
     */
    public static boolean isDefined(int codePoint) {
        boolean bDefined = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bDefined = CharacterDataLatin1.isDefined(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bDefined = CharacterData00.isDefined(codePoint);
                break;
            case(1):
                bDefined = CharacterData01.isDefined(codePoint);
                break;
            case(2):
                bDefined = CharacterData02.isDefined(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bDefined = CharacterDataUndefined.isDefined(codePoint);
                break;
            case(14): 
                bDefined = CharacterData0E.isDefined(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bDefined = CharacterDataPrivateUse.isDefined(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bDefined remains false;
                break;
            }
        }
        return bDefined;
    }

    /**
     * Determines if the specified character is a letter.
     * <p>
     * A character is considered to be a letter if its general
     * category type, provided by <code>Character.getType(ch)</code>,
     * is any of the following:
     * <ul>
     * <li> <code>UPPERCASE_LETTER</code>
     * <li> <code>LOWERCASE_LETTER</code>
     * <li> <code>TITLECASE_LETTER</code>
     * <li> <code>MODIFIER_LETTER</code>
     * <li> <code>OTHER_LETTER</code>
     * </ul>
     *
     * Not all letters have case. Many characters are
     * letters but are neither uppercase nor lowercase nor titlecase.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isLetter(int)} method.
     *
     * @param   ch   the character to be tested.
     * @return  <code>true</code> if the character is a letter;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isDigit(char)
     * @see     java.lang.Character#isJavaIdentifierStart(char)
     * @see     java.lang.Character#isJavaLetter(char)
     * @see     java.lang.Character#isJavaLetterOrDigit(char)
     * @see     java.lang.Character#isLetterOrDigit(char)
     * @see     java.lang.Character#isLowerCase(char)
     * @see     java.lang.Character#isTitleCase(char)
     * @see     java.lang.Character#isUnicodeIdentifierStart(char)
     * @see     java.lang.Character#isUpperCase(char)
     */
    public static boolean isLetter(char ch) {
        return isLetter((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) is a letter.
     * <p>
     * A character is considered to be a letter if its general
     * category type, provided by {@link Character#getType(int) getType(codePoint)},
     * is any of the following:
     * <ul>
     * <li> <code>UPPERCASE_LETTER</code>
     * <li> <code>LOWERCASE_LETTER</code>
     * <li> <code>TITLECASE_LETTER</code>
     * <li> <code>MODIFIER_LETTER</code>
     * <li> <code>OTHER_LETTER</code>
     * </ul>
     *
     * Not all letters have case. Many characters are
     * letters but are neither uppercase nor lowercase nor titlecase.
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is a letter;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isDigit(int)
     * @see     java.lang.Character#isJavaIdentifierStart(int)
     * @see     java.lang.Character#isLetterOrDigit(int)
     * @see     java.lang.Character#isLowerCase(int)
     * @see     java.lang.Character#isTitleCase(int)
     * @see     java.lang.Character#isUnicodeIdentifierStart(int)
     * @see     java.lang.Character#isUpperCase(int)
     * @since   1.5
     */
    public static boolean isLetter(int codePoint) {
        boolean bLetter = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bLetter = CharacterDataLatin1.isLetter(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bLetter = CharacterData00.isLetter(codePoint);
                break;
            case(1):
                bLetter = CharacterData01.isLetter(codePoint);
                break;
            case(2):
                bLetter = CharacterData02.isLetter(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bLetter = CharacterDataUndefined.isLetter(codePoint);
                break;
            case(14):
                bLetter = CharacterData0E.isLetter(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bLetter = CharacterDataPrivateUse.isLetter(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bLetter remains false;
                break;
            }
        }
        return bLetter;
    }

    /**
     * Determines if the specified character is a letter or digit.
     * <p>
     * A character is considered to be a letter or digit if either
     * <code>Character.isLetter(char ch)</code> or
     * <code>Character.isDigit(char ch)</code> returns
     * <code>true</code> for the character.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isLetterOrDigit(int)} method.
     *
     * @param   ch   the character to be tested.
     * @return  <code>true</code> if the character is a letter or digit;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isDigit(char)
     * @see     java.lang.Character#isJavaIdentifierPart(char)
     * @see     java.lang.Character#isJavaLetter(char)
     * @see     java.lang.Character#isJavaLetterOrDigit(char)
     * @see     java.lang.Character#isLetter(char)
     * @see     java.lang.Character#isUnicodeIdentifierPart(char)
     * @since   1.0.2
     */
    public static boolean isLetterOrDigit(char ch) {
        return isLetterOrDigit((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) is a letter or digit.
     * <p>
     * A character is considered to be a letter or digit if either
     * {@link #isLetter(int) isLetter(codePoint)} or
     * {@link #isDigit(int) isDigit(codePoint)} returns
     * <code>true</code> for the character.
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is a letter or digit;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isDigit(int)
     * @see     java.lang.Character#isJavaIdentifierPart(int)
     * @see     java.lang.Character#isLetter(int)
     * @see     java.lang.Character#isUnicodeIdentifierPart(int)
     * @since   1.5
     */
    public static boolean isLetterOrDigit(int codePoint) {
        boolean bLetterOrDigit = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bLetterOrDigit = CharacterDataLatin1.isLetterOrDigit(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bLetterOrDigit = CharacterData00.isLetterOrDigit(codePoint);
                break;
            case(1):
                bLetterOrDigit = CharacterData01.isLetterOrDigit(codePoint);
                break;
            case(2):
                bLetterOrDigit = CharacterData02.isLetterOrDigit(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bLetterOrDigit = CharacterDataUndefined.isLetterOrDigit(codePoint);
                break;
            case(14): // Undefined
                bLetterOrDigit = CharacterData0E.isLetterOrDigit(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bLetterOrDigit = CharacterDataPrivateUse.isLetterOrDigit(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bLetterOrDigit remains false;
                break;
            }
        }
        return bLetterOrDigit;
    }

    /**
     * Determines if the specified character is permissible as the first
     * character in a Java identifier.
     * <p>
     * A character may start a Java identifier if and only if
     * one of the following is true:
     * <ul>
     * <li> {@link #isLetter(char) isLetter(ch)} returns <code>true</code>
     * <li> {@link #getType(char) getType(ch)} returns <code>LETTER_NUMBER</code>
     * <li> ch is a currency symbol (such as "$")
     * <li> ch is a connecting punctuation character (such as "_").
     * </ul>
     *
     * @param   ch the character to be tested.
     * @return  <code>true</code> if the character may start a Java
     *          identifier; <code>false</code> otherwise.
     * @see     java.lang.Character#isJavaLetterOrDigit(char)
     * @see     java.lang.Character#isJavaIdentifierStart(char)
     * @see     java.lang.Character#isJavaIdentifierPart(char)
     * @see     java.lang.Character#isLetter(char)
     * @see     java.lang.Character#isLetterOrDigit(char)
     * @see     java.lang.Character#isUnicodeIdentifierStart(char)
     * @since   1.02
     * @deprecated Replaced by isJavaIdentifierStart(char).
     */
    public static boolean isJavaLetter(char ch) {
        return isJavaIdentifierStart(ch);
    }

    /**
     * Determines if the specified character may be part of a Java
     * identifier as other than the first character.
     * <p>
     * A character may be part of a Java identifier if and only if any
     * of the following are true:
     * <ul>
     * <li>  it is a letter
     * <li>  it is a currency symbol (such as <code>'$'</code>)
     * <li>  it is a connecting punctuation character (such as <code>'_'</code>)
     * <li>  it is a digit
     * <li>  it is a numeric letter (such as a Roman numeral character)
     * <li>  it is a combining mark
     * <li>  it is a non-spacing mark
     * <li> <code>isIdentifierIgnorable</code> returns
     * <code>true</code> for the character.
     * </ul>
     *
     * @param   ch the character to be tested.
     * @return  <code>true</code> if the character may be part of a
     *          Java identifier; <code>false</code> otherwise.
     * @see     java.lang.Character#isJavaLetter(char)
     * @see     java.lang.Character#isJavaIdentifierStart(char)
     * @see     java.lang.Character#isJavaIdentifierPart(char)
     * @see     java.lang.Character#isLetter(char)
     * @see     java.lang.Character#isLetterOrDigit(char)
     * @see     java.lang.Character#isUnicodeIdentifierPart(char)
     * @see     java.lang.Character#isIdentifierIgnorable(char)
     * @since   1.02
     * @deprecated Replaced by isJavaIdentifierPart(char).
     */
    public static boolean isJavaLetterOrDigit(char ch) {
        return isJavaIdentifierPart(ch);
    }

    /**
     * Determines if the specified character is
     * permissible as the first character in a Java identifier.
     * <p>
     * A character may start a Java identifier if and only if
     * one of the following conditions is true:
     * <ul>
     * <li> {@link #isLetter(char) isLetter(ch)} returns <code>true</code>
     * <li> {@link #getType(char) getType(ch)} returns <code>LETTER_NUMBER</code>
     * <li> ch is a currency symbol (such as "$")
     * <li> ch is a connecting punctuation character (such as "_").
     * </ul>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isJavaIdentifierStart(int)} method.
     *
     * @param   ch the character to be tested.
     * @return  <code>true</code> if the character may start a Java identifier;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isJavaIdentifierPart(char)
     * @see     java.lang.Character#isLetter(char)
     * @see     java.lang.Character#isUnicodeIdentifierStart(char)
     * @since   1.1
     */
    public static boolean isJavaIdentifierStart(char ch) {
        return isJavaIdentifierStart((int)ch);
    }

    /**
     * Determines if the character (Unicode code point) is
     * permissible as the first character in a Java identifier.
     * <p>
     * A character may start a Java identifier if and only if
     * one of the following conditions is true:
     * <ul>
     * <li> {@link #isLetter(int) isLetter(codePoint)}
     *      returns <code>true</code>
     * <li> {@link #getType(int) getType(codePoint)}
     *      returns <code>LETTER_NUMBER</code>
     * <li> the referenced character is a currency symbol (such as "$")
     * <li> the referenced character is a connecting punctuation character
     *      (such as "_").
     * </ul>
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character may start a Java identifier;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isJavaIdentifierPart(int)
     * @see     java.lang.Character#isLetter(int)
     * @see     java.lang.Character#isUnicodeIdentifierStart(int)
     * @since   1.5
     */
    public static boolean isJavaIdentifierStart(int codePoint) {
        boolean bJavaStart = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bJavaStart = CharacterDataLatin1.isJavaIdentifierStart(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bJavaStart = CharacterData00.isJavaIdentifierStart(codePoint);
                break;
            case(1):
                bJavaStart = CharacterData01.isJavaIdentifierStart(codePoint);
                break;
            case(2):
                bJavaStart = CharacterData02.isJavaIdentifierStart(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bJavaStart = CharacterDataUndefined.isJavaIdentifierStart(codePoint);
                break;
            case(14): 
                bJavaStart = CharacterData0E.isJavaIdentifierStart(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bJavaStart = CharacterDataPrivateUse.isJavaIdentifierStart(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bJavaStart remains false;
                break;
            }   
        }
        return bJavaStart;
    }

    /**
     * Determines if the specified character may be part of a Java
     * identifier as other than the first character.
     * <p>
     * A character may be part of a Java identifier if any of the following
     * are true:
     * <ul>
     * <li>  it is a letter
     * <li>  it is a currency symbol (such as <code>'$'</code>)
     * <li>  it is a connecting punctuation character (such as <code>'_'</code>)
     * <li>  it is a digit
     * <li>  it is a numeric letter (such as a Roman numeral character)
     * <li>  it is a combining mark
     * <li>  it is a non-spacing mark
     * <li> <code>isIdentifierIgnorable</code> returns
     * <code>true</code> for the character
     * </ul>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isJavaIdentifierPart(int)} method.
     *
     * @param   ch      the character to be tested.
     * @return <code>true</code> if the character may be part of a
     *          Java identifier; <code>false</code> otherwise.
     * @see     java.lang.Character#isIdentifierIgnorable(char)
     * @see     java.lang.Character#isJavaIdentifierStart(char)
     * @see     java.lang.Character#isLetterOrDigit(char)
     * @see     java.lang.Character#isUnicodeIdentifierPart(char)
     * @since   1.1
     */
    public static boolean isJavaIdentifierPart(char ch) {
        return isJavaIdentifierPart((int)ch);
    }

    /**
     * Determines if the character (Unicode code point) may be part of a Java
     * identifier as other than the first character.
     * <p>
     * A character may be part of a Java identifier if any of the following
     * are true:
     * <ul>
     * <li>  it is a letter
     * <li>  it is a currency symbol (such as <code>'$'</code>)
     * <li>  it is a connecting punctuation character (such as <code>'_'</code>)
     * <li>  it is a digit
     * <li>  it is a numeric letter (such as a Roman numeral character)
     * <li>  it is a combining mark
     * <li>  it is a non-spacing mark
     * <li> {@link #isIdentifierIgnorable(int)
     * isIdentifierIgnorable(codePoint)} returns <code>true</code> for
     * the character
     * </ul>
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return <code>true</code> if the character may be part of a
     *          Java identifier; <code>false</code> otherwise.
     * @see     java.lang.Character#isIdentifierIgnorable(int)
     * @see     java.lang.Character#isJavaIdentifierStart(int)
     * @see     java.lang.Character#isLetterOrDigit(int)
     * @see     java.lang.Character#isUnicodeIdentifierPart(int)
     * @since   1.5
     */
    public static boolean isJavaIdentifierPart(int codePoint) {
        boolean bJavaPart = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bJavaPart = CharacterDataLatin1.isJavaIdentifierPart(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bJavaPart = CharacterData00.isJavaIdentifierPart(codePoint);
                break;
            case(1):
                bJavaPart = CharacterData01.isJavaIdentifierPart(codePoint);
                break;
            case(2):
                bJavaPart = CharacterData02.isJavaIdentifierPart(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bJavaPart = CharacterDataUndefined.isJavaIdentifierPart(codePoint);
                break;
            case(14): 
                bJavaPart = CharacterData0E.isJavaIdentifierPart(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bJavaPart = CharacterDataPrivateUse.isJavaIdentifierPart(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bJavaPart remains false;
                break;
            }
        }
        return bJavaPart;
    }

    /**
     * Determines if the specified character is permissible as the
     * first character in a Unicode identifier.
     * <p>
     * A character may start a Unicode identifier if and only if
     * one of the following conditions is true:
     * <ul>
     * <li> {@link #isLetter(char) isLetter(ch)} returns <code>true</code>
     * <li> {@link #getType(char) getType(ch)} returns 
     *      <code>LETTER_NUMBER</code>.
     * </ul>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isUnicodeIdentifierStart(int)} method.
     *
     * @param   ch      the character to be tested.
     * @return  <code>true</code> if the character may start a Unicode 
     *          identifier; <code>false</code> otherwise.
     * @see     java.lang.Character#isJavaIdentifierStart(char)
     * @see     java.lang.Character#isLetter(char)
     * @see     java.lang.Character#isUnicodeIdentifierPart(char)
     * @since   1.1
     */
    public static boolean isUnicodeIdentifierStart(char ch) {
        return isUnicodeIdentifierStart((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) is permissible as the
     * first character in a Unicode identifier.
     * <p>
     * A character may start a Unicode identifier if and only if
     * one of the following conditions is true:
     * <ul>
     * <li> {@link #isLetter(int) isLetter(codePoint)}
     *      returns <code>true</code>
     * <li> {@link #getType(int) getType(codePoint)}
     *      returns <code>LETTER_NUMBER</code>.
     * </ul>
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character may start a Unicode 
     *          identifier; <code>false</code> otherwise.
     * @see     java.lang.Character#isJavaIdentifierStart(int)
     * @see     java.lang.Character#isLetter(int)
     * @see     java.lang.Character#isUnicodeIdentifierPart(int)
     * @since   1.5
     */
    public static boolean isUnicodeIdentifierStart(int codePoint) {
        boolean bUnicodeStart = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bUnicodeStart = CharacterDataLatin1.isUnicodeIdentifierStart(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bUnicodeStart = CharacterData00.isUnicodeIdentifierStart(codePoint);
                break;
            case(1):
                bUnicodeStart = CharacterData01.isUnicodeIdentifierStart(codePoint);
                break;
            case(2):
                bUnicodeStart = CharacterData02.isUnicodeIdentifierStart(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bUnicodeStart = CharacterDataUndefined.isUnicodeIdentifierStart(codePoint);
                break;
            case(14): 
                bUnicodeStart = CharacterData0E.isUnicodeIdentifierStart(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bUnicodeStart = CharacterDataPrivateUse.isUnicodeIdentifierStart(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bUnicodeStart remains false;
                break;
            }
        }
        return bUnicodeStart;
    }

    /**
     * Determines if the specified character may be part of a Unicode
     * identifier as other than the first character.
     * <p>
     * A character may be part of a Unicode identifier if and only if
     * one of the following statements is true:
     * <ul>
     * <li>  it is a letter
     * <li>  it is a connecting punctuation character (such as <code>'_'</code>)
     * <li>  it is a digit
     * <li>  it is a numeric letter (such as a Roman numeral character)
     * <li>  it is a combining mark
     * <li>  it is a non-spacing mark
     * <li> <code>isIdentifierIgnorable</code> returns
     * <code>true</code> for this character.
     * </ul>
     * 
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isUnicodeIdentifierPart(int)} method.
     *
     * @param   ch      the character to be tested.
     * @return  <code>true</code> if the character may be part of a 
     *          Unicode identifier; <code>false</code> otherwise.
     * @see     java.lang.Character#isIdentifierIgnorable(char)
     * @see     java.lang.Character#isJavaIdentifierPart(char)
     * @see     java.lang.Character#isLetterOrDigit(char)
     * @see     java.lang.Character#isUnicodeIdentifierStart(char)
     * @since   1.1
     */
    public static boolean isUnicodeIdentifierPart(char ch) {
        return isUnicodeIdentifierPart((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) may be part of a Unicode
     * identifier as other than the first character.
     * <p>
     * A character may be part of a Unicode identifier if and only if
     * one of the following statements is true:
     * <ul>
     * <li>  it is a letter
     * <li>  it is a connecting punctuation character (such as <code>'_'</code>)
     * <li>  it is a digit
     * <li>  it is a numeric letter (such as a Roman numeral character)
     * <li>  it is a combining mark
     * <li>  it is a non-spacing mark
     * <li> <code>isIdentifierIgnorable</code> returns
     * <code>true</code> for this character.
     * </ul>
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character may be part of a 
     *          Unicode identifier; <code>false</code> otherwise.
     * @see     java.lang.Character#isIdentifierIgnorable(int)
     * @see     java.lang.Character#isJavaIdentifierPart(int)
     * @see     java.lang.Character#isLetterOrDigit(int)
     * @see     java.lang.Character#isUnicodeIdentifierStart(int)
     * @since   1.5
     */
    public static boolean isUnicodeIdentifierPart(int codePoint) {
        boolean bUnicodePart = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bUnicodePart = CharacterDataLatin1.isUnicodeIdentifierPart(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bUnicodePart = CharacterData00.isUnicodeIdentifierPart(codePoint);
                break;
            case(1):
                bUnicodePart = CharacterData01.isUnicodeIdentifierPart(codePoint);
                break;
            case(2):
                bUnicodePart = CharacterData02.isUnicodeIdentifierPart(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bUnicodePart = CharacterDataUndefined.isUnicodeIdentifierPart(codePoint);
                break;
            case(14): 
                bUnicodePart = CharacterData0E.isUnicodeIdentifierPart(codePoint);      
                break;
            case(15): // Private Use
            case(16): // Private Use
                bUnicodePart = CharacterDataPrivateUse.isUnicodeIdentifierPart(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                //bUnicodePart remains false;
                break;
            }
        }
        return bUnicodePart;
    }

    /**
     * Determines if the specified character should be regarded as
     * an ignorable character in a Java identifier or a Unicode identifier.
     * <p>
     * The following Unicode characters are ignorable in a Java identifier
     * or a Unicode identifier:
     * <ul>
     * <li>ISO control characters that are not whitespace
     * <ul>
     * <li><code>'&#92;u0000'</code> through <code>'&#92;u0008'</code>
     * <li><code>'&#92;u000E'</code> through <code>'&#92;u001B'</code>
     * <li><code>'&#92;u007F'</code> through <code>'&#92;u009F'</code>
     * </ul>
     *
     * <li>all characters that have the <code>FORMAT</code> general
     * category value
     * </ul>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isIdentifierIgnorable(int)} method.
     *
     * @param   ch      the character to be tested.
     * @return  <code>true</code> if the character is an ignorable control 
     *          character that may be part of a Java or Unicode identifier;
     *           <code>false</code> otherwise.
     * @see     java.lang.Character#isJavaIdentifierPart(char)
     * @see     java.lang.Character#isUnicodeIdentifierPart(char)
     * @since   1.1
     */
    public static boolean isIdentifierIgnorable(char ch) {
        return isIdentifierIgnorable((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) should be regarded as
     * an ignorable character in a Java identifier or a Unicode identifier.
     * <p>
     * The following Unicode characters are ignorable in a Java identifier
     * or a Unicode identifier:
     * <ul>
     * <li>ISO control characters that are not whitespace
     * <ul>
     * <li><code>'&#92;u0000'</code> through <code>'&#92;u0008'</code>
     * <li><code>'&#92;u000E'</code> through <code>'&#92;u001B'</code>
     * <li><code>'&#92;u007F'</code> through <code>'&#92;u009F'</code>
     * </ul>
     *
     * <li>all characters that have the <code>FORMAT</code> general
     * category value
     * </ul>
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is an ignorable control 
     *          character that may be part of a Java or Unicode identifier;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isJavaIdentifierPart(int)
     * @see     java.lang.Character#isUnicodeIdentifierPart(int)
     * @since   1.5
     */
    public static boolean isIdentifierIgnorable(int codePoint) {
        boolean bIdentifierIgnorable = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bIdentifierIgnorable = CharacterDataLatin1.isIdentifierIgnorable(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bIdentifierIgnorable = CharacterData00.isIdentifierIgnorable(codePoint);
                break;
            case(1):
                bIdentifierIgnorable = CharacterData01.isIdentifierIgnorable(codePoint);
                break;
            case(2):
                bIdentifierIgnorable = CharacterData02.isIdentifierIgnorable(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bIdentifierIgnorable = CharacterDataUndefined.isIdentifierIgnorable(codePoint);
                break;
            case(14): 
                bIdentifierIgnorable = CharacterData0E.isIdentifierIgnorable(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bIdentifierIgnorable = CharacterDataPrivateUse.isIdentifierIgnorable(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bIdentifierIgnorable remains false;
                break;
            }
        }
        return bIdentifierIgnorable;
    }

    /**
     * Converts the character argument to lowercase using case
     * mapping information from the UnicodeData file.
     * <p>
     * Note that
     * <code>Character.isLowerCase(Character.toLowerCase(ch))</code>
     * does not always return <code>true</code> for some ranges of
     * characters, particularly those that are symbols or ideographs.
     *
     * <p>In general, {@link java.lang.String#toLowerCase()} should be used to map
     * characters to lowercase. <code>String</code> case mapping methods
     * have several benefits over <code>Character</code> case mapping methods.
     * <code>String</code> case mapping methods can perform locale-sensitive
     * mappings, context-sensitive mappings, and 1:M character mappings, whereas
     * the <code>Character</code> case mapping methods cannot.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #toLowerCase(int)} method.
     *
     * @param   ch   the character to be converted.
     * @return  the lowercase equivalent of the character, if any;
     *          otherwise, the character itself.
     * @see     java.lang.Character#isLowerCase(char)
     * @see     java.lang.String#toLowerCase()
     */
    public static char toLowerCase(char ch) {
        return (char)toLowerCase((int)ch);
    }

    /**
     * Converts the character (Unicode code point) argument to
     * lowercase using case mapping information from the UnicodeData
     * file.
     *
     * <p> Note that
     * <code>Character.isLowerCase(Character.toLowerCase(codePoint))</code>
     * does not always return <code>true</code> for some ranges of
     * characters, particularly those that are symbols or ideographs.
     *
     * <p>In general, {@link java.lang.String#toLowerCase()} should be used to map
     * characters to lowercase. <code>String</code> case mapping methods
     * have several benefits over <code>Character</code> case mapping methods.
     * <code>String</code> case mapping methods can perform locale-sensitive
     * mappings, context-sensitive mappings, and 1:M character mappings, whereas
     * the <code>Character</code> case mapping methods cannot.
     *
     * @param   codePoint   the character (Unicode code point) to be converted.
     * @return  the lowercase equivalent of the character (Unicode code
     *          point), if any; otherwise, the character itself.
     * @see     java.lang.Character#isLowerCase(int)
     * @see     java.lang.String#toLowerCase()
     *
     * @since   1.5
     */
    public static int toLowerCase(int codePoint) {
        int lowerCase = codePoint;
        int plane = 0;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            lowerCase = CharacterDataLatin1.toLowerCase(codePoint);
        } else {
            plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                lowerCase = CharacterData00.toLowerCase(codePoint);
                break;
            case(1):
                lowerCase = CharacterData01.toLowerCase(codePoint);
                break;
            case(2):
                lowerCase = CharacterData02.toLowerCase(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                lowerCase = CharacterDataUndefined.toLowerCase(codePoint);
                break;
            case(14):
                lowerCase = CharacterData0E.toLowerCase(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                lowerCase = CharacterDataPrivateUse.toLowerCase(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // lowerCase remains codePoint;
                break;
            }
        }
        return lowerCase;
    }

    /**
     * Converts the character argument to uppercase using case mapping
     * information from the UnicodeData file.
     * <p>
     * Note that
     * <code>Character.isUpperCase(Character.toUpperCase(ch))</code>
     * does not always return <code>true</code> for some ranges of
     * characters, particularly those that are symbols or ideographs.
     *
     * <p>In general, {@link java.lang.String#toUpperCase()} should be used to map
     * characters to uppercase. <code>String</code> case mapping methods
     * have several benefits over <code>Character</code> case mapping methods.
     * <code>String</code> case mapping methods can perform locale-sensitive
     * mappings, context-sensitive mappings, and 1:M character mappings, whereas
     * the <code>Character</code> case mapping methods cannot.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #toUpperCase(int)} method.
     *
     * @param   ch   the character to be converted.
     * @return  the uppercase equivalent of the character, if any;
     *          otherwise, the character itself.
     * @see     java.lang.Character#isUpperCase(char)
     * @see     java.lang.String#toUpperCase()
     */
    public static char toUpperCase(char ch) {
        return (char)toUpperCase((int)ch);
    }

    /**
     * Converts the character (Unicode code point) argument to
     * uppercase using case mapping information from the UnicodeData
     * file.
     * 
     * <p>Note that
     * <code>Character.isUpperCase(Character.toUpperCase(codePoint))</code>
     * does not always return <code>true</code> for some ranges of
     * characters, particularly those that are symbols or ideographs.
     *
     * <p>In general, {@link java.lang.String#toUpperCase()} should be used to map
     * characters to uppercase. <code>String</code> case mapping methods
     * have several benefits over <code>Character</code> case mapping methods.
     * <code>String</code> case mapping methods can perform locale-sensitive
     * mappings, context-sensitive mappings, and 1:M character mappings, whereas
     * the <code>Character</code> case mapping methods cannot.
     *
     * @param   codePoint   the character (Unicode code point) to be converted.
     * @return  the uppercase equivalent of the character, if any;
     *          otherwise, the character itself.
     * @see     java.lang.Character#isUpperCase(int)
     * @see     java.lang.String#toUpperCase()
     * 
     * @since   1.5
     */
    public static int toUpperCase(int codePoint) {
        int upperCase = codePoint;
        int plane = 0;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            upperCase = CharacterDataLatin1.toUpperCase(codePoint);
        } else {
            plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                upperCase = CharacterData00.toUpperCase(codePoint);
                break;
            case(1):
                upperCase = CharacterData01.toUpperCase(codePoint);
                break;
            case(2):
                upperCase = CharacterData02.toUpperCase(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                upperCase = CharacterDataUndefined.toUpperCase(codePoint);
                break;
            case(14): 
                upperCase = CharacterData0E.toUpperCase(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                upperCase = CharacterDataPrivateUse.toUpperCase(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // upperCase remains codePoint;
                break;
            }
        }
        return upperCase;
    }

    /**
     * Converts the character argument to titlecase using case mapping
     * information from the UnicodeData file. If a character has no
     * explicit titlecase mapping and is not itself a titlecase char
     * according to UnicodeData, then the uppercase mapping is
     * returned as an equivalent titlecase mapping. If the
     * <code>char</code> argument is already a titlecase
     * <code>char</code>, the same <code>char</code> value will be
     * returned.
     * <p>
     * Note that
     * <code>Character.isTitleCase(Character.toTitleCase(ch))</code>
     * does not always return <code>true</code> for some ranges of
     * characters.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #toTitleCase(int)} method.
     *
     * @param   ch   the character to be converted.
     * @return  the titlecase equivalent of the character, if any;
     *          otherwise, the character itself.
     * @see     java.lang.Character#isTitleCase(char)
     * @see     java.lang.Character#toLowerCase(char)
     * @see     java.lang.Character#toUpperCase(char)
     * @since   1.0.2
     */
    public static char toTitleCase(char ch) {
        return (char)toTitleCase((int)ch);
    }

    /**
     * Converts the character (Unicode code point) argument to titlecase using case mapping
     * information from the UnicodeData file. If a character has no
     * explicit titlecase mapping and is not itself a titlecase char
     * according to UnicodeData, then the uppercase mapping is
     * returned as an equivalent titlecase mapping. If the
     * character argument is already a titlecase
     * character, the same character value will be
     * returned.
     * 
     * <p>Note that
     * <code>Character.isTitleCase(Character.toTitleCase(codePoint))</code>
     * does not always return <code>true</code> for some ranges of
     * characters.
     *
     * @param   codePoint   the character (Unicode code point) to be converted.
     * @return  the titlecase equivalent of the character, if any;
     *          otherwise, the character itself.
     * @see     java.lang.Character#isTitleCase(int)
     * @see     java.lang.Character#toLowerCase(int)
     * @see     java.lang.Character#toUpperCase(int)
     * @since   1.5
     */
    public static int toTitleCase(int codePoint) {
        int titleCase = codePoint;
        int plane = 0;
        
        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            titleCase = CharacterDataLatin1.toTitleCase(codePoint);
        } else {
            plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                titleCase = CharacterData00.toTitleCase(codePoint);
                break;
            case(1):
                titleCase = CharacterData01.toTitleCase(codePoint);
                break;
            case(2):
                titleCase = CharacterData02.toTitleCase(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                titleCase = CharacterDataUndefined.toTitleCase(codePoint);
                break;
            case(14): 
                titleCase = CharacterData0E.toTitleCase(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                titleCase = CharacterDataPrivateUse.toTitleCase(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // titleCase remains codePoint;
                break;
            }
        }
        return titleCase;
    }

    /**
     * Returns the numeric value of the character <code>ch</code> in the
     * specified radix.
     * <p>
     * If the radix is not in the range <code>MIN_RADIX</code>&nbsp;&lt;=
     * <code>radix</code>&nbsp;&lt;= <code>MAX_RADIX</code> or if the
     * value of <code>ch</code> is not a valid digit in the specified
     * radix, <code>-1</code> is returned. A character is a valid digit
     * if at least one of the following is true:
     * <ul>
     * <li>The method <code>isDigit</code> is <code>true</code> of the character
     *     and the Unicode decimal digit value of the character (or its
     *     single-character decomposition) is less than the specified radix.
     *     In this case the decimal digit value is returned.
     * <li>The character is one of the uppercase Latin letters
     *     <code>'A'</code> through <code>'Z'</code> and its code is less than
     *     <code>radix&nbsp;+ 'A'&nbsp;-&nbsp;10</code>.
     *     In this case, <code>ch&nbsp;- 'A'&nbsp;+&nbsp;10</code>
     *     is returned.
     * <li>The character is one of the lowercase Latin letters
     *     <code>'a'</code> through <code>'z'</code> and its code is less than
     *     <code>radix&nbsp;+ 'a'&nbsp;-&nbsp;10</code>.
     *     In this case, <code>ch&nbsp;- 'a'&nbsp;+&nbsp;10</code>
     *     is returned.
     * </ul>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #digit(int, int)} method.
     *
     * @param   ch      the character to be converted.
     * @param   radix   the radix.
     * @return  the numeric value represented by the character in the
     *          specified radix.
     * @see     java.lang.Character#forDigit(int, int)
     * @see     java.lang.Character#isDigit(char)
     */
    public static int digit(char ch, int radix) {
        return digit((int)ch, radix);
    }

    /**
     * Returns the numeric value of the specified character (Unicode
     * code point) in the specified radix.
     * 
     * <p>If the radix is not in the range <code>MIN_RADIX</code>&nbsp;&lt;=
     * <code>radix</code>&nbsp;&lt;= <code>MAX_RADIX</code> or if the
     * character is not a valid digit in the specified
     * radix, <code>-1</code> is returned. A character is a valid digit
     * if at least one of the following is true:
     * <ul>
     * <li>The method {@link #isDigit(int) isDigit(codePoint)} is <code>true</code> of the character
     *     and the Unicode decimal digit value of the character (or its
     *     single-character decomposition) is less than the specified radix.
     *     In this case the decimal digit value is returned.
     * <li>The character is one of the uppercase Latin letters
     *     <code>'A'</code> through <code>'Z'</code> and its code is less than
     *     <code>radix&nbsp;+ 'A'&nbsp;-&nbsp;10</code>.
     *     In this case, <code>ch&nbsp;- 'A'&nbsp;+&nbsp;10</code>
     *     is returned.
     * <li>The character is one of the lowercase Latin letters
     *     <code>'a'</code> through <code>'z'</code> and its code is less than
     *     <code>radix&nbsp;+ 'a'&nbsp;-&nbsp;10</code>.
     *     In this case, <code>ch&nbsp;- 'a'&nbsp;+&nbsp;10</code>
     *     is returned.
     * </ul>
     *
     * @param   codePoint the character (Unicode code point) to be converted.
     * @param   radix   the radix.
     * @return  the numeric value represented by the character in the
     *          specified radix.
     * @see     java.lang.Character#forDigit(int, int)
     * @see     java.lang.Character#isDigit(int)
     * @since   1.5
     */
    public static int digit(int codePoint, int radix) {
        int digit = -1;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            digit = CharacterDataLatin1.digit(codePoint, radix);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                digit = CharacterData00.digit(codePoint, radix);
                break;
            case(1):
                digit = CharacterData01.digit(codePoint, radix);
                break;
            case(2):
                digit = CharacterData02.digit(codePoint, radix);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                digit = CharacterDataUndefined.digit(codePoint, radix);
                break;
            case(14): 
                digit = CharacterData0E.digit(codePoint, radix);
                break;
            case(15): // Private Use
            case(16): // Private Use
                digit = CharacterDataPrivateUse.digit(codePoint, radix);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // digit remains -1;
                break;
            }
        }
        return digit;
    }

    /**
     * Returns the <code>int</code> value that the specified Unicode
     * character represents. For example, the character
     * <code>'&#92;u216C'</code> (the roman numeral fifty) will return
     * an int with a value of 50.
     * <p>
     * The letters A-Z in their uppercase (<code>'&#92;u0041'</code> through
     * <code>'&#92;u005A'</code>), lowercase
     * (<code>'&#92;u0061'</code> through <code>'&#92;u007A'</code>), and
     * full width variant (<code>'&#92;uFF21'</code> through
     * <code>'&#92;uFF3A'</code> and <code>'&#92;uFF41'</code> through
     * <code>'&#92;uFF5A'</code>) forms have numeric values from 10
     * through 35. This is independent of the Unicode specification,
     * which does not assign numeric values to these <code>char</code>
     * values.
     * <p>
     * If the character does not have a numeric value, then -1 is returned.
     * If the character has a numeric value that cannot be represented as a
     * nonnegative integer (for example, a fractional value), then -2
     * is returned.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #getNumericValue(int)} method.
     *
     * @param   ch      the character to be converted.
     * @return  the numeric value of the character, as a nonnegative <code>int</code>
     *           value; -2 if the character has a numeric value that is not a
     *          nonnegative integer; -1 if the character has no numeric value.
     * @see     java.lang.Character#forDigit(int, int)
     * @see     java.lang.Character#isDigit(char)
     * @since   1.1
     */
    public static int getNumericValue(char ch) {
        return getNumericValue((int)ch);
    }

    /**
     * Returns the <code>int</code> value that the specified 
     * character (Unicode code point) represents. For example, the character
     * <code>'&#92;u216C'</code> (the Roman numeral fifty) will return
     * an <code>int</code> with a value of 50.
     * <p>
     * The letters A-Z in their uppercase (<code>'&#92;u0041'</code> through
     * <code>'&#92;u005A'</code>), lowercase
     * (<code>'&#92;u0061'</code> through <code>'&#92;u007A'</code>), and
     * full width variant (<code>'&#92;uFF21'</code> through
     * <code>'&#92;uFF3A'</code> and <code>'&#92;uFF41'</code> through
     * <code>'&#92;uFF5A'</code>) forms have numeric values from 10
     * through 35. This is independent of the Unicode specification,
     * which does not assign numeric values to these <code>char</code>
     * values.
     * <p>
     * If the character does not have a numeric value, then -1 is returned.
     * If the character has a numeric value that cannot be represented as a
     * nonnegative integer (for example, a fractional value), then -2
     * is returned.
     *
     * @param   codePoint the character (Unicode code point) to be converted.
     * @return  the numeric value of the character, as a nonnegative <code>int</code>
     *          value; -2 if the character has a numeric value that is not a
     *          nonnegative integer; -1 if the character has no numeric value.
     * @see     java.lang.Character#forDigit(int, int)
     * @see     java.lang.Character#isDigit(int)
     * @since   1.5
     */
    public static int getNumericValue(int codePoint) {
        int numericValue = -1;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            numericValue = CharacterDataLatin1.getNumericValue(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                numericValue = CharacterData00.getNumericValue(codePoint);
                break;
            case(1):
                numericValue = CharacterData01.getNumericValue(codePoint);
                break;
            case(2):
                numericValue = CharacterData02.getNumericValue(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                numericValue = CharacterDataUndefined.getNumericValue(codePoint);
                break;
            case(14): 
                numericValue = CharacterData0E.getNumericValue(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                numericValue = CharacterDataPrivateUse.getNumericValue(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // numericValue remains -1
                break;
            }
        }
        return numericValue;
    }

    /**
     * Determines if the specified character is ISO-LATIN-1 white space.
     * This method returns <code>true</code> for the following five
     * characters only:
     * <table>
     * <tr><td><code>'\t'</code></td>            <td><code>'&#92;u0009'</code></td>
     *     <td><code>HORIZONTAL TABULATION</code></td></tr>
     * <tr><td><code>'\n'</code></td>            <td><code>'&#92;u000A'</code></td>
     *     <td><code>NEW LINE</code></td></tr>
     * <tr><td><code>'\f'</code></td>            <td><code>'&#92;u000C'</code></td>
     *     <td><code>FORM FEED</code></td></tr>
     * <tr><td><code>'\r'</code></td>            <td><code>'&#92;u000D'</code></td>
     *     <td><code>CARRIAGE RETURN</code></td></tr>
     * <tr><td><code>'&nbsp;'</code></td>  <td><code>'&#92;u0020'</code></td>
     *     <td><code>SPACE</code></td></tr>
     * </table>
     *
     * @param      ch   the character to be tested.
     * @return     <code>true</code> if the character is ISO-LATIN-1 white
     *             space; <code>false</code> otherwise.
     * @see        java.lang.Character#isSpaceChar(char)
     * @see        java.lang.Character#isWhitespace(char)
     * @deprecated Replaced by isWhitespace(char).
     */
    public static boolean isSpace(char ch) {
        return (ch <= 0x0020) &&
            (((((1L << 0x0009) |
            (1L << 0x000A) |
            (1L << 0x000C) |
            (1L << 0x000D) |
            (1L << 0x0020)) >> ch) & 1L) != 0);
    }


    /**
     * Determines if the specified character is a Unicode space character.
     * A character is considered to be a space character if and only if
     * it is specified to be a space character by the Unicode standard. This
     * method returns true if the character's general category type is any of
     * the following:
     * <ul>
     * <li> <code>SPACE_SEPARATOR</code>
     * <li> <code>LINE_SEPARATOR</code>
     * <li> <code>PARAGRAPH_SEPARATOR</code>
     * </ul>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isSpaceChar(int)} method.
     *
     * @param   ch      the character to be tested.
     * @return  <code>true</code> if the character is a space character; 
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isWhitespace(char)
     * @since   1.1
     */
    public static boolean isSpaceChar(char ch) {
        return isSpaceChar((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) is a
     * Unicode space character.  A character is considered to be a
     * space character if and only if it is specified to be a space
     * character by the Unicode standard. This method returns true if
     * the character's general category type is any of the following:
     *
     * <ul>
     * <li> {@link #SPACE_SEPARATOR}
     * <li> {@link #LINE_SEPARATOR}
     * <li> {@link #PARAGRAPH_SEPARATOR}
     * </ul>
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is a space character; 
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isWhitespace(int)
     * @since   1.5
     */
    public static boolean isSpaceChar(int codePoint) {
        boolean bSpaceChar = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <=  FAST_PATH_MAX) {
            bSpaceChar =  CharacterDataLatin1.isSpaceChar(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bSpaceChar = CharacterData00.isSpaceChar(codePoint);
                break;
            case(1):
                bSpaceChar = CharacterData01.isSpaceChar(codePoint);
                break;
            case(2):
                bSpaceChar = CharacterData02.isSpaceChar(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bSpaceChar = CharacterDataUndefined.isSpaceChar(codePoint);
                break;
            case(14): 
                bSpaceChar = CharacterData0E.isSpaceChar(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bSpaceChar = CharacterDataPrivateUse.isSpaceChar(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bSpaceChar remains false
                break;
            }
        }
        return bSpaceChar;
    }

    /**
     * Determines if the specified character is white space according to Java.
     * A character is a Java whitespace character if and only if it satisfies
     * one of the following criteria:
     * <ul>
     * <li> It is a Unicode space character (<code>SPACE_SEPARATOR</code>,
     *      <code>LINE_SEPARATOR</code>, or <code>PARAGRAPH_SEPARATOR</code>) 
     *      but is not also a non-breaking space (<code>'&#92;u00A0'</code>,
     *      <code>'&#92;u2007'</code>, <code>'&#92;u202F'</code>).
     * <li> It is <code>'&#92;u0009'</code>, HORIZONTAL TABULATION.
     * <li> It is <code>'&#92;u000A'</code>, LINE FEED.
     * <li> It is <code>'&#92;u000B'</code>, VERTICAL TABULATION.
     * <li> It is <code>'&#92;u000C'</code>, FORM FEED.
     * <li> It is <code>'&#92;u000D'</code>, CARRIAGE RETURN.
     * <li> It is <code>'&#92;u001C'</code>, FILE SEPARATOR.
     * <li> It is <code>'&#92;u001D'</code>, GROUP SEPARATOR.
     * <li> It is <code>'&#92;u001E'</code>, RECORD SEPARATOR.
     * <li> It is <code>'&#92;u001F'</code>, UNIT SEPARATOR.
     * </ul>
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isWhitespace(int)} method.
     *
     * @param   ch the character to be tested.
     * @return  <code>true</code> if the character is a Java whitespace
     *          character; <code>false</code> otherwise.
     * @see     java.lang.Character#isSpaceChar(char)
     * @since   1.1
     */
    public static boolean isWhitespace(char ch) {
        return isWhitespace((int)ch);
    }

    /**
     * Determines if the specified character (Unicode code point) is
     * white space according to Java.  A character is a Java
     * whitespace character if and only if it satisfies one of the
     * following criteria:
     * <ul>
     * <li> It is a Unicode space character ({@link #SPACE_SEPARATOR},
     *      {@link #LINE_SEPARATOR}, or {@link #PARAGRAPH_SEPARATOR}) 
     *      but is not also a non-breaking space (<code>'&#92;u00A0'</code>,
     *      <code>'&#92;u2007'</code>, <code>'&#92;u202F'</code>).
     * <li> It is <code>'&#92;u0009'</code>, HORIZONTAL TABULATION.
     * <li> It is <code>'&#92;u000A'</code>, LINE FEED.
     * <li> It is <code>'&#92;u000B'</code>, VERTICAL TABULATION.
     * <li> It is <code>'&#92;u000C'</code>, FORM FEED.
     * <li> It is <code>'&#92;u000D'</code>, CARRIAGE RETURN.
     * <li> It is <code>'&#92;u001C'</code>, FILE SEPARATOR.
     * <li> It is <code>'&#92;u001D'</code>, GROUP SEPARATOR.
     * <li> It is <code>'&#92;u001E'</code>, RECORD SEPARATOR.
     * <li> It is <code>'&#92;u001F'</code>, UNIT SEPARATOR.
     * </ul>
     * <p>
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is a Java whitespace
     *          character; <code>false</code> otherwise.
     * @see     java.lang.Character#isSpaceChar(int)
     * @since   1.5
     */
    public static boolean isWhitespace(int codePoint) {
        boolean bWhiteSpace = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            bWhiteSpace =  CharacterDataLatin1.isWhitespace(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bWhiteSpace = CharacterData00.isWhitespace(codePoint);
                break;
            case(1):
                bWhiteSpace = CharacterData01.isWhitespace(codePoint);
                break;
            case(2):
                bWhiteSpace = CharacterData02.isWhitespace(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bWhiteSpace = CharacterDataUndefined.isWhitespace(codePoint);
                break;
            case(14): 
                bWhiteSpace = CharacterData0E.isWhitespace(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bWhiteSpace = CharacterDataPrivateUse.isWhitespace(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bWhiteSpace remains false
                break;
            }
        }
        return bWhiteSpace;
    }

    /**
     * Determines if the specified character is an ISO control
     * character.  A character is considered to be an ISO control
     * character if its code is in the range <code>'&#92;u0000'</code>
     * through <code>'&#92;u001F'</code> or in the range
     * <code>'&#92;u007F'</code> through <code>'&#92;u009F'</code>.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isISOControl(int)} method.
     *
     * @param   ch      the character to be tested.
     * @return  <code>true</code> if the character is an ISO control character;
     *          <code>false</code> otherwise.
     *
     * @see     java.lang.Character#isSpaceChar(char)
     * @see     java.lang.Character#isWhitespace(char)
     * @since   1.1
     */
    public static boolean isISOControl(char ch) {
        return isISOControl((int)ch);
    }

    /**
     * Determines if the referenced character (Unicode code point) is an ISO control
     * character.  A character is considered to be an ISO control
     * character if its code is in the range <code>'&#92;u0000'</code>
     * through <code>'&#92;u001F'</code> or in the range
     * <code>'&#92;u007F'</code> through <code>'&#92;u009F'</code>.
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is an ISO control character;
     *          <code>false</code> otherwise.
     * @see     java.lang.Character#isSpaceChar(int)
     * @see     java.lang.Character#isWhitespace(int)
     * @since   1.5
     */
    public static boolean isISOControl(int codePoint) {
        return (codePoint >= 0x0000 && codePoint <= 0x001F) || 
            (codePoint >= 0x007F && codePoint <= 0x009F);
    }

    /**
     * Returns a value indicating a character's general category.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #getType(int)} method.
     *
     * @param   ch      the character to be tested.
     * @return  a value of type <code>int</code> representing the 
     *          character's general category.
     * @see     java.lang.Character#COMBINING_SPACING_MARK
     * @see     java.lang.Character#CONNECTOR_PUNCTUATION
     * @see     java.lang.Character#CONTROL
     * @see     java.lang.Character#CURRENCY_SYMBOL
     * @see     java.lang.Character#DASH_PUNCTUATION
     * @see     java.lang.Character#DECIMAL_DIGIT_NUMBER
     * @see     java.lang.Character#ENCLOSING_MARK
     * @see     java.lang.Character#END_PUNCTUATION
     * @see     java.lang.Character#FINAL_QUOTE_PUNCTUATION
     * @see     java.lang.Character#FORMAT
     * @see     java.lang.Character#INITIAL_QUOTE_PUNCTUATION
     * @see     java.lang.Character#LETTER_NUMBER
     * @see     java.lang.Character#LINE_SEPARATOR
     * @see     java.lang.Character#LOWERCASE_LETTER
     * @see     java.lang.Character#MATH_SYMBOL
     * @see     java.lang.Character#MODIFIER_LETTER
     * @see     java.lang.Character#MODIFIER_SYMBOL
     * @see     java.lang.Character#NON_SPACING_MARK
     * @see     java.lang.Character#OTHER_LETTER
     * @see     java.lang.Character#OTHER_NUMBER
     * @see     java.lang.Character#OTHER_PUNCTUATION
     * @see     java.lang.Character#OTHER_SYMBOL
     * @see     java.lang.Character#PARAGRAPH_SEPARATOR
     * @see     java.lang.Character#PRIVATE_USE
     * @see     java.lang.Character#SPACE_SEPARATOR
     * @see     java.lang.Character#START_PUNCTUATION
     * @see     java.lang.Character#SURROGATE
     * @see     java.lang.Character#TITLECASE_LETTER
     * @see     java.lang.Character#UNASSIGNED
     * @see     java.lang.Character#UPPERCASE_LETTER
     * @since   1.1
     */
    public static int getType(char ch) {
        return getType((int)ch);
    }

    /**
     * Returns a value indicating a character's general category.
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  a value of type <code>int</code> representing the 
     *          character's general category.
     * @see     Character#COMBINING_SPACING_MARK COMBINING_SPACING_MARK
     * @see     Character#CONNECTOR_PUNCTUATION CONNECTOR_PUNCTUATION
     * @see     Character#CONTROL CONTROL
     * @see     Character#CURRENCY_SYMBOL CURRENCY_SYMBOL
     * @see     Character#DASH_PUNCTUATION DASH_PUNCTUATION
     * @see     Character#DECIMAL_DIGIT_NUMBER DECIMAL_DIGIT_NUMBER
     * @see     Character#ENCLOSING_MARK ENCLOSING_MARK
     * @see     Character#END_PUNCTUATION END_PUNCTUATION
     * @see     Character#FINAL_QUOTE_PUNCTUATION FINAL_QUOTE_PUNCTUATION
     * @see     Character#FORMAT FORMAT
     * @see     Character#INITIAL_QUOTE_PUNCTUATION INITIAL_QUOTE_PUNCTUATION
     * @see     Character#LETTER_NUMBER LETTER_NUMBER
     * @see     Character#LINE_SEPARATOR LINE_SEPARATOR
     * @see     Character#LOWERCASE_LETTER LOWERCASE_LETTER
     * @see     Character#MATH_SYMBOL MATH_SYMBOL
     * @see     Character#MODIFIER_LETTER MODIFIER_LETTER
     * @see     Character#MODIFIER_SYMBOL MODIFIER_SYMBOL
     * @see     Character#NON_SPACING_MARK NON_SPACING_MARK
     * @see     Character#OTHER_LETTER OTHER_LETTER
     * @see     Character#OTHER_NUMBER OTHER_NUMBER
     * @see     Character#OTHER_PUNCTUATION OTHER_PUNCTUATION
     * @see     Character#OTHER_SYMBOL OTHER_SYMBOL
     * @see     Character#PARAGRAPH_SEPARATOR PARAGRAPH_SEPARATOR
     * @see     Character#PRIVATE_USE PRIVATE_USE
     * @see     Character#SPACE_SEPARATOR SPACE_SEPARATOR
     * @see     Character#START_PUNCTUATION START_PUNCTUATION
     * @see     Character#SURROGATE SURROGATE
     * @see     Character#TITLECASE_LETTER TITLECASE_LETTER
     * @see     Character#UNASSIGNED UNASSIGNED
     * @see     Character#UPPERCASE_LETTER UPPERCASE_LETTER
     * @since   1.5
     */
    public static int getType(int codePoint) {
        int type = Character.UNASSIGNED;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            type = CharacterDataLatin1.getType(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                type = CharacterData00.getType(codePoint);
                break;
            case(1):
                type = CharacterData01.getType(codePoint);
                break;
            case(2):
                type = CharacterData02.getType(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined      
                type = CharacterDataUndefined.getType(codePoint);
                break;
            case(14): 
                type = CharacterData0E.getType(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                type = CharacterDataPrivateUse.getType(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // type remains UNASSIGNED
                break;
            }
        }
        return type;
    }

    /**
     * Determines the character representation for a specific digit in
     * the specified radix. If the value of <code>radix</code> is not a
     * valid radix, or the value of <code>digit</code> is not a valid
     * digit in the specified radix, the null character
     * (<code>'&#92;u0000'</code>) is returned.
     * <p>
     * The <code>radix</code> argument is valid if it is greater than or
     * equal to <code>MIN_RADIX</code> and less than or equal to
     * <code>MAX_RADIX</code>. The <code>digit</code> argument is valid if
     * <code>0&nbsp;&lt;=digit&nbsp;&lt;&nbsp;radix</code>.
     * <p>
     * If the digit is less than 10, then
     * <code>'0'&nbsp;+ digit</code> is returned. Otherwise, the value
     * <code>'a'&nbsp;+ digit&nbsp;-&nbsp;10</code> is returned.
     *
     * @param   digit   the number to convert to a character.
     * @param   radix   the radix.
     * @return  the <code>char</code> representation of the specified digit
     *          in the specified radix.
     * @see     java.lang.Character#MIN_RADIX
     * @see     java.lang.Character#MAX_RADIX
     * @see     java.lang.Character#digit(char, int)
     */
    public static char forDigit(int digit, int radix) {
        if ((digit >= radix) || (digit < 0)) {
            return '\0';
        }
        if ((radix < Character.MIN_RADIX) || (radix > Character.MAX_RADIX)) {
            return '\0';
        }
        if (digit < 10) {
            return (char)('0' + digit);
        }
        return (char)('a' - 10 + digit);
    }

    /**
     * Returns the Unicode directionality property for the given
     * character.  Character directionality is used to calculate the
     * visual ordering of text. The directionality value of undefined
     * <code>char</code> values is <code>DIRECTIONALITY_UNDEFINED</code>.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #getDirectionality(int)} method.
     *
     * @param  ch <code>char</code> for which the directionality property 
     *            is requested.
     * @return the directionality property of the <code>char</code> value.
     *
     * @see Character#DIRECTIONALITY_UNDEFINED
     * @see Character#DIRECTIONALITY_LEFT_TO_RIGHT
     * @see Character#DIRECTIONALITY_RIGHT_TO_LEFT
     * @see Character#DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
     * @see Character#DIRECTIONALITY_EUROPEAN_NUMBER
     * @see Character#DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR
     * @see Character#DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR
     * @see Character#DIRECTIONALITY_ARABIC_NUMBER
     * @see Character#DIRECTIONALITY_COMMON_NUMBER_SEPARATOR
     * @see Character#DIRECTIONALITY_NONSPACING_MARK
     * @see Character#DIRECTIONALITY_BOUNDARY_NEUTRAL
     * @see Character#DIRECTIONALITY_PARAGRAPH_SEPARATOR
     * @see Character#DIRECTIONALITY_SEGMENT_SEPARATOR
     * @see Character#DIRECTIONALITY_WHITESPACE
     * @see Character#DIRECTIONALITY_OTHER_NEUTRALS
     * @see Character#DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING
     * @see Character#DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE
     * @see Character#DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING
     * @see Character#DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE
     * @see Character#DIRECTIONALITY_POP_DIRECTIONAL_FORMAT
     * @since 1.4
     */
    public static byte getDirectionality(char ch) {
        return getDirectionality((int)ch);
    }

    /**
     * Returns the Unicode directionality property for the given
     * character (Unicode code point).  Character directionality is
     * used to calculate the visual ordering of text. The
     * directionality value of undefined character is {@link
     * #DIRECTIONALITY_UNDEFINED}.
     *
     * @param   codePoint the character (Unicode code point) for which
     *          the directionality property * is requested.
     * @return the directionality property of the character.
     *
     * @see Character#DIRECTIONALITY_UNDEFINED DIRECTIONALITY_UNDEFINED
     * @see Character#DIRECTIONALITY_LEFT_TO_RIGHT DIRECTIONALITY_LEFT_TO_RIGHT
     * @see Character#DIRECTIONALITY_RIGHT_TO_LEFT DIRECTIONALITY_RIGHT_TO_LEFT
     * @see Character#DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
     * @see Character#DIRECTIONALITY_EUROPEAN_NUMBER DIRECTIONALITY_EUROPEAN_NUMBER
     * @see Character#DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR
     * @see Character#DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR
     * @see Character#DIRECTIONALITY_ARABIC_NUMBER DIRECTIONALITY_ARABIC_NUMBER
     * @see Character#DIRECTIONALITY_COMMON_NUMBER_SEPARATOR DIRECTIONALITY_COMMON_NUMBER_SEPARATOR
     * @see Character#DIRECTIONALITY_NONSPACING_MARK DIRECTIONALITY_NONSPACING_MARK
     * @see Character#DIRECTIONALITY_BOUNDARY_NEUTRAL DIRECTIONALITY_BOUNDARY_NEUTRAL
     * @see Character#DIRECTIONALITY_PARAGRAPH_SEPARATOR DIRECTIONALITY_PARAGRAPH_SEPARATOR
     * @see Character#DIRECTIONALITY_SEGMENT_SEPARATOR DIRECTIONALITY_SEGMENT_SEPARATOR
     * @see Character#DIRECTIONALITY_WHITESPACE DIRECTIONALITY_WHITESPACE
     * @see Character#DIRECTIONALITY_OTHER_NEUTRALS DIRECTIONALITY_OTHER_NEUTRALS
     * @see Character#DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING
     * @see Character#DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE
     * @see Character#DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING
     * @see Character#DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE
     * @see Character#DIRECTIONALITY_POP_DIRECTIONAL_FORMAT DIRECTIONALITY_POP_DIRECTIONAL_FORMAT
     * @since    1.5
     */
    public static byte getDirectionality(int codePoint) {
        byte directionality = Character.DIRECTIONALITY_UNDEFINED;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
            directionality = CharacterDataLatin1.getDirectionality(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                directionality = CharacterData00.getDirectionality(codePoint);
                break;
            case(1):
                directionality = CharacterData01.getDirectionality(codePoint);
                break;
            case(2):
                directionality = CharacterData02.getDirectionality(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                directionality = CharacterDataUndefined.getDirectionality(codePoint);
                break;
            case(14): 
                directionality = CharacterData0E.getDirectionality(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                directionality = CharacterDataPrivateUse.getDirectionality(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // directionality remains DIRECTIONALITY_UNDEFINED
                break;
            }
        }
        return directionality;
    }

    /**
     * Determines whether the character is mirrored according to the
     * Unicode specification.  Mirrored characters should have their
     * glyphs horizontally mirrored when displayed in text that is
     * right-to-left.  For example, <code>'&#92;u0028'</code> LEFT
     * PARENTHESIS is semantically defined to be an <i>opening
     * parenthesis</i>.  This will appear as a "(" in text that is
     * left-to-right but as a ")" in text that is right-to-left.
     *
     * <p><b>Note:</b> This method cannot handle <a
     * href="#supplementary"> supplementary characters</a>. To support
     * all Unicode characters, including supplementary characters, use
     * the {@link #isMirrored(int)} method.
     *
     * @param  ch <code>char</code> for which the mirrored property is requested
     * @return <code>true</code> if the char is mirrored, <code>false</code>
     *         if the <code>char</code> is not mirrored or is not defined.
     * @since 1.4
     */
    public static boolean isMirrored(char ch) {
        return isMirrored((int)ch);
    }

    /**
     * Determines whether the specified character (Unicode code point)
     * is mirrored according to the Unicode specification.  Mirrored
     * characters should have their glyphs horizontally mirrored when
     * displayed in text that is right-to-left.  For example,
     * <code>'&#92;u0028'</code> LEFT PARENTHESIS is semantically
     * defined to be an <i>opening parenthesis</i>.  This will appear
     * as a "(" in text that is left-to-right but as a ")" in text
     * that is right-to-left.
     *
     * @param   codePoint the character (Unicode code point) to be tested.
     * @return  <code>true</code> if the character is mirrored, <code>false</code>
     *          if the character is not mirrored or is not defined.
     * @since   1.5
     */
    public static boolean isMirrored(int codePoint) {
        boolean bMirrored = false;

        if (codePoint >= MIN_CODE_POINT && codePoint <= FAST_PATH_MAX) {
           bMirrored = CharacterDataLatin1.isMirrored(codePoint);
        } else {
            int plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                bMirrored = CharacterData00.isMirrored(codePoint);
                break;
            case(1):
                bMirrored = CharacterData01.isMirrored(codePoint);
                break;
            case(2):
                bMirrored = CharacterData02.isMirrored(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                bMirrored = CharacterDataUndefined.isMirrored(codePoint);
                break;
            case(14): 
                bMirrored = CharacterData0E.isMirrored(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                bMirrored = CharacterDataPrivateUse.isMirrored(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // bMirrored remains false
                break;
            }
        }
        return bMirrored;
    }

    /**
     * Compares two <code>Character</code> objects numerically.
     *
     * @param   anotherCharacter   the <code>Character</code> to be compared.

     * @return  the value <code>0</code> if the argument <code>Character</code> 
     *          is equal to this <code>Character</code>; a value less than 
     *          <code>0</code> if this <code>Character</code> is numerically less 
     *          than the <code>Character</code> argument; and a value greater than 
     *          <code>0</code> if this <code>Character</code> is numerically greater 
     *          than the <code>Character</code> argument (unsigned comparison).  
     *          Note that this is strictly a numerical comparison; it is not 
     *          locale-dependent.
     * @since   1.2
     */
    public int compareTo(Character anotherCharacter) {
        return this.value - anotherCharacter.value;
    }

    /**
     * Converts the character (Unicode code point) argument to uppercase using
     * information from the UnicodeData file.
     * <p>
     *
     * @param   codePoint   the character (Unicode code point) to be converted.
     * @return  either the uppercase equivalent of the character, if 
     *          any, or an error flag (<code>Character.ERROR</code>) 
     *          that indicates that a 1:M <code>char</code> mapping exists.
     * @see     java.lang.Character#isLowerCase(char)
     * @see     java.lang.Character#isUpperCase(char)
     * @see     java.lang.Character#toLowerCase(char)
     * @see     java.lang.Character#toTitleCase(char)
     * @since 1.4
     */
    static int toUpperCaseEx(int codePoint) throws Exception {
        int upperCase = codePoint;
        int plane = 0;

        if(!isValidCodePoint(codePoint))
            throw new Exception();

        if (codePoint <= FAST_PATH_MAX) {
            upperCase = CharacterDataLatin1.toUpperCaseEx(codePoint);
        } else {
            plane = getPlane(codePoint);
            switch(plane) {
            case(0):
                upperCase = CharacterData00.toUpperCaseEx(codePoint);
                break;
            case(1):
                upperCase = CharacterData01.toUpperCase(codePoint);
                break;
            case(2):
                upperCase = CharacterData02.toUpperCase(codePoint);
                break;
            case(3): // Undefined
            case(4): // Undefined
            case(5): // Undefined
            case(6): // Undefined
            case(7): // Undefined
            case(8): // Undefined
            case(9): // Undefined
            case(10): // Undefined
            case(11): // Undefined
            case(12): // Undefined
            case(13): // Undefined
                upperCase = CharacterDataUndefined.toUpperCase(codePoint);
                break;
            case(14):
                upperCase = CharacterData0E.toUpperCase(codePoint);
                break;
            case(15): // Private Use
            case(16): // Private Use
                upperCase = CharacterDataPrivateUse.toUpperCase(codePoint);
                break;
            default:
                // the argument's plane is invalid, and thus is an invalid codepoint
                // upperCase remains codePoint;
                break;
            }
        }
        return upperCase;
    }

    /**
     * Converts the character (Unicode code point) argument to uppercase using case
     * mapping information from the SpecialCasing file in the Unicode
     * specification. If a character has no explicit uppercase
     * mapping, then the <code>char</code> itself is returned in the
     * <code>char[]</code>.
     *
     * @param   codePoint   the character (Unicode code point) to be converted.
     * @return a <code>char[]</code> with the uppercased character.
     * @since 1.4
     */
    static char[] toUpperCaseCharArray(int codePoint) throws Exception {
        char[] upperCase = null;

        // As of Unicode 4.0, 1:M uppercasings only happen in the BMP.
        if(!isValidCodePoint(codePoint) || isSupplementaryCodePoint(codePoint))
            throw new Exception();

        if (codePoint <= FAST_PATH_MAX) {
            upperCase = CharacterDataLatin1.toUpperCaseCharArray(codePoint);
        } else {
            upperCase = CharacterData00.toUpperCaseCharArray(codePoint);
        }
        return upperCase;
    }

    /**
     * The number of bits used to represent a <tt>char</tt> value in unsigned
     * binary form.
     *
     * @since 1.5
     */
    public static final int SIZE = 16;

    /**
     * Returns the value obtained by reversing the order of the bytes in the
     * specified <tt>char</tt> value.
     *
     * @return the value obtained by reversing (or, equivalently, swapping)
     *     the bytes in the specified <tt>char</tt> value.
     * @since 1.5
     */
    public static char reverseBytes(char ch) {
        return (char) (((ch & 0xFF00) >> 8) | (ch << 8));
    }
}
