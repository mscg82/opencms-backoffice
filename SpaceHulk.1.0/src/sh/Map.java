// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.InputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.Random;
import util.Point2I;
import util.Direction;
import los.BresLos;
import los.ILosBoard;
import util.MiscUtils;

public class Map
{
    private int minx_ = 0;
    private int maxx_ = 0;
    private int miny_ = 0;
    private int maxy_ = 0;

    private int blipsPerTurn_ = 2;
    private int blipsPerEntry_ = 3;
    private boolean initialMoveStealers_ = false;
    private int initialBlips_ = 0;

    private int tile_[][];
    private int object_[][];
    private int item_[][];
    private Piece piece_[][];
    private boolean fire_[][];
    private Vector entry_ = new Vector();
    private Vector startPos_[] = { new Vector(), new Vector() };
    private Vector marineStart_[] = { new Vector(), new Vector() };

    public Map(String filename)
        throws IOException
    {
        //util.Debug.message("+Map::Map");
        InputStream is = getClass().getResourceAsStream(MiscUtils.RESOURCES_FOLDER + filename);
        new MapLoader(this, is);
    }

    static class MarineStart
    {
        MarineStart(String name, int type)
        {
            this.name = name;
            this.type = type;
            this.carrying = 0;
        }

        MarineStart(String name, int type, int carrying)
        {
            this.name = name;
            this.type = type;
            this.carrying = carrying;
        }

        String name;
        int type;
        int carrying;
    }

    static class StartPos
    {
        StartPos(String name, int x, int y, Face face)
        {
            this.name = name;
            this.x = x;
            this.y = y;
            this.face = face;
        }

        String name;
        int x;
        int y;
        Face face;
    }

    void add(int team, StartPos sp)
    {
        startPos_[team].addElement(sp);
    }

    Vector getStartPos(int team)
    {
        return startPos_[team];
    }

    void add(int team, MarineStart ms)
    {
        marineStart_[team].addElement(ms);
    }

    Vector getMarineStart(int team)
    {
        return marineStart_[team];
    }

    void create(int x, int y)
    {
        maxx_ = x - 1;
        maxy_ = y - 1;

        tile_ = new int[maxy_ + 1][maxx_ + 1];
        object_ = new int[maxy_ + 1][maxx_ + 1];
        item_ = new int[maxy_ + 1][maxx_ + 1];
        piece_ = new Piece[maxy_ + 1][maxx_ + 1];
        fire_ = new boolean[maxy_ + 1][maxx_ + 1];
    }

    public int getMinX()
    {
        return minx_;
    }
    
    public int getMaxX()
    {
        return maxx_;
    }
    
    public int getMinY()
    {
        return miny_;
    }
    
    public int getMaxY()
    {
        return maxy_;
    }

    int getBlipsPerTurn()
    {
        return blipsPerTurn_;
    }

    void setBlipsPerTurn(int blipsPerTurn)
    {
        blipsPerTurn_ = blipsPerTurn;
    }

    int getBlipsPerEntry()
    {
        return blipsPerEntry_;
    }

    void setBlipsPerEntry(int blipsPerEntry)
    {
        blipsPerEntry_ = blipsPerEntry;
    }

    boolean getInitialMoveStealers()
    {
        return initialMoveStealers_;
    }

    int getInitialBlips()
    {
        return initialBlips_;
    }

    void setInitialBlips(int initialBlips)
    {
        initialBlips_ = initialBlips;
    }

    void setInitialMoveStealers(boolean initialMoveStealers)
    {
        initialMoveStealers_ = initialMoveStealers;
    }

    private Face calcEntryFace(int x, int y)
    {
        for (int f = 0; f < Face.ordered.length; ++f)
        {
            Face face = Face.ordered[f];
            if ((getTile(x + face.getOffsetX(), y + face.getOffsetY()) & TileType.TYPE_MASK) == TileType.FLOOR)
                return face;
        }
        return Face.NONE;
    }
    
