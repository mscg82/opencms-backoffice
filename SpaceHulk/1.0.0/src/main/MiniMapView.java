package main;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

import javax.microedition.lcdui.Graphics;

import radui.View;
import sh.Blip;
import sh.Map;
import sh.Marine;
import sh.Piece;
import sh.Stealer;
import sh.TileType;
import util.Point2I;

public class MiniMapView extends View
{
    private Map m_;
    
    private Point2I vc_;
    
    private int dotWidth_ = 1;
    private int dotHeight_ = 1;
    
    public MiniMapView(Map m, int w, int h, Point2I vc)
    {
        m_ = m;
        dotWidth_ = w;
        dotHeight_ = h;
        vc_ = vc;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        int tx = ((w + dotWidth_ - 1) / 2) / dotWidth_;
        int ty = ((h + dotHeight_ - 1) / 2) / dotHeight_;
        
        int sy = (h - dotHeight_)/2 - ty * dotHeight_;
        //for (int y = m_.miny; y <= m_.maxy; ++y)
        for (int y = vc_.y - ty; y <= vc_.y + ty; ++y)
        {
            int sx = (w - dotWidth_)/2 - tx * dotWidth_;
            //for (int x = m_.minx; x <= m_.maxx; ++x)
            for (int x = vc_.x - tx; x <= vc_.x + tx; ++x)
            {
                int tile = m_.getTile(x, y);
                int object = m_.getObject(x, y);
                Piece piece = m_.getPiece(x, y);
                
                if (piece != null)
                {
                    if (piece instanceof Marine)
                    {
                        g.setColor(255, 0, 0);
                        g.fillRect(sx, sy, dotWidth_, dotHeight_);
                    }
                    else if (piece instanceof Blip)
                    {
                        g.setColor(128, 255, 255);
                        g.fillRect(sx, sy, dotWidth_, dotHeight_);
                    }
                    else if (piece instanceof Stealer)
                    {
                        g.setColor(128, 255, 255);
                        g.fillRect(sx, sy, dotWidth_, dotHeight_);
                    }
                }
                else if ((object & TileType.TYPE_MASK) == TileType.DOOR)
                {
                    g.setColor(0, 0, 255);
                    g.fillRect(sx, sy, dotWidth_, dotHeight_);
                }
                else if ((object & TileType.TYPE_MASK) == TileType.BULKHEAD)
                {
                    g.setColor(0, 128, 0);
                    g.fillRect(sx, sy, dotWidth_, dotHeight_);
                }
                else if ((tile & TileType.TYPE_MASK) == TileType.ENTRY)
                {
                    //util.Debug.message("minimap entry");
                    g.setColor(0, 128, 0);
                    g.fillRect(sx, sy, dotWidth_, dotHeight_);
                }
                else if ((tile & TileType.TYPE_MASK) == TileType.EXIT)
                {
                    //util.Debug.message("minimap exit");
                    g.setColor(128, 0, 0);
                    g.fillRect(sx, sy, dotWidth_, dotHeight_);
                }
                else if ((object != 0 && object != TileType.OBJECT_BLOOD && object != TileType.OBJECT_SPLAT) || m_.getItem(x, y) != 0)
                {
                    //util.Debug.message("minimap something");
                    g.setColor(255, 255, 255);
                    g.fillRect(sx, sy, dotWidth_, dotHeight_);
                }
                else if ((tile & TileType.TYPE_MASK) == TileType.FLOOR)
                {
                    g.setColor(128, 128, 128);
                    g.fillRect(sx, sy, dotWidth_, dotHeight_);
                }
                    
                sx += dotWidth_;
            }
            sy += dotHeight_;
        }
    }
}
