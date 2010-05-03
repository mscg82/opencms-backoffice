// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

public class TileType
{
    public final static int TYPE_MASK = 0xF00;
    public final static int TILE_MASK = 0x0FF;
    public final static int FLOOR = 0x100;
    public final static int WALL = 0x200;
    public final static int DOOR = 0x300;
    public final static int BULKHEAD = 0x400;
    public final static int OBJECT = 0x500;
    public final static int ENTRY = 0x600;
    public final static int EXIT = 0x700;

    public final static int OBJECT_ARCHIVE = OBJECT | 0;    // 34
    public final static int OBJECT_TOXIN = OBJECT | 1;  // 35
    public final static int OBJECT_CARGO = OBJECT | 2;  // 36
    public final static int OBJECT_DAMPING = OBJECT | 3;    // 28
    public final static int OBJECT_CONTROLS1 = OBJECT | 4;    // 4
    public final static int OBJECT_CONTROLS2 = OBJECT | 5;    // 5
    public final static int OBJECT_CONTROLS3 = OBJECT | 6;    // 6
    public final static int OBJECT_AIRPUMP1 = OBJECT | 7;    // 10
    public final static int OBJECT_AIRPUMP2 = OBJECT | 8;    // 11
    public final static int OBJECT_AIRPUMP3 = OBJECT | 9;    // 12
    public final static int OBJECT_AIRPUMP4 = OBJECT | 10;    // 13

    public final static int OBJECT_BLOOD = OBJECT | 11;
    public final static int OBJECT_SPLAT = OBJECT | 12;
    public final static int OBJECT_CAT = OBJECT | 13;

    public final static int OBJECT_CRYO_BEGIN = OBJECT | 14;
    public final static int OBJECT_CRYO_NW_2 = OBJECT_CRYO_BEGIN + 0;
    public final static int OBJECT_CRYO_NE_2 = OBJECT_CRYO_BEGIN + 1;    // 79
    public final static int OBJECT_CRYO_SE_2 = OBJECT_CRYO_BEGIN + 2;    // 80
    public final static int OBJECT_CRYO_SW_2 = OBJECT_CRYO_BEGIN + 3;    // 81
    public final static int OBJECT_CRYO_NW_1 = OBJECT_CRYO_BEGIN + 4;    // 78
    public final static int OBJECT_CRYO_NE_1 = OBJECT_CRYO_BEGIN + 5;
    public final static int OBJECT_CRYO_SE_1 = OBJECT_CRYO_BEGIN + 6;
    public final static int OBJECT_CRYO_SW_1 = OBJECT_CRYO_BEGIN + 7;
    public final static int OBJECT_CRYO_NW_0 = OBJECT_CRYO_BEGIN + 8;
    public final static int OBJECT_CRYO_NE_0 = OBJECT_CRYO_BEGIN + 9;
    public final static int OBJECT_CRYO_SE_0 = OBJECT_CRYO_BEGIN + 10;
    public final static int OBJECT_CRYO_SW_0 = OBJECT_CRYO_BEGIN + 11;
}
