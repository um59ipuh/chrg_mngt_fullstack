package com.dcs.api.crm.exceptions;

public class UnauthorizedException extends RuntimeException {

  private static final long serialVersionUID = -8510387282746152747L;

  public UnauthorizedException(String code) {
    super(code);
  }
}
