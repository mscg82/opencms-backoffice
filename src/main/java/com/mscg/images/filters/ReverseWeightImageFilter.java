package com.mscg.images.filters;

import java.awt.image.BufferedImage;

public class ReverseWeightImageFilter implements ImageFilter {

    private ImageFilter innerFilter;

    public ReverseWeightImageFilter(ImageFilter innerFilter) {
        this.innerFilter = innerFilter;
    }

    public int getSize() {
        return innerFilter.getSize();
    }

    public float getData(int x, int y) {
        return -innerFilter.getData(x, y);
    }

    public void setData(int x, int y, float data) {
        innerFilter.setData(x, y, data);
    }

    public void normalize() {
        innerFilter.normalize();
    }

    public void apply(BufferedImage inputImage, BufferedImage outputImage) {
        innerFilter.apply(inputImage, outputImage);
    }

}
