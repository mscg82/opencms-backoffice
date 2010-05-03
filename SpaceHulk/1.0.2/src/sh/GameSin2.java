// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSin2 extends Game
{
    public final static String NAME = "Exterminate";
    public final static String INTRO = "/maps/12.html";
    private final int stealersToKill_ = 30;
    
    public GameSin2()
        throws IOException
    {
        super("/maps/12.shmap");

        getAI().setOverFear(2);
        // TODO No lurking, dont use entrys near Marines
    }

    public String getName()
    {
        return NAME;
    }
    
    public int getStatus()
    {
        if (stealerKills_ >= stealersToKill_)
            return STATUS_COMPLETED;
        else if (marines_.size() <= 0)
            return STATUS_FAILED;
        else
            return STATUS_NONE;
    }

    public String getStatusText(int status)
    {
        switch (status)
        {
        case STATUS_NONE:
            return "Kill " + ((stealerKills_ >= stealersToKill_) ? 0 : stealersToKill_ - stealerKills_) + " Genestealers.";

        case STATUS_COMPLETED:
            return "The Marines of Squad Cyrano eventually died honourable deaths, destroying many foes. The blessings of the Emperor are with them.";

        case STATUS_FAILED:
            return "Squad Cyrano was wiped out. This was expected, but they did little to reduce the Genestealer presence in their sector. A higher body count is expected of Blood Angels.";

        default:
            return null;
        }
    }
}
