package com.mscg.images.filters;

import java.awt.image.BufferedImage;

public interface ImageFilter {

    public int getSize();

    public void apply(BufferedImage inputImage, BufferedImage outputImage);

}