    private Face calcDoorFace(int x, int y)
    {
        if (tile_[y - 1][x] == (TileType.WALL | 15)
            || tile_[y - 1][x] == (TileType.WALL | 20)
            || tile_[y + 1][x] == (TileType.WALL | 12)
            || tile_[y + 1][x] == (TileType.WALL | 22))
            return Face.WEST;
        else if (tile_[y - 1][x] == (TileType.WALL | 17)
            // Missing tile would go here
            || tile_[y + 1][x] == (TileType.WALL | 19)
            || tile_[y + 1][x] == (TileType.WALL | 25))
            return Face.EAST;
        else if (tile_[y][x - 1] == (TileType.WALL | 13)
            // Missing tile would go here
            || tile_[y][x + 1] == (TileType.WALL | 18)
            || tile_[y][x + 1] == (TileType.WALL | 24))
            return Face.SOUTH;
        else if (tile_[y][x - 1] == (TileType.WALL | 14)
            || tile_[y][x - 1] == (TileType.WALL | 23)
            || tile_[y][x + 1] == (TileType.WALL | 16)
            || tile_[y][x + 1] == (TileType.WALL | 21))
            return Face.NORTH;
        else
            return Face.NONE;
    }
    
    public int getTile(int x, int y)
    {
        if (isValid(x, y))
            return tile_[y][x];
        else
            return 0;
    }

    void setTile(int x, int y, int t)
    {
        boolean valid = isValid(x, y);
        util.Debug.assert2(valid, "Map::setTile invalid coord " + x + ", " + y);
        if (valid)
        {
            util.Debug.assert2(t == 0 || tile_[y][x] == 0, "Map::setTile not empty " + tile_[y][x]);
            int type = t & TileType.TYPE_MASK;
            if (type == TileType.ENTRY)
                entry_.addElement(new Point2I(x, y));
            if (type == TileType.ENTRY || type == TileType.EXIT)
            {
                Face entryFace = calcEntryFace(x, y);
                if (entryFace == Face.NONE)
                    util.Debug.message("Map::setTile entryFace is NONE");
                else
                    t = type | entryFace.getValue();
            }
            tile_[y][x] = t;
        }
    }

    public int getObject(int x, int y)
    {
        if (isValid(x, y))
            return object_[y][x];
        else
            return 0;
    }
    
    void setObject(int x, int y, int t)
    {
        boolean valid = isValid(x, y);
        util.Debug.assert2(valid, "Map::setObject invalid coord " + x + ", " + y);
        if (valid)
        {
            util.Debug.assert2(t == 0 || object_[y][x] == 0, "Map::setObject not empty " + object_[y][x]);
            int type = t & TileType.TYPE_MASK;
            if (type == TileType.DOOR || type == TileType.BULKHEAD)
            {
                Face doorFace = calcDoorFace(x, y);
                DoorState state = DoorState.getState(type, t);
                if (doorFace == Face.NONE)
                    util.Debug.message("Map::setTile doorFace is NONE");
                else
                    t = DoorState.getTile(type, state, doorFace);
            }
            object_[y][x] = t;
        }
    }

    public int getItem(int x, int y)
    {
        if (isValid(x, y))
            return item_[y][x];
        else
            return 0;
    }

    void setItem(int x, int y, int t)
    {
        boolean valid = isValid(x, y);
        util.Debug.assert2(valid, "Map::setItem invalid coord " + x + ", " + y);
        if (valid)
        {
            util.Debug.assert2(t == 0 || item_[y][x] == 0, "Map::setItem not empty " + item_[y][x]);
            item_[y][x] = t;
        }
    }

    void placePiece(Piece p)
    {
        // Blips can be placed outside the valid area at entry points
        boolean valid = isValidRelaxed(p.getPosX(), p.getPosY());
        util.Debug.assert2(valid, "Map::placePiece invalid coord " + p.getPosX() + ", " + p.getPosY());
        Piece op = getPiece(p.getPosX(), p.getPosY());
        util.Debug.assert2(op == null, "Map::placePiece not empty " + ((op == null) ? null : op.getClass()));
        if (valid)
            piece_[p.getPosY()][p.getPosX()] = p; 
    }
    
