package it.ludonet.tps.suggestion.source.impl;

import it.ludonet.tps.suggestion.exception.RatingSourceAlreadyInitializedException;
import org.apache.commons.math.linear.OpenMapRealMatrix;

/**
 * This class implements an in-memory rating source backed-up by
 * a real sparse matrix.
 */
public class RealSparseMatrixRatingSource extends RealMatrixRatingSource
 {

  public void init(int usersNumber, int itemsNumber) throws RatingSourceAlreadyInitializedException
   {
    ratings = new OpenMapRealMatrix(usersNumber, itemsNumber);
   }
 }
