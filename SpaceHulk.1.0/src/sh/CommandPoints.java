// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.util.Random;

public class CommandPoints
{
    private int points_;

    CommandPoints(Random r)
    {
        reset(r);
    }

    void reset(Random r)
    {
        points_ = r.nextInt(6) + 1;
    }

    public int get()
    {
        return points_;
    }

    boolean use(int cp)
    {
        if (points_ >= cp)
        {
            points_ -= cp;
            return true;
        }
        else
            return false;
    }
}
