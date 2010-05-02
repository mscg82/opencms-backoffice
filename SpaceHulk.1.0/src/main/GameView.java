package main;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import main.managers.SoundManager;
import main.managers.Tile;
import main.managers.TileManager;
import main.settings.Settings;
import main.settings.SettingsView;
import path.AStar;
import path.IPathBoard;
import radui.BorderView;
import radui.ButtonCallbackView;
import radui.Callback;
import radui.FillView;
import radui.FrameView;
import radui.MenuTheme;
import radui.MenuView;
import radui.ResizeView;
import radui.ScreenCanvas;
import radui.StackView;
import radui.StringView;
import radui.TextView;
import radui.VerticalView;
import radui.View;
import sh.Blip;
import sh.CommandPoints;
import sh.DoorState;
import sh.Game;
import sh.GameListener;
import sh.Map;
import sh.Marine;
import sh.Piece;
import sh.Stealer;
import sh.TileType;
import util.Direction;
import util.Point2I;

public class GameView extends View implements GameListener
{
    private class ClearJammedCallback implements Callback
    {
        public void perform()
        {
            Marine m = mainView_.getActive();
            if (m != null)
            {
                sm_.playSound(SoundManager.CLEAR_JAM);
                game_.clearJammed(m);
            }
        }
    }
    private class ClearMsg implements Callback
    {
        public void perform()
        {
            msgView_ = null;
        }
    }
    private class DropCallback implements Callback
    {
        public void perform()
        {
            Marine m = mainView_.getActive();
            if (m != null)
                game_.drop(m);
        }
    }
    private class EndTurnCallback implements Callback
    {
    	public void perform()
        {
            interactive_ = false;
            mainView_.setActive(null);
            boolean over = game_.endTurn();
            interactive_ = true;
            if (over)
            {
                String title[] = { "Continue", "Mission Completed", "Mission Failed", "Mission Drawn" };
                int status = game_.getStatus();
                Callback next = back_;
                if (next_ != null && (status == Game.STATUS_COMPLETED || status == Game.STATUS_DRAWN))
                    next = next_;
                showMsg(title[status], game_.getStatusText(status), next);
            }
            else
            {
                mainView_.setActive(game_.getNextMarine(null));
                mainView_.setFocus(mainView_.getActive());
            }
        }
    }
    private static class MarinePathBoard implements IPathBoard
    {
        private Map map_;
        private Marine m_;
        private int x_;
        private int y_;

        MarinePathBoard(Map map, Marine m, int x, int y)
        {
            map_ = map;
            m_ = m;
            x_ = x;
            y_ = y;
        }

        public boolean isEnd(int x, int y)
        {
            //util.Debug.message("MarinePathBoard::isEnd is " + x + " " + y);
            return x == x_ && y == y_;
        }

        public boolean isObstacle(int x, int y)
        {
            //util.Debug.message("MarinePathBoard::isObstacle " + x + " " + y);
            boolean surroundingCell = Math.abs(x - m_.getPosX()) <= 1
                && Math.abs(y - m_.getPosY()) <= 1;
            //util.Debug.message("MarinePathBoard::isObstacle surroundingCell " + surroundingCell);
            return !map_.canMove(x, y, false, false, surroundingCell);
        }
    }
    private class OverwatchCallback implements Callback
    {
        public void perform()
        {
            Marine m = mainView_.getActive();
            if (m != null)
            {
                sm_.playSound(SoundManager.SET_OVERWATCH);
                game_.toggleOverwatch(m);
            }
        }
    }
    private class GuardCallback implements Callback
    {
        public void perform()
        {
            Marine m = mainView_.getActive();
            if (m != null)
            {
                sm_.playSound(SoundManager.SET_OVERWATCH);
                game_.toggleGuard(m);
            }
        }
    }
    private class PickupCallback implements Callback
    {
        public void perform()
        {
            Marine m = mainView_.getActive();
            if (m != null)
                game_.pickup(m);
        }
    }
    private class ShowObjective implements Callback
    {
        public void perform()
        {
            showMsg("Mission Objective", game_.getStatusText(Game.STATUS_NONE), new ClearMsg());
        }
    }
    private class ShowSettings implements Callback
    {
        public void perform()
        {
            Callback back = new ClearMsg();

            SettingsView settingsView = new SettingsView(sc_, menuTheme_, settings_, back);

            StackView sv = new StackView();
            sv.add(new FillView(0x80000000, true));
            sv.add(settingsView);

            ResizeView rv = new ResizeView(sv.getMaxWidth() + 4, sv.getMaxHeight() + 2);
            //rv.setAnchor(Graphics.BOTTOM | Graphics.RIGHT);
            rv.setView(sv);

            msgView_ = rv;
        }
    }

