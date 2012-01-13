package it.ludonet.tps.suggestion.preprocessor.impl;

import it.ludonet.tps.suggestion.exception.GlobalEffectRemovalException;
import it.ludonet.tps.suggestion.source.RatingsSource;
import it.ludonet.tps.suggestion.source.impl.RealSparseMatrixRatingSource;

import java.util.Map;

/**
 * This global effect remover implementation removes
 * the items main global effect.
 */
public class ItemMainGlobalEffectRemover extends EstimationShrinkGlobalEffectRemover
 {

  public RatingsSource removeEffects(RatingsSource trainingData, boolean needCentering) throws GlobalEffectRemovalException
   {
    RatingsSource effects = new RealSparseMatrixRatingSource();

    for(int j = 0, n = trainingData.getItemsNumber(); j < n; j++)
     {
      Map<Integer,Double> itemRatings = trainingData.getItemRatings(j);
      int support = itemRatings.size();

      double num = 0.0;
      double den = 0.0;
      for(Map.Entry<Integer, Double> entry : itemRatings.entrySet())
       {
        // item main global effect doesn't need centering and the explanatory variables
        // are identically 1.
        double Xij = 1.0;
        num += entry.getValue() * Xij;
        den += Xij * Xij;
       }

      double theta = num / den;
      theta = shrinkEstimate(support, theta);

      for(Map.Entry<Integer, Double> entry : itemRatings.entrySet())
       {
        // item main global effect doesn't need centering and the explanatory variables
        // are identically 1.
        double Xij = 1.0;
        double estimate = theta * Xij;
        effects.setRating(entry.getKey(), j, estimate);
        trainingData.setRating(entry.getKey(), j, entry.getValue() - estimate);
       }

     }

    return effects;
   }

 }
