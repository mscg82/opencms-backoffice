package com.mscg.images;

public class ColorHelper {

    public static enum ColorChannel {
        RED, GREEN, BLUE
    }

    public static int WHITE = getRGB(255, 255, 255);
    public static int BLACK = getRGB(0, 0, 0);
    public static int RED = getRGB(255, 0, 0);
    public static int GREEN = getRGB(0, 255, 0);
    public static int BLUE = getRGB(0, 0, 255);


    public static int getRGB(int r, int g, int b) {
        if(r < 0 || r > 255) throw new IllegalArgumentException("Invalid red value");
        if(g < 0 || g > 255) throw new IllegalArgumentException("Invalid green value");
        if(b < 0 || b > 255) throw new IllegalArgumentException("Invalid blue value");

        return r << 16 | g << 8 | b;
    }

    public static int negative(int color) {
        return WHITE - color;
    }

    public static int greyScaleRGB(int color) {
        int red = getRed(color);
        int green = getGreen(color);
        int blue = getBlue(color);

        int mean = (red + green + blue) / 3;
        return getRGB(mean, mean, mean);
    }

    public static int greyScaleHSV(int color) {
        ColorRGB colorRGB = ColorRGB.fromIntColor(color);
        ColorHSV colorHSV = RGBtoHSV(colorRGB);
        colorHSV.saturation = 0;
        return HSVtoRGB(colorHSV).toIntColor();
    }

    public static int greyScaleHSL(int color) {
        ColorRGB colorRGB = ColorRGB.fromIntColor(color);
        ColorHSL colorHSL = RGBtoHSL(colorRGB);
        colorHSL.saturation = 0;
        return HSLtoRGB(colorHSL).toIntColor();
    }

    public static int changeBrightnessRGB(int color, float factor) {
        if(factor < 0.0f) throw new IllegalArgumentException("Invalid factor value");

        int red = Math.min(Math.round(getRed(color) * factor), 255);
        int green = Math.min(Math.round(getGreen(color) * factor), 255);
        int blue = Math.min(Math.round(getBlue(color) * factor), 255);

        return getRGB(red, green, blue);
    }

    public static int changeBrightnessHSV(int color, float factor) {
        if(factor < 0.0f) throw new IllegalArgumentException("Invalid factor value");

        ColorRGB colorRGB = ColorRGB.fromIntColor(color);
        ColorHSV colorHSV = RGBtoHSV(colorRGB);
        colorHSV.value = Math.min(Math.round(colorHSV.value * factor), 255);

        return HSVtoRGB(colorHSV).toIntColor();
    }

    public static int changeBrightnessHSL(int color, float factor) {
        if(factor < 0.0f) throw new IllegalArgumentException("Invalid factor value");

        ColorRGB colorRGB = ColorRGB.fromIntColor(color);
        ColorHSL colorHSL = RGBtoHSL(colorRGB);
        colorHSL.lightness = Math.min(Math.round(colorHSL.lightness * factor), 255);

        return HSLtoRGB(colorHSL).toIntColor();
    }

    public static int getRed(int color) {
        return (color & 0xFF0000) >> 16;
    }

    public static int getGreen(int color) {
        return (color & 0x00FF00) >> 8;
    }

    public static int getBlue(int color) {
        return (color & 0x0000FF);
    }

    public static int switchChannel(int color, ColorChannel sourceChannel, ColorChannel destinationChannel) {
        int sourceVal = 0;
        switch(sourceChannel) {
        case RED:
            sourceVal = getRed(color);
            break;
        case BLUE:
            sourceVal = getBlue(color);
            break;
        case GREEN:
            sourceVal = getGreen(color);
            break;
        }

        int destVal = 0;
        switch(destinationChannel) {
        case RED:
            destVal = getRed(color);
            break;
        case BLUE:
            destVal = getBlue(color);
            break;
        case GREEN:
            destVal = getGreen(color);
            break;
        }

        int red = (sourceChannel == ColorChannel.RED ? destVal : (destinationChannel == ColorChannel.RED ? sourceVal : getRed(color)));
        int green = (sourceChannel == ColorChannel.GREEN ? destVal : (destinationChannel == ColorChannel.GREEN ? sourceVal : getGreen(color)));
        int blue = (sourceChannel == ColorChannel.BLUE ? destVal : (destinationChannel == ColorChannel.BLUE ? sourceVal : getBlue(color)));

        return getRGB(red, green, blue);
    }

    public static ColorHSL RGBtoHSL(ColorRGB colorRGB) {
        float r, g, b, h = 0, s = 0, l; // this function works with floats
                                        // between 0 and 1
        r = colorRGB.red / 255.0f;
        g = colorRGB.green / 255.0f;
        b = colorRGB.blue / 255.0f;

        float maxColor = Math.max(r, Math.max(g, b));
        float minColor = Math.min(r, Math.min(g, b));

        if(minColor == maxColor) { // R = G = B, so it's a shade of grey
            h = 0; // it doesn't matter what value it has
            s = 0;
            l = r; // doesn't matter if you pick r, g, or b
        }
        else {
            l = (minColor + maxColor) / 2.0f;

            float colorDiff = maxColor - minColor;
            if(l < 0.5)
                s = colorDiff / (maxColor + minColor);
            if(l >= 0.5)
                s = colorDiff / (2.0f - maxColor - minColor);

            if(r == maxColor)
                h = (g - b) / colorDiff;
            if(g == maxColor)
                h = 2.0f + (b - r) / colorDiff;
            if(b == maxColor)
                h = 4.0f + (r - g) / colorDiff;

            h /= 6.0f; // to bring it to a number between 0 and 1
            if(h < 0)
                h += 1.0f;
        }

        ColorHSL colorHSL = new ColorHSL();
        colorHSL.hue = 255 - Math.round(h * 255.0f);
        colorHSL.saturation = Math.round(s * 255.0f);
        colorHSL.lightness = Math.round(l * 255.0f);

        return colorHSL;
    }

