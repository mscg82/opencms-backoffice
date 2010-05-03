// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

public class DoorState
{
    public final static DoorState NONE    = new DoorState(-1, '-');
    public final static DoorState CLOSED  = new DoorState(0, '1');
    public final static DoorState PARTIAL = new DoorState(1, '2');
    public final static DoorState OPEN    = new DoorState(2, '3');
    public final static DoorState BLASTED = new DoorState(3, 'b');
    
    public final static DoorState[] ordered = { CLOSED, PARTIAL, OPEN };
    public final static DoorState[] all = { CLOSED, PARTIAL, OPEN, BLASTED };
    
    private int value_;
    private char char_;
    
    private DoorState(int value, char c)
    {
        value_ = value;
        char_ = c;
    }
    
    public int getValue()
    {
        return value_;
    }
    
    public char getChar()
    {
        return char_;
    }
    
    static public int getTile(int type, DoorState s, Face f)
    {
        return type | (f.getValue() * all.length + s.getValue());
    };

    static public int getTile(int type, DoorState s)
    {
        return type | s.getValue();
    };

    static public DoorState getState(int type, int tile)
    {
        if ((tile & TileType.TYPE_MASK) == type)
            return all[(tile & TileType.TILE_MASK) % all.length];
        else
            return NONE;
    }
    
    static public Face getFace(int tile)
    {
        return Face.ordered[(tile & TileType.TILE_MASK) / Face.ordered.length];
    }
}
