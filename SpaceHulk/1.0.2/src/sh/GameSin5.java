// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSin5 extends Game
{
    public final static String NAME = "Decoy";
    public final static String INTRO = "/maps/15.html";
    private final int marinesToEscape_ = 5;
    
    public GameSin5()
        throws IOException
    {
        super("/maps/15.shmap");
    }

    public String getName()
    {
        return NAME;
    }
    
    public int getStatus()
    {
        if (marineExits_ >= marinesToEscape_)
            return STATUS_COMPLETED;
        else if ((marineExits_ + marines_.size()) < marinesToEscape_)
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
            return "Your decoy action has bought us precious time. The mission was a complete success.";

        case STATUS_FAILED:
            return "The decoy action proved pointless. The Genestealers had no trouble wiping out both squads, and their main assault was not delayed at all. We must abandon the hulk while we can.";

        default:
            return null;
        }
    }
}
