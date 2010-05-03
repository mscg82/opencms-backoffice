// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package path;

import java.util.Hashtable;
import java.util.Vector;

import util.Direction;
import util.Point2I;

public class AStar
{
    private Point2I dest_;
    private IPathBoard board_;
    private Hashtable visited_;
    private Vector next_;
    
    static class Cell
    {
        Point2I pt;
        int cost;
        
        Cell(Point2I pt, int dist)
        {
            this.pt = pt;
            //this.cost = cost;
        }
    }
    
    public boolean findPath(int sx, int sy, IPathBoard b)
    {
        if (b.isEnd(sx, sy))
            return false;
            
        board_ = b;
        dest_ = null;
        visited_ = new Hashtable();
        next_ = new Vector();

        Point2I src = new Point2I(sx, sy);
        visited_.put(src, new Cell(null, 0));
        addNext(new Cell(src, 0));
        
        while (!next_.isEmpty())
        {
            if (next())
                return true;
        }
        return false;
    }
    
    public Vector getPath()
    {
        Vector path = new Vector();
        
        Point2I p = dest_;
        while (true)
        {
            Cell c = (Cell) visited_.get(p);
            if (c == null)
            {
                util.Debug.error("AStar::getPath null visited cell");
                return null;
            }
            else if (c.pt == null)
                break;
            path.insertElementAt(p, 0);
            p = c.pt;
        }
        
        return path;
    }
    
    private boolean next()
    {
        Cell n = popNext();

        for (int di = 0; di < Direction.ordered8p.length; ++di)
        {
            int d = Direction.ordered8p[di];
            Point2I p = new Point2I(n.pt.x + Direction.getOffsetX(d), n.pt.y + Direction.getOffsetY(d));
            Cell vc = (Cell) visited_.get(p);
            int cost = n.cost + getCost(p.x, p.y, d);
            if (vc == null || vc.cost > cost)
            {
                if (board_.isObstacle(p.x, p.y))
                {
                    if (board_.isEnd(p.x, p.y))
                    {
                        visited_.put(p, new Cell(n.pt, cost));
                        dest_ = p;
                        return true; // TODO false or true ???
                    }
                }
                else
                {
                    visited_.put(p, new Cell(n.pt, cost));
                    addNext(new Cell(p, cost));
                    if (board_.isEnd(p.x, p.y))
                    {
                        dest_ = p;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Cell popNext()
    {
        Cell n = (Cell) next_.elementAt(0);
        next_.removeElementAt(0);
        return n;
    }

    private void addNext(Cell nc)
    {
        for (int i = 0; i < next_.size(); ++i)
        {
            Cell c = (Cell) next_.elementAt(i);
            if (nc.cost < c.cost)
            {
                next_.insertElementAt(nc, i);
                return;
            }
        }
        next_.addElement(nc);
    }

    private int getCost(int x, int y, int dir)
    {
        switch (dir)
        {
        case Direction.NORTH_EAST:
        case Direction.SOUTH_EAST:
        case Direction.SOUTH_WEST:
        case Direction.NORTH_WEST:
            return 14;

        default:
            return 10;
        }
    }
}
