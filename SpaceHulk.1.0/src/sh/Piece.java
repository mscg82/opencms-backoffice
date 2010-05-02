// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.util.Random;

public abstract class Piece {
	private int posx_;
	private int posy_;
	private Face face_;
	private boolean deleted_ = false;
	protected int action_;
	protected int animation_ = 0;

	Piece(Face face) {
		face_ = face;
		resetActionPoints();
	}

	public boolean canUseActionPoints(int ap) {
		return (ap < action_);
	}

	public void clearActionPoints() {
		action_ = 0;
	}

	void delete(Map m) {
		m.removePiece(this);
		deleted_ = true;
	}

	public boolean equals(Object obj) {
		return this == obj;
	}

	public int getActionPoints() {
		return action_;
	}

	public int getAnimation() {
		return animation_;
	}

	abstract int getCloseCombatValue(Random r, int dir);

	public Face getFace() {
		return face_;
	}

	public int getPosX() {
		return posx_;
	}

	public int getPosY() {
		return posy_;
	}

	public boolean isDeleted() {
		return deleted_;
	}

	void move(Map m, int x, int y, GameListener gl) {
		if (gl != null)
			gl.pieceMoving(this);
		m.removePiece(this);
		setPos(x, y);
		m.placePiece(this);
		if (gl != null)
			gl.pieceMoved(this);
	}

	abstract void resetActionPoints();

	public void setAnimation(int a) {
		animation_ = a;
	}

	void setFace(Face f) {
		if (f == Face.NONE)
			throw new IllegalArgumentException();
		face_ = f;
	}

	void setPos(int x, int y) {
		posx_ = x;
		posy_ = y;
	}

	public boolean useActionPoints(int ap) {
		if (action_ >= ap) {
			action_ -= ap;
			return true;
		} else
			return false;
	}
}
