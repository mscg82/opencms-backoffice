package it.ludonet.tps.suggestion.exception;

public class GlobalEffectRemovalException extends SuggesterTrainException
 {
  private static final long serialVersionUID = -8149520147127140243L;

  public GlobalEffectRemovalException()
   {
   }

  public GlobalEffectRemovalException(String message)
   {
    super(message);
   }

  public GlobalEffectRemovalException(String message, Throwable cause)
   {
    super(message, cause);
   }

  public GlobalEffectRemovalException(Throwable cause)
   {
    super(cause);
   }
 }
