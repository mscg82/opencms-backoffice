// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package util;

public class Direction
{
    public final static int NONE  = 0;

    public final static int NORTH = 1;
    public final static int EAST  = 2;
    public final static int SOUTH = 4;
    public final static int WEST  = 8;

    public final static int NORTH_EAST = NORTH | EAST;
    public final static int SOUTH_EAST = SOUTH | EAST;
    public final static int SOUTH_WEST = SOUTH | WEST;
    public final static int NORTH_WEST = NORTH | WEST;
    
    public final static int[] ordered4 = { NORTH, EAST, SOUTH, WEST };
    public final static int[] ordered8 = { NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST };
    public final static int[] ordered8p = { NORTH, EAST, SOUTH, WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST };
    
    public static int getDirection(int sx, int sy, int dx, int dy)
    {
        int dir = NONE;
        if ((sx - dx) > 0)
            dir = dir | WEST;
        else if ((sx - dx) < 0)
            dir = dir | EAST;
        if ((sy - dy) > 0)
            dir = dir | NORTH;
        else if ((sy - dy) < 0)
            dir = dir | SOUTH;
        return dir;
    }
    
    public static int getOffsetX(int dir)
    {
        if ((dir & EAST) != 0)
            return +1; 
        else if ((dir & WEST) != 0)
            return -1; 
        else
            return 0;
    }
    
    public static int getOffsetY(int dir)
    {
        if ((dir & SOUTH) != 0)
            return +1; 
        else if ((dir & NORTH) != 0)
            return -1; 
        else
            return 0;
    }

    public static int flip(int dir)
    {
        for (int di = 0; di < ordered8.length; ++di)
        {
            if (ordered8[di] == dir)
                return ordered8[(di + ordered8.length / 2) % ordered8.length];
        }
        util.Debug.error("Direction::flip could not find " + dir);
        return dir;
    }
}
