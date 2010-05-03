// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import util.Direction;

public class Face
{
    public final static Face NONE  = new Face(-1, 0, 0, '-');
    public final static Face NORTH = new Face(0, 0, -1, 'n');
    public final static Face EAST  = new Face(1, +1, 0, 'e');
    public final static Face SOUTH = new Face(2, 0, +1, 's');
    public final static Face WEST  = new Face(3, -1, 0, 'w');
    
    public final static Face[] ordered = { NORTH, EAST, SOUTH, WEST };
    
    private int value_;
    private int offsetx_;
    private int offsety_;
    private char char_;
    
    private Face(int value, int offsetx, int offsety, char c)
    {
        value_ = value;
        offsetx_ = offsetx;
        offsety_ = offsety;
        char_ = c;
    }
    
    public int getValue()
    {
        return value_;
    }
    
    public int getOffsetX()
    {
        return offsetx_;
    }
    
    public int getOffsetY()
    {
        return offsety_;
    }
    
    public char getChar()
    {
        return char_;
    }
    
    public Face right()
    {
        if ((value_ + 1) < ordered.length)
            return ordered[value_ + 1];
        else
            return ordered[0];
    }
    
    public Face left()
    {
        if ((value_ - 1) >= 0)
            return ordered[value_ - 1];
        else
            return ordered[ordered.length - 1];
    }

    public Face flip()
    {
        int v = value_ + 2;
        if (v >= ordered.length)
            v -= ordered.length;
        return ordered[v];
    }

    static boolean isForward(int dir, Face f)
    {
        return ((Direction.getOffsetX(dir) == f.getOffsetX() && f.getOffsetX() != 0)
            || (Direction.getOffsetY(dir) == f.getOffsetY() && f.getOffsetY() != 0));
    }

    static boolean isBackward(int dir, Face f)
    {
        return ((Direction.getOffsetX(dir) == -f.getOffsetX() && f.getOffsetX() != 0)
            || (Direction.getOffsetY(dir) == -f.getOffsetY() && f.getOffsetY() != 0));
    }

    static boolean isRight(int dir, Face f)
    {
        return ((Direction.getOffsetX(dir) == -f.getOffsetY() && f.getOffsetY() != 0)
            || (Direction.getOffsetY(dir) == f.getOffsetX() && f.getOffsetX() != 0));
    }

    static boolean isLeft(int dir, Face f)
    {
        return ((Direction.getOffsetX(dir) == f.getOffsetY() && f.getOffsetY() != 0)
            || (Direction.getOffsetY(dir) == -f.getOffsetX() && f.getOffsetX() != 0));
    }

    static Face getFace(int dir)
    {
        if ((dir & Direction.EAST) == Direction.EAST)
            return EAST;
        else if ((dir & Direction.WEST) == Direction.WEST)
            return WEST;
        else if ((dir & Direction.NORTH) == Direction.NORTH)
            return NORTH;
        else if ((dir & Direction.SOUTH) == Direction.SOUTH)
            return SOUTH;
        else
            return NONE;
    }
}
