package com.morpheus.util.backoff

/**
 * The BackOffException wraps various BackOffUtility exceptions with custom error code.
 * @author Chris Taylor
 */
public class BackOffException extends Exception {
  
  private final String code;

  public BackOffException(String code) {
    super();
    this.code = code;
  }

  public BackOffException(String message, Throwable cause, String code) {
    super(message, cause);
    this.code = code;
  }

  public BackOffException(String message, String code) {
    super(message);
    this.code = code;
  }

  public BackOffException(Throwable cause, String code) {
    super(cause);
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}