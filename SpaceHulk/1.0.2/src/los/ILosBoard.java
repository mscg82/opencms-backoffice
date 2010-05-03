// Orignally taken from http://rlforj.sourceforge.net
// Modified by Adam Gates

// AG package rlforj.los;
package los;

public interface ILosBoard
{

	/**
	 * Is the location (x, y) inside the board ?
	 * @param x
	 * @param y
	 * @return
	 */
	// AG public boolean contains(int x, int y);
	
	/**
	 * Is the location (x, y) an obstacle
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isObstacle(int x, int y);
	
	/**
	 * Location (x,y) is visible
	 * Visit the location (x,y)
	 * 
	 * This can involve saving the points in a collection,
	 * setting flags on a 2D map etc.
	 * 
	 * @param x
	 * @param y
	 */
	// AG public void visit(int x, int y);
	
}
