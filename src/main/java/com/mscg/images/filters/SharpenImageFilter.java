package com.mscg.images.filters;

public class SharpenImageFilter extends BaseImageFilter {

    public SharpenImageFilter(int size) {
        this(size, 0);
    }

    public SharpenImageFilter(int size, int enhancement) {
        super(size);
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                filterData[i][j] = -1.0f;
            }
        }
        filterData[size / 2][size / 2] = size * size + enhancement;
    }

}
