// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.util.Random;
import java.util.Vector;
import java.io.IOException;
import util.Point2I;
import util.Direction;
import los.ShadowCasting;
import los.IFovBoard;

// TODO
// Opening a door with fire behind it
// Can move in flamed area if already in it
// Can only activate a marine once
// Flamer self-destruct
// Move and shoot

public abstract class Game
{
    protected Random r_ = new Random();
    private Map map_;
    private CommandPoints cp_ = new CommandPoints(r_);
    private GameListener gl_ = null;
    private StealerAI ai_ = new StealerAI(this);
    protected Vector marines_ = new Vector();
    private Vector blips_ = new Vector();
    private Vector stealers_ = new Vector();
    protected int blipsPerTurn_;
    private int blipsPerEntry_;
    protected int round_ = 0;
    protected int stealerKills_ = 0;
    protected int marineExits_ = 0;
    protected int bulkHeads_ = 0;

    public Game(String map)
        throws IOException
    {
        map_ = new Map(map);
        blipsPerTurn_ = map_.getBlipsPerTurn();
        blipsPerEntry_ = map_.getBlipsPerEntry();

        for (int y = map_.getMinY(); y <= map_.getMaxY(); ++y)
        {
            for (int x = map_.getMinX(); x <= map_.getMaxX(); ++x)
            {
                Piece p = map_.getPiece(x, y);

                if (p instanceof Blip)
                    blips_.addElement(p);
                else if (p != null)
                    util.Debug.error("Unexpected piece");

                int o = map_.getObject(x, y);
                if ((o & TileType.TYPE_MASK) == TileType.BULKHEAD)
                    ++bulkHeads_;
            }
        }

        placeMarines(0);
        placeMarines(1);
    }

    public void start()
    {
        createBlip(map_.getInitialBlips());
        if (map_.getInitialMoveStealers())
            doStealerTurn();
    }

    Vector getBlips()
    {
        return blips_;
    }

    Vector getStealers()
    {
        return stealers_;
    }

    GameListener getListener()
    {
        return gl_;
    }

    public void setListener(GameListener gl)
    {
        gl_ = gl;
    }

    public abstract String getName();

    public Map getMap()
    {
        return map_;
    }

    StealerAI getAI()
    {
        return ai_;
    }

    public CommandPoints getCommandPoints()
    {
        return cp_;
    }

    private void createMarine(Map.MarineStart ms, Map.StartPos sp)
    {
        //util.Debug.message("createMarine: " + ms.name + " " + sp.x + "," + sp.y);
        Marine marine = new Marine(ms.name, ms.type, cp_);
        if (sp.face == Face.NONE)
            marine.setFace(Face.ordered[r_.nextInt(Face.ordered.length)]);
        else
            marine.setFace(sp.face);
        if (ms.carrying != 0)
            marine.setCarrying(ms.carrying);
        marine.setPos(sp.x, sp.y);
        map_.placePiece(marine);
        marines_.addElement(marine);
    }

