package com.mscg.images.filters;

public class BlurImageFilter extends ConvolutionImageFilter {

    public BlurImageFilter(int size) {
        this(size, 0.0f);
    }

    public BlurImageFilter(int size, float bias) {
        super(size, bias);

        for(int i = 0; i < size; i++) {
            if(i <= size / 2) {
                for(int j = (size / 2 - i); j <= (size / 2 + i); j++) {
                    filterData[i][j] = 1.0f;
                }
            }
            else {
                for(int j = (size / 2 - (size - i - 1)); j <= (size / 2 + (size - i - 1)); j++) {
                    filterData[i][j] = 1.0f;
                }
            }
        }

        normalize();
    }

}
