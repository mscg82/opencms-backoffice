package it.ludonet.tps.suggestion.exception;

public class SuggesterTrainException extends SuggestionException
 {

  private static final long serialVersionUID = 5355895217341924400L;

  public SuggesterTrainException()
   {
   }

  public SuggesterTrainException(String message)
   {
    super(message);
   }

  public SuggesterTrainException(String message, Throwable cause)
   {
    super(message, cause);
   }

  public SuggesterTrainException(Throwable cause)
   {
    super(cause);
   }
 }
