// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package util;

public class ComparableComparator implements Comparator
{
    public int compare(Object l, Object r)
    {
        return ((Comparable) l).compareTo(r);
    }
}
