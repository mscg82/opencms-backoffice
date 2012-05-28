package com.mscg.images;

public class ColorRGB {

    public int red;
    public int green;
    public int blue;

    public static ColorRGB fromIntColor(int color) {
        ColorRGB colorRGB = new ColorRGB();
        colorRGB.red = ColorHelper.getRed(color);
        colorRGB.green = ColorHelper.getBlue(color);
        colorRGB.blue = ColorHelper.getGreen(color);
        return colorRGB;
    }

    public int toIntColor() {
        return ColorHelper.getRGB(red, green, blue);
    }

}
