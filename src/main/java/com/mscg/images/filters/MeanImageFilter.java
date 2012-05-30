package com.mscg.images.filters;

public class MeanImageFilter extends ConvolutionImageFilter {

    public MeanImageFilter(int size) {
        this(size, 0.0f);
    }

    public MeanImageFilter(int size, float bias) {
        super(size, bias);

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                filterData[i][j] = 1.0f;
            }
        }

        normalize();
    }

}