    public static ColorRGB HSLtoRGB(ColorHSL colorHSL) {
        float r, g, b, h, s, l; // this function works with floats between 0 and
                                // 1
        float temp1, temp2, tempr, tempg, tempb;
        h = colorHSL.hue / 255.0f;
        s = colorHSL.saturation / 255.0f;
        l = colorHSL.lightness / 255.0f;

        // If saturation is 0, the color is a shade of grey
        if(s == 0)
            r = g = b = l;
        // If saturation > 0, more complex calculations are needed
        else {
            // set the temporary values
            if(l < 0.5)
                temp2 = l * (1 + s);
            else
                temp2 = (l + s) - (l * s);
            temp1 = 2 * l - temp2;
            tempr = h + 1.0f / 3.0f;
            if(tempr > 1.0f)
                tempr--;
            tempg = h;
            tempb = h - 1.0f / 3.0f;
            if(tempb < 0.0f)
                tempb++;

            // red
            if(tempr < 1.0f / 6.0f)
                r = temp1 + (temp2 - temp1) * 6.0f * tempr;
            else if(tempr < 0.5f)
                r = temp2;
            else if(tempr < 2.0f / 3.0f)
                r = temp1 + (temp2 - temp1) * ((2.0f / 3.0f) - tempr) * 6.0f;
            else
                r = temp1;

            // green
            if(tempg < 1.0f / 6.0f)
                g = temp1 + (temp2 - temp1) * 6.0f * tempg;
            else if(tempg < 0.5f)
                g = temp2;
            else if(tempg < 2.0f / 3.0f)
                g = temp1 + (temp2 - temp1) * ((2.0f / 3.0f) - tempg) * 6.0f;
            else
                g = temp1;

            // blue
            if(tempb < 1.0f / 6.0f)
                b = temp1 + (temp2 - temp1) * 6.0f * tempb;
            else if(tempb < 0.5f)
                b = temp2;
            else if(tempb < 2.0f / 3.0f)
                b = temp1 + (temp2 - temp1) * ((2.0f / 3.0f) - tempb) * 6.0f;
            else
                b = temp1;
        }

        ColorRGB colorRGB = new ColorRGB();
        colorRGB.red = Math.round(r * 255.0f);
        colorRGB.green = Math.round(g * 255.0f);
        colorRGB.blue = Math.round(b * 255.0f);
        return colorRGB;
    }

    public static ColorHSV RGBtoHSV(ColorRGB colorRGB) {
        float r, g, b, h = 0.0f, s = 0.0f, v; // this function works with floats between 0 and 1
        r = colorRGB.red / 256.0f;
        g = colorRGB.green / 256.0f;
        b = colorRGB.blue / 256.0f;

        float maxColor = Math.max(r, Math.max(g, b));
        float minColor = Math.min(r, Math.min(g, b));

        v = maxColor;

        float colorDiff = maxColor - minColor;
        if(maxColor != 0.0f) // avoid division by zero when the color is black
        {
            s = colorDiff / maxColor;
        }

        if(s == 0.0f) {
            h = 0.0f; // it doesn't matter what value it has
        } else {
            if(r == maxColor)
                h = (g - b) / colorDiff;
            if(g == maxColor)
                h = 2.0f + (b - r) / colorDiff;
            if(b == maxColor)
                h = 4.0f + (r - g) / colorDiff;

            h /= 6.0f; // to bring it to a number between 0 and 1
            if(h < 0.0f)
                h++;
        }

        ColorHSV colorHSV = new ColorHSV();
        colorHSV.hue = 255 - Math.round(h * 255.0f);
        colorHSV.saturation = Math.round(s * 255.0f);
        colorHSV.value = Math.round(v * 255.0f);
        return colorHSV;
    }

    // Converts an HSV color to RGB color
    public static ColorRGB HSVtoRGB(ColorHSV colorHSV) {
        float r, g, b, h, s, v; // this function works with floats between 0 and 1
        h = colorHSV.hue / 256.0f;
        s = colorHSV.saturation / 256.0f;
        v = colorHSV.value / 256.0f;

        // if saturation is 0, the color is a shade of grey
        if(s == 0.0f)
            r = g = b = v;
        // if saturation > 0, more complex calculations are needed
        else {
            float f, p, q, t;
            int i;
            h *= 6.0f; // to bring hue to a number between 0 and 6, better for
                       // the calculations
            i = (int) (Math.floor(h)); // e.g. 2.7 becomes 2 and 3.0f1 becomes 3
                                       // or 4.9999 becomes 4
            f = h - i;// the fractional part of h

            p = v * (1.0f - s);
            q = v * (1.0f - (s * f));
            t = v * (1.0f - (s * (1.0f - f)));

            switch (i) {
            case 0:
                r = v;
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = v;
                b = p;
                break;
            case 2:
                r = p;
                g = v;
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = v;
                break;
            case 4:
                r = t;
                g = p;
                b = v;
                break;
            case 5:
                r = v;
                g = p;
                b = q;
                break;
            default:
                r = g = b = 0;
                break;
            }
        }
        ColorRGB colorRGB = new ColorRGB();
        colorRGB.red = Math.round(r * 255.0f);
        colorRGB.green = Math.round(g * 255.0f);
        colorRGB.blue = Math.round(b * 255.0f);
        return colorRGB;
    }

}
