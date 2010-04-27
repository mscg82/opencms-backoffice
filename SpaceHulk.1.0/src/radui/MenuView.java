// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;

public class MenuView extends View
{
    private Vector commands_ = new Vector();
    private int selected_ = 0;
    private int offset_ = 0;

    private MenuTheme t_;

    private static class Command
    {
        Command(String label, Callback cb)
        {
            this.label = label;
            this.cb = cb;
        }

        String label;
        Callback cb;
    }

    public MenuView(MenuTheme t)
    {
        t_ = t;
    }

    public MenuTheme getTheme()
    {
        return t_;
    }
    
    public int add(String label, Callback c)
    {
        commands_.addElement(new Command(label, c));
        return commands_.size() - 1;
    }

    public void setLabel(int i, String label)
    {
        Command c = (Command) commands_.elementAt(i);
        c.label = label;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        makeSelectedVisible(h);

        Font oldFont = g.getFont();
        int oldColor = g.getColor();
        
        g.setFont(t_.font);
        
        final int vspace = t_.font.getHeight();
        
        int x = (w - getMaxWidth()) / 2;
        int y = (h - commands_.size() * vspace) / 2 + offset_;
        for (int i = 0; i < commands_.size(); ++i)
        {
            if (i == selected_)
                g.setColor(t_.colorSelected);
            else
                g.setColor(t_.color);
            Command c = (Command) commands_.elementAt(i);
            if (c.label != null)
                g.drawString(c.label, x, y, Graphics.TOP | Graphics.LEFT);
            y += vspace;
        }
        
        g.setColor(oldColor);
        g.setFont(oldFont);
    }

    private void makeSelectedVisible(int h)
    {
        final int vspace = t_.font.getHeight();
        int top = (commands_.size() * vspace - h) / 2 - selected_ * vspace;
        int bottom = (commands_.size() * vspace + h) / 2 - selected_ * vspace - vspace;
        //util.Debug.message(" " + (commands_.size() * vspace) + " " + (selected_ * vspace) + " " + h + " " + top + " " + bottom);
        if (offset_ > bottom)
            offset_ = bottom;
        if (offset_ < top)
            offset_ = top;
    }

    public void keyPressed(ScreenCanvas sc, int keyCode)
    {
        int gameAction = sc.getGameAction(keyCode);

        switch (gameAction)
        {
        case ScreenCanvas.UP:
            do
            {
                --selected_;
                if (selected_ < 0)
                {
                    selected_ = commands_.size() - 1;
                    break;
                }
            } while (((Command) commands_.elementAt(selected_)).label == null);
            sc.repaint();
            break;
            
        case ScreenCanvas.DOWN:
            do
            {
                ++selected_;
                if (selected_ >= commands_.size())
                {
                    selected_ = 0;
                    break;
                }
            } while (((Command) commands_.elementAt(selected_)).label == null);
            sc.repaint();
            break;
            
        case ScreenCanvas.FIRE:
            Command c = (Command) commands_.elementAt(selected_);
            if (c.cb != null)
                c.cb.perform();
            break;
        }
    }
    
    public int getMaxWidth()
    {
        int w  = 0;
        for (int i = 0; i < commands_.size(); ++i)
        {
            Command c = (Command) commands_.elementAt(i);
            if (c.label != null)
                w = Math.max(w, t_.font.stringWidth(c.label));
        }
        return w;
    }
    
    public int getMaxHeight()
    {
        int vspace = t_.font.getHeight();
        return commands_.size() * vspace;
    }
}
