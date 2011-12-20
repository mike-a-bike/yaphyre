package yaphyre.util.scenereaders;

public class HandlerNotFoundException extends Exception {

  private static final long serialVersionUID = -8003355399788365548L;

  public HandlerNotFoundException() {
  }

  public HandlerNotFoundException(String message) {
    super(message);
  }

  public HandlerNotFoundException(Throwable cause) {
    super(cause);
  }

  public HandlerNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
