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
	
	protected Random r_[];
	protected int actualDice;
	protected int maxValue;
	protected Vector generatedSequence;
	
	protected Dice() {
		r_ = new Random[5];
		actualDice = 1;
		r_[0] = new Random();
		for(int i = 1, l = r_.length; i < l; i++) {
			r_[i] = new Random(r_[0].nextLong());
		}
		if(enableDiceTrace)
			generatedSequence = new Vector();
	}
	
	public int getDiceRoll() {
		return getDiceRoll2();
	}
	
	protected int getDiceRoll2() {
		int ret = 1;
		ret = (r_[actualDice].nextInt(60) / 10) + 1;
		actualDice = (actualDice + 1) % r_.length;
		if(enableDiceTrace)
			generatedSequence.addElement(new Integer(ret));
		return ret;
	}
	
	protected int getDiceRoll1() {
		int ret = 1;
		int it = r_[0].nextInt(10) + 1;
		for(int i = 0; i < it; i++) {
			ret = r_[0].nextInt(maxValue) + 1;
		}
		if(it <= 6 && it % 2 == 0) {
			// re-seed number sequence
			r_[0] = new Random();
		}
		if(enableDiceTrace)
			generatedSequence.addElement(new Integer(ret));
		return ret;
	}
	
	public Vector getGeneratedSequence() {
		return generatedSequence;
	}
}
