// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;

public class StringView extends View
{
    private String text_;
    private int color_ = 0xFFFFFF;
    private int anchor_ = Graphics.VCENTER | Graphics.HCENTER;
    private Font font_ = Font.getDefaultFont();
    
    public StringView(String text)
    {
        text_ = text;
    }
    
    public void setText(String text)
    {
        text_ = text;
    }
    
    public void setColor(int c)
    {
        color_ = c;
    }
    
    public void setAnchor(int a)
    {
        anchor_ = a;
    }
    
    public void setFont(Font f)
    {
        font_ = f;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        Font oldFont = g.getFont();
        int oldColor = g.getColor();
        
        if (text_ != null)
        {
            g.setColor(color_);
            g.setFont(font_);
            //util.Debug.message("StringView::paint " + getPosX(w) + " " + getPosY(h) + " " + getDrawAnchor());
            g.drawString(text_, getPosX(w), getPosY(h), getDrawAnchor());
        }
        
        g.setColor(oldColor);
        g.setFont(oldFont);
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
            return (h - font_.getHeight())/2;
        else if ((anchor_ & Graphics.BOTTOM) == Graphics.BOTTOM)
            return h;
        else
            return 0;
    }

    private int getDrawAnchor()
    {
        if ((anchor_ & Graphics.VCENTER) == Graphics.VCENTER)
            return (anchor_ & ~Graphics.VCENTER) | Graphics.TOP;
        else
            return anchor_;
    }
    
    public int getMaxWidth()
    {
        return font_.stringWidth(text_);
    }
    
    public int getMaxHeight()
    {
        return font_.getHeight();
    }
}
