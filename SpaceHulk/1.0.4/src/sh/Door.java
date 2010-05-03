/**
 * 
 */
package sh;

import java.util.Random;

/**
 * @author Giuseppe Miscione
 *
 */
public class Door extends Piece {

	public Door() {
		this(Face.NORTH);
	}
	
	public Door(int x, int y) {
		this(Face.NORTH, x, y);
	}
	
	public Door(Face face) {
		super(face);
	}
	
	public Door(Face face, int x, int y) {
		super(face);
		setPos(x, y);
	}

	int getCloseCombatValue(Random r, int dir) {
		return 0;
	}

	void resetActionPoints() {
		
	}

	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		if(this == obj)
			return true;
		
		if(obj instanceof Door) {
			Door door = (Door) obj;
			if(door.getPosX() == getPosX() && door.getPosY() == getPosY())
				return true;
		}
		
		return false;
	}

}
