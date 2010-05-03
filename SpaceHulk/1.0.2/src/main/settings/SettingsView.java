package main.settings;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

import radui.Callback;
import radui.CallbackChainer;
import radui.MenuTheme;
import radui.MenuView;
import radui.ScreenCanvas;

public class SettingsView extends MenuView
{
    private ScreenCanvas sc_;
    private Settings settings_;
    private int soundIndex_;
    private int fullscreenIndex_;
    private int rotateIndex_;
    private int invertKeysIndex_;
    //private int defaultIndex_;

    public SettingsView(ScreenCanvas sc, MenuTheme mt, Settings settings, Callback back)
    {
        super(mt);
        sc_ = sc;
        settings_ = settings;

        Callback changeSoundCallback = new Callback()
            {
                public void perform()
                {
                    settings_.sound = !settings_.sound;
                }
            };
        Callback soundCallback = new Callback()
            {
                public void perform()
                {
                    setLabel(soundIndex_, getSoundLabel());
                    sc_.repaint();
                }
            };
        soundIndex_ = add(getSoundLabel(), new CallbackChainer(changeSoundCallback, soundCallback));

        Callback changeFullscreenCallback = new Callback()
            {
                public void perform()
                {
                    settings_.fullscreen = !settings_.fullscreen;
                }
            };
        Callback fullscreenCallback = new Callback()
            {
                public void perform()
                {
                    setLabel(fullscreenIndex_, getFullscreenLabel());
                    sc_.setFullScreenMode(settings_.fullscreen);
                    sc_.repaint();
                }
            };
        fullscreenIndex_ = add(getFullscreenLabel(), new CallbackChainer(changeFullscreenCallback, fullscreenCallback));

        Callback changeRotateCallback = new Callback()
            {
                public void perform()
                {
                    settings_.rotate = !settings_.rotate;
                }
            };
        Callback rotateCallback = new Callback()
            {
                public void perform()
                {
                    setLabel(rotateIndex_, getRotateLabel());
                    sc_.setRotate(settings_.rotate);
                    sc_.repaint();
                }
            };
        rotateIndex_ = add(getRotateLabel(), new CallbackChainer(changeRotateCallback, rotateCallback));

        Callback changeInvertCallback = new Callback()
            {
                public void perform()
                {
                    settings_.invertKeys = !settings_.invertKeys;
                }
            };
        Callback invertCallback = new Callback()
            {
                public void perform()
                {
                    setLabel(invertKeysIndex_, getInvertkeysLabel());
                    sc_.repaint();
                }
            };
        invertKeysIndex_ = add(getInvertkeysLabel(), new CallbackChainer(changeInvertCallback, invertCallback));

        add(null, null);

        Callback defaultCallback = new Callback()
            {
                public void perform()
                {
                    settings_.setDefaults();
                }
            };
        Callback defaultExtCallback = new CallbackChainer(
                new Callback[]{
                    defaultCallback, soundCallback,
                    fullscreenCallback, rotateCallback,
                    invertCallback});
        /*defaultIndex_ =*/ add("Restore defaults", defaultExtCallback);

        add(null, null);

        Callback extBack = new CallbackChainer(new Callback() {
            public void perform() {
                settings_.saveSettings();
            }
        }, back);

        add("Back", extBack);
    }

    private String getSoundLabel()
    {
        return "Sound: " + (settings_.sound ? "on" : "off");
    }

    private String getFullscreenLabel()
    {
        return "Full screen: " + (settings_.fullscreen? "on" : "off");
    }

    private String getRotateLabel()
    {
        return "Orientation: " + (settings_.rotate ? "landscape" : "portrait");
    }

    private String getInvertkeysLabel()
    {
        return "Invert keys: " + (settings_.invertKeys ? "on" : "off");
    }

    public int getMaxWidth()
    {
        // Lomgest label
        return getTheme().font.stringWidth("Orientation: landscape");
    }
}
