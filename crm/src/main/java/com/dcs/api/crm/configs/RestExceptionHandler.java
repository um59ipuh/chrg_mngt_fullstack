package com.dcs.api.crm.configs;

import com.dcs.api.crm.exceptions.BadRequestException;
import com.dcs.api.crm.exceptions.ForbiddenException;
import com.dcs.api.crm.exceptions.NotFoundException;
import com.dcs.api.crm.exceptions.UnauthorizedException;
import com.dcs.api.crm.models.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private final MessageSource messageSource;

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNoContent(final Exception ex) {
    log.error("Error 500, No Content");
    final ApiError apiError = new ApiError(HttpStatus.NO_CONTENT,
        ex.getLocalizedMessage(), ex.getClass().getName() + "error occurred");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleBadRequest(final Exception ex) {
    log.error("Error 400, Bad Request. Please check the request");
    final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
        ex.getLocalizedMessage(), ex.getClass().getName() + "error occurred");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<Object> handleUnAuthorized(UnauthorizedException ex) {
    log.warn(getUnlocalizedMessage(ex));
    final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<Object> handleForbidden(ForbiddenException ex) {
    log.warn(getUnlocalizedMessage(ex));
    final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getMessage());
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
    final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getLocalizedMessage(), ex.getClass().getName() + "error occurred");
    return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
  }

  public static String getStackTrace(Exception exception) {
    StringWriter stringWriter = new StringWriter();
    exception.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }

  private String getUnlocalizedMessage(Exception exception) {
    return messageSource.getMessage(exception.getMessage(), null, null);
  }
}
