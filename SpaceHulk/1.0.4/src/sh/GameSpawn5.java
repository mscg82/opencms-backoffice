// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSpawn5 extends Game
{
    public final static String NAME = "Sabotage";
    public final static String INTRO = "/maps/25.html";
    private int dampingHP_ = 2;
    
    public GameSpawn5()
        throws IOException
    {
        super("/maps/25.shmap");
        
        getAI().setOverFear(1);
    }

    public String getName()
    {
        return NAME;
    }

    protected boolean shootObject(Marine m, int x, int y, int object)
    {
        if (m.getType() != Marine.FLAMER && object == TileType.OBJECT_DAMPING)
        {
            if (m.useActionPoints(1))
            {
                m.setOverwatch(false);
                m.setShoot(true);
                getListener().pieceShoots(m);
                m.setTarget(null);
                if (m.getShootValue(r_, false, getListener()) >= 6)
                {
                    --dampingHP_;
                    if (dampingHP_ <= 0)
                    {
                        getMap().setObject(x, y, 0);
                        getListener().objectDestroyed(x, y, object);
                    }
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

    public int getStatus()
    {
        if (bulkHeads_ < 2)
            blipsPerTurn_ = bulkHeads_;
        // TODO target any bolter-toting marines who get too close to the control room
        if (getMap().getObject(20, 2) == 0)
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
            return "Destroy damping controls, Entry points may be sealed";

        case STATUS_COMPLETED:
            return "With the damping controls destroyed, the hulk is becoming unstable. Soon our foes will perish in flames!";

        case STATUS_FAILED:
            return "The damping controls are still functional. There is no other option - we must make another attempt. It is the only way to destroy the hulk.";

        default:
            return null;
        }
    }
}
