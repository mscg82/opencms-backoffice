// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSpawn2 extends Game
{
    public final static String NAME = "Forge Ahead";
    public final static String INTRO = "/maps/22.html";
    private final int stealersToKill_ = 25;
    private final int marinesToEscape_ = 4;
    
    public GameSpawn2()
        throws IOException
    {
        super("/maps/22.shmap");
        
        getAI().setOverFear(1);
    }
    
    public String getName()
    {
        return NAME;
    }
    
    public int getStatus()
    {
        if (stealerKills_ >= stealersToKill_)
            getMap().setTile(17, 1, TileType.EXIT | Face.SOUTH.getValue());

        if (stealerKills_ >= stealersToKill_ && marineExits_ >= marinesToEscape_)
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
            return "Kill " + ((stealerKills_ >= stealersToKill_) ? 0 : stealersToKill_ - stealerKills_)
                + " stealers and escape " + ((marineExits_ >= marinesToEscape_) ? 0 : marinesToEscape_ - marineExits_) + " marines";

        case STATUS_COMPLETED:
            return "Well done. You have demonstrated the superiority of Man over the alien horde. We may yet cleanse this hulk.";

        case STATUS_FAILED:
            return "The Genestealers are better organised than we first suspected. The kill ratio is simply not high enough - we are losing too many Marines.";

        case STATUS_DRAWN:
            return "At this rate we will be overrun before a perimeter can be established. This is a marginal result at best.";

        default:
            return null;
        }
    }
}
