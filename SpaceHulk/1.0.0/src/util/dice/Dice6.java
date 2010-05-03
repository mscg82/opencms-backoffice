/**
 * 
 */
package util.dice;

/**
 * @author Giuseppe Miscione
 *
 */
public class Dice6 extends Dice {

	private static Dice instance;
	
	public static Dice getDice() {
		if(instance == null)
			instance = new Dice6();
		return instance;
	}
	
	public Dice6() {
		super();
		maxValue = 6;
	}

}
