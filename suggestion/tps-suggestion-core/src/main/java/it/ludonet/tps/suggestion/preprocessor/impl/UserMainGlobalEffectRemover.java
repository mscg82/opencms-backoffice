package it.ludonet.tps.suggestion.preprocessor.impl;

import it.ludonet.tps.suggestion.exception.GlobalEffectRemovalException;
import it.ludonet.tps.suggestion.source.RatingsSource;
import it.ludonet.tps.suggestion.source.impl.RealSparseMatrixRatingSource;

import java.util.Map;

/**
 * This global effect remover implementation removes
 * the users main global effect.
 */
public class UserMainGlobalEffectRemover extends EstimationShrinkGlobalEffectRemover
 {

  public RatingsSource removeEffects(RatingsSource trainingData, boolean needCentering) throws GlobalEffectRemovalException
   {
    RatingsSource effects = new RealSparseMatrixRatingSource();

    for(int i = 0, m = trainingData.getUsersNumber(); i < m; i++)
     {
      Map<Integer, Double> userRatings = trainingData.getUserRatings(i);
      int support = userRatings.size();
      double num = 0.0;
      double den = 0.0;
      for(Map.Entry<Integer, Double> entry : userRatings.entrySet())
       {
        // user main global effect doesn't need centering and the explanatory variables
        // are identically 1.
        double Xij = 1.0;
        num += entry.getValue() * Xij;
        den += Xij * Xij;
       }

      double theta = num / den;
      theta = shrinkEstimate(support, theta);

      for(Map.Entry<Integer, Double> entry : userRatings.entrySet())
       {
        // user main global effect doesn't need centering and the explanatory variables
        // are identically 1.
        double Xij = 1.0;
        double estimate = theta * Xij;
        effects.setRating(i, entry.getKey(), estimate);
        trainingData.setRating(i, entry.getKey(), entry.getValue() - estimate);
       }

     }

    return effects;
   }

 }
