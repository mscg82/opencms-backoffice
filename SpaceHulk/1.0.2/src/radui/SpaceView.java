// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;


public class SpaceView extends View
{
    private int vw_;
    private int vh_;
    
    public SpaceView(int w, int h)
    {
        vw_ = w;
        vh_ = h;
    }
    
    public int getMaxWidth()
    {
        return vw_;
    }
    
    public int getMaxHeight()
    {
        return vh_;
    }
}
