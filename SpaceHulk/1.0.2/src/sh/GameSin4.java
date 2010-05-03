// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSin4 extends Game
{
    public final static String NAME = "Cleanse and Burn";
    public final static String INTRO = "/maps/14.html";
    private boolean room1Flamed = false;
    private boolean room2Flamed = false;
    
    public GameSin4()
        throws IOException
    {
        super("/maps/14.shmap");

        getAI().setMarineTypeTarget(Marine.FLAMER);
    }

    public String getName()
    {
        return NAME;
    }
    
    public int getStatus()
    {
        room1Flamed = room1Flamed || getMap().isFire(10, 8);
        room2Flamed = room2Flamed || getMap().isFire(10, 19);

        if (room1Flamed && room2Flamed)
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
            return "Flame the gene-banks in the western rooms";

        case STATUS_COMPLETED:
            return "Our gene-seed has been preserved. The cleansing flames have saved our Brothers from the Genestealer corruption. Well Done.";

        case STATUS_FAILED:
            return "Disaster! The Genestealers will use the Blood Angel genes to strengthen their brood. Our Chapter is forever tainted by their corruption. This is a black day for the Imperium.";

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
        if (!room1Flamed)
            --shots;
        if (!room2Flamed)
            --shots;
        return shots < 0;    // No Flamer
    }
}
