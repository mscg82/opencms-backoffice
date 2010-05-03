// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package radui;

import java.util.Hashtable;

public class ButtonCallbackView extends View
{
    private Hashtable keyCodes_ = new Hashtable();
    private Hashtable gameActions_ = new Hashtable();

    public ButtonCallbackView()
    {
    }

    public ButtonCallbackView(int keyCode, Callback c)
    {
        addKeyCode(keyCode, c);
    }

    public void addKeyCode(int keyCode, Callback c)
    {
        keyCodes_.put(new Integer(keyCode), c);
    }

    public void addGameAction(int gameAction, Callback c)
    {
        gameActions_.put(new Integer(gameAction), c);
    }

    public void keyPressed(ScreenCanvas sc, int keyCode)
    {
        Callback c = (Callback) keyCodes_.get(new Integer(keyCode));
        if (c == null)
        {
            int gameAction = sc.getGameAction(keyCode);
            c = (Callback) gameActions_.get(new Integer(gameAction));
        }

        if (c != null)
            c.perform();
    }
}