    void removePiece(Piece p)
    {
        boolean valid = isValidRelaxed(p.getPosX(), p.getPosY());
        util.Debug.assert2(valid, "Map::removePiece invalid coord " + p.getPosX() + ", " + p.getPosY());
        Piece op = getPiece(p.getPosX(), p.getPosY());
        util.Debug.assert2(op == p, "Map::removePiece not there " + ((op == null) ? null : op.getClass()));
        if (valid)
            piece_[p.getPosY()][p.getPosX()] = null; 
    }
    
    public Piece getPiece(int x, int y)
    {
        if (isValidRelaxed(x, y))
            return piece_[y][x];
        else
            return null;
    }

    void setFire(int x, int y)
    {
        boolean valid = isValid(x, y);
        util.Debug.assert2(valid, "Map::setFire invalid coord " + x + ", " + y);
        if (valid)
            fire_[y][x] = true;
    }

    public boolean isFire(int x, int y)
    {
        if (isValid(x, y))
            return fire_[y][x];
        else
            return false;
    }

    void clearFire()
    {
        for (int y = getMinY(); y <= getMaxY(); ++y)
        {
            for (int x = getMinX(); x <= getMaxX(); ++x)
            {
                fire_[y][x] = false;
            }
        }
    }

    Blip createBlip(Random r, int maxBlipsPerEntry)
    {
        //util.Debug.message("+Map::createBlip " + maxBlipsPerEntry);
        // TODO Lurk
        Vector entry = new Vector();
        for (int i = 0; i < entry_.size(); ++i)
        {
            //util.Debug.message("+Map::createBlip entry " + i);
            Point2I p = (Point2I) entry_.elementAt(i);
            int tile = getTile(p.x, p.y);
            if ((tile & TileType.TYPE_MASK) == TileType.ENTRY)
            {
                int blipsAtEntry = (getPiece(p.x, p.y) != null) ? 1 : 0;
                for (int di = 0; di < Direction.ordered8.length; ++di)
                {
                    int dir = Direction.ordered8[di];
                    //util.Debug.message("+Map::createBlip dir " + dir);
                    int nx = p.x + Direction.getOffsetX(dir);
                    int ny = p.y + Direction.getOffsetY(dir);
                    if (getPiece(nx, ny) != null && getTile(nx, ny) == 0)
                        ++blipsAtEntry;
                }
                //util.Debug.message(" Map::createBlip blipsAtEntry " + blipsAtEntry);
                if (blipsAtEntry < maxBlipsPerEntry)
                    entry.addElement(p);
            }
            else
                if (getPiece(p.x, p.y) == null)
                    entry.addElement(p);
        }

        if (entry.size() > 0)
        {
            int i = r.nextInt(entry.size());
            Point2I p = (Point2I) entry.elementAt(i);
            int px = p.x;
            int py = p.y;
            int tile = getTile(p.x, p.y);
            if ((tile & TileType.TYPE_MASK) == TileType.ENTRY)
            {
                Face f = Face.ordered[tile & TileType.TILE_MASK];
                //util.Debug.message("+Map::createBlip face " + f.getChar());

                int testdirset[][] = 
                    {
                        { Direction.SOUTH, Direction.EAST, Direction.WEST }, // NORTH
                        { Direction.WEST,  Direction.WEST, Direction.WEST }, // EAST
                        { Direction.NORTH, Direction.EAST, Direction.WEST }, // SOUTH
                        { Direction.EAST,  Direction.EAST, Direction.EAST }, // WEST
                    };
                int testdir[] = testdirset[f.getValue()];
                for (int tdi = 0; tdi < testdir.length; ++tdi)
                {
                    int tdx = p.x + Direction.getOffsetX(testdir[tdi]);
                    int tdy = p.y + Direction.getOffsetY(testdir[tdi]);
                    if (getPiece(tdx, tdy) == null)
                    {
                        //util.Debug.message("+Map::createBlip dir " + tdi);
                        px = tdx;
                        py = tdy;
                        break;
                    }
                }
            }
            //util.Debug.message("+Map::createBlip pos " + px + " " + py);
            Blip blip = new Blip();
            blip.setPos(px, py);
            //util.Debug.message("-Map::createBlip");
            return blip;
        }
        else
            return null;
    }

