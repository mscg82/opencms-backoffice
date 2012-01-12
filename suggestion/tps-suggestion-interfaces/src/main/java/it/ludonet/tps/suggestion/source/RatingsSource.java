package it.ludonet.tps.suggestion.source;

import it.ludonet.tps.suggestion.exception.RatingSourceAlreadyInitializedException;

import java.util.*;

/**
 * Implementations of this interface will provide
 * ratings data that can be used by suggesters to
 * compute suggestions.
 */
public interface RatingsSource
 {

  /**
   * Inits this reating source by setting the users and items
   * number. This method must be called only <b>once</b>.
   * Calling this method multipple times will result in a
   * {@link it.ludonet.tps.suggestion.exception.RatingSourceAlreadyInitializedException} exception
   *
   * @param usersNumber The number of users.
   * @param itemsNumber The number of items.
   *
   * @throws RatingSourceAlreadyInitializedException If the source is already initialized when this
   * method is called.
   */
  public void init(int usersNumber, int itemsNumber) throws RatingSourceAlreadyInitializedException;

  /**
   * Returns the number of users in this
   * ratings source.
   *
   * @return The number of users in this
   * ratings source.
   */
  public int getUsersNumber();

  /**
   * Returns the number of items in this
   * ratings source.
   *
   * @return The number of items in this
   * ratings source.
   */
  public int getItemsNumber();

  /**
   * Returns the rating of the provided user for the specified item.
   *
   * @param userIndex The 0-based user index.
   * @param itemIndex The 0-based item index.
   *
   * @return The rating provided by the user for the item.
   *
   * @throws IndexOutOfBoundsException If the user index or the item index
   * falls of the source ranges.
   */
  public double getRating(int userIndex, int itemIndex) throws IndexOutOfBoundsException;

  /**
   * Updates the rating for the couple user-item.
   *
   * @param userIndex The 0-based user index.
   * @param itemIndex The 0-based item index.
   * @param newRating The new rating.
   *
   * @return A reference to this rating source, so that this method can be used in
   * a method-call chain.
   *
   * @throws IndexOutOfBoundsException If the user index or the item index
   * falls of the source ranges.
   */
  public RatingsSource setRating(int userIndex, int itemIndex, double newRating) throws IndexOutOfBoundsException;

  /**
   * Returns a <code>{@link List}&lt;Integer&gt;</code> with the user indexes with at least
   * one valid rating.
   *
   * @return A <code>{@link List}&lt;Integer&gt;</code> with the user indexes with at least
   * one valid rating.
   */
  public List<Integer> getUsersWithRatings();

  /**
   * Returns all the ratings of the specified user, indexed by the corresponding item index.
   *
   * @param userIndex The 0-based user index.
   *
   * @return A <code>{@link Map}&lt;Integer, Double&gt;</code> with th user ratings, indexed
   * by the corresponding item index.
   *
   * @throws IndexOutOfBoundsException If the user index falls of the source ranges.
   */
  public Map<Integer, Double> getUserRatings(int userIndex) throws IndexOutOfBoundsException;

  /**
   * Returns a <code>{@link List}&lt;Integer&gt;</code> with the item indexes with at least
   * one valid rating.
   *
   * @return A <code>{@link List}&lt;Integer&gt;</code> with the item indexes with at least
   * one valid rating.
   */
  public List<Integer> getItemsWithRatings();

  /**
   * Returns all the ratings of the specified item, indexed by the corresponding user index.
   *
   * @param itemIndex The 0-based item index.
   *
   * @return A <code>{@link Map}&lt;Integer, Double&gt;</code> with th item ratings, indexed
   * by the corresponding user index.
   *
   * @throws IndexOutOfBoundsException If the item index falls of the source ranges.
   */
  public Map<Integer, Double> getItemRatings(int itemIndex) throws IndexOutOfBoundsException;

  /**
   * Sums the rating of the provided source with the ones contained in this object.
   *
   * @param other The other ratings source.
   *
   * @return A reference to this rating source, so that this method can be used in
   * a method-call chain.
   *
   * @throws IndexOutOfBoundsException If the sizes of the two sources don't agree.
   */
  public RatingsSource sum(RatingsSource other) throws IndexOutOfBoundsException;

 }
