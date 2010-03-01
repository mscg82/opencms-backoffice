/**
 *
 */
package eni.virtualoffice.beans;

/**
 * This is a bean to hold informations
 * on theme element.
 *
 * @author Giuseppe Miscione
 *
 */
public class ThemeElementBean implements Comparable<ThemeElementBean> {

	/**
	 * The element type. Can be
	 * css or javascript.
	 */
	private String elementType;

	/**
	 * The source file of the element.
	 */
	private String sourceFile;

	/**
	 * Builds an instance of a {@link ThemeElementBean}
	 * of type css.
	 */
	public ThemeElementBean() {
		this("css");
	}

	/**
	 * Builds an instance of a {@link ThemeElementBean}
	 * of the specified type.
	 *
	 * @param elementType The type of the element.
	 */
	public ThemeElementBean(String elementType) {
		this.elementType = elementType;
	}

	/**
	 * Returns the element type.
	 *
	 * @return The element type.
	 */
	public String getElementType() {
		return elementType;
	}

	/**
	 * Returns the source file of the element.
	 *
	 * @return The source file of the element.
	 */
	public String getSourceFile() {
		return sourceFile;
	}

	/**
	 * Sets the source file of the element.
	 *
	 * @param sourceFile The source file to set.
	 */
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	@Override
	public int compareTo(ThemeElementBean el2) {
		return this.getSourceFile().compareTo(el2.getSourceFile());
	}

}