    boolean openDoor(int x, int y, GameListener gl)
    {
        int object = getObject(x, y);
        DoorState ds = DoorState.getState(TileType.DOOR, object);
        if (ds == DoorState.CLOSED)
        {
            gl.doorStateChanging(x, y, TileType.DOOR, DoorState.CLOSED, DoorState.OPEN);
            setObject(x, y, 0);
            setObject(x, y, DoorState.getTile(TileType.DOOR, DoorState.PARTIAL));
            if (gl != null)
                gl.doorStateChanged(x, y, TileType.DOOR, DoorState.PARTIAL);
            setObject(x, y, 0);
            setObject(x, y, DoorState.getTile(TileType.DOOR, DoorState.OPEN));
            if (gl != null)
                gl.doorStateChanged(x, y, TileType.DOOR, DoorState.OPEN);
            return true;
        }
        else
            return false;
    }

    boolean blastDoor(int x, int y, GameListener gl)
    {
        int object = getObject(x, y);
        int tileType = (object & TileType.TYPE_MASK);
        if (tileType == TileType.DOOR)
        {
            DoorState ds = DoorState.getState(tileType, object);
            if (ds == DoorState.CLOSED)
            {
                gl.doorStateChanging(x, y, tileType, DoorState.CLOSED, DoorState.BLASTED);
                setObject(x, y, 0);
                setObject(x, y, DoorState.getTile(tileType, DoorState.BLASTED));
                if (gl != null)
                    gl.doorStateChanged(x, y, tileType, DoorState.BLASTED);
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    boolean toggleDoor(int x, int y, GameListener gl)
    {
        int object = getObject(x, y);
        int tileType = (object & TileType.TYPE_MASK);
        if (tileType == TileType.DOOR || tileType == TileType.BULKHEAD)
        {
            DoorState ds = DoorState.getState(tileType, object);
            if (ds != DoorState.NONE && ds != DoorState.BLASTED
                && (tileType != TileType.BULKHEAD || ds == DoorState.OPEN))
            {
                // TODO If closing then move item out of way
                DoorState nds = ds;
                if (ds == DoorState.CLOSED)
                    nds = DoorState.OPEN;
                else if (ds == DoorState.OPEN)
                    nds = DoorState.CLOSED;
                gl.doorStateChanging(x, y, tileType, ds, nds);
                setObject(x, y, 0);
                setObject(x, y, DoorState.getTile(tileType, DoorState.PARTIAL));
                if (gl != null)
                    gl.doorStateChanged(x, y, tileType, DoorState.PARTIAL);
                setObject(x, y, 0);
                setObject(x, y, DoorState.getTile(tileType, nds));
                if (tileType == TileType.BULKHEAD && nds == DoorState.CLOSED)
                {
                    for (int ei = 0; ei < entry_.size(); ++ei)
                    {
                        Point2I p = (Point2I) entry_.elementAt(ei);
                        if (p.x == x && p.y == y)
                        {
                            util.Debug.message("remove entry");
                            entry_.removeElementAt(ei);
                            break;
                        }
                    }
                }
                if (gl != null)
                    gl.doorStateChanged(x, y, tileType, nds);
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    public boolean canMove(int x, int y, boolean checkDoor, boolean checkFire, boolean checkPiece)
    {
        int tile = getTile(x, y);
        if ((tile & TileType.TYPE_MASK) == TileType.FLOOR)
        {
            int o = getObject(x, y);
            if (checkPiece && getPiece(x, y) != null)
            {
                //util.Debug.message("Map::canMove piece in way");
                return false;
            }
            else if (checkFire && isFire(x, y))
            {
                //util.Debug.message("Map::canMove fire");
                return false;
            }
            else if (o != 0)
            {
                if ((o & TileType.TYPE_MASK) != TileType.DOOR)
                {
                    //util.Debug.message("Map::canMove object");
                    return o >= TileType.OBJECT_BLOOD && o <= TileType.OBJECT_SPLAT;
                }
                else if (checkDoor && DoorState.getState(TileType.DOOR, o) == DoorState.CLOSED)
                {
                    //util.Debug.message("Map::canMove door closed");
                    return false;
                }
                else
                    return true;
            }
            else
                return true;
        }
        else
        {
            //util.Debug.message("Map::canMove no floor");
            return false;
        }
    }

    private class LosBoard implements ILosBoard
    {
        private int sx_;
        private int sy_;
        private int dx_;
        private int dy_;

        LosBoard(int sx, int sy, int dx, int dy)
        {
            sx_ = sx;
            sy_ = sy;
            dx_ = dx;
            dy_ = dy;
        }

        public boolean isObstacle(int x, int y)
        {
            //util.Debug.message("Map::LosBoard isObstacle " + x + " " + y);
            if (x == sx_ && y == sy_)
                return false;
            else if (x == dx_ && y == dy_)
                return false;
            else
                return !canMove(x, y, true, true, true);
        }
    };

    public boolean existsLineOfSight(Marine m, int x, int y, boolean fireArc)
    {
        LosBoard lb = new LosBoard(m.getPosX(), m.getPosY(), x, y);
        BresLos bl = new BresLos(true);
        if (bl.existsLineOfSight(lb, m.getPosX(), m.getPosY(), x, y, true))
        {
            Vector path = bl.getProjectPath();
            if (path.size() > 1)
            {
                Point2I p = (Point2I) path.elementAt(1);
                int dir = Direction.getDirection(m.getPosX(), m.getPosY(), p.x, p.y);
                //util.Debug.message("Game::existsLineOfSight dir " + dir);
                util.Debug.assert2(dir != Direction.NONE, "Invalid direction");
                if (fireArc)
                    return Face.isForward(dir, m.getFace());
                else
                    return !Face.isBackward(dir, m.getFace());
            }
            else
                return false;
        }
        else
            return false;
    }

    public Vector getLineOfSight(int sx, int sy, int dx, int dy)
    {
        LosBoard lb = new LosBoard(sx, sy, dx, dy);
        BresLos bl = new BresLos(true);
        if (bl.existsLineOfSight(lb, sx, sy, dx, dy, true))
            return bl.getProjectPath();
        else
            return null;
    }

    Marine findToTake(int x, int y)
    {
        for (int di = 0; di < Direction.ordered8.length; ++di)
        {
            int dir = Direction.ordered8[di];
            int nx = x + Direction.getOffsetX(dir);
            int ny = y + Direction.getOffsetY(dir);
            Piece p = getPiece(nx, ny);
            if (p instanceof Marine)
            {
                Marine m = (Marine) p;
                if (m.getCarrying() != 0)
                    return m;
            }
        }
        return null;
    }

    public boolean isValid(int x, int y)
    {
        if (x < minx_)
            return false;
        else if (x > maxx_)
            return false;
        else if (y < miny_)
            return false;
        else if (y > maxy_)
            return false;
        else
            return true;
    }

    public boolean isValidRelaxed(int x, int y)
    {
        if (x < 0 || x < (minx_ - 1))
            return false;
        else if (x > maxx_)
            return false;
        else if (y < 0 || y < (miny_ - 1))
            return false;
        else if (y > maxy_)
            return false;
        else
            return true;
    }
}
