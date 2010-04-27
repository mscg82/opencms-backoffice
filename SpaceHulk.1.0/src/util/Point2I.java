// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package util;

public class Point2I
{
    public int x;
    public int y;
    
	public Point2I(int x, int y)
	{
		this.x=x; this.y=y;
	}

	public boolean equals(Object o)
	{
        Point2I p = (Point2I) o;
        if (p != null)
            return x == p.x && y == p.y;
        else
            return false;
	}

    public double distance(int ox, int oy)
    {
        return Math.sqrt((ox - x) * (ox - x) + (oy - y) * (oy - y));
    }
	
	public int hashCode()
	{
		return x<<7-x+y;//x*prime+y
	}
	
	public String toString()
	{
		return "Point2I[ "+x+", "+y+" ]";
	}
}
