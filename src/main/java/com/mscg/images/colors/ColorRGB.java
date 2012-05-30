package com.mscg.images.colors;

public class ColorRGB {

    public int red;
    public int green;
    public int blue;

    public static ColorRGB fromIntColor(int color) {
        ColorRGB colorRGB = new ColorRGB();
        colorRGB.red = ColorHelper.getRed(color);
        colorRGB.green = ColorHelper.getGreen(color);
        colorRGB.blue = ColorHelper.getBlue(color);
        return colorRGB;
    }

    public int toIntColor() {
        return ColorHelper.getRGB(red, green, blue);
    }

}
