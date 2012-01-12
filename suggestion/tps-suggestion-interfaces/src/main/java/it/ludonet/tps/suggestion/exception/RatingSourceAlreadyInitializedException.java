package it.ludonet.tps.suggestion.exception;

public class RatingSourceAlreadyInitializedException extends SuggestionException
 {
  private static final long serialVersionUID = 1831723668131396000L;

  public RatingSourceAlreadyInitializedException()
   {
   }

  public RatingSourceAlreadyInitializedException(String message)
   {
    super(message);
   }

  public RatingSourceAlreadyInitializedException(String message, Throwable cause)
   {
    super(message, cause);
   }

  public RatingSourceAlreadyInitializedException(Throwable cause)
   {
    super(cause);
   }
 }
