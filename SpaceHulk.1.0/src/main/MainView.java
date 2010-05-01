package main;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

import javax.microedition.lcdui.Graphics;

import main.managers.ImageManager;
import main.managers.SoundManager;
import main.managers.Tile;
import radui.BorderView;
import radui.FillView;
import radui.FrameView;
import radui.ResizeView;
import radui.ScreenCanvas;
import radui.StackView;
import radui.TextView;
import radui.View;
import sh.CommandPoints;
import sh.Game;
import sh.Map;
import sh.Marine;
import sh.Piece;
import util.Point2I;

public class MainView extends View
{
    protected Map map_;
    
    private Tile targetTileHit_ = new Tile(ImageManager.load("/targetring.png"));
    private Tile targetTileMiss_ = new Tile(ImageManager.load("/targetring2.png"));
    
    private CommandPoints cp_;
    private Game game_;
    private Point2I vc_;
    private MapView mapView_;
    private View minMapView_;

    MainView(SoundManager sm, Game game)
    {
    	game_ = game;
        map_ = game_.getMap();
        cp_ = game_.getCommandPoints();
        vc_ = new Point2I(
            (map_.getMinX() + map_.getMaxX()) / 2,
            (map_.getMinY() + map_.getMaxY()) / 2);

        mapView_ = new MapView(map_, sm, vc_);
        
        {
            ResizeView rv = new ResizeView(40, 40);
            rv.setAnchor(Graphics.BOTTOM | Graphics.LEFT);
            
            StackView sv = new StackView();
            sv.add(new FillView(0x80000000, true));
            sv.add(new FrameView(0xFFFFFF));
            sv.add(new BorderView(new MiniMapView(map_, 2, 2, vc_), 1, 1));
            
            rv.setView(sv);
            
            minMapView_ = rv;
        }
    }
    
    int getPosX()
    {
        return vc_.x;
    }

    int getPosY()
    {
        return vc_.y;
    }

    Marine getActive()
    {
        return mapView_.getActive();
    }

    void setActive(Marine m)
    {
        mapView_.setActive(m);
    }

    void setFocus(Piece p)
    {
        vc_.x = p.getPosX();
        vc_.y = p.getPosY();
    }
    
    public void paint(Graphics g, int w, int h)
    {
        //util.Debug.message("+MainView::paint");
        mapView_.paint(g, w, h);
        Marine active = getActive();
        if (active != null)
        {
            int round = game_.getRound() + 1;
            String roundStr = (round < 10 ? "0" : "") + round;
        	int kills = game_.getStealearKills();
        	String killsStr = (kills < 10 ? "0" : "") + kills;
        	int casualties = game_.getCasualties();
        	String casualtiesStr = (casualties < 10 ? "0" : "") + casualties;
        	
            if (map_.existsLineOfSight(active, vc_.x, vc_.y, true))
                targetTileHit_.paint(g, w / 2, h / 2);
            else
                targetTileMiss_.paint(g, w / 2, h / 2);
            g.drawString(active.getName(), 0, 0, Graphics.TOP | Graphics.LEFT);
            if (active.getType() == Marine.FLAMER)
                g.drawString("Ammo: " + active.getAmmunition(), 0, g.getFont().getHeight(), Graphics.TOP | Graphics.LEFT);
            g.drawString("AP: " + active.getActionPoints(), w, 0, Graphics.TOP | Graphics.RIGHT);
            g.drawString("CP: " + cp_.get(), w, g.getFont().getHeight(), Graphics.TOP | Graphics.RIGHT);
            int offset = 4 * g.getFont().stringWidth(TextView.WIDEST_CHAR);
            g.drawString("TR: " + roundStr, w - offset, 0, Graphics.TOP | Graphics.RIGHT);
            g.drawString("SK: " + killsStr, w - offset, g.getFont().getHeight(), Graphics.TOP | Graphics.RIGHT);
            g.drawString("CS: " + casualtiesStr, w - offset, 2 * g.getFont().getHeight(), Graphics.TOP | Graphics.RIGHT);

            //double angle = los.ShadowCasting.angle(vc_.y - active.getPosY(), vc_.x - active.getPosX());
            //util.Debug.message("MainView::paint m " + active.getPosX() + " " + active.getPosY());
            //util.Debug.message("MainView::paint t " + vc_.x + " " + vc_.y);
            //util.Debug.message("MainView::paint angle " + angle);
        }
        minMapView_.paint(g, w, h);
        //util.Debug.message("-MainView::paint");
    }

    public void keyPressed(ScreenCanvas sc, int keyCode)
    {
        int gameAction = sc.getGameAction(keyCode);
        
        mapView_.keyPressed(sc, keyCode);
        minMapView_.keyPressed(sc, keyCode);

        switch (gameAction)
        {
        case ScreenCanvas.LEFT:
            --vc_.x;
            if (vc_.x < map_.getMinX())
                vc_.x = map_.getMinX();
            sc.repaint();
            break;
            
        case ScreenCanvas.RIGHT:
            ++vc_.x;
            if (vc_.x > map_.getMaxX())
                vc_.x = map_.getMaxX();
            sc.repaint();
            break;

        case ScreenCanvas.UP:
            --vc_.y;
            if (vc_.y < map_.getMinY())
                vc_.y = map_.getMinY();
            sc.repaint();
            break;
            
        case ScreenCanvas.DOWN:
            ++vc_.y;
            if (vc_.y > map_.getMaxY())
                vc_.y = map_.getMaxY();
            sc.repaint();
            break;

        case ScreenCanvas.GAME_D:
            if(!SpaceHulk.instance.getSettings().invertKeys) {
                setFocus(getActive());
                sc.repaint();
            }
            break;
        case ScreenCanvas.GAME_C:
            if(SpaceHulk.instance.getSettings().invertKeys) {
                setFocus(getActive());
                sc.repaint();
            }
            break;
        }
        //util.Debug.message("MainView::keypressed " + vc_.x + " " + vc_.y);
    }

    public void keyRepeated(ScreenCanvas sc, int keyCode)
    {
        keyPressed(sc, keyCode);
    }
}
