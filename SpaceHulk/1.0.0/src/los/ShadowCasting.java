// Orignally taken from http://rlforj.sourceforge.net
// Modified by Adam Gates

package los;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import util.Float11;
import util.Comparable;
import util.ComparableComparator;

/**
 * Code adapted from NG roguelike engine http://roguelike-eng.sourceforge.net/
 * 
 * Recursive line-of-sight class implementing a spiraling shadow-casting
 * algorithm. This algorithm chosen because it can establish line-of-sight by
 * visiting each grid at most once, and is (for me) much simpler to implement
 * than octant oriented or non-recursive approaches. -TSS
 * 
 * @author sdatta
 */
public class ShadowCasting
{
    public void visitFieldOfView(IFovBoard b, int x, int y, int distance)
    {
        if (b == null)
            throw new IllegalArgumentException();
        if (distance < 1)
            throw new IllegalArgumentException();

        go(b, x, y, 1, distance, 0.0, 360.0);
    }

    private static void go(IFovBoard board, int cx, int cy, int r, int maxDistance,
        double th1, double th2)
    {
        //System.out.println("go r " + r + " c " + cx + " " + cy);
        //System.out.println("go th1 " + th1 + " th2 " + th2);
        if (r > maxDistance)
            throw new IllegalArgumentException();
        if (r <= 0)
            throw new IllegalArgumentException();

        ArcPoint[] circle = getCircle(r);
        boolean wasObstacle = false;
        boolean foundClear = false;
        for (int i = 0; i < circle.length; i++)
        {
            ArcPoint arcPoint = circle[i];
            int px = cx + arcPoint.x;
            int py = cy + arcPoint.y;
            //System.out.println("go pc " + px + " " + py);

            // if outside the board, ignore it and move to the next one
            if (!board.contains(px, py))
            {
                wasObstacle = true;
                continue;
            }

            if (arcPoint.lagging < th1 && arcPoint.theta != th1
                    && arcPoint.theta != th2)
            {
                //System.out.println("go < than " + arcPoint);
                continue;
            }
            if (arcPoint.leading > th2 && arcPoint.theta != th1
                    && arcPoint.theta != th2)
            {
                //System.out.println("go > than " + arcPoint);
                continue;
            }
            //System.out.println("go in " + arcPoint);

            // Accept this point
            board.visit(px, py);

            // Check to see if we have an obstacle here
            boolean isObstacle = board.isObstacle(px, py);

            // If obstacle is encountered, we start a new run from our start
            // theta
            // to the rightTheta of the current point at radius+1
            // We then proceed to the next non-obstacle, whose leftTheta
            // becomes
            // our new start theta
            // If the last point is an obstacle, we do not start a new Run
            // at the
            // end.
            if (isObstacle)
            {
                // keep going
                if (wasObstacle)
                {
                    continue;
                }

                // start a new run from our start to this point's right side
                else if (foundClear)
                {
                    double runEndTheta = arcPoint.leading;
                    double runStartTheta = th1;
                    // System.out.println("Spawn obstacle at " + arcPoint);
                    if (r < maxDistance)
                        go(board, cx, cy, r + 1, maxDistance,
                            runStartTheta, runEndTheta);
                    wasObstacle = true;
                    // System.out.println("Continuing..." + (runs++) + ": "
                    // + r + "," + (int)(th1) +
                    // ":" + (int)(th2));
                }
                else
                {
                    if (arcPoint.theta == 0.0)
                    {
                        th1 = 0.0;
                    }
                    else
                    {
                        th1 = arcPoint.leading;
                    }
                    //System.out.println("Adjusting start for obstacle "+th1+" at " + arcPoint);
                }
            }
            else
            {
                foundClear = true;
                // we're clear of obstacle; any runs propogated from this
                // run starts at this
                // point's leftTheta
                if (wasObstacle)
                {
                    ArcPoint last = circle[i - 1];
                    // if (last.theta == 0.0) {
                    // th1 = 0.0;
                    // }
                    // else {
                    th1 = last.lagging;
                    // }

                    //System.out.println("Adjusting start for clear of obstacle "+th1+" at " + arcPoint);

                    wasObstacle = false;
                }
                else
                {
                    wasObstacle = false;
                    continue;
                }
            }
            wasObstacle = isObstacle;
        }

        if (!wasObstacle && r < maxDistance)
        {
            go(board, cx, cy, r + 1, maxDistance, th1, th2);
        }
    }

