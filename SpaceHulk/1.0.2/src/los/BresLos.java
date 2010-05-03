// Orignally taken from http://rlforj.sourceforge.net
// Modified by Adam Gates

package los;

import java.util.Vector;

import util.Point2I;
import util.BresenhamLine;

public class BresLos
{
    boolean symmetricEnabled = false;

    private Vector path;

    public BresLos(boolean symmetric)
    {
        symmetricEnabled = symmetric;
    }

    public boolean existsLineOfSight(ILosBoard b, int startX, int startY,
            int x1, int y1, boolean calculateProject)
    {
        int dx = startX - x1, dy = startY - y1;
        int adx = dx > 0 ? dx : -dx, ady = dy > 0 ? dy : -dy;
        int len = (adx > ady ? adx : ady) + 1;

        int[] px = new int[len], py = new int[len];

        if (calculateProject)
            path = new Vector(len);

        BresenhamLine.plot(startX, startY, x1, y1, px, py);

        boolean los = true;
        for (int i = 0; i < len; i++)
        {
            if (calculateProject)
                path.addElement(new Point2I(px[i], py[i]));
            if (b.isObstacle(px[i], py[i]))
            {
                los = false;
                break;
            }
        }
        if (!los && symmetricEnabled)
        {
            Vector oldpath = path;
            if (calculateProject)
                path = new Vector(len);

            BresenhamLine.plot(x1, y1, startX, startY, px, py);

            los = true;
            for (int i = len - 1; i > -1; i--)
            {
                if (calculateProject)
                    path.addElement(new Point2I(px[i], py[i]));
                if (b.isObstacle(px[i], py[i]))
                {
                    los = false;
                    break;
                }
            }

            if (calculateProject)
                path = oldpath.size() > path.size() ? oldpath : path;
        }

        return los;
    }

    public Vector getProjectPath()
    {
        return path;
    }
}
