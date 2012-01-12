package it.ludonet.tps.suggestion.impl;

import it.ludonet.tps.suggestion.exception.SuggesterTrainException;
import it.ludonet.tps.suggestion.preprocessor.GlobalEffectRemover;
import it.ludonet.tps.suggestion.source.RatingsSource;

import java.util.List;

/**
 * This abstract suggester applies global effects removal on training datas.
 * Descendants of this implementation can use this class
 * {@link GlobalEffectRemoverSuggester#train(RatingsSource)} train(RatingsSource)}
 * method to preprocess the training data by removing the global effects.
 */
public abstract class GlobalEffectRemoverSuggester extends GenericSuggester
 {

  protected List<GlobalEffectRemover> globalEffectsRemovers;
  protected RatingsSource ratingEffects;

  public List<GlobalEffectRemover> getGlobalEffectsRemovers()
   {
    return globalEffectsRemovers;
   }

  public void setGlobalEffectsRemovers(List<GlobalEffectRemover> globalEffectsRemovers)
   {
    this.globalEffectsRemovers = globalEffectsRemovers;
   }

  public RatingsSource getRatingEffects()
   {
    return ratingEffects;
   }

  public void setRatingEffects(RatingsSource ratingEffects)
   {
    this.ratingEffects = ratingEffects;
   }

  /**
   * This method will train the suggester using the data
   * provided in the training ratings.
   *
   * In this implementations, global effects will be removed from the
   * training data. Descendant of this class, after calling this method,
   * will find a modified training ratings source.
   *
   * @param trainingRatings The training ratings.
   *
   * @throws SuggesterTrainException If an error occurs while
   * training the suggester.
   */
  @Override
  public void train(RatingsSource trainingRatings) throws SuggesterTrainException
  {
   super.train(trainingRatings);

   if(ratingEffects == null)
    throw new SuggesterTrainException("Cannot train the suggester without a rating source to store effects in");

   if(globalEffectsRemovers != null)
    {
     boolean center = false;
     for(GlobalEffectRemover globalEffectRemover : globalEffectsRemovers)
      {
       RatingsSource effects = globalEffectRemover.removeEffects(trainingRatings, center);
       center = true;
       ratingEffects.sum(effects);
      }
    }
  }

  public double getPredictedRating(int userIndex, int itemIndex) throws IndexOutOfBoundsException, SuggesterTrainException
  {
   return ratingEffects.getRating(userIndex, itemIndex);
  }
 }
