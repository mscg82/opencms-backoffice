// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.IOException;

import util.Direction;

// TODO
// Pass/pickup cat: 1 CAT damaged, 2 dropped ???

public class GameSin3 extends Game
{
    public final static String NAME = "Rescue";
    public final static String INTRO = "/maps/13.html";
    private boolean escapedWithCAT_ = false;
    private int catHP_ = 2;

    public GameSin3()
        throws IOException
    {
        super("/maps/13.shmap");

        getAI().setOverFear(1);
        getAI().setTargetObject(TileType.OBJECT_CAT);

        // This map has two starting locations for each team
        // TODO Randomly use a starting pos
    }

    public String getName()
    {
        return NAME;
    }

    private boolean catHP(int x, int y, int d)
    {
        catHP_ -= d;
        if (catHP_ <= 0)
        {
            catHP_ = 0;
            getListener().objectDestroyed(x, y, TileType.OBJECT_CAT);
            return true;
        }
        else
        {
            getListener().objectDamaged(x, y, TileType.OBJECT_CAT);
            return false;
        }
    }

    public void flame(int x, int y)
    {
        super.flame(x, y);
        if (getMap().getItem(x, y) == TileType.OBJECT_CAT)
        {
            catHP(x, y, 2);
            getMap().setItem(x, y, 0);
        }
        else
        {
            Piece p = getMap().getPiece(x, y);
            if (p instanceof Marine)
            {
                Marine m = (Marine) p;
                if (m.getCarrying() == TileType.OBJECT_CAT)
                {
                    catHP(x, y, 2);
                    m.setCarrying(0);
                }
            }
        }
    }

    protected void kill(Piece p)
    {
        if (p instanceof Marine)
        {
            Marine m = (Marine) p;
            if (m.getCarrying() == TileType.OBJECT_CAT)
            {
                switch (r_.nextInt(6) + 1)
                {
                case 1:
                    if (catHP(m.getPosX(), m.getPosY(), 2))
                        m.setCarrying(0);
                    break;

                case 2:
                    if (catHP(m.getPosX(), m.getPosY(), 1))
                        m.setCarrying(0);
                    break;
                }
            }
        }
        super.kill(p);
    }

    protected boolean testPickup(Marine m, int object)
    {
        // TODO How many points to drop ???
        if (object == TileType.OBJECT_CAT)
            return m.useActionPoints(1);
        else
            return super.testPickup(m, object);
    }

    public void take(Marine m)
    {
        if (m.useActionPoints(1))
            super.take(m);
    }

    protected void closeCombatMiss(Piece a, Piece d)
    {
        Marine m = null;
        if (a instanceof Marine)
            m = (Marine) a;
        if (d instanceof Marine)
            m = (Marine) d;
        if (m != null && m.getCarrying() == TileType.OBJECT_CAT)
        {
            switch (r_.nextInt(6) + 1)
            {
            case 1:
                if (catHP(m.getPosX(), m.getPosY(), 2))
                    m.setCarrying(0);
                break;

            case 2:
                // CAT dropped
                getMap().setItem(m.getPosX(), m.getPosY(), TileType.OBJECT_CAT);
                m.setCarrying(0);
                getListener().objectDropped(m, TileType.OBJECT_CAT);
                break;
            }
        }
        super.closeCombatMiss(a, d);
    }

    protected void exit(Marine m)
    {
        if (m.getCarrying() == TileType.OBJECT_CAT)
            escapedWithCAT_ = true;
        super.exit(m);
    }

    public int getStatus()
    {
        if (escapedWithCAT_)
            if (catHP_ <= 1)
                return STATUS_DRAWN;
            else
                return STATUS_COMPLETED;
        else if (marines_.size() <= 0 || catHP_ <= 0)
            return STATUS_FAILED;
        else
            return STATUS_NONE;
    }

    public String getStatusText(int status)
    {
        switch (status)
        {
        case STATUS_NONE:
            return "Escape with C.A.T.";

        case STATUS_COMPLETED:
            return "The vital data in the C.A.T. unit has been retreived. Well done. This information will save many Marine lives.";

        case STATUS_DRAWN:
            return "The C.A.T. has been damaged. Lets hope the vital data in the C.A.T. unit can be retreived. This information could save many Marine lives.";

        case STATUS_FAILED:
            return "The loss of the C.A.T. unit and its accumulated data will have dire consequences for the Blood Angels. Without that information we have no hope of cleansing this hulk.";

        default:
            return null;
        }
    }

    protected void marineEndTurn()
    {
        if (catHP_ >= 2)
        {
            for (int y = getMap().getMinY(); y <= getMap().getMaxY(); ++y)
            {
                for (int x = getMap().getMinX(); x <= getMap().getMaxX(); ++x)
                {
                    int item = getMap().getItem(x, y);
                    if (item == TileType.OBJECT_CAT)
                    {
                        for (int j = 0; j < 3; ++j)
                        {
                            // TODO Turn image
                            // TODO animate movement ie call GameListener
                            int di = r_.nextInt(Direction.ordered8.length);
                            int dir = Direction.ordered8[di];
                            int nx = x + Direction.getOffsetX(dir);
                            int ny = y + Direction.getOffsetY(dir);
                            if (getMap().canMove(nx, ny, true, true, false) && getMap().getItem(nx, ny) == 0)
                            {
                                getMap().setItem(nx, ny, item);
                                getMap().setItem(x, y, 0);
                            }
                        }
                    }
                }
            }
        }
        super.marineEndTurn();
    }
}
