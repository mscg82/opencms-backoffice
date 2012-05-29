package com.mscg.images.filters;

import java.awt.image.BufferedImage;

public interface ImageFilter {

    public int getSize();

    public float getData(int x, int y);

    public void setData(int x, int y, float data);

    public void normalize();

    public void apply(BufferedImage inputImage, BufferedImage outputImage);

}