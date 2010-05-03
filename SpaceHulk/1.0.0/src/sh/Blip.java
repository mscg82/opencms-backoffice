// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.util.Random;

import util.dice.Dice6;

public class Blip extends Piece
{
    Blip()
    {
        super(Face.NONE);
    }

    void setFace(Face f)
    {
        throw new IllegalArgumentException();
    }

    int getCloseCombatValue(Random r, int dir)
    {
        int v = 0;
        for (int i = 0; i < 3; ++i)
            v = Math.max(v, Dice6.getDice().getDiceRoll());
        return v;
    }

    void resetActionPoints()
    {
        action_ = 6;
    }
}
