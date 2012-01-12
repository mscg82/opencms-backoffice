package it.ludonet.tps.suggestion.preprocessor;

import it.ludonet.tps.suggestion.exception.GlobalEffectRemovalException;
import it.ludonet.tps.suggestion.source.RatingsSource;

/**
 * Implementations of this interface are able
 * to apply global effects removal on the suggester
 * training datas.
 */
public interface GlobalEffectRemover
 {

  /**
   * This method applies the global effect removal
   * on the provided data and returns a {@link RatingsSource}
   * containg the "effects" values. The provided source is
   * modified and will contain only residuals.
   *
   * @param trainingData The training data from which the
   * global effect should be removed.
   * @param needCentering A boolean switch that indicates if the
   * effects must be centered on the mean value. It should be <code>false</code>
   * for the first effect remover applied on the data set, <code>true</code>
   * on the others.
   *
   * @return The "effects" values source.
   *
   * @throws GlobalEffectRemovalException If an error occurs while removing the
   * global effect.
   */
  public RatingsSource removeEffects(RatingsSource trainingData, boolean needCentering) throws GlobalEffectRemovalException;

 }
