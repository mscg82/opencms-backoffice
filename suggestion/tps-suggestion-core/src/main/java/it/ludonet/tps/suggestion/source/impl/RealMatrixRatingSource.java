package it.ludonet.tps.suggestion.source.impl;

import it.ludonet.tps.suggestion.source.RatingsSource;
import org.apache.commons.math.linear.*;

import java.util.*;

/**
 * This class implements an in-memory rating source backed-up by
 * a real (dense or sparse) matrix.
 */
public abstract class RealMatrixRatingSource implements RatingsSource
 {

  protected RealMatrix ratings;

  public int getUsersNumber()
   {
    return ratings.getRowDimension();
   }

  public int getItemsNumber()
   {
    return ratings.getColumnDimension();
   }

  public double getRating(int userIndex, int itemIndex) throws IndexOutOfBoundsException
  {
   try
    {
     return ratings.getEntry(userIndex, itemIndex);
    }
   catch(MatrixIndexException e)
    {
     throw new IndexOutOfBoundsException("Invalid indexes (" + userIndex + ", " + itemIndex + ")" + e.getMessage());
    }
  }

  public RatingsSource setRating(int userIndex, int itemIndex, double newRating) throws IndexOutOfBoundsException
   {
    try
     {
      ratings.setEntry(userIndex, itemIndex, newRating);
      return this;
     }
    catch(MatrixIndexException e)
     {
      throw new IndexOutOfBoundsException("Invalid indexes (" + userIndex + ", " + itemIndex + ")" + e.getMessage());
     }
   }

  public Map<Integer, Double> getUserRatings(int userIndex) throws IndexOutOfBoundsException
  {
   Map<Integer, Double> ret = new LinkedHashMap<Integer, Double>();

   if(userIndex < 0 || userIndex >= ratings.getRowDimension())
    throw new IndexOutOfBoundsException("Invalid user index " + userIndex);

   for(int j = 0, n = ratings.getColumnDimension(); j < n; j++)
    {
     double val = ratings.getEntry(userIndex, j);
     if(val != 0.0)
      ret.put(j, val);
    }

   return ret;
  }

  public Map<Integer, Double> getItemRatings(int itemIndex) throws IndexOutOfBoundsException
   {
    Map<Integer, Double> ret = new LinkedHashMap<Integer, Double>();

   if(itemIndex < 0 || itemIndex >= ratings.getColumnDimension())
    throw new IndexOutOfBoundsException("Invalid item index " + itemIndex);

   for(int i = 0, m = ratings.getRowDimension(); i < m; i++)
    {
     double val = ratings.getEntry(i, itemIndex);
     if(val != 0.0)
      ret.put(i, val);
    }

   return ret;
   }

  public List<Integer> getUsersWithRatings()
  {
   List<Integer> users = new ArrayList<Integer>(ratings.getRowDimension());

   for(int i = 0, m = ratings.getRowDimension(); i < m; i++)
    {
     for(int j = 0, n = ratings.getColumnDimension(); j < n; j++)
      {
       double val = ratings.getEntry(i, j);
       if(val != 0.0)
        {
         users.add(i);
         break;
        }
      }
    }

   ((ArrayList)users).trimToSize();
   return users;
  }

  public List<Integer> getItemsWithRatings()
   {
    List<Integer> items = new ArrayList<Integer>(ratings.getColumnDimension());

    for(int j = 0, n = ratings.getColumnDimension(); j < n; j++)
     {
      for(int i = 0, m = ratings.getRowDimension(); i < m; i++)
       {
        double val = ratings.getEntry(i, j);
        if(val != 0.0)
         {
          items.add(j);
          break;
         }
       }
     }

    ((ArrayList)items).trimToSize();
    return items;
   }

  public RatingsSource sum(final RatingsSource other) throws IndexOutOfBoundsException
  {
   int thisUsers = ratings.getRowDimension();
   int otherUsers = other.getUsersNumber();
   if(thisUsers != otherUsers)
    throw new IndexOutOfBoundsException("Incompatible number of users (this: " + thisUsers + ", other: " + otherUsers);

   int thisItems = ratings.getColumnDimension();
   int otherItems = other.getItemsNumber();
   if(thisItems != otherItems)
    throw new IndexOutOfBoundsException("Incompatible number of items (this: " + thisItems + ", other: " + otherItems);

   ratings.walkInRowOrder(new DefaultRealMatrixChangingVisitor()
    {
     @Override
     public double visit(int row, int column, double value) throws MatrixVisitorException
      {
       return value + other.getRating(row, column);
      }
    });

   return this;
  }
 }
