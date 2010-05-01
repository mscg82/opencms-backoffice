package main;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import main.managers.ImageManager;
import main.settings.Settings;
import main.settings.SettingsView;
import radui.ButtonCallbackView;
import radui.Callback;
import radui.FillView;
import radui.ImageView;
import radui.MenuTheme;
import radui.MenuView;
import radui.ResizeView;
import radui.ScreenCanvas;
import radui.SpaceView;
import radui.StackView;
import radui.StringView;
import radui.VerticalView;
import radui.View;
import sh.Game;
import sh.GameHarbinger1;
import sh.GameHarbinger2;
import sh.GameHarbinger3;
import sh.GameHarbinger4;
import sh.GameHarbinger5;
import sh.GameHarbinger6;
import sh.GameSin1;
import sh.GameSin2;
import sh.GameSin3;
import sh.GameSin4;
import sh.GameSin5;
import sh.GameSin6;
import sh.GameSpawn1;
import sh.GameSpawn2;
import sh.GameSpawn3;
import sh.GameSpawn4;
import sh.GameSpawn5;
import sh.GameSpawn6;
import util.dice.Dice6;

public class SpaceHulk extends MIDlet {

    public static SpaceHulk instance;
    private MenuTheme mt_ = new MenuTheme();
    private ScreenCanvas sc_;
    private Image menuBackground_;
    private Font titleFont_ = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
    private Settings settings_;

    public SpaceHulk() {
    }

    public Settings getSettings() {
        return settings_;
    }

