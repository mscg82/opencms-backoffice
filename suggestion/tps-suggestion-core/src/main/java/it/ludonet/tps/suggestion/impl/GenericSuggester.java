package it.ludonet.tps.suggestion.impl;

import it.ludonet.tps.suggestion.Suggester;
import it.ludonet.tps.suggestion.exception.SuggesterTrainException;
import it.ludonet.tps.suggestion.source.RatingsSource;

/**
 * A generic suggester implementations that provides a generic
 * mechanism to save and retrieve the users and items number of
 * the suggester.
 */
public abstract class GenericSuggester implements Suggester
 {

  protected int usersNumber;
  protected int itemsNumber;

  public int getUsersNumber()
   {
    return usersNumber;
   }

  public int getItemsNumber()
   {
    return itemsNumber;
   }

  public void train(RatingsSource trainingRatings) throws SuggesterTrainException
  {
   usersNumber = trainingRatings.getUsersNumber();
   itemsNumber = trainingRatings.getItemsNumber();
  }

 }
