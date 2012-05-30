package com.mscg.images.filters;

public class EdgeDetectionImageFilter extends ConvolutionImageFilter {

    public EdgeDetectionImageFilter(int size) {
        this(size, 0.0f);
    }

    public EdgeDetectionImageFilter(int size, float bias) {
        super(size, bias);
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                filterData[i][j] = -1.0f;
            }
        }
        filterData[size / 2][size / 2] = size * size - 1;
        //normalize();
    }

}
