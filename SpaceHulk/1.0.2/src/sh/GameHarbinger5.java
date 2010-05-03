// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameHarbinger5 extends Game
{
    public final static String NAME = "Annihilate";
    public final static String INTRO = "/maps/35.html";

    public GameHarbinger5()
        throws IOException
    {
        super("/maps/35.shmap");

        getAI().setOverFear(1);
    }
    
    public String getName()
    {
        return NAME;
    }

    protected boolean shootObject(Marine m, int x, int y, int object)
    {
        if (m.getType() != Marine.FLAMER
            && object >= TileType.OBJECT_CRYO_NW_2
            && object <= TileType.OBJECT_CRYO_SW_1)
        {
            if (m.useActionPoints(1))
            {
                m.setOverwatch(false);
                m.setShoot(true);
                getListener().pieceShoots(m);
                m.setTarget(null);
                if (m.getShootValue(r_, false, getListener()) >= 6)
                {
                    getMap().setObject(x, y, object + 4);
                    if (object >= TileType.OBJECT_CRYO_NW_1)
                        getListener().objectDestroyed(x, y, object);
                    else
                        getListener().objectDamaged(x, y, object);
                }
                else
                    getListener().pieceShootsMissDoor(m);
                m.setShoot(false);
            }
            return true;
        }
        else
            return super.shootObject(m, x, y, object);
    }

    private boolean isDestroyed(int x, int y)
    {
        int object = getMap().getObject(x, y);
        return object >= TileType.OBJECT_CRYO_NW_0 && object <= TileType.OBJECT_CRYO_SW_0;
    }

    public int getStatus()
    {
        if (isDestroyed(13, 19)
            && isDestroyed(15, 19)
            && isDestroyed(13, 21)
            && isDestroyed(15, 21))
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
            return "Destroy cryogenic pods";

        case STATUS_COMPLETED:
            return "All cryogenic pods have been destroyed. If there was a Patriarch in there, it could not have survived. The Emperor has smiled on us today.";

        case STATUS_FAILED:
            return "They were good Marines, they should have completed the task. Perhaps their tactical commander is to blame. Who knows? It is too late for them now.";

        default:
            return null;
        }
    }
}
