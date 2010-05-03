// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class ImageView extends View
{
    private Image image_ = null;
    private int anchor_ = Graphics.VCENTER | Graphics.HCENTER;
    
    public ImageView(Image i)
    {
        image_ = i;
    }
    
    public void setImage(Image i)
    {
        image_ = i;
    }
    
    public void setAnchor(int a)
    {
        anchor_ = a;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        if (image_ != null)
        {
            g.drawImage(image_, getPosX(w), getPosY(h), anchor_);
        }
    }
    
    private int getPosX(int w)
    {
        if ((anchor_ & Graphics.LEFT) == Graphics.LEFT)
            return 0;
        else if ((anchor_ & Graphics.HCENTER) == Graphics.HCENTER)
            return w/2;
        else if ((anchor_ & Graphics.RIGHT) == Graphics.RIGHT)
            return w;
        else
            return 0;
    }
    
    private int getPosY(int h)
    {
        if ((anchor_ & Graphics.TOP) == Graphics.TOP)
            return 0;
        else if ((anchor_ & Graphics.VCENTER) == Graphics.VCENTER)
            return h/2;
        else if ((anchor_ & Graphics.BOTTOM) == Graphics.BOTTOM)
            return h;
        else
            return 0;
    }
    
    public int getMaxWidth()
    {
        return image_.getWidth();
    }
    
    public int getMaxHeight()
    {
        return image_.getHeight();
    }
}