    private class TakeCallback implements Callback
    {
        public void perform()
        {
            Marine m = mainView_.getActive();
            if (m != null)
                game_.take(m);
        }
    }
    private class ToggleZoom implements Callback
    {
        public void perform()
        {
            Tile.zoom_ = !Tile.zoom_;
            sc_.repaint();
        }
    }
    private static void sleep(int sleep)
    {
        try
        {
            Thread.sleep(sleep);
        }
        catch (InterruptedException e)
        {
        }
    }
    protected Game game_;
    private ScreenCanvas sc_;

    private Settings settings_;

    private Callback back_;
    
    private Callback next_;

    private SoundManager sm_;

    private MenuTheme menuTheme_;

    private Random r_ = new Random();

    private boolean interactive_ = true;

    private MainView mainView_;
    
    private View altView_;

    private View menuView_ = null;

    private View msgView_ = null;

    private boolean showMiniMap_ = false;

    GameView(ScreenCanvas sc, Settings settings, Game game, Callback back, Callback next)
    {
        sc_ = sc;
        settings_ = settings;
        game_ =  game;
        game_.setListener(this);
        back_ = back;
        next_ = next;
        sm_ = new SoundManager(settings_);

        menuTheme_ = new MenuTheme();
        menuTheme_.color = 0x00FF00;
        menuTheme_.colorSelected = 0xFF0000;

        mainView_ = new MainView(sm_, game_);
        
        {
            StringView titlev = new StringView(game_.getName());
            //titlev.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
            titlev.setAnchor(Graphics.TOP | Graphics.HCENTER);
        
            StackView sv = new StackView();
            sv.add(new MiniMapView(game_.getMap(), 4, 4, new Point2I(mainView_.getPosX(), mainView_.getPosY())));
            sv.add(titlev);
            
            altView_ = sv;
        }
    }

    public void askResetCommandPoints(CommandPoints cp, Random r) {
    	Callback back = new ClearMsg();

        CommandPointsView cpv = new CommandPointsView(sc_, menuTheme_, cp, r, back);

        StackView sv = new StackView();
        sv.add(new FillView(0x80000000, true));
        sv.add(cpv);

        ResizeView rv = new ResizeView(sv.getMaxWidth() + 4, sv.getMaxHeight() + 2);
        //rv.setAnchor(Graphics.BOTTOM | Graphics.RIGHT);
        rv.setView(sv);

        msgView_ = rv;
        
        sc_.repaint();
	}

    public void beginTurn(Piece p)
    {
        //util.Debug.message("GameView::beginTurn " + p.getClass());
        if (p instanceof Blip)
        {
            sm_.playSound(SoundManager.BLIP_MOVE);
            mainView_.setFocus(p);
            updateScreen();
            sleep(200);
        }
        else if (p instanceof Stealer)
        {
            sm_.playSound(SoundManager.STEALER_MOVE);
            mainView_.setFocus(p);
            updateScreen();
            sleep(200);
        }
    }

    public void doorStateChanged(int x, int y, int type, DoorState ds)
    {
        //util.Debug.message("GameView::doorStateChanged " + x + ", " + y + " " + ds.getChar());
        if (ds == DoorState.PARTIAL)
        {
            updateScreen();
            sleep(200);
        }
        else
        {
            if (interactive_)
                sc_.repaint();
            else
            {
                updateScreen();
                sleep(200);
            }
        }
    }

    public void doorStateChanging(int x, int y, int type, DoorState oldds, DoorState newds)
    {
        if (newds == DoorState.OPEN)
        {
            sm_.playSound(SoundManager.DOOR_OPEN);
        }
        else if (newds == DoorState.CLOSED)
        {
            if (type == TileType.BULKHEAD)
                sm_.playSound(SoundManager.BULKHEAD_CLOSE);
            else
                sm_.playSound(SoundManager.DOOR_CLOSE);
        }
        else if (newds == DoorState.BLASTED)
        {
            sm_.playSound(SoundManager.DOOR_BLAST);
        }
    }