    public void visitConeFieldOfView(IFovBoard b, int x, int y, int distance,
            double startAngle, double finishAngle)
    {
        startAngle = normalizeAngle(startAngle);
        finishAngle = normalizeAngle(finishAngle);
        //System.out.println("visitConeFieldOfView " + startAngle + " " + finishAngle);
        
        if (b == null)
            throw new IllegalArgumentException();
        if (distance < 1)
            throw new IllegalArgumentException();

        if(startAngle>finishAngle) {
            go(b, x, y, 1, distance, startAngle, 360.0);
            go(b, x, y, 1, distance, 0.0, finishAngle);
        }
        else
            go(b, x, y, 1, distance, startAngle, finishAngle);
    }

    // TODO move somewhere
    public static double angle(double y, double x)
    {
        double a = 360.0 - Math.toDegrees(Float11.atan2(y, x));
        return normalizeAngle(a);
    }

    public static double normalizeAngle(double a)
    {
        if (a < 0) { a %= 360; a += 360; }
        if (a >= 360)
            a %= 360;
        return a;
    }

    public static double distance(int x, int y)
    {
        return Math.sqrt(x * x + y * y);
    }

    static class ArcPoint implements Comparable
    {
        int x, y;

        double theta;

        double leading;

        double lagging;

        public String toString()
        {
            return "[" + x + "," + y + "=" + (int) (theta) + "/"
                    + (int) (leading) + "/" + (int) (lagging);
        }

        ArcPoint(int dx, int dy)
        {
            this.x = dx;
            this.y = dy;
            theta = angle(y, x);
            // System.out.println(x + "," + y + ", theta=" + theta);
            // top left
            if (x < 0 && y < 0)
            {
                leading = angle(y - 0.5, x + 0.5);
                lagging = angle(y + 0.5, x - 0.5);
            }
            // bottom left
            else if (x < 0)
            {
                leading = angle(y - 0.5, x - 0.5);
                lagging = angle(y + 0.5, x + 0.5);
            }
            // bottom right
            else if (y > 0)
            {
                leading = angle(y + 0.5, x - 0.5);
                lagging = angle(y - 0.5, x + 0.5);
            }
            // top right
            else
            {
                leading = angle(y + 0.5, x + 0.5);
                lagging = angle(y - 0.5, x - 0.5);
            }
        }

        public int compareTo(Object o)
        {
            ArcPoint oap = (ArcPoint) o;
            if (theta == oap.theta)
                return 0;
            else if (theta > oap.theta)
                return 1;
            else
                return -1;
        }

        public boolean equals(Object o)
        {
            return theta == ((ArcPoint) o).theta;
        }

        public int hashCode()
        {
            return x * y;
        }
    }

    private static final int MAX_CACHED_RADIUS = 6;
    private static Hashtable circles = null;

    static
    {
        circles = new Hashtable();
        //long t1 = System.currentTimeMillis();

        for (int i = -MAX_CACHED_RADIUS; i <= MAX_CACHED_RADIUS; i++)
        {
            for (int j = -MAX_CACHED_RADIUS; j <= MAX_CACHED_RADIUS; j++)
            {
                int distance = (int) Math.floor(distance(i, j));

                // If filled, add anything where Math.floor(distance) <= radius
                // If not filled, require that Math.floor(distance) == radius
                if (distance <= MAX_CACHED_RADIUS)
                {
                    Integer key = new Integer(distance);
                    Vector circ = (Vector) circles.get(key);
                    if (circ == null)
                    {
                        circ = new Vector();
                        circles.put(new Integer(distance), circ);
                    }
                    circ.addElement(new ArcPoint(i, j));
                }
            }
        }

        Enumeration e = circles.keys();
        while (e.hasMoreElements())
        {
            Integer key = (Integer) e.nextElement();
            Vector circ = (Vector) circles.get(key);
            ArcPoint pts[] = new ArcPoint[circ.size()];
            circ.copyInto(pts);
            mindprod.QuickSort.sort(pts, new ComparableComparator());
            // System.out.println("Sorted " + key);
            // for (int i = 0; i < pts.length; ++ i)
            //     System.out.println(pts[i]);
            circles.put(key, pts);
        }
        // Logger.getLogger(ShadowCasting.class.getName()).log(Level.INFO,
        //     "Circles cached after " + (System.currentTimeMillis() - t1));
    }

    private static ArcPoint[] getCircle(int r)
    {
        if (r > MAX_CACHED_RADIUS)
            throw new IllegalArgumentException();
        return (ArcPoint[]) circles.get(new Integer(r));
    }
}
