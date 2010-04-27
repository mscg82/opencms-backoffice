// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class ScreenCanvas extends Canvas
{
    public final static int LEFT_SOFTKEY = -6;
    public final static int RIGHT_SOFTKEY = -7;
    public final static int CANCEL = -8;
    public final static int BACK = -11;
    
    private boolean rotate_ = false;
    private Image screen_ = null;
    private View view_ = null;
    
    public void setView(View view)
    {
        view_ = view;
        repaint();
    }
    
    public void paint(Graphics g)
    {
        if (rotate_)
        {
            if (screen_ == null || screen_.getWidth() != getWidth() || screen_.getHeight() != getHeight())
                screen_ = Image.createImage(getWidth(), getHeight());

            Graphics sg = screen_.getGraphics();
            initScreen(sg);
            if (view_ != null)
                view_.paint(sg, getWidth(), getHeight());

            g.drawRegion(screen_, 0, 0, getWidth(), getHeight(), Sprite.TRANS_ROT90, 0, 0, 0);
        }
        else
        {
            initScreen(g);
            if (view_ != null)
                view_.paint(g, getWidth(), getHeight());
        }
    }

    public boolean getRotate()
    {
        return rotate_;
    }

    public void setRotate(boolean r)
    {
        rotate_ = r;
        screen_ = null;
        repaint();
    }

    public int getWidth()
    {
        if (rotate_)
            return super.getHeight();
        else
            return super.getWidth();
    }

    public int getHeight()
    {
        if (rotate_)
            return super.getWidth();
        else
            return super.getHeight();
    }

    public int getGameAction(int keyCode)
    {
        int ga = super.getGameAction(keyCode);
        if (rotate_)
        {
            switch (ga)
            {
            case UP:
                ga = LEFT;
                break;

            case DOWN:
                ga = RIGHT;
                break;

            case LEFT:
                ga = DOWN;
                break;

            case RIGHT:
                ga = UP;
                break;

            }
        }
        return ga;
    }
    
    private void initScreen(Graphics g)
    {
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(255, 255, 255);
    }
    
    protected void keyPressed(int keyCode)
    {
        if (view_ != null)
            view_.keyPressed(this, keyCode);
    }
    
    protected void keyRepeated(int keyCode)
    {
        if (view_ != null)
            view_.keyRepeated(this, keyCode);
    }
}