    public void keyPressed(ScreenCanvas sc, int keyCode)
    {
        int gameAction = sc.getGameAction(keyCode);

        if (msgView_ != null)
        {
            msgView_.keyPressed(sc, keyCode);
        }
        else if (menuView_ != null)
        {
            View mv = menuView_;
            if (keyCode == ScreenCanvas.RIGHT_SOFTKEY)
                menuView_ = null;
            else if (keyCode == ScreenCanvas.BACK)
                menuView_ = null;
            else if (gameAction == ScreenCanvas.FIRE)
                menuView_ = null;
            mv.keyPressed(sc, keyCode);
        }
        else
        {
            if (showMiniMap_)
                altView_.keyPressed(sc, keyCode);
            else
                mainView_.keyPressed(sc, keyCode);

            if (keyCode == ScreenCanvas.RIGHT_SOFTKEY)
                showMenu();
            else if (keyCode == ScreenCanvas.LEFT_SOFTKEY)
                showMiniMap_ = !showMiniMap_;
            else if (gameAction == ScreenCanvas.FIRE)
            {
                final Marine m = mainView_.getActive();
                if (m != null)
                {
                    //util.Debug.message("GameView::keyPressed move marine from " + m.getPosX() + " " + m.getPosY());
                    //util.Debug.message("GameView::keyPressed move marine to   " + mainView_.getPosX() + " " + mainView_.getPosY());
                    AStar as = new AStar();
                    IPathBoard pb = new MarinePathBoard(game_.getMap(), m, mainView_.getPosX(), mainView_.getPosY());
                    if (as.findPath(m.getPosX(), m.getPosY(), pb))
                    {
                        //util.Debug.message("GameView::keyPressed found path");
                        Vector path = as.getPath();
                        Point2I p = (Point2I) path.firstElement();
                        int dir = Direction.getDirection(m.getPosX(), m.getPosY(), p.x, p.y);
                        game_.move(m, dir, path.size() > 1);
                        if (m.isDeleted())
                            mainView_.setActive(game_.getNextMarine(m));
                    }
                    //util.Debug.message("GameView::keyPressed end move marine");
                }
            }
            else if ((!settings_.invertKeys && gameAction == ScreenCanvas.GAME_A) ||
                    (settings_.invertKeys && gameAction == ScreenCanvas.GAME_B))
            {
                //util.Debug.message("GameView::keyPressed select next marine");
            	Marine actual = mainView_.getActive();
            	Marine next = game_.getNextMarine(actual);
                mainView_.setActive(next);
                // clear actual Marine action points if
    			// he had made some actions
    			if(next != null && actual.getActionPoints() < Marine.MAX_MARINE_ACTION_POINTS)
    				actual.clearActionPoints();
                sm_.playSound(SoundManager.SELECT);
            }
            else if ((!settings_.invertKeys && gameAction == ScreenCanvas.GAME_B) ||
                    (settings_.invertKeys && gameAction == ScreenCanvas.GAME_A))
            {
                //util.Debug.message("GameView::keyPressed select prev marine");
            	Marine actual = mainView_.getActive();
                Marine prev = game_.getPrevMarine(actual);
                mainView_.setActive(prev);
                // clear actual Marine action points if
    			// he had made some actions
    			if(prev != null && actual.getActionPoints() < Marine.MAX_MARINE_ACTION_POINTS)
    				actual.clearActionPoints();
                sm_.playSound(SoundManager.SELECT);
            }
            else if ((!settings_.invertKeys && gameAction == ScreenCanvas.GAME_C) ||
                    (settings_.invertKeys && gameAction == ScreenCanvas.GAME_D))
            {
                Marine m = mainView_.getActive();
                if (m != null)
                {
                    int dir = Direction.getDirection(m.getPosX(), m.getPosY(), mainView_.getPosX(), mainView_.getPosY());
                    boolean surroundingCell = ((m.getPosX() + Direction.getOffsetX(dir)) == mainView_.getPosX())
                        && ((m.getPosY() + Direction.getOffsetY(dir)) == mainView_.getPosY());

                    //util.Debug.message("GameView::keyPressed surroundingCell " + surroundingCell);
                    if (surroundingCell && game_.toggleDoor(m, dir))
                    {
                        //util.Debug.message("GameView::keyPressed done toggleDoor");
                        // done
                    }
                    else
                    {
                        Piece p = game_.getMap().getPiece(mainView_.getPosX(), mainView_.getPosY());
                        if (!(p instanceof Marine))
                        {
                            //util.Debug.message("GameView::keyPressed shoot");
                            game_.shoot(mainView_.getActive(), mainView_.getPosX(), mainView_.getPosY());
                        }
                    }
                }
            }
        }

        sc.repaint();
    }

