package com.mscg.images.filters;

public class EmbossImageFilter extends ConvolutionImageFilter {

    public EmbossImageFilter(int size) {
        this(size, 0.0f);
    }

    public EmbossImageFilter(int size, float bias) {
        super(size, bias);
        for(int i = size - 1; i >= 0; i--) {
            int revert_i = size - 1 - i;
            for(int j = 0; j < size; j++) {
                if(revert_i == j)
                    filterData[i][j] = 0.0f;
                else if (revert_i > j)
                    filterData[i][j] = -1.0f;
                else
                    filterData[i][j] = 1.0f;
            }
        }
    }


}
