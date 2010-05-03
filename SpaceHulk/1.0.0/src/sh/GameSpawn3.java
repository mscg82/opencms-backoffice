// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSpawn3 extends Game
{
    public final static String NAME = "Regroup";
    public final static String INTRO = "/maps/23.html";
    private final int marinesToEscape_ = 3;
    
    public GameSpawn3()
        throws IOException
    {
        super("/maps/23.shmap");
        
        getAI().setOverFear(1);
    }

    public String getName()
    {
        return NAME;
    }
    
    public int getStatus()
    {
        if (round_ >= 8)
            blipsPerTurn_ = 0;

        if (marines_.size() <= 0 && marineExits_ >= marinesToEscape_)
            return STATUS_COMPLETED;
        else if (marines_.size() <= 0)
            if (marineExits_ >= 1)
                return STATUS_DRAWN;
            else
                return STATUS_FAILED;
        else
            return STATUS_NONE;
    }

    public String getStatusText(int status)
    {
        switch (status)
        {
        case STATUS_NONE:
            return ((marineExits_ >= marinesToEscape_) ? 0 : marinesToEscape_ - marineExits_) + " marines must escape";

        case STATUS_COMPLETED:
            return "The marines arrived safely at the regroup point. Every Marine is vital to the cause. We must conserve our force - we are heavily outnumbered.";

        case STATUS_FAILED:
            return "Not enough Marines have returned to the regroup point. Our chances of escaping the hulk are slim.";

        case STATUS_DRAWN:
            return "Casualty counts of this magnitude cannot be sustained for long, but we will press on regardless.";

        default:
            return null;
        }
    }
}
