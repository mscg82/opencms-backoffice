package main.managers;

// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

import java.io.IOException;
import javax.microedition.lcdui.Image;
import util.MiscUtils;

public class ImageManager
{
    public ImageManager() {
    }
    
    public static Image load(String f) {
        Image i = null;
        try
        {
            i = Image.createImage(MiscUtils.RESOURCES_FOLDER + f);
        }
        catch (IOException ex)
        {
            System.out.println("Exception loading image " + f);
            ex.printStackTrace();
        }
        return i;
    }
}
