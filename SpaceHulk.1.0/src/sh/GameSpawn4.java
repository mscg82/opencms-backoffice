// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSpawn4 extends Game
{
    public final static String NAME = "Capture";
    public final static String INTRO = "/maps/24.html";
    private boolean escapedWithArchive_ = false;
    
    public GameSpawn4()
        throws IOException
    {
        super("/maps/24.shmap");
        
        getAI().setOverFear(1);
    }

    public String getName()
    {
        return NAME;
    }
    
    protected void exit(Marine m)
    {
        if (m.getCarrying() == TileType.OBJECT_ARCHIVE)
            escapedWithArchive_ = true;
        super.exit(m);
    }

    protected boolean testPickup(Marine m, int object)
    {
        // TODO How many points to drop ???
        if (object == TileType.OBJECT_ARCHIVE)
            return m.useActionPoints(2);
        else
            return super.testPickup(m, object);
    }

    public void take(Marine m)
    {
        if (m.useActionPoints(1))
            super.take(m);
    }

    public int getStatus()
    {
        if (escapedWithArchive_)
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
            return "Escape with archive";

        case STATUS_COMPLETED:
            return "The hulk's archive is a priceless find. Squads Lorenzo and Vitor have earned a place in Blood Angels history today.";

        case STATUS_FAILED:
            return "Failure to capture the hulk archive is unforgivable. Two squads should have been more than enough. That information is vital to the Imperium!";

        default:
            return null;
        }
    }
}