    public void keyRepeated(ScreenCanvas sc, int keyCode)
    {
        if (msgView_ == null && menuView_ == null && !showMiniMap_)
            mainView_.keyPressed(sc, keyCode);
    }

    public void objectDamaged(int x, int y, int object)
    {
        if (object == TileType.OBJECT_CAT)
            showMsg("C.A.T. Damaged", "The C.A.T. has been damaged", new ClearMsg());
    }

    public void objectDestroyed(int x, int y, int object)
    {
        if (object == TileType.OBJECT_CAT)
            showMsg("C.A.T. Destroyed", "The C.A.T. has been destroyed", new ClearMsg());
    }

    public void objectDropped(Marine m, int object)
    {
        if (object == TileType.OBJECT_CAT)
            showMsg("C.A.T. Dropped", "The C.A.T. has been dropped", new ClearMsg());
    }

    public void objectUsed(int x, int y, int object, String msg)
    {
        //if (object == TileType.OBJECT_CARGO) // Spawn1, Harbinger3
        //if ((object == TileType.OBJECT_CONTROLS?) // Harbinger4
        //if ((object == TileType.OBJECT_AIRPUMP?) // Harbinger6
        showMsg(null, msg, new ClearMsg());
    }

    public void paint(Graphics g, int w, int h)
    {
        //util.Debug.message("+GameView::paint");
        if (showMiniMap_)
            altView_.paint(g, w, h);
        else
            mainView_.paint(g, w, h);
        if (menuView_ != null)
            menuView_.paint(g, w, h);
        if (interactive_ && msgView_ != null)
            msgView_.paint(g, w, h);
        //util.Debug.message("-GameView::paint");
    }

    public void pieceCloseCombat(Piece p)
    {
        if (p instanceof Marine)
            sm_.playSound(SoundManager.MARINE_ATTACK);
        else if (p instanceof Stealer)
        {
            sm_.playSound(SoundManager.STEALER_ATTACK);
            for (int i = 0; i < 4; ++i)
            {
                int a = p.getAnimation() + 1;
                if (a >= TileManager.STEALER_COUNT)
                    a = 0;
                p.setAnimation(a);
                updateScreen();
                sleep(100);
            }
        }
    }

    public void pieceCloseCombatHit(Piece p)
    {
        if (p instanceof Marine)
        {
            Marine m = (Marine) p;
            if (m.getType() == Marine.SERGEANT)
                sm_.playSound(SoundManager.MARINE_SWORD_HIT);
            else
                sm_.playSound(SoundManager.MARINE_ATTACK_HIT);
        }
    }

    public void pieceCloseCombatMiss(Piece p)
    {
        if (p instanceof Marine)
        {
            Marine m = (Marine) p;
            if (m.getType() == Marine.SERGEANT)
                sm_.playSound(SoundManager.MARINE_SWORD_MISS);
        }
    }

    public void pieceCloseCombatObject(Piece p, int x, int y, int object)
    {
    }

    public void pieceDied(Piece p)
    {
        if (p instanceof Marine)
        {
            sm_.playSound(SoundManager.MARINE_DIED);
            updateScreen();
            sleep(200);
        }
        else if (p instanceof Stealer)
        {
            sm_.playSound(SoundManager.STEALER_DIED);
            updateScreen();
            sleep(200);
        }
    }

    public void pieceJams(Piece p)
    {
        util.Debug.assert2(p instanceof Marine, "GameView::pieceJams not Marine?");
        sm_.playSound(SoundManager.SHOOT_JAMS);
    }

    public void pieceMoved(Piece p)
    {
        //util.Debug.message("GameView::pieceMoved " + p.getClass());
        if (p instanceof Blip)
        {
            mainView_.setFocus(p);
            for (int i = (TileManager.BLIP_COUNT - 1); i >= 0; --i)
            {
                p.setAnimation(i);
                updateScreen();
                sleep(25);
            }
        }
        else if (p instanceof Stealer)
        {
            int a = p.getAnimation() + 1;
            if (a >= TileManager.STEALER_COUNT)
                a = 0;
            p.setAnimation(a);
            //util.Debug.message("GameView::pieceMoved stealer " + a);
            mainView_.setFocus(p);
            updateScreen();
            sleep(200);
        }
        else if (p instanceof Marine)
        {
            sm_.playSound(SoundManager.MARINE_MOVE);
            // TODO move sc_.repaint(); here
        }
    }

