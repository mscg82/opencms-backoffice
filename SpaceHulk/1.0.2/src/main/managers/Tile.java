package main.managers;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
//import javax.microedition.lcdui.game.Sprite;

public class Tile
{
    public static boolean zoom_ = true;

    private Image large_;
    private Image small_;
    private int x_;
    private int y_;
    private int w_;
    private int h_;

    public Tile(Image i)
    {
        large_ = i;
        small_ = util.MiscUtils.scaleImage(large_, large_.getWidth() / 2, large_.getHeight() / 2);
        x_ = 0;
        y_ = 0;
        w_ = large_.getWidth();
        h_ = large_.getHeight();
    }
    
    public void paint(Graphics g, int x, int y)
    {
        if (zoom_)
            g.drawRegion(large_, x_, y_, w_, h_, /*Sprite.TRANS_NONE*/ 0, x, y, Graphics.HCENTER | Graphics.VCENTER);
        else
            g.drawRegion(small_, x_, y_, w_/2, h_/2, /*Sprite.TRANS_NONE*/ 0, x, y, Graphics.HCENTER | Graphics.VCENTER);
    }
    
    public int getWidth()
    {
        if (zoom_)
            return w_;
        else
            return w_/2;
    }
    
    public int getHeight()
    {
        if (zoom_)
            return h_;
        else
            return h_/2;
    }
}
