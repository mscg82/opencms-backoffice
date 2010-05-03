/**
 * 
 */
package los.impl;

import los.IFovBoard;
import sh.Game;
import sh.Marine;

/**
 * @author Giuseppe Miscione
 *
 */
public class FlamerIFovBoard implements IFovBoard {
	
	private Game game_;
	private Marine piece_;
	private boolean costPayed_;
	
	public FlamerIFovBoard(Game game, Marine piece) {
		game_ = game;
		piece_ = piece;
		costPayed_ = false;
	}

	public boolean isObstacle(int x, int y)
    {
        return !game_.getMap().canMove(x, y, true, false, false);
    }

    public boolean contains(int x, int y)
    {
        return game_.getMap().isValid(x, y);
    }

    public void visit(int x, int y)
    {
        //util.Debug.message("Game::shoot Flamer visit " + x + " " + y);
        if (!isObstacle(x, y))
        {
        	game_.flame(x, y);
        	if(!costPayed_) {
        		piece_.useAmmunition();
        		piece_.useActionPoints(2);
        		costPayed_ = true;
        	}
        }
    }

}
