/**
 * 
 */
package util.dice;

import java.util.Random;
import java.util.Vector;

/**
 * @author Giuseppe Miscione
 *
 */
public abstract class Dice {

	protected static final boolean enableDiceTrace = false;
	
	protected Random r_;
	protected int maxValue;
	protected Vector generatedSequence;
	
	protected Dice() {
		r_ = new Random();
		if(enableDiceTrace)
			generatedSequence = new Vector();
	}
	
	public int getDiceRoll() {
		int ret = 1;
		int it = r_.nextInt(10) + 1;
		for(int i = 0; i < it; i++) {
			ret = r_.nextInt(maxValue) + 1;
		}
		if(it <= 6 && it % 2 == 0) {
			// re-seed number sequence
			r_ = new Random();
		}
		if(enableDiceTrace)
			generatedSequence.addElement(new Integer(ret));
		return ret;
	}
	
	public Vector getGeneratedSequence() {
		return generatedSequence;
	}
}
