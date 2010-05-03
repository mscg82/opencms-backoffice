// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class VerticalView extends View
{
    private Vector views_ = new Vector();
    
    public void add(View v)
    {
        views_.addElement(v);
    }
    
    public void paint(Graphics g, int w, int h)
    {
        //util.Debug.message("VerticalView::paint " + w + " " + h);
        //util.Debug.message("VerticalView::paint size " + views_.size());
        int tx = g.getTranslateX();
        int ty = g.getTranslateY();
        int cx = g.getClipX();
        int cy = g.getClipY();
        int cw = g.getClipWidth();
        int ch = g.getClipHeight();
        //util.Debug.message("VerticalView::paint trans " + tx + " " + ty);
        //util.Debug.message("VerticalView::paint clip " + cy + " " + ch);
        int y = 0;
        for (int i = 0; i < views_.size(); ++i)
        {
            View v = (View) views_.elementAt(i);
            int vh = v.getHeight(w);
            //util.Debug.message("VerticalView::paint vh " + vh);
            if ((y + vh) >= cy)
            {
                //util.Debug.message("VerticalView::paint subpaint " + v.getClass());
                g.setClip(cx, 0, cw, vh);
                v.paint(g, w, vh);
            }
            y += vh;
            g.translate(0, vh);
            if (y > (cy + ch))
                break;
        }
        g.translate(0, -y);
        g.setClip(cx, cy, cw, ch);
        util.Debug.assert2(g.getTranslateX() == tx,
            "VerticalView::paint translation x error " + tx + " " + g.getTranslateX());
        util.Debug.assert2(g.getTranslateY() == ty,
            "VerticalView::paint translation y error " + ty + " " + g.getTranslateY());
    }
    
    public void keyPressed(ScreenCanvas sc, int keyCode)
    {
        for (int i = 0; i < views_.size(); ++i)
        {
            View v = (View) views_.elementAt(i);
            v.keyPressed(sc, keyCode);
        }
    }

    public void keyRepeated(ScreenCanvas sc, int keyCode)
    {
        for (int i = 0; i < views_.size(); ++i)
        {
            View v = (View) views_.elementAt(i);
            v.keyRepeated(sc, keyCode);
        }
    }

    public int getMaxWidth()
    {
        int w = 0;
        for (int i = 0; i < views_.size(); ++i)
        {
            View v = (View) views_.elementAt(i);
            w = Math.max(w, v.getMaxWidth());
        }
        return w;
    }
    
    public int getMaxHeight()
    {
        int h = 0;
        for (int i = 0; i < views_.size(); ++i)
        {
            View v = (View) views_.elementAt(i);
            h += v.getMaxHeight();
        }
        return h;
    }

    public int getHeight(int w)
    {
        int h = 0;
        for (int i = 0; i < views_.size(); ++i)
        {
            View v = (View) views_.elementAt(i);
            h += v.getHeight(w);
        }
        return h;
    }
}
