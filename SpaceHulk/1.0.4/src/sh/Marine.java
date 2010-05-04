// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.util.Random;
import java.util.Vector;

import main.managers.Tile;
import main.managers.TileManager;
import util.TileInfo;
import util.dice.Dice6;

public class Marine extends Piece {
	public final static int MAX_SUSTAINED_FIRE_BONUS = 1;
	public final static int MAX_MARINE_ACTION_POINTS = 4;

	public final static int SERGEANT = 2;
	public final static int FLAMER   = 1;
	public final static int STANDARD = 0;
	
	public final static int WEAPONS_BOLTER = 0;
	public final static int WEAPONS_FLAMER = 1;
	public final static int WEAPONS_SERGEANT_BOLTER = 2;

	private String name_;
	private int type_;
	private boolean shoot_ = false;
	private boolean overwatch_ = false;
	private boolean guard_ = false;
	private boolean jammed_ = false;
	private CommandPoints cp_;
	private int rangedWeapon_;

	private int carrying_ = 0;

	private int ammunition_ = 6;

	private Piece target_ = null;
	private int targetX_;
	private int targetY_;
	private int bonus_ = 0;

	Marine(String name, int type, CommandPoints cp) {
		super(Face.NORTH);
		name_ = name;
		type_ = type;
		cp_ = cp;
		
		switch (type) {
		case FLAMER:
			rangedWeapon_ = WEAPONS_FLAMER;
			break;
		case SERGEANT:
			rangedWeapon_ = WEAPONS_SERGEANT_BOLTER;
			break;
		default:
			rangedWeapon_ = WEAPONS_BOLTER;
			break;
		}
	}

	public boolean canOverwatch() {
		return type_ != FLAMER;
	}

	public boolean canUseActionPoints(int ap) {
		return (ap <= (action_ + cp_.get()));
	}

	void clearJammed() {
		jammed_ = false;
	}

	public int getAmmunition() {
		return ammunition_;
	}

	public int getCarrying() {
		return carrying_;
	}
	
	int getCloseCombatValue(Random r, int dir) {
		int v = Dice6.getDice().getDiceRoll();
		if (type_ == SERGEANT) {
			if(Face.isForward(dir, getFace()))
				++v;
		}
		return v;
	}

	public boolean getGuard() {
		return guard_;
	}

	public boolean getJammed() {
		return jammed_;
	}

	public String getName() {
		return name_;
	}

	public boolean getOverwatch() {
		return overwatch_;
	}

	public boolean getShoot() {
		return shoot_;
	}

	int getShootValue(Random r, boolean jam, GameListener gl) {
		int v1 = Dice6.getDice().getDiceRoll();
		int v2 = Dice6.getDice().getDiceRoll();
		// util.Debug.message("Marine::getShootValue bonus " + bonus_);
		// if (jam && v1 >= 6 && v2 >= 6)
		if (jam && v1 == v2) {
			jammed_ = true;
			gl.pieceJams(this);
		}
		return Math.max(v1, v2) + bonus_;
	}

	public int getType() {
		return type_;
	}

	public void resetActionPoints() {
		action_ = MAX_MARINE_ACTION_POINTS;
	}

	void setCarrying(int c) {
		carrying_ = c;
	}

	void setGuard(boolean g) {
		guard_ = g;
		if(guard_)
			setOverwatch(false);
	}

	void setOverwatch(boolean o) {
		overwatch_ = o;
		if(overwatch_)
			setGuard(false);
	}

	void setShoot(boolean s) {
		shoot_ = s;
	}

	void setTarget(Piece target) {
		if (target != null) {
			if (target.equals(target_) && targetX_ == this.getPosX() && targetY_ == this.getPosY()) {
				// Target is the same and Marine has not moved
				// util.Debug.message("Marine::setTarget bonus");
				if (bonus_ < MAX_SUSTAINED_FIRE_BONUS)
					++bonus_;
			}
			else {
				// util.Debug.message("Marine::setTarget " + target);
				target_ = target;
				targetX_ = this.getPosX();// target.getPosX();
				targetY_ = this.getPosY(); // target.getPosY();
				bonus_ = 0;
			}
		}
		else {
			target_ = null;
			bonus_ = 0;
		}
	}

	public boolean useActionPoints(int ap) {
		if (super.useActionPoints(ap))
			return true;
		else if (cp_.use(ap - getActionPoints()))
			return useActionPoints(getActionPoints()); // Should always be true
		else
			return false;
	}
	
	public void useAmmunition() {
		util.Debug.assert2(ammunition_ >= 1, "Marine::useAmmunition out of ammo");
		--ammunition_;
	}
	
	public Vector getTiles(TileManager tm) {
		Vector tiles = new Vector();
		
		// check if there is the a tile for the carried item
		Tile c = tm.getTile(getCarrying());
		if(c != null)
			tiles.addElement(new TileInfo(c, 0, 0));
		
		// check if the marine is shooting
		if (getShoot()) {
            Face f = getFace();
            Tile t = tm.getShoot(f);
            if (t != null) {
            	tiles.addElement(new TileInfo(t,
            		f.getOffsetX() * (TileManager.getTileWidth() + tm.getShootOffset()), 
            		f.getOffsetY() * (TileManager.getTileHeight() + tm.getShootOffset())));
            }
		}
		
		if(getType() != SERGEANT) {
			// get the weapon tile
			Tile w = tm.getWeapon(rangedWeapon_, getFace());
			if(w != null)
				tiles.addElement(new TileInfo(w, 0, 0));
			// get the main tile
			Tile t = tm.getTile(this);
			if (t != null)
				tiles.addElement(new TileInfo(t, 0, 0));
		}
		else {
			// get the main tile
			Tile t = tm.getTile(this);
			if (t != null)
				tiles.addElement(new TileInfo(t, 0, 0));
		}
		
		
		// additionals overlaying tiles
		if (getOverwatch() && !getJammed())
			tiles.addElement(new TileInfo(TileManager.overwatchTile, 0, 0));
        if (getJammed())
        	tiles.addElement(new TileInfo(TileManager.jammedTile, 0, 0));
        if (getGuard())
        	tiles.addElement(new TileInfo(TileManager.guardTile, 0, 0));
		
		return tiles;
	}
}
