// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

public class GameSpawn6 extends Game
{
    public final static String NAME = "Race Against Time";
    public final static String INTRO = "/maps/26.html";
    private final int marinesToEscape_ = 2;
    private final int surviveRounds_ = 12;

    public GameSpawn6()
        throws IOException
    {
        super("/maps/26.shmap");
    }
    
    public String getName()
    {
        return NAME;
    }
    
    public int getStatus()
    {
        if (round_ > 4)
        {
            blipsPerTurn_ = 2;
            getMap().setTile(29, 12, TileType.ENTRY | Face.WEST.getValue());
            getMap().setTile(29, 18, TileType.ENTRY | Face.WEST.getValue());
        }

        if (marines_.size() <= 0 || round_ > surviveRounds_)
        {
            if (marineExits_ >= marinesToEscape_)
                return STATUS_COMPLETED;
            else
                return STATUS_FAILED;
        }
        else
            return STATUS_NONE;
    }

    public String getStatusText(int status)
    {
        switch (status)
        {
        case STATUS_NONE:
            return ((marineExits_ >= marinesToEscape_) ? 0 : marinesToEscape_ - marineExits_)
                + " marines must escape in " + (surviveRounds_ - round_) + " turns";

        case STATUS_COMPLETED:
            return "The surviving Marines were teleported from the hulk's outer levels, just before the hulk's engines went critical. It was a close thing, but we are victorious!";

        case STATUS_FAILED:
            return "The loss of so many lives was avoidable. Those Marines should have escaped in time. What went wrong?";

        default:
            return null;
        }
    }
}
