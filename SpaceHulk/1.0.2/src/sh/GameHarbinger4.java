// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameHarbinger4 extends Game
{
    public final static String NAME = "Isolate";
    public final static String INTRO = "/maps/34.html";
    private boolean terminateLink_ = false;

    public GameHarbinger4()
        throws IOException
    {
        super("/maps/34.shmap");

        getAI().setOverFear(1);
    }
    
    public String getName()
    {
        return NAME;
    }

    protected boolean useObject(Piece p, int x, int y, int object)
    {
        if ((object == TileType.OBJECT_CONTROLS1
            || object == TileType.OBJECT_CONTROLS2
            || object == TileType.OBJECT_CONTROLS3)
            && p instanceof Marine)
        {
            if (p.useActionPoints(4))
            {
                terminateLink_ = true;
                getListener().objectUsed(x, y, object, "Computer shutdown");
            }
            return true;
        }
        else
            return super.useObject(p, x, y, object);
    }

    public int getStatus()
    {
        if (terminateLink_)
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
            return "Shut down control computer";

        case STATUS_COMPLETED:
            return "The hulk's outside control link has been severed. Sensors indicate no other control activity onboard.";

        case STATUS_FAILED:
            return "If the outside control link cannot be cut, we are at the mercy of an unknown enemy. We must succeed!";

        default:
            return null;
        }
    }
}
