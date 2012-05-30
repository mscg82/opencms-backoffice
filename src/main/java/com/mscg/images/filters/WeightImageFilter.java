package com.mscg.images.filters;

public interface WeightImageFilter extends ImageFilter {

    public float getData(int x, int y);

    public void setData(int x, int y, float data);

    public void normalize();

}
