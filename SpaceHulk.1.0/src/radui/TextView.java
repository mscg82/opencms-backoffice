// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import main.SpaceHulk;
import util.MiscUtils;

// TODO I dont think this works well with vertical anchors
// TODO I dont think this works well with embedded newlines

public class TextView extends View
{
    public static char START_TAG = '\0';
    public static char END_TAG = '\0';
    public static String OPEN_ITALIC = START_TAG + "I" + END_TAG;
    public static String CLOSE_ITALIC = START_TAG + "i" + END_TAG;
    public static String OPEN_BOLD = START_TAG + "B" + END_TAG;
    public static String CLOSE_BOLD = START_TAG + "b" + END_TAG;
    public static String WHITESPACE = " \r\n";
    private static String WIDEST_CHAR = "W";
    
    private String text_;
    private int color_ = 0xFFFFFF;
    private int anchor_ = Graphics.TOP | Graphics.HCENTER;
    private Font font_ = Font.getDefaultFont();
    
    // These are tmeporary during the paint
    private int x_;
    private int y_;
    
    private int cacheW_ = -1;
    private int cacheH_ = -1;
    
    public TextView(String text)
    {
        text_ = text;
    }
    
    public void setText(String text)
    {
        text_ = text;
        clearCache();
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
        clearCache();
    }
    
    public void paint(Graphics g, int w, int h)
    {
        //util.Debug.message("TextView::paint " + w + ", " + h);
        //util.Debug.message("TextView::paint '" + text_ + "'");
        Font oldFont = g.getFont();
        int oldColor = g.getColor();
        
        if (text_ != null)
        {
            x_ = 0;
            y_ = 0;
            g.setColor(color_);
            g.setFont(font_);
            writeLine(g, g.getFont(), text_, w);
        }
        
        g.setColor(oldColor);
        g.setFont(oldFont);
    }
    
    public int getMaxWidth()
    {
        return font_.stringWidth(text_);
    }
    
    public int getMaxHeight()
    {
        return font_.getHeight();
    }
    
    public int getHeight(int w)
    {
        if (cacheW_ != w)
        {
            //util.Debug.message("TextView::getHeight " + text_);
            x_ = 0;
            y_ = 0;
            writeLine(null, font_, text_, w);
            if (x_ > 0)
                newLine(font_);
                
            cacheW_ = w;
            cacheH_ = y_;
        }
        return cacheH_;
    }
    
    private void clearCache()
    {
        cacheW_ = -1;
    }

    private void newLine(Font f)
    {
        //util.Debug.message("TextView::newLine");
        x_ = 0;
        y_ += f.getHeight();
    }
    
