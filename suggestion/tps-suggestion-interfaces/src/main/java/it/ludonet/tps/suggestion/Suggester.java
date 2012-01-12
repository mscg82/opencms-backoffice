package it.ludonet.tps.suggestion;

import it.ludonet.tps.suggestion.exception.SuggesterTrainException;
import it.ludonet.tps.suggestion.source.RatingsSource;

import java.util.List;

/**
 * Implementations of this interface will provide
 * engines to suggest user-item rating. The suggestions
 * will be calculated upon some informations built in
 * the training phase.
 */
public interface Suggester
 {

  /**
   * Returns the number of users in the suggester system.
   * The maximum user index accepted by this suggester is
   * <code>{@link #getUsersNumber()} - 1</code>
   *
   * @return The number of users in the suggester system.
   */
  public int getUsersNumber();

  /**
   * Returns the number of items in the suggester system.
   * The maximum item index accepted by this suggester is
   * <code>{@link #getItemsNumber()} - 1</code>
   *
   * @return The number of items in the suggester system.
   */
  public int getItemsNumber();

  /**
   * This method will train the suggester using the data
   * provided in the training ratings.
   *
   * @param trainingRatings The training ratings.
   *
   * @throws SuggesterTrainException If an error occurs while
   * training the suggester.
   */
  public void train(RatingsSource trainingRatings) throws SuggesterTrainException;

  /**
   * This method calculates and returns the predicted rating that an user
   * will make on an item.
   *
   * @param userIndex The index of the user whose rating must be predicted.
   * @param itemIndex The index of the item on which the rating must be predicted.
   *
   * @return The predicted rating.
   *
   * @throws IndexOutOfBoundsException If the user or item index falls outside the
   * suggester ranges.
   * @throws SuggesterTrainException If an error occurs during the computation of
   * the prediction.
   */
  public double getPredictedRating(int userIndex, int itemIndex) throws IndexOutOfBoundsException,
                                                                        SuggesterTrainException;

  /**
   * This method will predict the items an user will like the most. The number of
   * returned items can be specified.
   *
   * @param userIndex The index of the user whose favourite items will be computed.
   * @param maxNumberOfItems The maximum number of items to return. This parameters can be
   * <code>null</code>. The effect of passing a <code>null</code> value may vary according
   * to the implementation.
   *
   * @return A <code>{@link List}&lt;{@link Integer}&gt;</code> with the item indexes that the
   * specified user will like the most, ordered from the most liked to the less liked one.
   *
   * @throws IndexOutOfBoundsException If the user index falls outside the suggester range.
   * @throws SuggesterTrainException If an error occurs during the computation of
   * the suggested items.
   */
  public List<Integer> getSuggestedItems(int userIndex, Integer maxNumberOfItems) throws IndexOutOfBoundsException,
                                                                                         SuggesterTrainException;

  /**
   * This method will calculate the items that are similar to the provided one. The number of
   * returned items can be specified.
   *
   * @param itemIndex The index of the item whose similar ones will be computed.
   * @param maxNumberOfItems The maximum number of items to return. This parameters can be
   * <code>null</code>. The effect of passing a <code>null</code> value may vary according
   * to the implementation.
   *
   * @return A <code>{@link List}&lt;{@link Integer}&gt;</code> with the item indexes that are
   * more similar to the provided one, ordered from the most similar to the less one.
   *
   * @throws IndexOutOfBoundsException If the item index falls outside the suggester range.
   * @throws SuggesterTrainException If an error occurs during the computation of
   * the similar items.
   */
  public List<Integer> getSimilarItems(int itemIndex, Integer maxNumberOfItems) throws IndexOutOfBoundsException,
                                                                                       SuggesterTrainException;

 }
