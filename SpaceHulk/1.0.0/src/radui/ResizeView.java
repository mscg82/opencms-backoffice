// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import javax.microedition.lcdui.Graphics;

public class ResizeView extends View
{
    private View view_ = null;
    private int vw_;
    private int vh_;
    private int anchor_ = Graphics.VCENTER | Graphics.HCENTER;
    
    public ResizeView(int w, int h)
    {
        vw_ = w;
        vh_ = h;
    }
    
    public void setView(View v)
    {
        view_ = v;
    }
    
    public void setAnchor(int a)
    {
        anchor_ = a;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        if (view_ != null)
        {
            int x = getPosX(w);
            int y = getPosY(h);
            int cx = g.getClipX();
            int cy = g.getClipY();
            int cw = g.getClipWidth();
            int ch = g.getClipHeight();
            //System.out.println("ResizeView::paint " + x + " " + y);
            g.translate(x, y);
            g.setClip(0, 0, vw_, vh_);
            view_.paint(g, vw_, vh_);
            g.translate(-x, -y);
            g.setClip(cx, cy, cw, ch);
        }
    }
    
    private int getPosX(int w)
    {
        if ((anchor_ & Graphics.LEFT) == Graphics.LEFT)
            return 0;
        else if ((anchor_ & Graphics.HCENTER) == Graphics.HCENTER)
            return (w - vw_)/2;
        else if ((anchor_ & Graphics.RIGHT) == Graphics.RIGHT)
            return w - vw_;
        else
            return 0;
    }
    
    private int getPosY(int h)
    {
        if ((anchor_ & Graphics.TOP) == Graphics.TOP)
            return 0;
        else if ((anchor_ & Graphics.VCENTER) == Graphics.VCENTER)
            return (h - vh_)/2;
        else if ((anchor_ & Graphics.BOTTOM) == Graphics.BOTTOM)
            return h - vh_;
        else
            return 0;
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
        return vw_;
    }
    
    public int getMaxHeight()
    {
        return vh_;
    }
}
