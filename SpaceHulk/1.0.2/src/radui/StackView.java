// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class StackView extends View
{
    private Vector views_ = new Vector();
    
    public void add(View v)
    {
        views_.addElement(v);
    }
    
    public void paint(Graphics g, int w, int h)
    {
        for (int i = 0; i < views_.size(); ++i)
        {
            View v = (View) views_.elementAt(i);
            v.paint(g, w, h);
        }
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
            h = Math.max(h, v.getMaxHeight());
        }
        return h;
    }
    
    public int getHeight(int w)
    {
        int h = 0;
        for (int i = 0; i < views_.size(); ++i)
        {
            View v = (View) views_.elementAt(i);
            h = Math.max(h, v.getHeight(w));
        }
        return h;
    }
}
