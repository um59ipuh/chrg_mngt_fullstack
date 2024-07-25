package com.dcs.api.crm.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Global error response body
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

  private HttpStatus status;

  private int code;

  private String message;

  private List<Object> errors;

  public ApiError(HttpStatus status, String message, List<Object> errors) {
    this.status = status;
    this.code = status.value();
    this.message = message;
    this.errors = errors;
  }

  public ApiError(HttpStatus status, String message, Object error) {
    this(status, message, Arrays.asList(error));
  }

  public ApiError(HttpStatus status, String message) {
    this(status, message, new ArrayList<Object>());
  }

}