    private void placeMarines(int team)
    {
        //util.Debug.message("placeMarines: " + team);
        Vector startPos = getMap().getStartPos(team);
        Vector marineStart = getMap().getMarineStart(team);
        {
            int i = 0;
            while (i < startPos.size())
            {
                Map.StartPos sp = (Map.StartPos) startPos.elementAt(i);
                boolean found = false;
                if (sp.name != null)
                {
                    for (int j = 0; j < marineStart.size(); ++j)
                    {
                        Map.MarineStart ms = (Map.MarineStart) marineStart.elementAt(j);
                        if (sp.name.equals(ms.name))
                        {
                            createMarine(ms, sp);
                            marineStart.removeElementAt(j);
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        util.Debug.error("Game::placeMarines: cannot find " + sp.name);
                }
                if (found)
                    startPos.removeElementAt(i);
                else
                    ++i;
            }
        }
        {
            while (marineStart.size() > 0)
            {
                Map.MarineStart ms = (Map.MarineStart) marineStart.elementAt(0);
                int i = r_.nextInt(startPos.size());
                Map.StartPos sp = (Map.StartPos) startPos.elementAt(i);
                createMarine(ms, sp);
                startPos.removeElementAt(i);
                marineStart.removeElementAt(0);
            }
        }
        util.Debug.assert2(marineStart.size() == 0, "Game::placeMarines " + marineStart.size()  + "marines left to place");
    }

    public Marine getNextMarine(Marine m)
    {
        if (marines_.size() > 0)
        {
            int mi = marines_.indexOf(m);
            int i = mi;
            Marine nm = null;
            do
            {
                ++i;
                if (i >= marines_.size())
                    i = 0;
                nm = (Marine) marines_.elementAt(i);
            } while (i != mi && nm.isDeleted());
            if (nm.isDeleted())
                return null;
            else
                return nm;
        }
        else
            return null;
    }

    public Marine getPrevMarine(Marine m)
    {
        if (marines_.size() > 0)
        {
            int mi = marines_.indexOf(m);
            int i = mi;
            Marine pm = null;
            do
            {
                --i;
                if (i < 0)
                    i = marines_.size() - 1;
                pm = (Marine) marines_.elementAt(i);
            } while (i != mi && pm.isDeleted());
            if (pm.isDeleted())
                return null;
            else
                return pm;
        }
        else
            return null;
    }

    protected void kill(Piece p)
    {
        p.delete(map_);
        if (p instanceof Blip)
        {
            stealerKills_ += r_.nextInt(3) + 1;
            flipBlips();
            if (map_.getObject(p.getPosX(), p.getPosY()) == 0)
                map_.setObject(p.getPosX(), p.getPosY(), TileType.OBJECT_SPLAT);
        }
        if (p instanceof Stealer)
        {
            ++stealerKills_;
            flipBlips();
            if (map_.getObject(p.getPosX(), p.getPosY()) == 0)
                map_.setObject(p.getPosX(), p.getPosY(), TileType.OBJECT_SPLAT);
        }
        else if (p instanceof Marine)
        {
            Marine m = (Marine) p;
            if (m.getCarrying() != 0)
            {
                map_.setItem(m.getPosX(), m.getPosY(), m.getCarrying());
                m.setCarrying(0);
            }
            if (map_.getObject(p.getPosX(), p.getPosY()) == 0)
                map_.setObject(p.getPosX(), p.getPosY(), TileType.OBJECT_BLOOD);
        }
        gl_.pieceDied(p);
    }

    protected void exit(Marine m)
    {
        m.delete(map_);
        ++marineExits_;
    }

    protected void closeCombatHit(Piece a, Piece d)
    {
        gl_.pieceCloseCombatHit(a);
        kill(d);
    }

    protected void closeCombatMiss(Piece a, Piece d)
    {
        gl_.pieceCloseCombatMiss(a);
    }

    protected void flame(int x, int y)
    {
        map_.setFire(x, y);
        Piece p = map_.getPiece(x, y);
        if (p != null && (r_.nextInt(6) + 1) >= 2)
            kill(p);
    }

    protected boolean useObject(Piece p, int x, int y, int object)
    {
        return false;
    }

    protected boolean shootObject(Marine m, int x, int y, int object)
    {
        return false;
    }

    private void closeCombat(Piece a, Piece d, int dir)
    {
        util.Debug.assert2(!(a instanceof Blip), "Game::closeCombat blips cannot attack");
        util.Debug.assert2(!(d instanceof Blip), "Game::closeCombat blips cannot be attacked");
        util.Debug.assert2(dir == Direction.getDirection(a.getPosX(), a.getPosY(), d.getPosX(), d.getPosY()), "Game::closeCombat dir is invalid");
        util.Debug.assert2(Face.isForward(dir, a.getFace()), "Game::closeCombat Defender must be in front of Attacker");

        int odir = Direction.flip(dir);
        util.Debug.assert2(odir == Direction.getDirection(d.getPosX(), d.getPosY(), a.getPosX(), a.getPosY()),
            "Game::closeCombat odir is invalid " + odir + " " + Direction.getDirection(d.getPosX(), d.getPosY(), a.getPosX(), a.getPosY()));

        gl_.pieceCloseCombat(a);

        int av = a.getCloseCombatValue(r_);
        int dv = d.getCloseCombatValue(r_);

        if (av > dv)
        {
            //util.Debug.message("Attacker destroys Defender");
            closeCombatHit(a, d);
        }
        else if (dv > av)
        {
            if (Face.isForward(odir, d.getFace()))
            {
                //util.Debug.message("Defender destroys Attacker");
                closeCombatHit(d, a);
            }
            else
            {
                d.setFace(Face.getFace(odir));
                closeCombatMiss(d, a);
            }
        }
        else
        {
            if (!Face.isForward(odir, d.getFace()))
                d.setFace(Face.getFace(odir));
            closeCombatMiss(a, d);
        }
    }

    public boolean toggleDoor(Marine m, int dir)
    {
        boolean changed = false;
        int x = m.getPosX() + Direction.getOffsetX(dir);
        int y = m.getPosY() + Direction.getOffsetY(dir);
        if (!m.getOverwatch()
            && Face.isForward(dir, m.getFace())
            && map_.getPiece(x, y) == null)
        {
            int o = map_.getObject(x, y);
            int tileType = (o & TileType.TYPE_MASK);
            if (tileType == TileType.DOOR || tileType == TileType.BULKHEAD)
            {
                DoorState ds = DoorState.getState(tileType, o);
                if (ds == DoorState.OPEN || (tileType != TileType.BULKHEAD && ds == DoorState.CLOSED))
                {
                    if (m.useActionPoints(1))
                    {
                        changed = true;
                        map_.toggleDoor(x, y, gl_);
                        if ((o & TileType.TYPE_MASK) == TileType.BULKHEAD)
                            --bulkHeads_;
                    }
                }
            }
        }
        return changed;
    }

    protected boolean testDrop(Marine m, int object)
    {
        return true;
    }

    public void drop(Marine m)
    {
        int object = m.getCarrying();
        if (object != 0 && map_.getItem(m.getPosX(), m.getPosY()) == 0 && !m.getOverwatch())
        {
            if (testDrop(m, object))
            {
                map_.setItem(m.getPosX(), m.getPosY(), object);
                m.setCarrying(0);
            }
        }
    }

    protected boolean testPickup(Marine m, int object)
    {
        return true;
    }

    public void pickup(Marine m)
    {
        int object = map_.getItem(m.getPosX(), m.getPosY());
        if (m.getCarrying() == 0 && object != 0 && !m.getOverwatch())
        {
            if (testPickup(m, object))
            {
                m.setCarrying(map_.getItem(m.getPosX(), m.getPosY()));
                map_.setItem(m.getPosX(), m.getPosY(), 0);
            }
        }
    }

    public boolean canTake(Marine m)
    {
        return map_.findToTake(m.getPosX(), m.getPosY()) != null;
    }

    public void take(Marine m)
    {
        Marine om = map_.findToTake(m.getPosX(), m.getPosY());
        int object = m.getCarrying();
        int oobject = om.getCarrying();
        m.setCarrying(oobject);
        om.setCarrying(object);
    }

    public void toggleOverwatch(Marine m)
    {
        if (m.getOverwatch())
        {
            m.setOverwatch(false);
        }
        else if (!m.getJammed() && m.getType() != Marine.FLAMER)
        {
            if (m.useActionPoints(2))
                m.setOverwatch(true);
        }
    }

    public void clearJammed(Marine m)
    {
        if (m.getJammed())
        {
            if (m.useActionPoints(1))
                m.clearJammed();
        }
    }

    public void shoot(Marine m, int x, int y)
    {
        if (!m.getJammed() && !m.getOverwatch())
        {
            if (m.getType() == Marine.FLAMER)
            {
                int dir = Direction.getDirection(m.getPosX(), m.getPosY(), x, y);
                if (Face.isForward(dir, m.getFace()) && m.getAmmunition() >= 1 && m.useActionPoints(2))
                {
                    //util.Debug.message("Game::shoot Flamer " + x + " " + y);
                    m.useAmmunition();
                    double angle = ShadowCasting.angle(y - m.getPosY(), x - m.getPosX());
                    ShadowCasting sc = new ShadowCasting();
                    IFovBoard b = new IFovBoard()
                        {
                            public boolean isObstacle(int x, int y)
                            {
                                return !map_.canMove(x, y, true, false, false);
                            }

                            public boolean contains(int x, int y)
                            {
                                return map_.isValid(x, y);
                            }

                            public void visit(int x, int y)
                            {
                                //util.Debug.message("Game::shoot Flamer visit " + x + " " + y);
                                if (!isObstacle(x, y))
                                {
                                    flame(x, y);
                                }
                            }
                        };
                    //util.Debug.message("Game::shoot Flamer m " + m.getPosX() + " " + m.getPosY());
                    //util.Debug.message("Game::shoot Flamer s " + x + " " + x);
                    //util.Debug.message("Game::shoot Flamer " + angle);
                    sc.visitConeFieldOfView(b, m.getPosX(), m.getPosY(), 6, angle - 10, angle + 10);
                    gl_.pieceShoots(m);
                }
            }
            else if (map_.existsLineOfSight(m, x, y, true))
            {
                Piece p = map_.getPiece(x, y);
                int object = map_.getObject(x, y);
                if (p instanceof Stealer)
                {
                    if (m.useActionPoints(1))
                    {
                        m.setOverwatch(false);
                        m.setShoot(true);
                        gl_.pieceShoots(m);
                        m.setTarget(p);
                        if (m.getShootValue(r_, false, gl_) >= 6)
                        {
                            //util.Debug.message("Marine shoots Stealer");
                            kill(p);
                        }
                        else
                            gl_.pieceShootsMiss(m);
                        m.setShoot(false);
                    }
                }
                else if (DoorState.getState(TileType.DOOR, object) == DoorState.CLOSED)
                {
                    if (m.useActionPoints(1))
                    {
                        m.setOverwatch(false);
                        m.setShoot(true);
                        gl_.pieceShoots(m);
                        m.setTarget(null);
                        // TODO Sustained fire on doors ???
                        if (m.getShootValue(r_, false, gl_) >= 6)
                        {
                            //util.Debug.message("Marine shoots door");
                            map_.blastDoor(x, y, gl_);
                            flipBlips();
                        }
                        else
                            gl_.pieceShootsMissDoor(m);
                        m.setShoot(false);
                    }
                }
                else if (shootObject(m, x, y, object))
                {
                    // done
                }
            }
        }
    }

    public void move(Marine m, int dir, boolean preferTurn)
    {
        int x = m.getPosX() + Direction.getOffsetX(dir);
        int y = m.getPosY() + Direction.getOffsetY(dir);

        //util.Debug.message("Game::move marine " + m.getPosX() + ", " + m.getPosY() + "  " + x + ", " + y);

        if (Face.isForward(dir, m.getFace()))
        {
            //util.Debug.message("Game::move marine move forward");
            Piece p = map_.getPiece(x, y);
            int object = map_.getObject(x, y);
            if (p instanceof Stealer)
            {
                if (m.useActionPoints(1))
                {
                    m.setOverwatch(false);
                    m.setTarget(null);
                    closeCombat(m, p, dir);
                }
            }
            else if (DoorState.getState(TileType.DOOR, object) == DoorState.CLOSED)
            {
                if (m.useActionPoints(1))
                {
                    m.setOverwatch(false);
                    m.setTarget(null);
                    map_.openDoor(x, y, gl_);
                    flipBlips();
                }
            }
            else if (useObject(m, x, y, object))
            {
                // done
            }
            else if ((map_.getTile(x, y) & TileType.TYPE_MASK) == TileType.EXIT)
            {
                if (m.useActionPoints(1))
                    exit(m);
            }
            else if (map_.canMove(x, y, true, true, true))
            {
                boolean canmove = true;
                switch (dir)
                {
                case Direction.NORTH_EAST:
                case Direction.SOUTH_EAST:
                case Direction.SOUTH_WEST:
                case Direction.NORTH_WEST:
                    canmove = canmove && (map_.canMove(x - Direction.getOffsetX(dir), y, true, true, true)
                        || map_.canMove(x, y - Direction.getOffsetY(dir), true, true, true));
                    break;
                }
                if (canmove)
                {
                    if (m.useActionPoints(1))
                    {
                        m.setOverwatch(false);
                        m.setTarget(null);
                        m.move(map_, x, y, gl_);
                        flipBlips();
                    }
                }
            }
        }
        else if (Face.isBackward(dir, m.getFace()))
        {
            //util.Debug.message("Game::move marine move backward");
            if (!preferTurn && map_.canMove(x, y, true, true, true))
            {
                boolean canmove = true;
                switch (dir)
                {
                case Direction.NORTH_EAST:
                case Direction.SOUTH_EAST:
                case Direction.SOUTH_WEST:
                case Direction.NORTH_WEST:
                    canmove = canmove && (map_.canMove(x - Direction.getOffsetX(dir), y, true, true, true)
                        || map_.canMove(x, y - Direction.getOffsetY(dir), true, true, true));
                    break;
                }
                if (canmove)
                {
                    if (m.useActionPoints(2))
                    {
                        m.setOverwatch(false);
                        m.setTarget(null);
                        m.move(map_, x, y, gl_);
                        flipBlips();
                    }
                }
            }
            else if (m.useActionPoints(2))
            {
                m.setOverwatch(false);
                m.setTarget(null);
                m.setFace(m.getFace().flip());
                flipBlips();
                gl_.pieceMoved(m);
            }
        }
        else if (Face.isRight(dir, m.getFace()))
        {
            //util.Debug.message("Game::move marine turn right");
            if (m.useActionPoints(1))
            {
                m.setOverwatch(false);
                m.setTarget(null);
                m.setFace(m.getFace().right());
                flipBlips();
                gl_.pieceMoved(m);
            }
        }
        else if (Face.isLeft(dir, m.getFace()))
        {
            //util.Debug.message("Game::move marine turn left");
            if (m.useActionPoints(1))
            {
                m.setOverwatch(false);
                m.setTarget(null);
                m.setFace(m.getFace().left());
                flipBlips();
                gl_.pieceMoved(m);
            }
        }
    }

    private void doOverwatch(Stealer s)
    {
        for (int i = 0; i < marines_.size(); ++i)
        {
            Marine m = (Marine) marines_.elementAt(i);
            if (m.getType() != Marine.FLAMER && (m.getOverwatch() || cp_.get() >= 1) && !m.getJammed())
            {
                //util.Debug.message("+Game::doOverwatch " + m.getName());
                Vector path = map_.getLineOfSight(m.getPosX(), m.getPosY(), s.getPosX(), s.getPosY());
                if (path != null && path.size() > 1 && path.size() <= 12)
                {
                    //util.Debug.message("Game::doOverwatch foundpath");
                    Point2I p = (Point2I) path.elementAt(1);
                    int dir = Direction.getDirection(m.getPosX(), m.getPosY(), p.x, p.y);
                    util.Debug.assert2(dir != Direction.NONE, "Invalid direction");
                    if (Face.isForward(dir, m.getFace()))
                    {
                        if (m.getOverwatch() || m.useActionPoints(1))
                        {
                            m.setShoot(true);
                            gl_.pieceShoots(m);
                            m.setTarget(null);
                            if (m.getShootValue(r_, m.getOverwatch(), gl_) >= 6)
                            {
                                //util.Debug.message("Marine shoots Stealer");
                                kill(s);
                            }
                            else
                                gl_.pieceShootsMiss(m);
                            if (m.getJammed())
                                m.setOverwatch(false);
                            m.setShoot(false);
                        }
                    }
                }
            }
            //util.Debug.message("-Game::doOverwatch " + x + " " + y);
        }
    }

    boolean move(Stealer s, int dir, boolean preferTurn)
    {
        boolean moved = false;
        int x = s.getPosX() + Direction.getOffsetX(dir);
        int y = s.getPosY() + Direction.getOffsetY(dir);

        //util.Debug.message("Game::move stealer " + s.getPosX() + ", " + s.getPosY() + "  " + x + ", " + y);

        if (Face.isForward(dir, s.getFace()))
        {
            //util.Debug.message("Game::move stealer move forward");
            int object = map_.getObject(x, y);
            Piece p = map_.getPiece(x, y);
            if (p instanceof Marine)
            {
                if (s.useActionPoints(1))
                {
                    moved = true;
                    Marine m = (Marine) p;
                    closeCombat(s, m, dir);
                }
            }
            else if (DoorState.getState(TileType.DOOR, object) == DoorState.CLOSED)
            {
                if (s.useActionPoints(1))
                {
                    gl_.pieceCloseCombat(s);
                    moved = true;
                    int c = s.getCloseCombatValue(r_);
                    if (c >= 6)
                    {
                        map_.blastDoor(x, y, gl_);
                        flipBlips();
                        gl_.pieceCloseCombatObject(s, x, y, object);
                    }
                    else
                        gl_.pieceCloseCombatMiss(s);
                }
            }
            else if (useObject(s, x, y, object))
            {
                // done
                moved = true; // TODO ???
            }
            else if (map_.canMove(x, y, true, true, true))
            {
                boolean canmove = true;
                switch (dir)
                {
                case Direction.NORTH_EAST:
                case Direction.SOUTH_EAST:
                case Direction.SOUTH_WEST:
                case Direction.NORTH_WEST:
                    canmove = canmove && (map_.canMove(x - Direction.getOffsetX(dir), y, true, true, true)
                        || map_.canMove(x, y - Direction.getOffsetY(dir), true, true, true));
                    break;
                }
                if (canmove)
                {
                    if (s.useActionPoints(1))
                    {
                        s.move(map_, x, y, gl_);
                        doOverwatch(s);
                        flipBlips();
                        moved = true;
                    }
                }
            }
        }
        else if (Face.isBackward(dir, s.getFace()))
        {
            //util.Debug.message("Game::move stealer move backward");
            if (!preferTurn && map_.canMove(x, y, true, true, true))
            {
                boolean canmove = true;
                switch (dir)
                {
                case Direction.NORTH_EAST:
                case Direction.SOUTH_EAST:
                case Direction.SOUTH_WEST:
                case Direction.NORTH_WEST:
                    canmove = canmove && (map_.canMove(x - Direction.getOffsetX(dir), y, true, true, true)
                        || map_.canMove(x, y - Direction.getOffsetY(dir), true, true, true));
                    break;
                }
                if (canmove)
                {
                    if (s.useActionPoints(2))
                    {
                        s.move(map_, x, y, gl_);
                        doOverwatch(s);
                        flipBlips();
                        moved = true;
                    }
                }
            }
            else if (s.useActionPoints(1))
            {
                s.setFace(s.getFace().flip());
                moved = true;
                gl_.pieceMoved(s);
            }
        }
        else if (Face.isRight(dir, s.getFace()))
        {
            //util.Debug.message("Game::move stealer turn right");
            if (s.useActionPoints(0))
            {
                s.setFace(s.getFace().right());
                moved = true;
                gl_.pieceMoved(s);
            }
        }
        else if (Face.isLeft(dir, s.getFace()))
        {
            //util.Debug.message("Game::move stealer turn left");
            if (s.useActionPoints(0))
            {
                s.setFace(s.getFace().left());
                moved = true;
                gl_.pieceMoved(s);
            }
        }
        return moved;
    }

    void startMove(Piece p)
    {
        if (map_.getTile(p.getPosX(), p.getPosY()) == 0)
        {
            for (int di = 0; di < Direction.ordered4.length; ++di)
            {
                int dir = Direction.ordered4[di];
                int x = p.getPosX() + Direction.getOffsetX(dir);
                int y = p.getPosY() + Direction.getOffsetY(dir);
                if ((map_.getTile(x, y) & TileType.TYPE_MASK) == TileType.ENTRY)
                {
                    p.move(map_, x, y, gl_);
                    break;
                }
            }
        }
    }

    boolean move(Blip blip, int dir)
    {
        //util.Debug.message("+Game::move blip " + dir);
        boolean moved = false;
        int x = blip.getPosX() + Direction.getOffsetX(dir);
        int y = blip.getPosY() + Direction.getOffsetY(dir);

        int object = map_.getObject(x, y);

        if (DoorState.getState(TileType.DOOR, object) == DoorState.CLOSED)
        {
            if (blip.useActionPoints(1))
            {
                gl_.pieceCloseCombat(blip);
                moved = true;
                int c = blip.getCloseCombatValue(r_);
                if (c >= 6)
                {
                    map_.blastDoor(x, y, gl_);
                    flipBlips();
                    gl_.pieceCloseCombatObject(blip, x, y, object);
                }
                else
                    gl_.pieceCloseCombatMiss(blip);
            }
        }
        else if (useObject(blip, x, y, object))
        {
            // done
            moved = true; // TODO ???
        }
        else if (map_.canMove(x, y, true, true, true))
        {
            boolean canmove = true;
            switch (dir)
            {
            case Direction.NORTH_EAST:
            case Direction.SOUTH_EAST:
            case Direction.SOUTH_WEST:
            case Direction.NORTH_WEST:
                canmove = canmove && (map_.canMove(x - Direction.getOffsetX(dir), y, true, true, true)
                    || map_.canMove(x, y - Direction.getOffsetY(dir), true, true, true));
                break;
            }
            if (canmove)
            {
                if (blip.useActionPoints(1))
                {
                    blip.move(map_, x, y, gl_);
                    moved = true;
                    if (findMarineLineofSight(blip.getPosX(), blip.getPosY(), false) != null)
                        flipBlip(blip, dir);
                }
            }
        }
        return moved;
    }

    private void createBlip(int count)
    {
        //util.Debug.message("+Game::createBlip");
        while (count > 0)
        {
            //util.Debug.message("createBlip " + count);
            Blip blip = map_.createBlip(r_, blipsPerEntry_);
            if (blip == null)
                break;
            //util.Debug.message(" Game::createBlip placing blip " + blip.getPosX() + " " + blip.getPosY());
            map_.placePiece(blip);
            blips_.addElement(blip);
            --count;
            util.Debug.assert2(map_.getPiece(blip.getPosX(), blip.getPosY()) == blip, "Blip not placed");
        }
        //util.Debug.message("-Game::createBlip");
    }

    private Stealer createStealer(int x, int y, int dir)
    {
        Stealer stealer = new Stealer();
        stealer.setPos(x, y);
        stealer.setFace(Face.getFace(dir));
        map_.placePiece(stealer);
        stealers_.addElement(stealer);
        return stealer;
    }

    Stealer[] flipBlip(Blip blip, int lastdir)
    {
        //util.Debug.message("flipBlip " + blip);
        Stealer stealers[] = { null, null, null };
        blip.delete(map_);

        stealers[0] = createStealer(blip.getPosX(), blip.getPosY(), lastdir);

        int count = r_.nextInt(stealers.length);
        if (count > 0)
        {
            // Find free surrounding places
            Vector pos = new Vector();
            for (int di = 0; di < Direction.ordered8p.length; ++di)
            {
                int d = Direction.ordered8p[di];
                int x = blip.getPosX() + Direction.getOffsetX(d);
                int y = blip.getPosY() + Direction.getOffsetY(d);
                if (map_.canMove(x, y, true, true, true))
                    pos.addElement(new Point2I(x, y));
            }

            while (pos.size() > 0 && count > 0)
            {
                int pi = r_.nextInt(pos.size());
                Point2I p = (Point2I) pos.elementAt(pi);
                pos.removeElementAt(pi);

                stealers[count] = createStealer(p.x, p.y, lastdir);
                --count;
            }
        }

        return stealers;
    }

    private void flipBlips()
    {
        //util.Debug.message("flipBlips" + blips_.size());
        for (int i = 0; i < blips_.size(); ++i)
        {
            Blip blip = (Blip) blips_.elementAt(i);
            if (!blip.isDeleted())
            {
                boolean flipped = false;
                for (int di = 0; di < Direction.ordered8.length; ++di)
                {
                    int d = Direction.ordered8[di];
                    Piece p = map_.getPiece(blip.getPosX() + Direction.getOffsetX(d), blip.getPosY() + Direction.getOffsetY(d));
                    if (p instanceof Marine)
                    {
                        flipBlip(blip, d);
                        flipped = true;
                        break;
                    }
                }
                if (!flipped)
                {
                    Vector path = findMarineLineofSight(blip.getPosX(), blip.getPosY(), false);
                    if (path != null)
                    {
                        //util.Debug.message("Game::flipBlips found");
                        Point2I p = (Point2I) path.elementAt(1);
                        int dir = Direction.getDirection(blip.getPosX(), blip.getPosY(), p.x, p.y);
                        util.Debug.assert2(dir != Direction.NONE, "Invalid direction");
                        flipBlip(blip, dir);
                    }
                }
            }
        }
    }

    Vector findMarineLineofSight(int x, int y, boolean fireArc)
    {
        for (int i = 0; i < marines_.size(); ++i)
        {
            //util.Debug.message("+Game::findMarineLineofSight " + x + " " + y);
            Marine m = (Marine) marines_.elementAt(i);
            Vector path = map_.getLineOfSight(x, y, m.getPosX(), m.getPosY());
            if (path != null && path.size() > 1)
            {
                //util.Debug.message("Game::findMarineLineofSight foundpath");
                Point2I p = (Point2I) path.elementAt(path.size() - 2);
                int dir = Direction.getDirection(m.getPosX(), m.getPosY(), p.x, p.y);
                util.Debug.assert2(dir != Direction.NONE, "Invalid direction");
                if (fireArc && Face.isForward(dir, m.getFace()))
                    return path;
                else if (!fireArc && !Face.isBackward(dir, m.getFace()))
                    return path;
            }
            //util.Debug.message("-Game::findMarineLineofSight " + x + " " + y);
        }
        return null;
    }

    private static void cleanup(Vector pieces)
    {
        int i = 0;
        while (i < pieces.size())
        {
            Piece piece = (Piece) pieces.elementAt(i);
            if (piece.isDeleted())
                pieces.removeElementAt(i);
            else
                ++i;
        }
    }

    private static void resetActionPoints(Vector pieces)
    {
        for (int i = 0; i < pieces.size(); ++i)
        {
            Piece piece = (Piece) pieces.elementAt(i);
            piece.resetActionPoints();
            if (piece instanceof Marine)
            {
                Marine m = (Marine) piece;
                m.setOverwatch(false);
            }
        }
    }

    public final static int STATUS_NONE = 0;
    public final static int STATUS_COMPLETED = 1;
    public final static int STATUS_FAILED = 2;
    public final static int STATUS_DRAWN = 3;

    abstract public int getStatus();

    abstract public String getStatusText(int status);

    protected void marineEndTurn()
    {
        for (int i = 0; i < marines_.size(); ++i)
        {
            Marine m = (Marine) marines_.elementAt(i);
            m.clearActionPoints();
        }
    }

    protected void doStealerTurn()
    {
        resetActionPoints(blips_);
        resetActionPoints(stealers_);
        createBlip(blipsPerTurn_);

        ai_.doTurn();
        cleanup(marines_);
        cleanup(blips_);
        cleanup(stealers_);
    }

    public boolean endTurn()
    {
        marineEndTurn();
        cleanup(marines_);
        cleanup(blips_);
        cleanup(stealers_);
        if (getStatus() != STATUS_NONE)
            return true;

        doStealerTurn();
        ++round_;
        if (getStatus() != STATUS_NONE)
            return true;

        resetActionPoints(marines_);
        cp_.reset(r_);
        map_.clearFire();
        return false;
    }
}
