package com.powercast.shared.exception;



import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(new ErrorResponse("Not Found", ex.getMessage()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(new ErrorResponse("Bad Request", ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(new ErrorResponse("Internal Server Error", "Um erro inesperado ocorreu"));
  }
  
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex,
          HttpHeaders headers,
          HttpStatus status,
          WebRequest request) {

      Map<String, String> errors = new HashMap<>();
      ex.getBindingResult().getFieldErrors()
        .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

      return ResponseEntity
          .status(HttpStatus.UNPROCESSABLE_ENTITY) // 422
          .body(new ValidationError("Validation Failed", errors));
  }

  public record ErrorResponse(String error, String message) {}
  
  public record ValidationError(String error, Map<String, String> details) {}
}
