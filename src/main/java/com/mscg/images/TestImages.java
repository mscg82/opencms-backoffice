package com.mscg.images;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class TestImages {

    public static void main1(String[] args) {
        ColorHSL colorHSL = ColorHelper.RGBtoHSL(ColorRGB.fromIntColor(ColorHelper.getRGB(255, 255, 36)));
        //colorHSL.hue = 42;
        ColorRGB colorRGB = ColorHelper.HSLtoRGB(colorHSL);
        System.out.println("hsl: " + colorHSL.hue + ", " + colorHSL.saturation + ", " + colorHSL.lightness);
        System.out.println("rgb: " + colorRGB.red + ", " + colorRGB.green + ", " + colorRGB.blue);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        InputStream imageIS = null;
        OutputStream imageOS = null;
        try {
            imageIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.jpg");
            BufferedImage bufferedImage = ImageIO.read(imageIS);
            System.out.println("Image size: " + bufferedImage.getWidth() + "x" + bufferedImage.getHeight());
            System.out.println("Image type: " + bufferedImage.getType());

            BufferedImage outImage1 = new BufferedImage(bufferedImage.getWidth(),
                                                        bufferedImage.getHeight(),
                                                        bufferedImage.getType());
            BufferedImage outImage2 = new BufferedImage(bufferedImage.getWidth(),
                                                        bufferedImage.getHeight(),
                                                        bufferedImage.getType());
            BufferedImage outImage3 = new BufferedImage(bufferedImage.getWidth(),
                                                        bufferedImage.getHeight(),
                                                        bufferedImage.getType());

            for(int i = 0, w = bufferedImage.getWidth(); i < w; i++) {
                for(int j = 0, h = bufferedImage.getHeight(); j < h; j++) {
                    int rgb = bufferedImage.getRGB(i, j);
//                    outImage1.setRGB(i, j, ColorHelper.greyScaleRGB(rgb));
//                    outImage2.setRGB(i, j, ColorHelper.greyScaleHSL(rgb));
//                    outImage3.setRGB(i, j, ColorHelper.greyScaleHSV(rgb));
                    float factor = 1.5f;
                    outImage1.setRGB(i, j, ColorHelper.changeBrightnessRGB(rgb, factor));
                    outImage2.setRGB(i, j, ColorHelper.changeBrightnessHSL(rgb, factor));
                    outImage3.setRGB(i, j, ColorHelper.changeBrightnessHSV(rgb, factor));
                }
            }

            File output = new File("./out-rgb.jpg");
            if(!output.exists())
                output.createNewFile();
            imageOS = new BufferedOutputStream(new FileOutputStream(output));
            ImageIO.write(outImage1, "jpg", imageOS);
            imageOS.flush();
            imageOS.close();

            output = new File("./out-hsl.jpg");
            if(!output.exists())
                output.createNewFile();
            imageOS = new BufferedOutputStream(new FileOutputStream(output));
            ImageIO.write(outImage2, "jpg", imageOS);
            imageOS.flush();
            imageOS.close();

            output = new File("./out-hsv.jpg");
            if(!output.exists())
                output.createNewFile();
            imageOS = new BufferedOutputStream(new FileOutputStream(output));
            ImageIO.write(outImage3, "jpg", imageOS);
            imageOS.flush();
            imageOS.close();

        } catch(Exception e) {
            try {
                imageIS.close();
            } catch(Exception e2){}
            try {
                imageOS.close();
            } catch(Exception e2){}
        }
    }

}
