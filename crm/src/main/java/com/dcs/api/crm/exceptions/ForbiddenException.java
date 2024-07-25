package com.dcs.api.crm.exceptions;

public class ForbiddenException extends RuntimeException {

  private static final long serialVersionUID = -1417555384242820216L;

  public ForbiddenException(String code) {
    super(code);
  }
  
  public ForbiddenException() {
    super("common.access_forbidden");
  }
}
