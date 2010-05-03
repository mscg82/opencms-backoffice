// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSin1 extends Game
{
    public final static String NAME = "Suicide Mission";
    public final static String INTRO = "/maps/11.html";
    
    public GameSin1()
        throws IOException
    {
        super("/maps/11.shmap");
        
        getAI().setMarineTypeTarget(Marine.FLAMER);
    }

    public String getName()
    {
        return NAME;
    }
    
    public int getStatus()
    {
        if (getMap().isFire(7, 22))
            return STATUS_COMPLETED;
        else if (hasFailed())
            return STATUS_FAILED;
        else
            return STATUS_NONE;
    }

    public String getStatusText(int status)
    {
        switch (status)
        {
        case STATUS_NONE:
            return "Flame the Launch Control room to the south";

        case STATUS_COMPLETED:
            return "Squad Lanarte has succeeded in destroying the launch control room. The Genestealers will not be able to use the escape pods.";

        case STATUS_FAILED:
            return "Squad Lanarte failed to destroy the launch controls. The hulk's escape pods can be used by the Genestealers to infect new worlds. This failure is unacceptable.";

        default:
            return null;
        }
    }

    private boolean hasFailed()
    {
        int shots = 0;
        for (int i = 0; i < marines_.size(); ++i)
        {
            Marine m = (Marine) marines_.elementAt(i);
            if (m.getType() == Marine.FLAMER)
                shots += m.getAmmunition();
        }
        return shots <= 0;    // No Flamer
    }
}
