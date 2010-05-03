// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSpawn1 extends Game
{
    public final static String NAME = "Break Through";
    public final static String INTRO = "/maps/21.html";
    private boolean cargoDoorsOpen_ = false;
    
    public GameSpawn1()
        throws IOException
    {
        super("/maps/21.shmap");
        
        getAI().setOverFear(1);
    }

    public String getName()
    {
        return NAME;
    }

    protected boolean useObject(Piece p, int x, int y, int object)
    {
        if (object == TileType.OBJECT_CARGO && p instanceof Marine)
        {
            if (p.useActionPoints(2))
            {
                cargoDoorsOpen_ = true;
                getListener().objectUsed(x, y, object, "Cargo bay door opened");
            }
            return true;
        }
        else
            return super.useObject(p, x, y, object);
    }

    public int getStatus()
    {
        if (cargoDoorsOpen_)
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
            return "Activate computer console to open cargo bay doors";

        case STATUS_COMPLETED:
            return "The hulk's cargo bay is now open. The main Blood Angel force can commence its landing.";

        case STATUS_FAILED:
            return "Obviously Squad Huon was not up to the task. Without the cargo bay opened, we cannot land a large enough force. This failure may have doomed us all.";

        default:
            return null;
        }
    }
}
