package com.mscg.images.filters;

public class IdentityImageFilter extends ConvolutionImageFilter {

    public IdentityImageFilter(int size) {
        super(size);
        filterData[size / 2][size / 2] = 1.0f;
    }

}
