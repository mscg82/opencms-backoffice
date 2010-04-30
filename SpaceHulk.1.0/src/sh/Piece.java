// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.util.Random;

public abstract class Piece
{
    private int posx_;
    private int posy_;
    private Face face_;
    private boolean deleted_ = false;
    protected int action_;
    protected int animation_ = 0;

    Piece(Face face)
    {
        face_ = face;
        resetActionPoints();
    }

    public int getPosX()
    {
        return posx_;
    }
    
    public int getPosY()
    {
        return posy_;
    }

    public int getAnimation()
    {
        return animation_;
    }

    public void setAnimation(int a)
    {
        animation_ = a;
    }

    void setPos(int x, int y)
    {
        posx_ = x;
        posy_ = y;
    }

    public Face getFace()
    {
        return face_;
    }

    void setFace(Face f)
    {
        if (f == Face.NONE)
            throw new IllegalArgumentException();
        face_ = f;
    }

    void move(Map m, int x, int y, GameListener gl)
    {
        if (gl != null)
            gl.pieceMoving(this);
        m.removePiece(this);
        setPos(x, y);
        m.placePiece(this);
        if (gl != null)
            gl.pieceMoved(this);
    }
    
    void delete(Map m)
    {
        m.removePiece(this);
        deleted_ = true;
    }
    
    public boolean isDeleted()
    {
        return deleted_;
    }

    public int getActionPoints()
    {
        return action_;
    }
    
    public boolean canUseActionPoints(int ap) {
    	return (ap < action_);
    }

    public boolean useActionPoints(int ap)
    {
        if (action_ >= ap)
        {
            action_ -= ap;
            return true;
        }
        else
            return false;
    }

    void clearActionPoints()
    {
        action_ = 0;
    }

    abstract int getCloseCombatValue(Random r);
    abstract void resetActionPoints();
}
