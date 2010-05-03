// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import javax.microedition.lcdui.Graphics;

public class BorderView extends View
{
    private View view_ = null;
    private int bx_;
    private int by_;
    
    public BorderView(View v, int bx, int by)
    {
        view_ = v;
        bx_ = bx;
        by_ = by;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        if (view_ != null)
        {
            int x = bx_;
            int y = by_;
            g.translate(x, y);
            // TODO Setup clipping?
            view_.paint(g, w - 2 * bx_, h - 2 * by_);
            g.translate(-x, -y);
        }
    }
    
    public void keyPressed(ScreenCanvas sc, int keyCode)
    {
        if (view_ != null)
            view_.keyPressed(sc, keyCode);
    }

    public void keyRepeated(ScreenCanvas sc, int keyCode)
    {
        if (view_ != null)
            view_.keyRepeated(sc, keyCode);
    }
    
    public int getMaxWidth()
    {
        int w = 2 * bx_;
        if (view_ != null)
            w += view_.getMaxWidth();
        return w;
    }
    
    public int getMaxHeight()
    {
        int h = 2 * by_;
        if (view_ != null)
            h += view_.getMaxHeight();
        return h;
    }
}
