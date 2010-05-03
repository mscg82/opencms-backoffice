// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import javax.microedition.lcdui.Graphics;

public class FrameView extends View
{
    private int color_;
    
    public FrameView(int c)
    {
        color_ = c;
    }
    
    public void setColor(int c)
    {
        color_ = c;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        int oldColor = g.getColor();
        g.setColor(color_);
        g.drawRect(0, 0, w - 1, h - 1);
        g.setColor(oldColor);
    }
}
