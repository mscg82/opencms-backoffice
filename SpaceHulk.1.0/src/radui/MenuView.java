// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class MenuView extends View
{
    private Vector commands_ = new Vector();
    private int selected_ = 0;
    private int offset_ = 0;

    private MenuTheme t_;

    private static class Command
    {
        Command(String label, Callback cb, Font font)
        {
        	this.label = label;
        	this.cb = cb;
        	this.font = font;
        }

        String label;
        Callback cb;
        Font font;
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
        return add(label, c, null);
    }

    public int add(String label, Callback c, Font f)
    {
    	commands_.addElement(new Command(label, c, f));
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
        
        int totalHeight = 0;
        for (int i = 0, l = commands_.size(); i < l; ++i) {
        	Command c = (Command) commands_.elementAt(i);
        	if(c.font == null)
        		totalHeight += t_.font.getHeight();
        	else
        		totalHeight += c.font.getHeight();
        }
        
        int x = (w - getMaxWidth()) / 2;
        int y = (h - totalHeight) / 2 + offset_;
        for (int i = 0, l = commands_.size(); i < l; ++i) {
            if (i == selected_)
                g.setColor(t_.colorSelected);
            else
                g.setColor(t_.color);
            Command c = (Command) commands_.elementAt(i);
            int vspace = t_.font.getHeight();
            if (c.label != null) {
            	if(c.font != null) {
            		g.setFont(c.font);
            		vspace = c.font.getHeight();
            	}
            	g.drawString(c.label, x, y, Graphics.TOP | Graphics.LEFT);
            	if(c.font != null)
            		g.setFont(t_.font);
            }
            y += vspace;
        }
        
        g.setColor(oldColor);
        g.setFont(oldFont);
    }

    private void makeSelectedVisible(int h)
    {
    	if(!isCommandSelectable((Command) commands_.elementAt(selected_)))
    		selectNext();
    	
        final int vspace = t_.font.getHeight();
        int top = (commands_.size() * vspace - h) / 2 - selected_ * vspace;
        int bottom = (commands_.size() * vspace + h) / 2 - selected_ * vspace - vspace;
        //util.Debug.message(" " + (commands_.size() * vspace) + " " + (selected_ * vspace) + " " + h + " " + top + " " + bottom);
        if (offset_ > bottom)
            offset_ = bottom;
        if (offset_ < top)
            offset_ = top;
    }
    
    private boolean isCommandSelectable(Command c) {
    	return c.label != null && c.cb != null;
    }
    
    private void selectPrevious() {
    	int start = selected_;
    	do {
            selected_ = (selected_ + commands_.size() - 1) % commands_.size();
            if(selected_ == start){
            	// cycled all items, exit loop
            	break;
            }
        } while (!isCommandSelectable((Command) commands_.elementAt(selected_)));
    }
    
    private void selectNext() {
    	int start = selected_;
    	do {
            selected_ = (selected_ + 1) % commands_.size();
            if(selected_ == start){
            	// cycled all items, exit loop
            	break;
            }
        } while (!isCommandSelectable((Command) commands_.elementAt(selected_)));
    }

    public void keyPressed(ScreenCanvas sc, int keyCode)
    {
        int gameAction = sc.getGameAction(keyCode);

        switch (gameAction)
        {
        case ScreenCanvas.UP:
            selectPrevious();
            sc.repaint();
            break;
            
        case ScreenCanvas.DOWN:
        	selectNext();
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
            if (c.label != null) {
            	if(c.font == null)
            		w = Math.max(w, t_.font.stringWidth(c.label));
            	else
            		w = Math.max(w, c.font.stringWidth(c.label));
            }
        }
        return w;
    }
    
    public int getMaxHeight()
    {
        int vspace = t_.font.getHeight();
        return commands_.size() * vspace;
    }
}
