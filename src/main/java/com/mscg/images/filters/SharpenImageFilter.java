package com.mscg.images.filters;

public class SharpenImageFilter extends ConvolutionImageFilter {

    public SharpenImageFilter(int size) {
        this(size, 0, 0.0f);
    }

    public SharpenImageFilter(int size, float bias) {
        this(size, 0, bias);
    }

    public SharpenImageFilter(int size, int enhancement) {
        this(size, enhancement, 0.0f);
    }

    public SharpenImageFilter(int size, int enhancement, float bias) {
        super(size, bias);
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                filterData[i][j] = -1.0f;
            }
        }
        filterData[size / 2][size / 2] = size * size + enhancement;
    }

}
