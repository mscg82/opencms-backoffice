package com.mscg.images.filters;

import java.awt.image.BufferedImage;

import com.mscg.images.colors.ColorRGB;

public class ConvolutionImageFilter implements WeightImageFilter {

    protected float [][]filterData;
    protected float bias;

    public ConvolutionImageFilter(int size) {
        this(size, 0.0f);
    }

    public ConvolutionImageFilter(int size, float bias) {
        if(size < 0 || size % 2 == 0)
            throw new IllegalArgumentException("The image filter size must be a positive and odd number");
        this.filterData = new float[size][size];
        this.bias = bias;
    }

    protected void init() {

    }

    public int getSize() {
        return filterData.length;
    }

    public float getData(int x, int y) {
        return filterData[x][y];
    }

    public void setData(int x, int y, float data) {
        filterData[x][y] = data;
    }

    public void normalize() {
        int l = getSize();
        float sum = 0.0f;
        for(int i = 0; i < l; i++) {
            for(int j = 0; j < l; j++)
                sum += filterData[i][j];
        }
        if(sum != 0.0f) {
            for(int i = 0; i < l; i++) {
                for(int j = 0; j < l; j++)
                    filterData[i][j] /= sum;
            }
        }
    }

    public void apply(BufferedImage inputImage, BufferedImage outputImage) {
        int filterSize = getSize();

        for(int i = 0, w = inputImage.getWidth(); i < w; i++) {
            for(int j = 0, h = inputImage.getHeight(); j < h; j++) {
                float r = 0.0f, g = 0.0f, b = 0.0f;
                for(int k1 = 0; k1 < filterSize; k1++) {
                    for(int k2 = 0; k2 < filterSize; k2++) {
                        float filterWeight = getData(k1, k2);
                        if(filterWeight != 0.0f) {
                            int x = (i + (k1 - filterSize / 2) + w) % w;
                            int y = (j + (k2 - filterSize / 2) + h) % h;
                            ColorRGB color = ColorRGB.fromIntColor(inputImage.getRGB(x, y));
                            r += color.red * filterWeight;
                            g += color.green * filterWeight;
                            b += color.blue * filterWeight;
                        }
                    }
                }
                ColorRGB destinationColor = new ColorRGB();
                destinationColor.red = Math.max(0, Math.min(255, Math.round(r + bias)));
                destinationColor.green = Math.max(0, Math.min(255, Math.round(g + bias)));
                destinationColor.blue = Math.max(0, Math.min(255, Math.round(b + bias)));
                outputImage.setRGB(i, j, destinationColor.toIntColor());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int size = getSize();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(j != 0)
                    sb.append(" ");
                sb.append(String.format("%7.4f", filterData[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
