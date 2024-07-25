package com.dcs.api.crm.exceptions;

public class NotFoundException extends RuntimeException {

  private static final long serialVersionUID = -7092369439757552897L;

  public NotFoundException(String code) {
    super(code);
  }
}