    public void pieceMoving(Piece p)
    {
        //util.Debug.message("GameView::pieceMoving " + p.getClass());
        if (p instanceof Blip)
        {
            mainView_.setFocus(p);
            for (int i = 0; i < TileManager.BLIP_COUNT; ++i)
            {
                p.setAnimation(i);
                updateScreen();
                sleep(25);
            }
        }
    }

    public void pieceShoots(Piece p)
    {
        util.Debug.assert2(p instanceof Marine, "GameView::pieceShoots not Marine?");
        Marine m = (Marine) p;
        if (m.getType() == Marine.FLAMER)
            sm_.playSound(SoundManager.SHOOT_FLAMER);
        else if (m.getOverwatch())
            sm_.playSound(SoundManager.SHOOT_OVERWATCH);
        else
            sm_.playSound(SoundManager.SHOOT_BOLTER);
        updateScreen();
        sleep(200);
    }

    public void pieceShootsMiss(Piece p)
    {
        util.Debug.assert2(p instanceof Marine, "GameView::pieceShootsMiss not Marine?");
        sm_.playSound(SoundManager.SHOOT_MISS[r_.nextInt(SoundManager.SHOOT_MISS.length)]);
    }

    public void pieceShootsMissDoor(Piece p)
    {
        util.Debug.assert2(p instanceof Marine, "GameView::pieceShootsMissDoor not Marine?");
        sm_.playSound(SoundManager.SHOOT_MISS_DOOR);
    }

    private void showMenu() {
        Marine m = mainView_.getActive();
        MenuView menuView = new MenuView(menuTheme_);
        menuView.add("End Turn", new EndTurnCallback());
        if (m != null) {
            if (m.getCarrying() != 0)
                menuView.add("Drop", new DropCallback());
            if (game_.getMap().getItem(m.getPosX(), m.getPosY()) != 0)
                menuView.add("Pickup", new PickupCallback());
            if (game_.canTake(m))
                menuView.add("Take", new TakeCallback());
            if (m.getJammed())
                menuView.add("Clear Jam", new ClearJammedCallback());
            if (m.canOverwatch())
                menuView.add("Overwatch", new OverwatchCallback());
            menuView.add("Guard", new GuardCallback());
        }
        menuView.add("Objective", new ShowObjective());
        menuView.add("Zoom", new ToggleZoom());
        menuView.add("Settings", new ShowSettings());
        menuView.add("Quit", back_);

        BorderView bv = new BorderView(menuView, 3, 1);

        StackView sv = new StackView();
        sv.add(new FillView(0x80000000, true));
        sv.add(new FrameView(0xFFFFFF));
        sv.add(bv);

        ResizeView rv = new ResizeView(sv.getMaxWidth(), sv.getMaxHeight());
        rv.setAnchor(Graphics.BOTTOM | Graphics.RIGHT);
        rv.setView(sv);

        menuView_ = rv;
    }

    private void showMsg(String title, String msg, Callback next)
    {
        int w = 200;
        View v = null;
        if (title != null)
        {
            //util.Debug.message("GameView::keyPressed showMsg '" + title + "' '" + msg + "'");
            StringView titleView = new StringView(title);
            titleView.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
            titleView.setColor(menuTheme_.color);

            TextView msgView = new TextView(msg);
            msgView.setColor(menuTheme_.color);

            VerticalView vv = new VerticalView();
            vv.add(titleView);
            vv.add(msgView);

            w = titleView.getMaxWidth();
            v = vv;
        }
        else
        {
            TextView msgView = new TextView(msg);
            msgView.setColor(menuTheme_.color);

            w = sc_.getWidth() / 2;
            v = msgView;
        }

        int h = v.getHeight(w);
        //util.Debug.message("GameView::keyPressed showMsg " + w + " " + h);

        ButtonCallbackView bcv = new ButtonCallbackView();
        bcv.addKeyCode(ScreenCanvas.BACK, next);
        bcv.addGameAction(ScreenCanvas.FIRE, next);

        StackView sv = new StackView();
        sv.add(new FillView(0x80000000, true));
        sv.add(v);
        sv.add(bcv);

        ResizeView rv = new ResizeView(w + 4, h + 2);
        rv.setView(sv);

        msgView_ = rv;
    }

    void start()
    {
        game_.start();

        mainView_.setActive(game_.getNextMarine(null));
        mainView_.setFocus(mainView_.getActive());
        sc_.repaint();
    }

	private void updateScreen()
    {
        sc_.repaint();
        sc_.serviceRepaints();
    }
}
