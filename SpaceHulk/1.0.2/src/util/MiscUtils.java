// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package util;

import javax.microedition.lcdui.Image;

public class MiscUtils
{
    public static final String RESOURCES_FOLDER = "/resources";
    
    public static Image createRGBImage(int rgbPixel, int width, int height, boolean alpha)
    {
        int[] rgb = new int[width*height];
        for (int i = 0; i < width * height; ++i)
            rgb[i] = rgbPixel;
        return Image.createRGBImage(rgb, width, height, alpha);
    }

    public static Image scaleImage(Image src, int dstW, int dstH)
    {
        int[] rgbDataSrc = new int[src.getWidth()*src.getHeight()];
        src.getRGB(rgbDataSrc, 0, src.getWidth(), 0, 0, src.getWidth(), src.getHeight());

        int[] rgbDataDst = new int[dstW*dstH];
        for (int y = 0; y < dstH; ++y)
        {
            for (int x = 0; x < dstW; ++x)
            {
                int c = rgbDataSrc[(y * src.getHeight() / dstH) * src.getWidth() + (x * src.getWidth() / dstW)];
                rgbDataDst[y * dstW + x] = c;
            }
        }

        return Image.createRGBImage(rgbDataDst, dstW, dstH, true);
    }

    //public static int indexOfAny(String s, String f, int from)
    //{
    //    for (int i = from; i < s.length(); ++i)
    //    {
    //        if (f.indexOf(s.charAt(i)) != -1)
    //            return i;
    //    }
    //    return -1;
    //}

    public static int indexNotOfAny(String s, String f, int from)
    {
        for (int i = from; i < s.length(); ++i)
        {
            if (f.indexOf(s.charAt(i)) == -1)
                return i;
        }
        return -1;
    }

    public static int lastIndexOfAny(String s, String f, int from)
    {
        for (int i = from; i >= 0; --i)
        {
            if (f.indexOf(s.charAt(i)) != -1)
                return i;
        }
        return -1;
    }

    //public static int lastIndexNotOfAny(String s, String f, int from)
    //{
    //    for (int i = from; i >= 0; --i)
    //    {
    //        if (f.indexOf(s.charAt(i)) == -1)
    //            return i;
    //    }
    //    return -1;
    //}

    //public static int lastIndexNotOf(String s, char c, int from)
    //{
    //    for (int i = from; i >= 0; --i)
    //    {
    //        if (s.charAt(i) != c)
    //            return i;
    //    }
    //    return -1;
    //}
}
