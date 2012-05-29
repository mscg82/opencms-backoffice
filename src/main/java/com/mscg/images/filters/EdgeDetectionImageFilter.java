package com.mscg.images.filters;

public class EdgeDetectionImageFilter extends BaseImageFilter {

    public EdgeDetectionImageFilter(int size) {
        super(size);
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                filterData[i][j] = -1.0f;
            }
        }
        filterData[size / 2][size / 2] = size * size - 1;
        //normalize();
    }

}
