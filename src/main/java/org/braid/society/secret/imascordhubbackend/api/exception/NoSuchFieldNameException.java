package org.braid.society.secret.imascordhubbackend.api.exception;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;

public class NoSuchFieldNameException extends JsonProcessingException {

  public NoSuchFieldNameException(String ghostFieldName, Throwable rootCause) {
    super("JsonParser couldn't find the specified field name: %s".formatted(ghostFieldName), null, rootCause);
  }

  public NoSuchFieldNameException(Throwable rootCause) {
    this("<not provided>", rootCause);
  }

  public NoSuchFieldNameException(String ghostFieldName) {
    this(ghostFieldName, null);
  }
}
