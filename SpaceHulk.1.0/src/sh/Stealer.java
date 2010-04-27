// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.util.Random;

public class Stealer extends Piece
{
    Stealer()
    {
        super(Face.NORTH);
    }
    
    int getCloseCombatValue(Random r)
    {
        int v = 0;
        for (int i = 0; i < 3; ++i)
            v = Math.max(v, r.nextInt(6) + 1);
        return v;
    }

    void resetActionPoints()
    {
        action_ = 6;
    }
}
