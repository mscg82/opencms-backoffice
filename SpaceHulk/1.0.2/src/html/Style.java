// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package html;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import main.SpaceHulk;

class Style
{
    Font font;
    int color = 0xFFFFFF;
    int bg = 0x000000;
    int anchor = Graphics.LEFT | Graphics.TOP;
    int paraSpace;
    int scrollArrowColor = 0xFFFFFF;
    int scrollBaseColor = 0x808080;
    int scrollTrackColor = 0xFFFFFF;
        
    Style(Font f)
    {
        font = f;
        paraSpace = font.getHeight();
    }
    
    void setStyle(String text)
    {
        try {
            int begin = 0;
            int end;
            while ((end = text.indexOf(';', begin)) != -1)
            {
                setStyleValue(text.substring(begin, end).trim());
                begin = end + 1;
            }
            setStyleValue(text.substring(begin).trim());
        } catch(Exception e) {
            SpaceHulk.instance.showException("Style::setStyle", e);
        }
    }
    
    void setStyleValue(String text)
    {
        try {
            //util.Debug.message("Style::setStyleValue '" + text + "'");
            int split = text.indexOf(':');
            if (split != -1)
            {
                String name = text.substring(0, split).trim();
                String value = text.substring(split + 1).trim();
                //util.Debug.message("Style::setStyleValue '" + name + "' '" + value + "'");
                if (name.equals("color"))
                    color = getColor(value);
                else if (name.equals("background"))
                    bg = getColor(value);
                else if (name.equals("font-weight"))
                {
                    if (value.equals("bold"))
                        font = Font.getFont(font.getFace(), font.getStyle() | Font.STYLE_BOLD, font.getSize());
                    else
                        util.Debug.error("Style::setStyleValue Unknown '" + name + "' = '" + value + "'");
                }
                else if (name.equals("font-family"))
                {
                    if (value.equals("sans-serif"))
                        font = Font.getFont(Font.FACE_PROPORTIONAL, font.getStyle(), font.getSize());
                    else
                        util.Debug.error("Style::setStyleValue Unknown '" + name + "' = '" + value + "'");
                }
                else if (name.equals("scrollbar-base-color"))
                    scrollBaseColor = getColor(value);
                else if (name.equals("scrollbar-arrow-color"))
                    scrollArrowColor = getColor(value);
                else if (name.equals("scrollbar-track-color"))
                    scrollTrackColor = getColor(value);
                else
                    util.Debug.error("Style::setStyleValue Unknown '" + name + "' = '" + value + "'");
            }
        } catch(Exception e) {
            SpaceHulk.instance.showException("Style::setStyleValue", e);
        }
    }
    
    private static int getColor(String s)
    {
        if (s.equals("black"))
            return 0x000000;
        else if (s.equals("red"))
            return 0xFF0000;
        else if (s.equals("green"))
            return 0x00FF00;
        else if (s.equals("blue"))
            return 0x0000FF;
        else
        {
            util.Debug.error("Style::getColor Unknown '" + s + "'");
            return 0xFF00FF;
        }
    }
    
    void setStyle(Graphics g)
    {
        g.setFont(font);
        g.setColor(color);
    }
    
    static Style createStyle(Graphics g)
    {
        Style s = new Style(g.getFont());
        s.color = g.getColor();
        return s;
    }
}
