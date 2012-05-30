package com.mscg.images.filters;

public class IdentityImageFilter extends ConvolutionImageFilter {

    public IdentityImageFilter(int size) {
        this(size, 0.0f);
    }

    public IdentityImageFilter(int size, float bias) {
        super(size, bias);
        filterData[size / 2][size / 2] = 1.0f;
    }

}
