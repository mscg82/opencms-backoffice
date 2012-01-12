package it.ludonet.tps.suggestion.exception;

public class SuggestionException extends Exception
 {

  private static final long serialVersionUID = -2788176549530555690L;

  public SuggestionException()
   {
   }

  public SuggestionException(String message)
   {
    super(message);
   }

  public SuggestionException(String message, Throwable cause)
   {
    super(message, cause);
   }

  public SuggestionException(Throwable cause)
   {
    super(cause);
   }
 }
