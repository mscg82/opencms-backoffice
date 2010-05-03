// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import util.MiscUtils;

public class FillView extends View
{
    private int color_;
    private boolean useAlpha_ = false;
    private Image i_ = null;
    
    public FillView(int c)
    {
        color_ = c;
    }

    public FillView(int c, boolean useAlpha)
    {
        color_ = c;
        useAlpha_ = useAlpha;
    }

    public void setColor(int c)
    {
        color_ = c;
    }

    public void setColor(int c, boolean useAlpha)
    {
        color_ = c;
        useAlpha_ = useAlpha;
    }

    public void paint(Graphics g, int w, int h)
    {
        //util.Debug.message("FillView::paint " + color_ + " " + w + ", " + h);
        if (useAlpha_)
        {
            if (i_ == null || i_.getWidth() != w || i_.getHeight() != h)
                i_ = MiscUtils.createRGBImage(color_, w, h, true);
            g.drawImage(i_, 0, 0, Graphics.TOP | Graphics.LEFT);
        }
        else
        {
            int oldColor = g.getColor();
            g.setColor(color_);
            g.fillRect(0, 0, w, h);
            g.setColor(oldColor);
        }
    }
}
