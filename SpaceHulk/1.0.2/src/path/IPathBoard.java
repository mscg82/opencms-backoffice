// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package path;

public interface IPathBoard
{
    public boolean isObstacle(int x, int y);
    public boolean isEnd(int x, int y);
}
