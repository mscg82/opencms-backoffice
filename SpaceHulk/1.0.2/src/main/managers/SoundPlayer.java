/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.managers;

import java.io.InputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

import radui.Callback;

/**
 *
 * @author Giuseppe Miscione
 */
public class SoundPlayer implements Runnable, PlayerListener {

//    private static int nextID = 0;

//    private int id;
    private InputStream is;
    private String mimeType;
    private Player player_;
    private Callback callback;

    public SoundPlayer(Callback callback, InputStream is, String mimeType) {
        this.callback = callback;
        this.is = is;
        this.mimeType = mimeType;
//        this.id = nextID++;
    }

    public void playSound() {
        new Thread(this).start();
    }

    public void run() {
        try {
            player_ = Manager.createPlayer(is, mimeType);
            player_.addPlayerListener(this);
            if (player_ != null) {
                player_.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
            try {
                callback.perform();
            } catch(Exception e2){}
        }
    }

    public void playerUpdate(Player player, String event, Object eventData) {
        if(event.equals(PlayerListener.END_OF_MEDIA)){
            try {
                player_.close();
                player_ = null;
                is.close();
            } catch(Exception e){
                e.printStackTrace();
            }
            try {
                callback.perform();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