    private void writeLine(Graphics g, Font f, String s, int w)
    {
        try {
            int offset = 0;
            int x = x_;
            if ((anchor_ & Graphics.HCENTER) == Graphics.HCENTER)
                x = w/2;
            else if ((anchor_ & Graphics.RIGHT) == Graphics.RIGHT)
                x = w;
            while (offset < s.length())
            {
                int chars = 0;
                if ((anchor_ & Graphics.LEFT) == Graphics.LEFT)
                {
                    x = x_;
                    chars = getFit(f, s, offset, w - x);
                }
                else
                    chars = getFit(f, s, offset, w);

                //util.Debug.message("Line: " + x + " Chars: '" + s.substring(offset, offset + chars) + "'");
                if (g != null)
                    g.drawSubstring(s, offset, chars, x, y_, anchor_);
                x_ += f.substringWidth(s, offset, chars);

                offset += chars;
                if (offset < s.length())
                {
                    int tempoffset = MiscUtils.indexNotOfAny(s, WHITESPACE, offset);

                    if (s.charAt(tempoffset) == START_TAG)
                    {
                        x_ += f.substringWidth(s, offset, tempoffset - offset);
                        //util.Debug.message("special char: " + s.charAt(offset + 1));
                        // TODO Here we assume we toggle on/off the font style rather than revert to the last font style
                        if (s.regionMatches(false, tempoffset, OPEN_ITALIC, 0, OPEN_ITALIC.length()))
                        {
                            //util.Debug.message("TextView::writeLine open italic");
                            f = Font.getFont(f.getFace(), f.getStyle() | Font.STYLE_ITALIC, f.getSize());
                            offset = tempoffset + OPEN_ITALIC.length();
                        }
                        else if (s.regionMatches(false, tempoffset, CLOSE_ITALIC, 0, CLOSE_ITALIC.length()))
                        {
                            //util.Debug.message("TextView::writeLine close italic");
                            f = Font.getFont(f.getFace(), f.getStyle() & ~Font.STYLE_ITALIC, f.getSize());
                            offset = tempoffset + CLOSE_ITALIC.length();
                        }
                        else if (s.regionMatches(false, tempoffset, OPEN_BOLD, 0, OPEN_BOLD.length()))
                        {
                            //util.Debug.message("TextView::writeLine open bold");
                            f = Font.getFont(f.getFace(), f.getStyle() | Font.STYLE_BOLD, f.getSize());
                            offset = tempoffset + OPEN_BOLD.length();
                        }
                        else if (s.regionMatches(false, tempoffset, CLOSE_BOLD, 0, CLOSE_BOLD.length()))
                        {
                            //util.Debug.message("TextView::writeLine close bold");
                            f = Font.getFont(f.getFace(), f.getStyle() & ~Font.STYLE_BOLD, f.getSize());
                            offset = tempoffset + CLOSE_BOLD.length();
                        }
                        else
                        {
                            util.Debug.assert2(false, "TextView::writeLine Unknown command");
                            tempoffset = s.indexOf(END_TAG, tempoffset);
                            offset = tempoffset + 1;
                        }
                        if (g != null)
                            g.setFont(f);
                        if(x_ > w) {
                            newLine(f);
                        }
                    }
                    else
                    {
                        offset = MiscUtils.indexNotOfAny(s, WHITESPACE, offset);
                        newLine(f);
                    }
                }
            }
        } catch(Exception e) {
            SpaceHulk.instance.showException("TextView::writeLine", e);
        }
    }
    
    private static int getFit(Font f, String s, int offset, int width)
    {
        //util.Debug.message("+Getfit: " + s.length() + " " + width + " '" + s.substring(offset) + "'");
        int length = 0;

        int sc = s.indexOf(START_TAG, offset);
        if (sc != -1)
            length = sc - offset;
        else
            length = s.length() - offset;
        int strwidth = f.substringWidth(s, offset, length);
        while (length >= 0 && strwidth > width)
        {
            int diff = (strwidth - width + f.stringWidth(WIDEST_CHAR) - 1) / f.stringWidth(WIDEST_CHAR);
            //util.Debug.message(" length: " + length + " diff: " + diff + " strwidth: " + strwidth);
            if (diff <= 0)
                break;
            length -= diff;
            strwidth = f.substringWidth(s, offset, length);
        }
        //util.Debug.message(" length: " + length + " strwidth: " + strwidth);

        if (length > 0)
        {
            //if ((offset + length) < s.length())
            //util.Debug.message(" Getfit: char " + (offset + length) + " "
            //+ (int) s.charAt(offset + length) + " '" + s.charAt(offset + length) + "'");
            if ((offset + length) < s.length() && s.charAt(offset + length) != START_TAG
                && WHITESPACE.indexOf(s.charAt(offset + length)) == -1)
            {   // Dont break in the middle of a word
                //util.Debug.message(" Getfit: middle of word");
                int breakChar = MiscUtils.lastIndexOfAny(s, WHITESPACE + END_TAG, offset + length);
                if (breakChar > offset)
                    length = breakChar - offset;
                else
                    length = 0;
            }
        }

        //util.Debug.message("-Getfit: " + length + " '" + s.substring(offset, offset + length) + "'");

        return length;
    }
}
