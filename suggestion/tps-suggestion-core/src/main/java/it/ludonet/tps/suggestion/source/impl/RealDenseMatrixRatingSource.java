package it.ludonet.tps.suggestion.source.impl;

import it.ludonet.tps.suggestion.exception.RatingSourceAlreadyInitializedException;
import org.apache.commons.math.linear.Array2DRowRealMatrix;

/**
 * This class implements an in-memory rating source backed-up by
 * a real dense matrix.
 */
public class RealDenseMatrixRatingSource extends RealMatrixRatingSource
 {

  public void init(int usersNumber, int itemsNumber) throws RatingSourceAlreadyInitializedException
   {
    ratings = new Array2DRowRealMatrix(usersNumber, itemsNumber);
   }
 }
