package com.mscg.images.filters;

public class MeanImageFilter extends BaseImageFilter {

    public MeanImageFilter(int size) {
        super(size);

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                filterData[i][j] = 1.0f;
            }
        }

        normalize();
    }

}
