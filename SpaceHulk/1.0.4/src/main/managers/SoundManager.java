package main.managers;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.
import main.settings.Settings;
import java.io.InputStream;
import java.util.Vector;
import radui.Callback;
import util.MiscUtils;

public class SoundManager implements Callback {

    public final static String SELECT = "/sound/select.wav";
    public final static String BLIP_MOVE = "/sound/blip.wav";
    public final static String STEALER_MOVE = "/sound/hiss.wav";
    public final static String MARINE_MOVE = "/sound/marstep.wav";
    public final static String STEALER_DIED = "/sound/deadstealer.wav";
    public final static String MARINE_DIED = "/sound/deadmarine.wav";
    public final static String STEALER_ATTACK = "/sound/stealerattack.wav";
    public final static String MARINE_ATTACK = "/sound/airswing.wav";
    public final static String MARINE_ATTACK_HIT = "/sound/fisthit.wav";
    public final static String MARINE_SWORD_HIT = "/sound/swordhit.wav";
    public final static String MARINE_SWORD_MISS = "/sound/parry.wav";
    public final static String SHOOT_BOLTER = "/sound/bolter.wav";
    public final static String SHOOT_OVERWATCH = "/sound/overwatch.wav";
    public final static String SHOOT_FLAMER = "/sound/flamer.wav";
    public final static String SHOOT_MISS[] = {"/sound/ric1.wav", "/sound/ric2.wav", "/sound/ric3.wav", "/sound/ric4.wav", "/sound/ric5.wav"};
    public final static String SHOOT_MISS_DOOR = "/sound/ric_metal.wav";
    public final static String SHOOT_JAMS = "/sound/jam.wav";
    public final static String DOOR_OPEN = "/sound/opendoor.wav";
    public final static String DOOR_CLOSE = "/sound/closedoor.wav";
    public final static String DOOR_BLAST = "/sound/doorhit.wav";
    public final static String BULKHEAD_CLOSE = "/sound/bulkhd.wav";
    public final static String CLEAR_JAM = "/sound/clearjam.wav";
    public final static String SET_OVERWATCH = "/sound/setover.wav";
    private Settings settings_;

    final private Vector soundsQueue;
    private long lastSound;
    private int gcCounter;

    public SoundManager(Settings settings) {
        settings_ = settings;
        soundsQueue = new Vector();
        gcCounter = 0;
        lastSound = 0;
        //util.Debug.message("SoundManager::SoundManager " + System.getProperty("microedition.media.version"));
    }

    public void playSound(String filename) {
        //util.Debug.message("+SoundManager::playSound " + filename);
        if (settings_.sound) {
            try {
                //util.Debug.message("SoundManager::playSound getSupportedContentTypes " + Manager.getSupportedContentTypes(null));
                InputStream is = getClass().getResourceAsStream(MiscUtils.RESOURCES_FOLDER + filename);
                if (is != null) {
                    SoundPlayer sp = new SoundPlayer(this, is, "audio/X-wav");
                    gcCounter = (gcCounter + 1) % 10;
                    if(gcCounter == 0) {
                        // every ten sounds, run garbage collector
                        System.gc();
                    }
                    synchronized(soundsQueue) {
                        soundsQueue.addElement(sp);
                        if(soundsQueue.size() == 1) {
                            sp.playSound();
                        }
                    }
                } else {
                    util.Debug.warning("SoundManager::playSound unable to open " + filename);
                }
            } catch (Exception e) {
                util.Debug.warning("SoundManager::playSound Exception " + filename);
            }
            //util.Debug.message("-SoundManager::playSound " + filename);
        }
    }

    public void perform() {
        synchronized(soundsQueue) {
            long now = System.currentTimeMillis();
            if(now - lastSound < 300) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) { }
            }
            lastSound = now;
            if(soundsQueue.size() > 0) {
                soundsQueue.removeElementAt(0);
                if(soundsQueue.size() > 0) {
                    SoundPlayer sp = (SoundPlayer)soundsQueue.firstElement();
                    sp.playSound();
                }
            }
        }
    }

}