    public void startApp() throws MIDletStateChangeException {
        SpaceHulk.instance = this;
        settings_ = Settings.getInstance();
        try {
            mt_.font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_LARGE);
            mt_.color = 0xFF0000;
            mt_.colorSelected = 0x00FF00;
            menuBackground_ = ImageManager.load("/Launch.jpg");

            sc_ = new ScreenCanvas();
            sc_.setFullScreenMode(settings_.fullscreen);
            sc_.setRotate(settings_.rotate);

            Display display = Display.getDisplay(this);
            display.setCurrent(sc_);

            new InitMenuCallback().perform();
            sc_.repaint();
        } catch (Exception e) {
            showException("SpaceHulk::startApp", e);
        }
    }

    public void destroyApp(boolean unconditional) throws MIDletStateChangeException {
        settings_.close();
        
        // Print out the dice generated sequence
        Vector diceSequence = Dice6.getDice().getGeneratedSequence();
        if(diceSequence != null) {
        	System.out.println("Dice generated sequence: ");
        	System.out.print("[");
        	for(int i = 0, l = diceSequence.size(); i < l; i++) {
        		System.out.print((i == 0 ? "" : ", ") + diceSequence.elementAt(i));
        	}
        	System.out.println("]");
        }
    }

    public void pauseApp() {
        settings_.close();
    }

    public void showException(String function, Exception e) {
        System.out.println("Exception caught in " + function);
        e.printStackTrace();

        Alert alert = new Alert(getAppProperty("MIDlet-Name") + ": Exception");
        alert.setType(AlertType.ERROR);
        alert.setTimeout(Alert.FOREVER);
        alert.setString("Exception caught in " + function + ": " + e.toString());
        //alert.setImage(errorImage);

        Display display = Display.getDisplay(this);
        display.setCurrent(alert);
    }

    public void exit() {
        try {
            destroyApp(false);
            notifyDestroyed();
        } catch (MIDletStateChangeException ex) {
        }
    }

    private class InitMenuCallback implements Callback {

        public void perform() {
            MenuView mv = new MenuView(mt_);
            mv.add(CampaignsMenuCallback.title_, new CampaignsMenuCallback(this));
            mv.add("About", new Callback() {

                public void perform() {
                    showAbout(InitMenuCallback.this);
                }
            });
            mv.add("Settings", new Callback() {

                public void perform() {
                    showSettings(InitMenuCallback.this);
                }
            });
            mv.add("Exit", new Callback() {

                public void perform() {
                    exit();
                }
            });
            showMenu(getAppProperty("MIDlet-Name"), mv, null);
        }
    }

    private static class Mission {

        String name;
        String introFile;
        //String mapFile;
        Class gameClass;

//        Mission(String name, String introFile, String mapFile) {
//            this.name = name;
//            this.introFile = introFile;
//            this.mapFile = mapFile;
//        }

        Mission(String name, String introFile, Class gameClass) {
            this.name = name;
            this.introFile = introFile;
            this.gameClass = gameClass;
        }
    }
    private Mission sinOfDamnation_[] = {
        new Mission(GameSin1.NAME, GameSin1.INTRO, GameSin1.class),
        new Mission(GameSin2.NAME, GameSin2.INTRO, GameSin2.class),
        new Mission(GameSin3.NAME, GameSin3.INTRO, GameSin3.class),
        new Mission(GameSin4.NAME, GameSin4.INTRO, GameSin4.class),
        new Mission(GameSin5.NAME, GameSin5.INTRO, GameSin5.class),
        new Mission(GameSin6.NAME, GameSin6.INTRO, GameSin6.class),};
    private Mission spawnOfExecration_[] = {
        new Mission(GameSpawn1.NAME, GameSpawn1.INTRO, GameSpawn1.class),
        new Mission(GameSpawn2.NAME, GameSpawn2.INTRO, GameSpawn2.class),
        new Mission(GameSpawn3.NAME, GameSpawn3.INTRO, GameSpawn3.class),
        new Mission(GameSpawn4.NAME, GameSpawn4.INTRO, GameSpawn4.class),
        new Mission(GameSpawn5.NAME, GameSpawn5.INTRO, GameSpawn5.class),
        new Mission(GameSpawn6.NAME, GameSpawn6.INTRO, GameSpawn6.class),};
    private Mission harbingerOfDespair_[] = {
        new Mission(GameHarbinger1.NAME, GameHarbinger1.INTRO, GameHarbinger1.class),
        new Mission(GameHarbinger2.NAME, GameHarbinger2.INTRO, GameHarbinger2.class),
        new Mission(GameHarbinger3.NAME, GameHarbinger3.INTRO, GameHarbinger3.class),
        new Mission(GameHarbinger4.NAME, GameHarbinger4.INTRO, GameHarbinger4.class),
        new Mission(GameHarbinger5.NAME, GameHarbinger5.INTRO, GameHarbinger5.class),
        new Mission(GameHarbinger6.NAME, GameHarbinger6.INTRO, GameHarbinger6.class),};

    private static class Campaign {

        String name;
        String introFile;
        Mission missions[];

        Campaign(String name, String introFile, Mission missions[]) {
            this.name = name;
            this.introFile = introFile;
            this.missions = missions;
        }
    }
    private Campaign campaigns_[] = {
        new Campaign("Sin of Damnation", "/maps/10.html", sinOfDamnation_),
        new Campaign("Spawn of Execration", "/maps/20.html", spawnOfExecration_),
        new Campaign("Harbinger of Despair", "/maps/30.html", harbingerOfDespair_),};

    private class CampaignsMenuCallback implements Callback {

        private static final String title_ = "Campaigns";
        private Callback back_;

        CampaignsMenuCallback(Callback back) {
            back_ = back;
        }

        public void perform() {
            MenuView mv = new MenuView(mt_);
            for (int i = 0; i < campaigns_.length; ++i) {
                mv.add(campaigns_[i].name, new CampaignCallback(campaigns_[i], this));
            }
            mv.add(null, null);
            mv.add("Back", back_);
            showMenu(title_, mv, back_);
        }
    }

    private class CampaignCallback implements Callback {

        private Campaign campaign_;
        private Callback back_;

        CampaignCallback(Campaign campaign, Callback back) {
            campaign_ = campaign;
            back_ = back;
        }

        public void perform() {
            MenuView mv = new MenuView(mt_);
            mv.add("Background", new HtmlCallback(campaign_.introFile, this, new HtmlCallback(campaign_.missions[0].introFile, back_, new MissionCallback(campaign_.missions, 0, this))));
            for (int i = 0; i < campaign_.missions.length; ++i) {
                mv.add(campaign_.missions[i].name, new HtmlCallback(campaign_.missions[i].introFile, back_, new MissionCallback(campaign_.missions, i, this)));
            }
            mv.add(null, null);
            mv.add("Back", back_);
            showMenu(campaign_.name, mv, back_);
        }
    }

    private View createTitle(String title) {
        StringView sv = new StringView(title);
        sv.setFont(titleFont_);
        sv.setColor(mt_.colorSelected);
        sv.setAnchor(Graphics.TOP | Graphics.HCENTER);
        return sv;
    }

    private void showMenu(String title, MenuView mv, Callback back) {
        View tv = createTitle(title);

        StackView svMenu = new StackView();
        svMenu.add(new FillView(0x80000000, true));
        svMenu.add(mv);

        ResizeView rv = new ResizeView(Math.min(mv.getMaxWidth() + 4, sc_.getWidth()),
                Math.min(mv.getMaxHeight() + 4, sc_.getHeight() - 2 * tv.getMaxHeight()));
        rv.setView(svMenu);

        StackView sv = new StackView();
        sv.add(new ImageView(menuBackground_));
        sv.add(tv);
        sv.add(rv);
        if (back != null) {
            ButtonCallbackView bvc = new ButtonCallbackView();
            bvc.addKeyCode(ScreenCanvas.BACK, back);
            bvc.addKeyCode(ScreenCanvas.RIGHT_SOFTKEY, back);
            sv.add(bvc);
        }
        sc_.setView(sv);
    }

    private void showAbout(Callback back) {
        VerticalView vv = new VerticalView();
        View titleView = createTitle(getAppProperty("MIDlet-Name"));
        vv.add(titleView);
        vv.add(new SpaceView(20, 20));
        vv.add(new ImageView(ImageManager.load("/icon.png")));
        vv.add(new SpaceView(20, 20));
        vv.add(new StringView("ver " + getAppProperty("MIDlet-Version")));
        vv.add(new StringView("by " + getAppProperty("MIDlet-Vendor")));
        vv.add(new StringView("http://spacehulkme.sf.net"));
        //vv.add(new SpaceView(20, 20));
        //vv.add(new StringView(System.getProperty("microedition.platform")));
        //vv.add(new StringView(System.getProperty("microedition.configuration")));
        //vv.add(new StringView(System.getProperty("microedition.profiles")));
        //vv.add(new StringView("JSR 135 " + System.getProperty("microedition.media.version")));
        if (back != null) {
            util.Debug.message("add back");
            ButtonCallbackView bvc = new ButtonCallbackView();
            bvc.addKeyCode(ScreenCanvas.BACK, back);
            bvc.addKeyCode(ScreenCanvas.RIGHT_SOFTKEY, back);
            bvc.addKeyCode(ScreenCanvas.LEFT_SOFTKEY, back);
            bvc.addGameAction(ScreenCanvas.FIRE, back);
            vv.add(bvc);
        }
        // TODO The vv.getHeight(w) is getting into an infinite loop
        //int w = titleView.getMaxWidth();
        //ResizeView rr = new ResizeView(w, vv.getHeight(w));
        //rr.setView(vv);
        //sc_.setView(rr);
        sc_.setView(vv);
    }

    private void showSettings(Callback back) {
        showMenu("Settings", new SettingsView(sc_, mt_, settings_, back), back);
    }

    private class HtmlCallback implements Callback {

        private String file_;
        private Callback back_;
        private Callback next_;

        HtmlCallback(String file, Callback back, Callback next) {
            file_ = file;
            back_ = back;
            next_ = next;
        }

        public void perform() {
            try {
                html.Html h = new html.Html(file_);
                StackView sv = new StackView();
                View hv = h.createView();
                sv.add(hv);
                ButtonCallbackView bcv = new ButtonCallbackView();
                bcv.addKeyCode(ScreenCanvas.LEFT_SOFTKEY, next_);
                bcv.addKeyCode(ScreenCanvas.RIGHT_SOFTKEY, back_);
                bcv.addKeyCode(ScreenCanvas.BACK, back_);
                bcv.addGameAction(ScreenCanvas.FIRE, next_);
                sv.add(bcv);
                sc_.setView(sv);
            } catch (Exception e) {
                showException("HtmlCallback::perform", e);
            }
        }
    }

    private class MissionCallback implements Callback {

        private Mission missions_[];
        private int index_;
        private Callback back_;

        MissionCallback(Mission missions[], int index, Callback back) {
            missions_ = missions;
            index_ = index;
            back_ = back;
        }

        public void perform() {
            try {
                {
                    StringView sv = new StringView("Loading...");
                    sv.setFont(titleFont_);
                    sv.setColor(mt_.colorSelected);
                    sc_.setView(sv);
                    sc_.repaint();
                    sc_.serviceRepaints();
                }

                Callback next = null;
                if ((index_ + 1) < missions_.length) {
                    next = new HtmlCallback(missions_[index_ + 1].introFile, back_, new MissionCallback(missions_, index_ + 1, back_));
                }

                Game game = (Game) missions_[index_].gameClass.newInstance();

                GameView gv = new GameView(sc_, settings_, game, new InitMenuCallback(), next);
                sc_.setView(gv);
                gv.start();
            } catch (Exception e) {
                showException("MissionCallback::perform " + missions_[index_].name, e);
            }
        }
    }
}
