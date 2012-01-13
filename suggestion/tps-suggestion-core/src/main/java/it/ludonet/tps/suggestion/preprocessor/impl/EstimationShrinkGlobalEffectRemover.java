package it.ludonet.tps.suggestion.preprocessor.impl;

import it.ludonet.tps.suggestion.preprocessor.GlobalEffectRemover;

public abstract class EstimationShrinkGlobalEffectRemover implements GlobalEffectRemover
 {

  protected double alpha;

  public EstimationShrinkGlobalEffectRemover()
   {
    alpha = 0.1;
   }

  public double getAlpha()
   {
    return alpha;
   }

  public void setAlpha(double alpha)
   {
    this.alpha = alpha;
   }

  protected double shrinkEstimate(int support, double theta)
   {
    theta = support * theta / (support + alpha);
    return theta;
   }
 }
