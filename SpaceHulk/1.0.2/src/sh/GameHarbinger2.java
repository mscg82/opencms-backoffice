// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameHarbinger2 extends Game
{
    public final static String NAME = "Menace";
    public final static String INTRO = "/maps/32.html";

    public GameHarbinger2()
        throws IOException
    {
        super("/maps/32.shmap");

        getAI().setOverFear(1);
    }
    
    public String getName()
    {
        return NAME;
    }
    
    public int getStatus()
    {
        if (round_ >= 11)
            blipsPerTurn_ = 0;
        else if (round_ >= 5)
            blipsPerTurn_ = 1;

        if (getStealers().size() <= 0 && getBlips().size() <= 0)
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
            return "Clear section of genestealers";

        case STATUS_COMPLETED:
            return "The area is clear. Sentries have been posted at the perimeter, and other squads are moving up to relieve you. Well done.";

        case STATUS_FAILED:
            return "We are losing too many Marines. We must redouble our efforts! That sector must be cleared!";

        default:
            return null;
        }
    }
}
