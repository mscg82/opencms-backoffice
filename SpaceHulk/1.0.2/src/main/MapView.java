package main;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

import javax.microedition.lcdui.Graphics;

import main.managers.ImageManager;
import main.managers.SoundManager;
import main.managers.Tile;
import main.managers.TileManager;
import main.settings.Settings;
import radui.ScreenCanvas;
import radui.View;
import sh.Face;
import sh.Map;
import sh.Marine;
import sh.Piece;
import util.Point2I;

public class MapView extends View
{
    private Map m_;
    private TileManager tm_ = new TileManager();
    private SoundManager sm_;
    private Tile activeTile_ = new Tile(ImageManager.load("/mselect.png"));
    private Tile overwatchTile_ = new Tile(ImageManager.load("/overwatch.png"));
    private Tile guardTile_ = new Tile(ImageManager.load("/guard.png"));
    private Tile jammedTile_ = new Tile(ImageManager.load("/jammed.png"));
    private Marine active_ = null;
    
    private Point2I vc_;

    public MapView(Map m, SoundManager sm, Point2I vc)
    {
        m_ = m;
        sm_ = sm;
        vc_ = vc;
    }
    
    void setActive(Marine m)
    {
        active_ = m;
    }
    
    Marine getActive()
    {
        return active_;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        boolean drawActive = false;
        int ax = 0;
        int ay = 0;

        int tx = ((w + TileManager.getTileWidth() - 1) / 2) / TileManager.getTileWidth();
        int ty = ((h + TileManager.getTileHeight() - 1) / 2) / TileManager.getTileHeight();
        {
            int sy = h / 2 - ty * TileManager.getTileHeight();
            //for (int y = m_.miny; y <= m_.maxy; ++y)
            for (int y = vc_.y - ty; y <= vc_.y + ty; ++y)
            {
                int sx = w / 2 - tx * TileManager.getTileWidth();
                //for (int x = m_.minx; x <= m_.maxx; ++x)
                for (int x = vc_.x - tx; x <= vc_.x + tx; ++x)
                {
                    Tile t = tm_.getTile(m_.getTile(x, y));
                    if (t != null)
                        t.paint(g, sx, sy);

                    t = tm_.getTile(m_.getObject(x, y));
                    if (t != null)
                        t.paint(g, sx, sy);

                    t = tm_.getTile(m_.getItem(x, y));
                    if (t != null)
                        t.paint(g, sx, sy);

                    sx += TileManager.getTileWidth();
                }
                sy += TileManager.getTileHeight();
            }
        }
        {
            int sy = h / 2 - ty * TileManager.getTileHeight();
            //for (int y = m_.miny; y <= m_.maxy; ++y)
            for (int y = vc_.y - ty; y <= vc_.y + ty; ++y)
            {
                int sx = w / 2 - tx * TileManager.getTileWidth();
                //for (int x = m_.minx; x <= m_.maxx; ++x)
                for (int x = vc_.x - tx; x <= vc_.x + tx; ++x)
                {
                    if (m_.isFire(x, y))
                        tm_.fire.paint(g, sx, sy);

                    sx += TileManager.getTileWidth();
                }
                sy += TileManager.getTileHeight();
            }
        }
        {
            int sy = h / 2 - ty * TileManager.getTileHeight();
            //for (int y = m_.miny; y <= m_.maxy; ++y)
            for (int y = vc_.y - ty; y <= vc_.y + ty; ++y)
            {
                int sx = w / 2 - tx * TileManager.getTileWidth();
                //for (int x = m_.minx; x <= m_.maxx; ++x)
                for (int x = vc_.x - tx; x <= vc_.x + tx; ++x)
                {
                    Piece p = m_.getPiece(x, y);
                    if (p != null)
                    {
                        if (active_ == p)
                        {
                            drawActive = true;
                            ax = sx;
                            ay = sy;
                        }
                        if (p instanceof Marine)
                        {
                            Marine m = (Marine) p;
                            Tile c = tm_.getTile(m.getCarrying());
                            if (c != null)
                                c.paint(g, sx, sy);
                            if (m.getShoot())
                            {
                                Face f = m.getFace();
                                Tile t = tm_.getShoot(f);
                                if (t != null)
                                    t.paint(g,
                                    	sx + f.getOffsetX() * (TileManager.getTileWidth() + tm_.getShootOffset()),
                                    	sy + f.getOffsetY() * (TileManager.getTileHeight() + tm_.getShootOffset()));
                            }
                        }
                        Tile t = tm_.getTile(p);
                        if (t != null)
                            t.paint(g, sx, sy);
                        if (p instanceof Marine)
                        {
                            Marine m = (Marine) p;
                            if (m.getOverwatch() && !m.getJammed())
                                overwatchTile_.paint(g, sx, sy);
                            if (m.getJammed())
                                jammedTile_.paint(g, sx, sy);
                            if (m.getGuard())
                            	guardTile_.paint(g, sx, sy);
                        }
                    }


                    sx += TileManager.getTileWidth();
                }
                sy += TileManager.getTileHeight();
            }
        }
        if (drawActive)
            activeTile_.paint(g, ax, ay);
    }

    public void keyPressed(ScreenCanvas sc, int keyCode)
    {
        int gameAction = sc.getGameAction(keyCode);

        //util.Debug.message("MapView::keyPressed");

        Settings settings = SpaceHulk.instance.getSettings();
        if ((!settings.invertKeys && gameAction == ScreenCanvas.GAME_C) ||
            (settings.invertKeys && gameAction == ScreenCanvas.GAME_D))
        {
            //if (m_.toggleDoor(vc_.x, vc_.y))
            //sc.repaint();
            Piece p = m_.getPiece(vc_.x, vc_.y);
            if (p instanceof Marine)
            {
            	getActive().clearActionPoints();
                setActive((Marine) p);
                sm_.playSound(SoundManager.SELECT);
                sc.repaint();
            }
        }
    }
}
